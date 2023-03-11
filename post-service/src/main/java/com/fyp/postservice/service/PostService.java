package com.fyp.postservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hiveshared.api.helpers.ApiHelpers;
import com.fyp.hiveshared.api.responses.ResponseDeserializer;
import com.fyp.postservice.config.ProducerServiceProperties;
import com.fyp.postservice.config.UserServiceProperties;
import com.fyp.postservice.dto.PostComment;
import com.fyp.postservice.dto.PostLike;
import com.fyp.postservice.dto.PostSender;
import com.fyp.postservice.dto.PostUnlike;
import com.fyp.postservice.dto.UserPost;
import com.fyp.postservice.model.Comment;
import com.fyp.postservice.model.Post;
import com.fyp.postservice.repository.CommentRepository;
import com.fyp.postservice.repository.PostRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
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

    @Autowired
    private ProducerServiceProperties producerServaiceProperties;

    @Autowired
    private CommentRepository commentRepository;

    private final PostRepository postRepository;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String EMPTY_ACCESS_TOKEN = "";

    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public void savePost(UserPost userPost) {
        Post post = Post.builder()
                .createdAt(String.valueOf(Instant.now().getEpochSecond()))
                .text(userPost.getText())
                .user(verifyUuid(userPost.getUser()))
                .name(getUserName(userPost.getUser()))
                .likes(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();

        postRepository.save(post);
    }

    private String getUserName(String uuid) {
        Response response = ApiHelpers.makeApiRequest(
                ApiHelpers.getRequest(
                        userServiceProperties.getUserProfileEndpoint(),
                        new HashMap<>() {{put("uuid", uuid);}},
                        EMPTY_ACCESS_TOKEN
                )
        );

        try {
            ResponseDeserializer responseDeserializer = new Gson().fromJson(response.body().string(), ResponseDeserializer.class);
            return (String) responseDeserializer.getData().get("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> requestFolowees(String follower) {
        Response response = ApiHelpers.makeApiRequest(
                ApiHelpers.getRequest(
                        userServiceProperties.getListFolloweesEndpoint(),
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

    public List<Post> getFolloweePosts(String follower) {
        return requestFolowees(follower)
                .stream()
                .map(postRepository::findByUser)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Post> getUserPosts(String uuid) {
        return postRepository.findByUser(uuid);
    }

    private String verifyUuid(String uuid) {
        UUID.fromString(uuid);
        return uuid;
    }

    public void likePost(PostLike postLike) {
        Post post = postRepository.findById(postLike.getPostId()).orElseThrow(IllegalArgumentException::new);
        if (!post.getLikes().contains(postLike.getUser())) {
            post.getLikes().add(postLike.getUser());
            postRepository.save(post);
        }
    }

    public void unlikePost(PostUnlike postUnlike) {
        Post post = postRepository.findById(postUnlike.getPostId()).orElseThrow(IllegalArgumentException::new);
        if (post.getLikes().contains(postUnlike.getUser())) {
            post.getLikes().remove(postUnlike.getUser());
            postRepository.save(post);
        }
    }

    public void savePostComment(PostComment postComment){
        Post post = postRepository.findById(postComment.getPostId()).orElseThrow(IllegalArgumentException::new);
        Comment comment = Comment.builder()
                .user(postComment.getUser())
                .text(postComment.getText())
                .name(getUserName(postComment.getUser()))
                .createdAt(String.valueOf(Instant.now().getEpochSecond()))
                .build();

        commentRepository.save(comment);
        post.getComments().add(comment);
        postRepository.save(post);
    }

    public List<String> getLikes(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(IllegalArgumentException::new);
        return post.getLikes();
    }

    public void publishNotifyFollowersEvent(String user) throws IOException {
        ApiHelpers.makeApiRequest(
                ApiHelpers.postRequest(
                        producerServaiceProperties.getNotifyFollowersEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(PostSender.builder().user(user).build()), JSON),
                        EMPTY_ACCESS_TOKEN
                )
        );
    }
}
