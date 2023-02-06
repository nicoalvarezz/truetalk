package com.fyp.postservice;

import com.fyp.postservice.model.Comment;
import com.fyp.postservice.model.Post;
import com.fyp.postservice.repository.PostRepository;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
class EntityTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

	@Autowired
	private PostRepository postRepository;

	@Mock
	private Post postMock;

	@Mock
	private Comment commentMock;

	private static final String POST_TEXT = "Text post";
	private static final String POST_USER = UUID.randomUUID().toString();
	private static final int LIKES = 12;
	private static final String COMMENT_USER = UUID.randomUUID().toString();
	private static final String COMMENT_TEXT = "Text comment";

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
		dymDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	void setUp() {
		postRepository.deleteAll();

		when(postMock.getText()).thenReturn(POST_TEXT);
		when(postMock.getUser()).thenReturn(POST_USER);
		when(postMock.getLikes()).thenReturn(LIKES);
		when(commentMock.getText()).thenReturn(COMMENT_TEXT);
		when(commentMock.getUser()).thenReturn(COMMENT_USER);
	}

	@Test
	void testValidPostInsert() {
		postRepository.save(Post.builder()
				.text(postMock.getText())
				.user(postMock.getUser())
				.build());

		assertTrue(postRepository.findAll().size() == 1);
		assertEquals(postRepository.findAll().get(0).getText(), postMock.getText());
		assertEquals(postRepository.findAll().get(0).getUser(), postMock.getUser());

		Post actualPost = postRepository.findAll().get(0);

		actualPost.setLikes(postMock.getLikes());
		postRepository.save(actualPost);

		assertEquals(postRepository.findAll().get(0).getLikes(), LIKES);

		Comment comment = Comment.builder()
				.text(COMMENT_TEXT)
				.user(COMMENT_USER)
				.build();

		List<Comment> comments = Stream.of(comment).collect(Collectors.toList());

		actualPost = postRepository.findAll().get(0);
		actualPost.setComments(comments);

		postRepository.save(actualPost);

		assertEquals(postRepository.findAll().get(0).getComments(), comments);
		assertEquals(postRepository.findAll().get(0).getComments().get(0).getText(), commentMock.getText());
		assertEquals(postRepository.findAll().get(0).getComments().get(0).getUser(), commentMock.getUser());
	}
}
