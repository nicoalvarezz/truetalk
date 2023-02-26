package com.fyp.postservice.controller;

import com.fyp.hiveshared.api.responses.ResponseHandlers;
import com.fyp.postservice.dto.PostComment;
import com.fyp.postservice.dto.PostLike;
import com.fyp.postservice.dto.PostUnlike;
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
    public ResponseEntity<Map<String, Object>> like(@Valid @RequestBody PostLike postLike) {
        postService.likePost(postLike);
        return ResponseHandlers.responseBody("Post liked successfully", HttpStatus.OK, SERVICE);
    }

    @PutMapping("/unlike")
    public ResponseEntity<Map<String, Object>> unlike(@Valid @RequestBody PostUnlike postUnlike){
        postService.unlikePost(postUnlike);
        return ResponseHandlers.responseBody("Post unliked successfully", HttpStatus.OK, SERVICE);
    }

    @PostMapping("/comment")
    public ResponseEntity<Map<String, Object>> comment(@RequestBody PostComment postComment) {
        postService.savePostComment(postComment);
        return ResponseHandlers.responseBody("Post commented saved successfully", HttpStatus.CREATED, SERVICE);
    }

    @GetMapping("/likes")
    public ResponseEntity<Map<String, Object>> likes(@RequestParam(value = "pot_id") String postId) {
        return ResponseHandlers.responseBody(
                "Post likes retrieved successfully",
                HttpStatus.OK,
                SERVICE,
                new HashMap<>(){{ put("post_likes", postService.getLikes(postId)); }}
        );
    }
}
