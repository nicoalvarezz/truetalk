package com.fyp.userservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailContents {

    private final String postNotificationSubject;
    private final String postNotificationFirstPart;
    private final String postNotificationSecondPart;
    private final String postNotificationThirdPart;
    private final String confirmationEmailSubject;
    private final String confirmationEmailFirstPart;
    private final String confirmationEmailSecondPart;

    public MailContents(
            @Value("${email.postNotification.subject}") String postNotificationSubject,
            @Value("${email.postNotification.firstPart}") String postNotificationFirstPart,
            @Value("${email.postNotification.secondPart}") String postNotificationSecondPart,
            @Value("${email.postNotification.thirdPart}") String postNotificationThirdPart,
            @Value("${email.confirmationEmail.subject}") String confirmationEmailSubject,
            @Value("${email.confirmationEmail.firstPart}") String confirmationEmailFirstPart,
            @Value("${email.confirmationEmail.secondPart}") String confirmationEmailSecondPart) {
        this.postNotificationSubject = postNotificationSubject;
        this.postNotificationFirstPart = postNotificationFirstPart;
        this.postNotificationSecondPart = postNotificationSecondPart;
        this.postNotificationThirdPart = postNotificationThirdPart;
        this.confirmationEmailSubject = confirmationEmailSubject;
        this.confirmationEmailFirstPart = confirmationEmailFirstPart;
        this.confirmationEmailSecondPart = confirmationEmailSecondPart;
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

    public String getConfirmationEmailSubject() {
        return confirmationEmailSubject;
    }

    public String getConfirmationEmailFirstPart() {
        return confirmationEmailFirstPart;
    }

    public String getConfirmationEmailSecondPart() {
        return confirmationEmailSecondPart;
    }
}
