package com.fyp.userservice.controller;

import com.fyp.hiveshared.api.responses.ResponseHandler;
import com.fyp.hiveshared.api.responses.excpetion.UnauthorizedException;
import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.dto.UserProfile;
import com.fyp.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final String SERVICE = "user-service";

    @PostMapping("/register-user")
    @ResponseBody
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest, HttpServletRequest request) {
        userService.registerUser(registerUserRequest);
        userService.publishConfirmationEvent(registerUserRequest, request.getLocale(), request.getContextPath());
        return ResponseHandler.responseBody("User registered successfully", HttpStatus.CREATED, SERVICE);
    }

    @GetMapping("/registration-confirm")
    public ResponseEntity<Object> registrationConfirm(@RequestParam(value = "token", required = false) String token) throws UnauthorizedException {
        // TODO:
        // confirm user

        // Should this in the future sent to a page or something... I'll have to think about it later
        userService.confirmUser(token);
        return ResponseHandler.responseBody("User confirmed successfully", HttpStatus.OK, SERVICE);
    }

    // TODO:
    // Define when this endpoint will be called ?????
    // This is something that has me confused as when this endpoint will be triggered...
    @PostMapping("/trigger-alethia-verification")
    @ResponseBody
    public ResponseEntity<Object> triggerAlethiaVerification(@Valid @RequestBody RegisterUserRequest registerUserRequest) throws IOException {
        userService.triggerAlethiaVerification(registerUserRequest);
        return ResponseHandler.responseBody("Verification triggered in alethia", HttpStatus.OK, SERVICE);
    }

    @PostMapping("/receive-user-profile")
    @ResponseBody
    public ResponseEntity<Object> receiveUserInformation(@Valid @RequestBody UserProfile userProfileInfo) {
        userService.saveUserProfileInfo(userProfileInfo);
        return ResponseHandler.responseBody("User profile information received and user created", HttpStatus.CREATED, SERVICE);
    }
}
