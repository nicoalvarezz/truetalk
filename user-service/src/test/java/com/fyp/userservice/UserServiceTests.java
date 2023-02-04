package com.fyp.userservice;

import com.fyp.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserServiceTests {

    @Autowired
    private UserService userService;

    private static final String RECEIVE_USER_PROFILE = "/api/users/receive-user-profile";
    private static final String VALID_EMAIL = "nicoalvarezgarrido@gmail.com";
    private static final String VALID_PHONE_NUMBER = "+3530838455233";
    private static final String VALID_PASSWORD = "dcfnsjdvffnA9cdnsj@";

}