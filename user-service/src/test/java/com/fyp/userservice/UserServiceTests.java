package com.fyp.userservice;

import com.fyp.hiveshared.api.responses.excpetion.UnauthorizedException;
import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.dto.UserProfile;
import com.fyp.userservice.model.ConfirmationToken;
import com.fyp.userservice.model.User;
import com.fyp.userservice.repository.ConfirmationTokenRepository;
import com.fyp.userservice.repository.UserRepository;
import com.fyp.userservice.repository.UserVerifiedProfileRepository;
import com.fyp.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserServiceTests {

    @Mock
    private RegisterUserRequest registerUserRequestMock;

    @Mock
    private UserProfile userProfileInfoMock;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserVerifiedProfileRepository userVerifiedProfileRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    private static final String EMAIL = "nicoalvarezgarrido@gmail.com";
    private static final String PASSWORD = "dcfnsjdvffnA9cdnsj@";
    private static final String FIRST_NAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String ACCOUNT_ID = "fake-account-id";
    private static final String PHONE_COUNTRY_CODE = "+353";
    private static final String PHONE_NUMBER = "209176583";
    private static final String GENDER = "male";
    private static final String DATE_OF_BIRTH = "2002-09-28";
    private static final String COUNTRY_OF_BIRTH = "Ireland";
    private static final String COUNTRY = "Ireland";
    private static final String ADDRESS_1 = "20, Brookwood Avenue";
    private static final String ADDRESS_2 = "";
    private static final String CITY = "Dublin";
    private static final String COUNTY = "Dublin";
    private static final String POSTAL_CODE = "D05 HE37";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        confirmationTokenRepository.deleteAll();
        userRepository.deleteAll();

        when(registerUserRequestMock.getEmail()).thenReturn(EMAIL);
        when(registerUserRequestMock.getPassword()).thenReturn(PASSWORD);
        when(userProfileInfoMock.getFirstName()).thenReturn(FIRST_NAME);
        when(userProfileInfoMock.getLastName()).thenReturn(LASTNAME);
        when(userProfileInfoMock.getEmail()).thenReturn(EMAIL);
        when(userProfileInfoMock.getAccountId()).thenReturn(ACCOUNT_ID);
        when(userProfileInfoMock.getPhoneCountryCode()).thenReturn(PHONE_COUNTRY_CODE);
        when(userProfileInfoMock.getPhoneNumber()).thenReturn(PHONE_NUMBER);
        when(userProfileInfoMock.getDateOfBirth()).thenReturn(DATE_OF_BIRTH);
        when(userProfileInfoMock.getCountryOfBirth()).thenReturn(COUNTRY_OF_BIRTH);
        when(userProfileInfoMock.getAddress1()).thenReturn(ADDRESS_1);
        when(userProfileInfoMock.getAddress2()).thenReturn(ADDRESS_2);
        when(userProfileInfoMock.getCity()).thenReturn(CITY);
        when(userProfileInfoMock.getCounty()).thenReturn(COUNTY);
        when(userProfileInfoMock.getCountryName()).thenReturn(COUNTRY);
        when(userProfileInfoMock.getPostalCode()).thenReturn(POSTAL_CODE);
    }

    @Test
    void testRegisterUser() {
        registerTesUser(registerUserRequestMock.getEmail(), registerUserRequestMock.getPassword());

        assertTrue(userRepository.findAll().size() == 1);
        assertEquals(userRepository.findAll().get(0).getEmail(), registerUserRequestMock.getEmail());
        assertEquals(userRepository.findAll().get(0).getPassword(), registerUserRequestMock.getPassword());
    }

    @Test
    void testRegisterRepeatedUser() {
        userService.registerUser(RegisterUserRequest.builder()
                .email(registerUserRequestMock.getEmail())
                .password(registerUserRequestMock.getPassword())
                .build());

        assertThrows(DataIntegrityViolationException.class, () ->
                registerTesUser(registerUserRequestMock.getEmail(), registerUserRequestMock.getPassword()));
        assertTrue(userRepository.findAll().size() == 1);
    }

    @Test
    void testConfirmUser() throws UnauthorizedException {
        registerTesUser(registerUserRequestMock.getEmail(), registerUserRequestMock.getPassword());

        assertTrue(userRepository.findAll().size() == 1);

        User user = userRepository.findAll().get(0);
        String token = UUID.randomUUID().toString();

        confirmationTokenRepository.save(ConfirmationToken.builder().token(token).user(user).build());

        assertTrue(confirmationTokenRepository.findAll().size() == 1);

        userService.confirmUser(token);
        assertTrue(userRepository.findAll().get(0).isEnabled());
    }

    @Test
    void testConfirmUserWithEmptyToken() throws UnauthorizedException {
        assertThrows(UnauthorizedException.class, () -> userService.confirmUser(""));
    }

    // there is no way for me to make the toke expire right now
//    @Test
//    void testConfirmUserWithExpiredToken() throws UnauthorizedException {
//        registerTesUser(registerUserRequestMock.getEmail(), registerUserRequestMock.getPassword());
//
//        assertTrue(userRepository.findAll().size() == 1);
//
//        User user = userRepository.findAll().get(0);
//        String token = UUID.randomUUID().toString();
//
//        confirmationTokenRepository.save(ConfirmationToken.builder().token(token).user(user).expiryDate(LocalDateTime.now().plusHours(25)).build());
//
//        assertTrue(confirmationTokenRepository.findAll().size() == 1);
//
//        userService.confirmUser(token);
//    }

//    @Test
//    void testSaveUserProfileInfo() {
//        registerTesUser(registerUserRequestMock.getEmail(), registerUserRequestMock.getPassword());
//
//        assertTrue(userRepository.findAll().size() == 1);
//
//        userService.saveUserProfileInfo(UserProfile.builder()
//                .email(EMAIL)
//                .firstName(userProfileInfoMock.getFirstName())
//                .lastName(userProfileInfoMock.getLastName())
//                .phoneCountryCode(userProfileInfoMock.getPhoneCountryCode())
//                .phoneNumber(userProfileInfoMock.getPhoneNumber())
//                .dateOfBirth(userProfileInfoMock.getDateOfBirth())
//                .countryOfBirth(userProfileInfoMock.getCountryOfBirth())
//                .address1(userProfileInfoMock.getAddress1())
//                .address2(userProfileInfoMock.getAddress2())
//                .city(userProfileInfoMock.getCity())
//                .county(userProfileInfoMock.getCounty())
//                .countryName(userProfileInfoMock.getCountryName())
//                .postalCode(userProfileInfoMock.getPostalCode())
//                .build());
//
//        assertTrue(userRepository.findAll().get(0).isVerified());
//        assertTrue(userVerifiedProfileRepository.findAll().size() == 1);
//
//        UserVerifiedProfile userVerifiedProfile = userVerifiedProfileRepository.findAll().get(0);
//
//        assertEquals(userProfileInfoMock.getFirstName(), userVerifiedProfile.getFirstName());
//        assertEquals(userProfileInfoMock.getLastName(), userVerifiedProfile.getLastName());
//        assertEquals(userProfileInfoMock.getPhoneCountryCode(), userVerifiedProfile.getPhoneCountryCode());
//        assertEquals(userProfileInfoMock.getDateOfBirth(), userVerifiedProfile.getDateOfBirth());
//        assertEquals(userProfileInfoMock.getCountryOfBirth(), userVerifiedProfile.getCountryOfBirth());
//        assertEquals(userProfileInfoMock.getAddress1(), userVerifiedProfile.getAddress1());
//        assertEquals(userProfileInfoMock.getAddress2(), userVerifiedProfile.getAddress2());
//        assertEquals(userProfileInfoMock.getCity(), userVerifiedProfile.getCity());
//        assertEquals(userProfileInfoMock.getCounty(), userVerifiedProfile.getCounty());
//        assertEquals(userProfileInfoMock.getCountryName(), userVerifiedProfile.getCountryName());
//        assertEquals(userProfileInfoMock.getPostalCode(), userVerifiedProfile.getPostalCode());
//    }

    private void registerTesUser(String email, String password) {
        userService.registerUser(RegisterUserRequest.builder()
                .email(email)
                .password(password)
                .build());
    }
}
