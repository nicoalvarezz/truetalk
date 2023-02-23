package com.fyp.postservice.controller;

import com.fyp.hiveshared.api.responses.ResponseHandlers;
import com.fyp.postservice.dto.UserPost;
import com.fyp.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        return ResponseHandlers.responseBody("Post saved successfully", HttpStatus.CREATED, SERVICE);
    }

    @GetMapping("/followee-posts")
    public ResponseEntity<Map<String, Object>> followeePost(@RequestParam(value = "uuid") String uuid) {
        return ResponseHandlers.responseBody("List of followee post retrieved successfully",
                HttpStatus.OK,
                SERVICE,
                new HashMap<>(){{ put("followees_posts", postService.getFolloweePosts(uuid)); }}
        );
    }

    @GetMapping("/user-posts")
    public ResponseEntity<Map<String, Object>> userPosts(@RequestParam(value = "uuid") String uuid) {
        return ResponseHandlers.responseBody(
                "user posts retrieve successfully",
                HttpStatus.OK,
                SERVICE,
                new HashMap<>(){{ put("user_posts", postService.getUserPosts(uuid)); }}
        );
    }

    @PutMapping("/like")
    public ResponseEntity<Map<String, Object>> like(@RequestParam(value = "post_id") String postId) {
        postService.likePost(postId);
        return ResponseHandlers.responseBody("Post liked successfully", HttpStatus.CREATED, SERVICE);
    }

    @PutMapping("/unlike")
    public ResponseEntity<Map<String, Object>> unlike(@RequestParam(value = "post_id") String postId) {
        postService.unlikePost(postId);
        return ResponseHandlers.responseBody("Post unliked successfully", HttpStatus.CREATED, SERVICE);
    }
}
