package com.fyp.merkurioservice;

import com.fyp.merkurioservice.model.Post;
import com.fyp.merkurioservice.repository.PostRepository;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
class ServicePostApplication {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

	@Autowired
	private PostRepository postRepository;

	@Mock
	private Post postMock;

	private static final String POST_TEXT = "Text post";

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
		dymDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	void setUp() {
		postRepository.deleteAll();

		when(postMock.getText()).thenReturn(POST_TEXT);
		when(postMock.getUser()).thenReturn(UUID.randomUUID().toString());
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
	}

}
