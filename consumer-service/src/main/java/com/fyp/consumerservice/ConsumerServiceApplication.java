package com.fyp.consumerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class ConsumerServiceApplication {

	private static final String POST_NOTIFICATION_TOPIC = "post-notification-topic";
	private static final String TEST_GROUP = "test-group";

	public static void main(String[] args) {
		SpringApplication.run(ConsumerServiceApplication.class, args);
	}

	@KafkaListener(topics = POST_NOTIFICATION_TOPIC, groupId = TEST_GROUP)
	public void userPostEventListener(String uuid) {
		System.out.println("Received Message: " + uuid);
	}
}
