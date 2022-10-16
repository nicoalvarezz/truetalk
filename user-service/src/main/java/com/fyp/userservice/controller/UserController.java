package com.fyp.userservice.controller;

import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register-user")
    public void registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        // TODO:
        // Call User service
        System.out.println(registerUserRequest.toString());
    }
}
