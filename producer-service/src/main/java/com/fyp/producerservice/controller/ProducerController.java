package com.fyp.producerservice.controller;

import com.fyp.hiveshared.api.responses.ResponseHandlers;
import com.fyp.producerservice.dto.PostSender;
import com.fyp.producerservice.service.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/producer")
public class ProducerController {

    @Autowired
    KafkaSender kafkaSender;

    private static String SERVICE = "producer-service";

    private static final String POST_NOTIFICATION_TOPIC = "post-notification-topic";

    @PostMapping("/notify-followers")
    public ResponseEntity<Map<String, Object>> notifyFollowers(@Valid @RequestBody PostSender postSender) {
        System.out.println("UUID: " + postSender.getUuid());
        kafkaSender.send(POST_NOTIFICATION_TOPIC, postSender.getUuid());
        return ResponseHandlers.responseBody("Post notification event produced successfully", HttpStatus.OK, SERVICE);
    }
}
