package com.fyp.userservice;

import com.fyp.userservice.model.ConfirmationToken;
import com.fyp.userservice.model.Followee;
import com.fyp.userservice.model.User;
import com.fyp.userservice.repository.ConfirmationTokenRepository;
import com.fyp.userservice.repository.FolloweeeRepository;
import com.fyp.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.TransactionSystemException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
public class EntityTests {

    @Mock
    private User validUser;

    @Mock
    private User invalidUser;

    @Mock
    private ConfirmationToken confirmationToken;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolloweeeRepository followeeeRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
            .withUsername("test")
            .withPassword("root");

    private static int MAX_PASSWORD_LENGTH = 46;

    private UUID uuid = UUID.randomUUID();
    private static final String EMAIL = "john.doe123@fakemail.com";
    private static final String PASSWORD = "some_password";
    private static final String INVALID_EMAIL = "example#example.com";
    private static final String INVALID_PASSWORD = new String(new char[MAX_PASSWORD_LENGTH]).replace("\0", "a");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
        dymDynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dymDynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        confirmationTokenRepository.deleteAll();
        userRepository.deleteAll();

        when(validUser.getId()).thenReturn(uuid);
        when(validUser.getEmail()).thenReturn(EMAIL);
        when(validUser.getPassword()).thenReturn(PASSWORD);
        when(invalidUser.getEmail()).thenReturn(INVALID_EMAIL);
        when(invalidUser.getPassword()).thenReturn(INVALID_PASSWORD);
    }

    @Test
    void testCreateValidUser() {
        when(validUser.isEnabled()).thenReturn(false);
        when(validUser.isVerified()).thenReturn(false);

        assertEquals(uuid, validUser.getId());
        assertEquals(EMAIL, validUser.getEmail());
        assertEquals(PASSWORD, validUser.getPassword());
        assertFalse(validUser.isEnabled());
        assertFalse(validUser.isVerified());
    }

    @Test
    void testInsertValidUser() {
        saveTestUser(validUser.getEmail(), validUser.getPassword());

        assertTrue(userRepository.findAll().size() == 1);

        validUser = userRepository.findAll().get(0);
        assertEquals(EMAIL, validUser.getEmail());
        assertEquals(PASSWORD, validUser.getPassword());
        assertEquals(false, validUser.isEnabled());
        assertEquals(false, validUser.isVerified());
    }

    @Test
    void testInsertUserWithInvalidEmail() {
        assertThrows(TransactionSystemException.class, () -> saveTestUser(invalidUser.getEmail(), validUser.getPassword()));
        assertTrue(userRepository.findAll().size() == 0);
    }

    @Test
    void testInsertUserWithEmptyEmail() {
        assertThrows(TransactionSystemException.class, () -> saveTestUser("", validUser.getPassword()));
        assertTrue(userRepository.findAll().size() == 0);
    }

    @Test
    void testInsertUserWithNullEmail() {
        assertThrows(TransactionSystemException.class, () -> saveTestUser(null, validUser.getPassword()));
        assertTrue(userRepository.findAll().size() == 0);
    }

    @Test
    void testInsertUserWithTooLongPassword() {
        assertThrows(DataIntegrityViolationException.class, () -> saveTestUser(validUser.getEmail(), invalidUser.getPassword()));
        assertTrue(userRepository.findAll().size() == 0);
    }
    
    @Test
    void testInsertUserWithEmptyPassword() {
        assertThrows(TransactionSystemException.class, () -> saveTestUser(validUser.getEmail(), ""));
        assertTrue(userRepository.findAll().size() == 0);
    }

    @Test
    void testInsertUserWithNullPassword() {
        assertThrows(TransactionSystemException.class, () -> saveTestUser(validUser.getEmail(), null));
        assertTrue(userRepository.findAll().size() == 0);
    }

    @Test
    void testUserRetrievalWithEmail() {
        saveTestUser(validUser.getEmail(), validUser.getPassword());

        assertTrue(userRepository.findAll().size() == 1);

        User tempUser = userRepository.findByEmail(validUser.getEmail()).get();
        assertEquals(tempUser.getEmail(), validUser.getEmail());
        assertEquals(tempUser.getPassword(), validUser.getPassword());
        assertEquals(tempUser.isEnabled(), validUser.isEnabled());
        assertEquals(tempUser.isVerified(), validUser.isVerified());
    }

    @Test
    void testUserRetrievalWithUuid() {
        saveTestUser(validUser.getEmail(), validUser.getPassword());

        assertTrue(userRepository.findAll().size() == 1);

        UUID tempUUID = userRepository.findAll().get(0).getId();
        when(validUser.getId()).thenReturn(tempUUID);

        // if user not found -> return an invalid user; this will make test fail if not found
        User tempUser = userRepository.findById(validUser.getId()).orElse(invalidUser);
        assertEquals(tempUser.getId(), validUser.getId());
        assertEquals(tempUser.getEmail(), validUser.getEmail());
        assertEquals(tempUser.getPassword(), validUser.getPassword());
        assertEquals(tempUser.isEnabled(), validUser.isEnabled());
        assertEquals(tempUser.isVerified(), validUser.isVerified());
    }

    @Test
    void testValidFollowees() {
        String tempEmail = "tempUser@gmail.com";

        saveTestUser(validUser.getEmail(), validUser.getPassword());
        saveTestUser(tempEmail, validUser.getPassword());

        assertTrue(userRepository.findAll().size() == 2);

        User follower = userRepository.findByEmail(validUser.getEmail()).get();
        User followee = userRepository.findByEmail(tempEmail).get();

        followeeeRepository.save(new Followee(follower.getId(), followee.getId()));

        assertEquals(followeeeRepository.findByFollowerId(follower.getId()).get(0).getFolloweeId(), followee.getId());
        assertTrue(followeeeRepository.findByFollowerId(follower.getId()).size() == 1);
    }

    @Test
    void testInvalidFollowees() {
        String tempEmail = "tempUser@gmail.com";

        saveTestUser(validUser.getEmail(), validUser.getPassword());
        saveTestUser(tempEmail, validUser.getPassword());

        assertTrue(userRepository.findAll().size() == 2);

        User follower = userRepository.findByEmail(validUser.getEmail()).get();
        User followee = userRepository.findByEmail(tempEmail).get();

        followeeeRepository.save(new Followee(follower.getId(), followee.getId()));

        assertTrue(followeeeRepository.findByFollowerId(follower.getId()).size() == 1);

        followeeeRepository.save(new Followee(follower.getId(), followee.getId()));

        // An exception is not thrown, but the new Followee is not added for a second time in the db
        assertTrue(followeeeRepository.findByFollowerId(follower.getId()).size() == 1);
    }

    @Test
    void testFollowerRetrieval() {
        String tempEmail = "tempUser@gmail.com";
        String tempEmail2 = "tempUser2@gmail.com";

        saveTestUser(validUser.getEmail(), validUser.getPassword());
        saveTestUser(tempEmail, validUser.getPassword());
        saveTestUser(tempEmail2, validUser.getPassword());

        assertTrue(userRepository.findAll().size() == 3);

        User follower = userRepository.findByEmail(validUser.getEmail()).get();
        User followee = userRepository.findByEmail(tempEmail).get();
        User follower2 = userRepository.findByEmail(tempEmail2).get();

        followeeeRepository.save(new Followee(follower.getId(), followee.getId()));
        followeeeRepository.save(new Followee(follower2.getId(), followee.getId()));

        assertTrue(followeeeRepository.findByFolloweeId(followee.getId()).size() == 2);
    }

    @Test
    void testInsertValidConfirmationToken() {
        saveTestUser(validUser.getEmail(), validUser.getPassword());

        assertTrue(userRepository.findAll().size() == 1);

        User user = userRepository.findByEmail(validUser.getEmail()).get();
        String token = UUID.randomUUID().toString();

        saveTestConfirmationToken(token, user);

        assertTrue(confirmationTokenRepository.findAll().size() == 1);
    }

    @Test
    void testInsertInvalidConfirmationTokenWithNonExistingUser() {
        String token = UUID.randomUUID().toString();
        User nonExistingUser = User.builder().email(validUser.getEmail()).password(validUser.getPassword()).build();

        assertThrows(InvalidDataAccessApiUsageException.class, () -> saveTestConfirmationToken(token, nonExistingUser));
        assertTrue(confirmationTokenRepository.findAll().size() == 0);
    }

    @Test
    void testInsertInvalidConfirmationTokenWithNullToken() {
        saveTestUser(validUser.getEmail(), validUser.getPassword());

        assertTrue(userRepository.findAll().size() == 1);

        User user = userRepository.findByEmail(validUser.getEmail()).get();

        assertThrows(DataIntegrityViolationException.class, () -> saveTestConfirmationToken(null, user));
        assertTrue(confirmationTokenRepository.findAll().size() == 0);
    }

    @Test
    void testInsertInvalidConfirmationTokenWithNullUser() {
        String token = UUID.randomUUID().toString();

        assertThrows(DataIntegrityViolationException.class, () -> saveTestConfirmationToken(token, null));
        assertTrue(confirmationTokenRepository.findAll().size() == 0);
    }

    private void saveTestUser(String email, String password) {
        userRepository.save(User.builder()
                .email(email)
                .password(password)
                .build());
    }

    private void saveTestConfirmationToken(String token, User user) {
        confirmationTokenRepository.save(ConfirmationToken.builder()
                .token(token)
                .user(user)
                .build());
    }
}
