package com.fyp.postservice.service;

import com.fyp.hiveshared.api.helpers.ApiHelpers;
import com.fyp.hiveshared.api.responses.ResponseDeserializer;
import com.fyp.postservice.config.UserServiceProperties;
import com.fyp.postservice.dto.UserPost;
import com.fyp.postservice.model.Post;
import com.fyp.postservice.repository.PostRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Component
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private UserServiceProperties userServiceProperties;

    private final PostRepository postRepository;
    private static final String EMPTY_ACCESS_TOKEN = "";

    public void savePost(UserPost userPost) {

        // TODO:
        // Verify that the user exists!
        Post post = Post.builder()
                .cratedAt(String.valueOf(Instant.now().getEpochSecond()))
                .text(userPost.getText())
                .user(verifyUuid(userPost.getUser()))
                .likes(0)
                .build();

        postRepository.save(post);
    }

    private List<String> requestFolowees(String follower) {
        Response response = ApiHelpers.makeApiRequest(
                ApiHelpers.getRequest(
                        userServiceProperties.getUserListFollowees(),
                        new HashMap<>() {{put("uuid", follower);}},
                        EMPTY_ACCESS_TOKEN
                )
        );

        try {
            ResponseDeserializer responseDeserializer = new Gson().fromJson(response.body().string(), ResponseDeserializer.class);
            return (List<String>) responseDeserializer.getData().get("followees");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<List<Post>> getFolloweePosts(String follower) {
        return requestFolowees(follower)
                .stream()
                .map(postRepository::findByUser)
                .collect(Collectors.toList());
    }

    private String verifyUuid(String uuid) {
        UUID.fromString(uuid);
        return uuid;
    }
}
