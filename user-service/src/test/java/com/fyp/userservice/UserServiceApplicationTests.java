package com.fyp.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hiveshared.api.responses.HiveResponseBody;
import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.dto.UserProfile;
import com.fyp.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest");

    @Autowired
    private UserRepository userRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String RECEIVE_USER_PROFILE = "/api/users/receive-user-profile";
    private static final String VALID_EMAIL = "nicoalvarezgarrido@gmail.com";
    private static final String VALID_PHONE_NUMBER = "+3530838455233";
    private static final String VALID_PASSWORD = "dcfnsjdvffnA9cdnsj@";

    private static final String SERVICE = "user-service";
    private static final String TRIGGER_ALETHIA_VERIFICATION_ENDPOINT = "/api/users/trigger-alethia-verification";

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.postgres.uri", postgreSQLContainer::getJdbcUrl);
    }

    // Let's leave this test in stand by for now
//    @Test
//    void testTriggerAlethiaVerification() throws Exception {
//        String content = MAPPER.writeValueAsString(getRegisterUserRequest(
//                                VALID_EMAIL,
//                                VALID_PHONE_NUMBER,
//                                VALID_PASSWORD
//                        ));
//        MvcResult result = performPost(TRIGGER_ALETHIA_VERIFICATION_ENDPOINT, content)
//                .andExpect(status().isOk())
//                .andReturn();
//    }

    @Test
    void testReceiveUserInformation() throws Exception {
        String content = MAPPER.writeValueAsString(generateUserProfileRequest("Nicolas", "Alvarez",
                VALID_EMAIL, "+353", VALID_PHONE_NUMBER, "2002-05-26",
                "Spain", "3 Novara Park", "Bray", "Wicklow",
                "Ireland", "A98 K535"));

        MvcResult result = performPost(RECEIVE_USER_PROFILE, content)
                .andExpect(status().isCreated())
                .andReturn();

        HiveResponseBody response = MAPPER.readValue(result.getResponse().getContentAsString(), HiveResponseBody.class);

        assertEquals("User profile information received and user created", response.getMessage());
        assertEquals(HttpStatus.CREATED, response.getMethod());
        assertEquals(SERVICE, response.getService());

        assertTrue(userRepository.findAll().size() == 1);
    }

    @Test
    void testReceiveUserInformationWithBlankData() throws Exception {
        String content = MAPPER.writeValueAsString(generateUserProfileRequest("", "",
                VALID_EMAIL, "", VALID_PHONE_NUMBER, "",
                "", "", "", "",
                "", ""));

        performPost(RECEIVE_USER_PROFILE, content).andExpect(status().isBadRequest());
    }

    private RegisterUserRequest getRegisterUserRequest(String email, String phoneNumber, String password) {
        return RegisterUserRequest.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
    }

    private UserProfile generateUserProfileRequest(String firstName, String lastName, String email,
                                                   String phoneCountryCode, String phoneNumber, String dateOfBirth,
                                                   String countryOfBirth, String address1, String city, String county,
                                                   String countryName, String postalCode) {
        return UserProfile.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneCountryCode(phoneCountryCode)
                .phoneNumber(phoneNumber)
                .dateOfBirth(dateOfBirth)
                .countryOfBirth(countryOfBirth)
                .address1(address1)
                .city(city)
                .county(county)
                .countryName(countryName)
                .postalCode(postalCode)
                .build();
    }

    private ResultActions performPost(String endpoint, String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        );
    }
}
