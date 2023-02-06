package com.fyp.postservice;

import com.fyp.postservice.dto.UserPost;
import com.fyp.postservice.repository.PostRepository;
import com.fyp.postservice.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
public class PostServiceTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Mock
    private UserPost userPostMock;

    private static final String POST_TEXT = "Text post";
    private static final String POST_USER = UUID.randomUUID().toString();

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
        dymDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();

        when(userPostMock.getText()).thenReturn(POST_TEXT);
        when(userPostMock.getUser()).thenReturn(POST_USER);
    }

    @Test
    void testSavePost() {
        postService.savePost(UserPost.builder()
                .text(userPostMock.getText())
                .user(userPostMock.getUser())
                .build());

        assertTrue(postRepository.findAll().size() == 1);
        assertEquals(postRepository.findAll().get(0).getText(), userPostMock.getText());
        assertEquals(postRepository.findAll().get(0).getUser(), userPostMock.getUser());
    }


    @Test
    void testSavePostWithInvalidUuid() {
        assertThrows(IllegalArgumentException.class, () ->
                postService.savePost(UserPost.builder()
                    .text(userPostMock.getText())
                    .user("invalid_uuid")
                    .build()));

        assertTrue(postRepository.findAll().size() == 0);
    }
}
