package com.fyp.userservice;

import com.fyp.userservice.dto.AlethiaRequest;
import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.dto.UserProfile;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DtoTests {

    @Mock
    private AlethiaRequest alethiaRequestMock;

    @Mock
    private RegisterUserRequest registerUserRequestMock;

    @Mock
    private UserProfile userProfileMock;

    private static final String EMAIL = "john.doe123@fakemail.com";
    private static final String COUNTRY_CODE_PHONE_NUMBER = "+353209176583";
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
        when(alethiaRequestMock.getEmail()).thenReturn(EMAIL);
        when(alethiaRequestMock.getPhoneNumber()).thenReturn(COUNTRY_CODE_PHONE_NUMBER);
        when(registerUserRequestMock.getEmail()).thenReturn(EMAIL);
        when(registerUserRequestMock.getPhoneNumber()).thenReturn(COUNTRY_CODE_PHONE_NUMBER);
        when(registerUserRequestMock.getPassword()).thenReturn(PASSWORD);
        when(userProfileMock.getFirstName()).thenReturn(FIRST_NAME);
        when(userProfileMock.getLastName()).thenReturn(LASTNAME);
        when(userProfileMock.getEmail()).thenReturn(EMAIL);
        when(userProfileMock.getAccountId()).thenReturn(ACCOUNT_ID);
        when(userProfileMock.getPhoneCountryCode()).thenReturn(PHONE_COUNTRY_CODE);
        when(userProfileMock.getPhoneNumber()).thenReturn(PHONE_NUMBER);
        when(userProfileMock.getDateOfBirth()).thenReturn(DATE_OF_BIRTH);
        when(userProfileMock.getCountryOfBirth()).thenReturn(COUNTRY_OF_BIRTH);
        when(userProfileMock.getAddress1()).thenReturn(ADDRESS_1);
        when(userProfileMock.getAddress2()).thenReturn(ADDRESS_2);
        when(userProfileMock.getCity()).thenReturn(CITY);
        when(userProfileMock.getCounty()).thenReturn(COUNTY);
        when(userProfileMock.getCountryName()).thenReturn(COUNTRY);
        when(userProfileMock.getPostalCode()).thenReturn(POSTAL_CODE);
    }

    @Test
    void testAlethiaRequest() {
        assertEquals(EMAIL, alethiaRequestMock.getEmail());
        assertEquals(COUNTRY_CODE_PHONE_NUMBER, alethiaRequestMock.getPhoneNumber());
    }

    @Test
    void testRegisterUserRequest() {
        assertEquals(EMAIL, registerUserRequestMock.getEmail());
        assertEquals(COUNTRY_CODE_PHONE_NUMBER, registerUserRequestMock.getPhoneNumber());
        assertEquals(PASSWORD, registerUserRequestMock.getPassword());
    }

    @Test
    void testUserProfile() {
        Assert.assertEquals(FIRST_NAME, userProfileMock.getFirstName());
        Assert.assertEquals(LASTNAME, userProfileMock.getLastName());
        Assert.assertEquals(EMAIL, userProfileMock.getEmail());
        Assert.assertEquals(ACCOUNT_ID, userProfileMock.getAccountId());
        Assert.assertEquals(PHONE_COUNTRY_CODE, userProfileMock.getPhoneCountryCode());
        Assert.assertEquals(PHONE_NUMBER, userProfileMock.getPhoneNumber());
        Assert.assertEquals(DATE_OF_BIRTH, userProfileMock.getDateOfBirth());
        Assert.assertEquals(COUNTRY_OF_BIRTH, userProfileMock.getCountryOfBirth());
        Assert.assertEquals(ADDRESS_1, userProfileMock.getAddress1());
        Assert.assertEquals(ADDRESS_2, userProfileMock.getAddress2());
        Assert.assertEquals(CITY, userProfileMock.getCity());
        Assert.assertEquals(COUNTY, userProfileMock.getCounty());
        Assert.assertEquals(COUNTRY, userProfileMock.getCountryName());
        Assert.assertEquals(POSTAL_CODE, userProfileMock.getPostalCode());
    }
}
