package com.fyp.userservice.controller;

import com.fyp.userservice.dto.TriggerVerificationResponse;
import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.dto.UserProfileInfo;
import com.fyp.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/trigger-alethia-verification")
    @ResponseBody
    public TriggerVerificationResponse triggerAlethiaVerification(@RequestBody RegisterUserRequest registerUserRequest) throws IOException {
        return userService.triggerAlethiaVerification(registerUserRequest);
    }

    @PostMapping("/receive-user-profile")
    @ResponseBody
    public void receiveUserInformation(@RequestBody UserProfileInfo userProfileInfo) {
        System.out.println("Thanks bro, I got the data. You can see it yourself: " + userProfileInfo.toString());
    }
}
