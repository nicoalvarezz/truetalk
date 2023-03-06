package com.fyp.producerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSender {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String POST_NOTIFICATION_TOPIC = "post-notification-topic";

    public void send(String uuid) {
        kafkaTemplate.send(POST_NOTIFICATION_TOPIC, uuid);
    }
}
