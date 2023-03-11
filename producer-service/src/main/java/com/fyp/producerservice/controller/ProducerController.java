package com.fyp.producerservice.controller;

import com.fyp.hiveshared.api.responses.ResponseHandlers;
import com.fyp.producerservice.dto.ConfirmationUser;
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

    private static final String SERVICE = "producer-service";
    private static final String POST_NOTIFICATION_TOPIC = "post-notification-topic";
    private static final String CONFIRMATION_EMAIL_TOPIC = "confirmation-email-topic";

    @PostMapping("/notify-followers")
    public ResponseEntity<Map<String, Object>> notifyFollowers(@Valid @RequestBody PostSender postSender) {
        kafkaSender.send(POST_NOTIFICATION_TOPIC, postSender.getUuid());
        return ResponseHandlers.responseBody("Post notification event produced successfully", HttpStatus.OK, SERVICE);
    }

    @PostMapping("/confirmation-email-event")
    public ResponseEntity<Map<String, Object>> confirmationEmailEvent(@Valid @RequestBody ConfirmationUser confirmationUser) {
        kafkaSender.send(CONFIRMATION_EMAIL_TOPIC, confirmationUser.getEmail());
        return ResponseHandlers.responseBody("Confirmation email event produced successfully", HttpStatus.OK, SERVICE);
    }
}
