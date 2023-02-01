package com.fyp.alethiaservice;

import com.fyp.alethiaservice.dto.users.UserProfileInfo;
import com.fyp.alethiaservice.dto.users.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDtoTests {

    @Mock
    private UserRequest userRequestMock;

    @Mock
    private UserProfileInfo userProfileInfoMock;

    private static final String EMAIL = "john.doe123@fakemail.com";
    private static final String COUNTRY_CODE_PHONE = "+353209176583";
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
        when(userRequestMock.getEmail()).thenReturn(EMAIL);
        when(userRequestMock.getPhoneNumber()).thenReturn(COUNTRY_CODE_PHONE);
        when(userProfileInfoMock.getFirstName()).thenReturn(FIRST_NAME);
        when(userProfileInfoMock.getLastName()).thenReturn(LASTNAME);
        when(userProfileInfoMock.getEmail()).thenReturn(EMAIL);
        when(userProfileInfoMock.getAccountId()).thenReturn(ACCOUNT_ID);
        when(userProfileInfoMock.getPhoneCountryCode()).thenReturn(PHONE_COUNTRY_CODE);
        when(userProfileInfoMock.getPhoneNumber()).thenReturn(PHONE_NUMBER);
        when(userProfileInfoMock.getGender()).thenReturn(GENDER);
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
    void testUserRequestDto() {
        assertEquals(EMAIL, userRequestMock.getEmail());
        assertEquals(COUNTRY_CODE_PHONE, userRequestMock.getPhoneNumber());
    }

    @Test
    void testUserProfileInfoDto() {
        assertEquals(FIRST_NAME, userProfileInfoMock.getFirstName());
        assertEquals(LASTNAME, userProfileInfoMock.getLastName());
        assertEquals(EMAIL, userProfileInfoMock.getEmail());
        assertEquals(ACCOUNT_ID, userProfileInfoMock.getAccountId());
        assertEquals(PHONE_COUNTRY_CODE, userProfileInfoMock.getPhoneCountryCode());
        assertEquals(PHONE_NUMBER, userProfileInfoMock.getPhoneNumber());
        assertEquals(GENDER, userProfileInfoMock.getGender());
        assertEquals(DATE_OF_BIRTH, userProfileInfoMock.getDateOfBirth());
        assertEquals(COUNTRY_OF_BIRTH, userProfileInfoMock.getCountryOfBirth());
        assertEquals(ADDRESS_1, userProfileInfoMock.getAddress1());
        assertEquals(ADDRESS_2, userProfileInfoMock.getAddress2());
        assertEquals(CITY, userProfileInfoMock.getCity());
        assertEquals(COUNTY, userProfileInfoMock.getCounty());
        assertEquals(COUNTRY, userProfileInfoMock.getCountryName());
        assertEquals(POSTAL_CODE, userProfileInfoMock.getPostalCode());
    }
}
