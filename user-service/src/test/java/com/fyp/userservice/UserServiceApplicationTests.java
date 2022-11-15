package com.fyp.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.dto.UserProfile;
import com.fyp.userservice.repository.UserRepository;
import com.fyp.userservice.response.ApiResponse;
import org.junit.jupiter.api.Assertions;
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
        String content = MAPPER.writeValueAsString(generateUserProfileRequest());
        MvcResult result = performPost(RECEIVE_USER_PROFILE, content)
                .andExpect(status().isCreated())
                .andReturn();

        ApiResponse response = MAPPER.readValue(result.getResponse().getContentAsString(), ApiResponse.class);

        assertEquals("User profile information received and user created", response.getMessage());
        assertEquals(HttpStatus.CREATED, response.getMethod());
        assertEquals(SERVICE, response.getService());

        assertTrue(userRepository.findAll().size() == 1);
    }

    private RegisterUserRequest getRegisterUserRequest(String email, String phoneNumber, String password) {
        return RegisterUserRequest.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
    }

    private UserProfile generateUserProfileRequest() {
        return UserProfile.builder()
                .firstName("Nicolas")
                .lastName("Alvarez")
                .email(VALID_EMAIL)
                .phoneCountryCode("+353")
                .phoneNumber(VALID_PHONE_NUMBER)
                .dateOfBirth("2002-05-16")
                .countryOfBirth("Spain")
                .address1("3 Novara Park")
                .city("Bray")
                .county("Wicklow")
                .countryName("Ireland")
                .postalCode("A98 K535")
                .build();
    }

    private ResultActions performPost(String endpoint, String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        );
    }
}
