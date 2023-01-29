package com.fyp.postservice.controller;

import com.fyp.hiveshared.api.responses.ResponseHandler;
import com.fyp.postservice.dto.UserPost;
import com.fyp.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts/")
@RequiredArgsConstructor
public class PostsController {

    private static String SERVICE = "post-service";
    private final PostService postService;

    @PostMapping("/save-post")
    public ResponseEntity<Object> savePost(@Valid @RequestBody UserPost userPost) {
        postService.processAndSavePost(userPost);
        return ResponseHandler.responseBody("Post saved successfully", HttpStatus.CREATED, SERVICE);
    }
}
