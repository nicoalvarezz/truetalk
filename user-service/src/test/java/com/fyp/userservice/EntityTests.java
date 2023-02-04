package com.fyp.userservice;

import com.fyp.userservice.model.Followee;
import com.fyp.userservice.model.User;
import com.fyp.userservice.repository.FolloweeeRepository;
import com.fyp.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EntityTests {

    @Mock
    private User validUser;

    @Mock
    private User invalidUser;

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolloweeeRepository followeeeRepository;

    private static int MAX_PASSWORD_LENGTH = 46;

    private UUID uuid = UUID.randomUUID();
    private static final String EMAIL = "john.doe123@fakemail.com";
    private static final String PASSWORD = "some_password";
    private static final String INVALID_EMAIL = "example#example.com";
    private static final String INVALID_PASSWORD = new String(new char[MAX_PASSWORD_LENGTH]).replace("\0", "a");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
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
        userRepository.save(User.builder()
                .email(validUser.getEmail())
                .password(validUser.getPassword())
                .build());

        assertTrue(userRepository.findAll().size() == 1);

        validUser = userRepository.findAll().get(0);
        assertEquals(EMAIL, validUser.getEmail());
        assertEquals(PASSWORD, validUser.getPassword());
        assertEquals(false, validUser.isEnabled());
        assertEquals(false, validUser.isVerified());
    }

    @Test
    void testInsertUserWithInvalidEmail() {
        assertThrows(TransactionSystemException.class, () -> {
            userRepository.save(User.builder()
                    .email(invalidUser.getEmail())
                    .password(validUser.getPassword())
                    .build());
        });
        assertTrue(userRepository.findAll().size() == 0);
    }

    @Test
    void testInsertUserWithEmptyEmail() {
        assertThrows(TransactionSystemException.class, () -> {
            userRepository.save(User.builder()
                    .email("")
                    .password(validUser.getPassword())
                    .build());
        });
        assertTrue(userRepository.findAll().size() == 0);
    }

    @Test
    void testInsertUserWithNullEmail() {
        assertThrows(TransactionSystemException.class, () -> {
            userRepository.save(User.builder()
                    .email(null)
                    .password(validUser.getPassword())
                    .build());
        });
        assertTrue(userRepository.findAll().size() == 0);
    }

    @Test
    void testInsertUserWithTooLongPassword() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(User.builder()
                    .email(validUser.getEmail())
                    .password(invalidUser.getPassword())
                    .build());
        });
        assertTrue(userRepository.findAll().size() == 0);
    }
    
    @Test
    void testInsertUserWithEmptyPassword() {
        assertThrows(TransactionSystemException.class, () -> {
            userRepository.save(User.builder()
                    .email(validUser.getEmail())
                    .password("")
                    .build());
        });
        assertTrue(userRepository.findAll().size() == 0);
    }

    @Test
    void testInsertUserWithNullPassword() {
        assertThrows(TransactionSystemException.class, () -> {
            userRepository.save(User.builder()
                    .email(validUser.getEmail())
                    .password(null)
                    .build());
        });
        assertTrue(userRepository.findAll().size() == 0);
    }

    @Test
    void testUserRetrievalWithEmail() {
        userRepository.save(User.builder()
                .email(validUser.getEmail())
                .password(validUser.getPassword())
                .build());

        assertTrue(userRepository.findAll().size() == 1);

        User tempUser = userRepository.findByEmail(validUser.getEmail());
        assertEquals(tempUser.getEmail(), validUser.getEmail());
        assertEquals(tempUser.getPassword(), validUser.getPassword());
        assertEquals(tempUser.isEnabled(), validUser.isEnabled());
        assertEquals(tempUser.isVerified(), validUser.isVerified());
    }

    @Test
    void testUserRetrievalWithUuid() {
        userRepository.save(User.builder()
                .email(validUser.getEmail())
                .password(validUser.getPassword())
                .build());

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
        userRepository.save(User.builder()
                .email(validUser.getEmail())
                .password(validUser.getPassword())
                .build());

        userRepository.save(User.builder()
                .email("tempUser@gmail.com")
                .password("tempuserpasswrod")
                .build());

        assertTrue(userRepository.findAll().size() == 2);

        User follower = userRepository.findByEmail(validUser.getEmail());
        User followee = userRepository.findByEmail("tempUser@gmail.com");

        followeeeRepository.save(new Followee(follower.getId(), followee.getId()));

        assertEquals(followeeeRepository.findByFollowerId(follower.getId()).get(0).getFolloweeId(), followee.getId());
        assertTrue(followeeeRepository.findByFollowerId(follower.getId()).size() == 1);
    }

    @Test
    void testInvalidFollowees() {
        userRepository.save(User.builder()
                .email(validUser.getEmail())
                .password(validUser.getPassword())
                .build());

        userRepository.save(User.builder()
                .email("tempUser@gmail.com")
                .password("tempuserpasswrod")
                .build());

        assertTrue(userRepository.findAll().size() == 2);

        User follower = userRepository.findByEmail(validUser.getEmail());
        User followee = userRepository.findByEmail("tempUser@gmail.com");

        followeeeRepository.save(new Followee(follower.getId(), followee.getId()));

        assertTrue(followeeeRepository.findByFollowerId(follower.getId()).size() == 1);

        followeeeRepository.save(new Followee(follower.getId(), followee.getId()));

        // An exception is not thrown, but the new Followee is not added for a second time in the db
        assertTrue(followeeeRepository.findByFollowerId(follower.getId()).size() == 1);
    }

    @Test
    void testFollowerRetrieval() {
        userRepository.save(User.builder()
                .email(validUser.getEmail())
                .password(validUser.getPassword())
                .build());

        userRepository.save(User.builder()
                .email("tempUser@gmail.com")
                .password("tempuserpasswrod")
                .build());

        userRepository.save(User.builder()
                .email("tempUser2@gmail.com")
                .password("tempuserpasswrod")
                .build());

        assertTrue(userRepository.findAll().size() == 3);

        User follower = userRepository.findByEmail(validUser.getEmail());
        User followee = userRepository.findByEmail("tempUser@gmail.com");
        User follower2 = userRepository.findByEmail("tempUser2@gmail.com");

        followeeeRepository.save(new Followee(follower.getId(), followee.getId()));
        followeeeRepository.save(new Followee(follower2.getId(), followee.getId()));

        assertTrue(followeeeRepository.findByFolloweeId(followee.getId()).size() == 2);
    }
}
