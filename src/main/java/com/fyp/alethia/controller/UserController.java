package com.fyp.alethia.controller;

import com.fyp.alethia.dto.IDPalWebHookRequest;
import com.fyp.alethia.dto.UserRequest;
import com.fyp.alethia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create-user")
    @ResponseStatus(HttpStatus.OK)
    public String createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        System.out.println("HELLO: "+ userRequest.getPhoneNumber());
        return "Success";
    }

    @PostMapping("/recieve-webhook")
    @ResponseStatus(HttpStatus.OK)
    public String receiveWebHook(@RequestBody IDPalWebHookRequest webHookRequest) {
        return "Webhook: " + webHookRequest.toString();
    }
}
