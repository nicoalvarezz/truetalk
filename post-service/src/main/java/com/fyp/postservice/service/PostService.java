package com.fyp.postservice.service;

import com.fyp.postservice.dto.UserPost;
import com.fyp.postservice.model.Post;
import com.fyp.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
@Component
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void savePost(UserPost userPost) {

        // TODO:
        // Verify that the user exists!
        Post post = Post.builder()
                .cratedAt(String.valueOf(Instant.now().getEpochSecond()))
                .text(verifyUuid(userPost.getText()))
                .user(userPost.getUser())
                .likes(0)
                .build();

        postRepository.save(post);
    }

    private String verifyUuid(String uuid) {
        UUID.fromString(uuid);
        return uuid;
    }
}
