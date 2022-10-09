package com.fyp.alethiaservice.controller;

import com.fyp.alethiaservice.dto.UserRequest;
import com.fyp.alethiaservice.service.AlethiaServce;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alethia")
@RequiredArgsConstructor
public class AlethiaController {

    private final AlethiaServce alethiaServce;

    public void triggerVerification(@RequestBody UserRequest userRequest) {

    }
}
