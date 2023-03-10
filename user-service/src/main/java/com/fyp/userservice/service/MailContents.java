package com.fyp.userservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailContents {

    private final String postNotificationSubject;
    private final String postNotificationFirstPart;
    private final String postNotificationSecondPart;
    private final String postNotificationThirdPart;

    public MailContents(
            @Value("${email.postNotification.subject}") String postNotificationSubject,
            @Value("${email.postNotification.firstPart}") String postNotificationFirstPart,
            @Value("${email.postNotification.secondPart}") String postNotificationSecondPart,
            @Value("${email.postNotification.thirdPart}") String postNotificationThirdPart) {
        this.postNotificationSubject = postNotificationSubject;
        this.postNotificationFirstPart = postNotificationFirstPart;
        this.postNotificationSecondPart = postNotificationSecondPart;
        this.postNotificationThirdPart = postNotificationThirdPart;
    }

    public String getPostNotificationSubject() {
        return postNotificationSubject;
    }

    public String getPostNotificationFirstPart() {
        return postNotificationFirstPart;
    }

    public String getPostNotificationSecondPart() {
        return postNotificationSecondPart;
    }

    public String getPostNotificationThirdPart() {
        return postNotificationThirdPart;
    }
}
