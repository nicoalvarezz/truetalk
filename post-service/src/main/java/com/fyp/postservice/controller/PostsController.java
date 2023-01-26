package com.fyp.postservice.controller;

import com.fyp.hiveshared.api.responses.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/")
@RequiredArgsConstructor
public class PostsController {

    private static String SERVICE = "post-service";

    @GetMapping("/test")
    public ResponseEntity<Object> test() {
        return ResponseHandler.responseBody("Hello from post-service", HttpStatus.OK, SERVICE);
    }
}
