package com.fyp.postservice.controller;

import com.fyp.hiveshared.api.responses.ResponseHandlers;
import com.fyp.postservice.dto.UserPost;
import com.fyp.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/")
@RequiredArgsConstructor
public class PostsController {

    private static String SERVICE = "post-service";
    private final PostService postService;

    @PostMapping("/save-post")
    public ResponseEntity<Map<String, Object>> savePost(@Valid @RequestBody UserPost userPost) {
        postService.savePost(userPost);
        return ResponseHandlers.baseResponseBody("Post saved successfully", HttpStatus.CREATED, SERVICE);
    }

    @GetMapping("/list-followee-posts")
    public ResponseEntity<Map<String, Object>> listFolloweePost(@RequestParam(value = "uuid") String uuid) {
        return ResponseHandlers.responseBodyWithData("List of followee post retrieved successfully",
                HttpStatus.OK,
                SERVICE,
                new HashMap<>(){{ put("followees_posts", postService.getFolloweePosts(uuid));}}
        );
    }
}
