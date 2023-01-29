package com.fyp.postservice.controller;

import com.fyp.hiveshared.api.responses.ResponseHandler;
import com.fyp.postservice.model.Post;
import com.fyp.postservice.repository.PostRepository;
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

    private final PostRepository postRepository;

    private static String SERVICE = "post-service";

    @GetMapping("/test")
    public ResponseEntity<Object> test() {
        Post post = Post.builder()
                .cratedAt("Wed Sep 09 00:00:00 +0000 2020")
                .text("Testing that the post is saved in the db")
                .likes(12)
                .user("682c8bae-9f43-11ed-9be3-4395a0af5ff3")
                .build();

        postRepository.save(post);
        return ResponseHandler.responseBody("Hello from post-service", HttpStatus.OK, SERVICE);
    }
}
