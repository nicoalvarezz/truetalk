package com.fyp.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.userservice.repository.UserRepository;
import com.fyp.userservice.repository.UserVerifiedProfileRepository;
import com.fyp.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserServiceTests {


    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserVerifiedProfileRepository userVerifiedProfileRepository;

    @Autowired
    private UserService userService;

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

    @Test
    void testCreateUserValidInput() {

    }

}
