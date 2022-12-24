package com.fyp.userservice.controller;

import com.fyp.hiveshared.api.responses.ResponseHandler;
import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.dto.UserProfile;
import com.fyp.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final String SERVICE = "user-service";

    @PostMapping("/register-user")
    @ResponseBody
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        // TODO:
        // The the email verification will start
        userService.registerUser(registerUserRequest);
        return ResponseHandler.serviceResponse("User registered successfully", HttpStatus.CREATED, SERVICE);
    }

    // TODO:
    // Define when this endpoint will be called ?????
    // This is something that has me confused as when this endpoint will be triggered...
    @PostMapping("/trigger-alethia-verification")
    @ResponseBody
    public ResponseEntity<Object> triggerAlethiaVerification(@Valid @RequestBody RegisterUserRequest registerUserRequest) throws IOException {
        userService.triggerAlethiaVerification(registerUserRequest);
        return ResponseHandler.serviceResponse("Verification triggered in alethia", HttpStatus.OK, SERVICE);
    }

    @PostMapping("/receive-user-profile")
    @ResponseBody
    public ResponseEntity<Object> receiveUserInformation(@Valid @RequestBody UserProfile userProfileInfo) {
        userService.saveUserProfileInfo(userProfileInfo);
        return ResponseHandler.serviceResponse("User profile information received and user created", HttpStatus.CREATED, SERVICE);
    }
}
