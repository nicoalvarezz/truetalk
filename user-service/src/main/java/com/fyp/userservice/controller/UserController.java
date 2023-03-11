package com.fyp.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.hiveshared.api.responses.ResponseHandlers;
import com.fyp.hiveshared.api.responses.excpetion.UnauthorizedException;
import com.fyp.userservice.dto.ConfirmationUser;
import com.fyp.userservice.dto.FollowRequest;
import com.fyp.userservice.dto.PostSender;
import com.fyp.userservice.dto.UnfollowRequest;
import com.fyp.userservice.dto.LoginUserRequest;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final String SERVICE = "user-service";

    @PostMapping("/register-user")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) throws JsonProcessingException {
        userService.registerUser(registerUserRequest);
        userService.publishConfirmationEmailEvent(registerUserRequest.getEmail());
        return ResponseHandlers.responseBody("User registered successfully", HttpStatus.CREATED, SERVICE);
    }

    @PostMapping("/send-confirmation-email")
    public ResponseEntity<Map<String, Object>> sendConfirmationEmail(@Valid @RequestBody ConfirmationUser confirmationUser) {
        userService.sendConfirmationEmail(confirmationUser.getEmail());
        return ResponseHandlers.responseBody("Confirmation email sent successfully", HttpStatus.CREATED, SERVICE);
    }

    @GetMapping("/registration-confirm")
    public ResponseEntity<Map<String, Object>> registrationConfirm(@RequestParam(value = "token", required = false) String token) throws UnauthorizedException, JsonProcessingException {
        userService.confirmUser(token);
        userService.triggerAlethiaVerification(token);
        return ResponseHandlers.responseBody("User confirmed successfully", HttpStatus.OK, SERVICE);
    }

    // TODO:
    // Define when this endpoint will be called ?????
    // This is something that has me confused as when this endpoint will be triggered...
    @PostMapping("/trigger-alethia-verification")
    public ResponseEntity<Map<String, Object>> triggerAlethiaVerification(@Valid @RequestBody RegisterUserRequest registerUserRequest) throws IOException {
        userService.triggerAlethiaVerification(registerUserRequest);
        return ResponseHandlers.responseBody("Verification triggered in alethia", HttpStatus.OK, SERVICE);
    }

    @PostMapping("/receive-user-profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> receiveUserInformation(@Valid @RequestBody UserProfile userProfileInfo) {
        userService.saveUserProfileInfo(userProfileInfo);
        return ResponseHandlers.responseBody("User profile information received and user created", HttpStatus.CREATED, SERVICE);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        return ResponseHandlers.responseBody(
                "token generated successfully",
                HttpStatus.OK,
                SERVICE,
                new HashMap<>(){{ put("token", userService.generateToken(loginUserRequest));}}
        );
    }

    @PostMapping("/follow")
    public ResponseEntity<Map<String, Object>> follow(@Valid @RequestBody FollowRequest followRequest) {
        userService.follow(followRequest);
        return ResponseHandlers.responseBody("User successfully followed", HttpStatus.CREATED, SERVICE);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Map<String, Object>> unfollow(@Valid @RequestBody UnfollowRequest unfollowRequest) {
        userService.unfollow(unfollowRequest);
        return ResponseHandlers.responseBody("User successfully unfollowed", HttpStatus.OK, SERVICE);
    }

    @GetMapping("/list-followees")
    public ResponseEntity<Map<String, Object>> listFollowees(@Valid @RequestParam(value = "uuid") String uuid) {
        return ResponseHandlers.responseBody(
                "list of followees retrived successfully",
                HttpStatus.OK,
                SERVICE,
                new HashMap<>(){{ put("followees", userService.getFollowees(uuid));}}
        );
    }

    @GetMapping("/user-profile")
    public ResponseEntity<Map<String, Object>> userProfilePage(@Valid @RequestParam(value = "uuid") String uuid) {
        return ResponseHandlers.responseBody(
                "User profile information",
                HttpStatus.OK,
                SERVICE,
                new HashMap<>(){{
                    put("name", userService.getUserName(uuid));
                    put("country", userService.getUserCountry(uuid));
                    put("language", userService.getUserLanguage(uuid));
                }}
        );
    }

    @PostMapping("/send-post-notification")
    public ResponseEntity<Map<String, Object>> sendPostNotification(@Valid @RequestBody PostSender postSender) {
        userService.sendPostNotification(postSender.getUuid());
        return ResponseHandlers.responseBody("Post notification sent successfully", HttpStatus.OK, SERVICE);
    }

    @GetMapping("/find-user")
    public ResponseEntity<Map<String, Object>> findUser(@Valid @RequestParam("first_name") String firstName, @Valid @RequestParam("last_name") String lastName) {
        userService.findUserByFirstAndLastName(firstName, lastName);
        return ResponseHandlers.responseBody("User found", HttpStatus.OK, SERVICE);
    }
}
