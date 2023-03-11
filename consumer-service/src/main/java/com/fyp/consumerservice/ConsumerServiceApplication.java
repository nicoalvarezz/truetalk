package com.fyp.consumerservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.consumerservice.config.UserServiceProperties;
import com.fyp.consumerservice.dto.ConfirmationUser;
import com.fyp.consumerservice.dto.PostSender;
import com.fyp.hiveshared.api.helpers.ApiHelpers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;

@SpringBootApplication
public class ConsumerServiceApplication {

	@Autowired
	private UserServiceProperties userServiceProperties;

	private static final String POST_NOTIFICATION_TOPIC = "post-notification-topic";
	private static final String CONFIRMATION_EMAIL_TOPIC = "confirmation-email-topic";
	private static final String TEST_GROUP = "test-group";
	private static final String EMPTY_ACCESS_TOKEN = "";
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private static ObjectMapper MAPPER = new ObjectMapper()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public static void main(String[] args) {
		SpringApplication.run(ConsumerServiceApplication.class, args);
	}

	@KafkaListener(topics = POST_NOTIFICATION_TOPIC, groupId = TEST_GROUP)
	public void userPostEventListener(String uuid) throws IOException {
		ApiHelpers.makeApiRequest(
				ApiHelpers.postRequest(
						userServiceProperties.getSendPostNotificationEndpoint(),
						RequestBody.create(MAPPER.writeValueAsString(PostSender.builder().uuid(uuid).build()), JSON),
						EMPTY_ACCESS_TOKEN
				)
		);
	}

	@KafkaListener(topics = CONFIRMATION_EMAIL_TOPIC, groupId = TEST_GROUP)
	public void confirmationEmailEventListener(String email) throws IOException{
		ApiHelpers.makeApiRequest(
				ApiHelpers.postRequest(
						userServiceProperties.getSendConfirmationEmailEndpoint(),
						RequestBody.create(MAPPER.writeValueAsString(ConfirmationUser.builder().email(email).build()), JSON),
						EMPTY_ACCESS_TOKEN
				)
		);
	}
}
