package com.fyp.consumerservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserServiceProperties {

    private final String sendPostNotificationEndpoint;
    private final String sendConfirmationEmailEndpoint;

    public UserServiceProperties(
            @Value("${userService.endpoint.sendPostNotification}") String sendPostNoificationEndpoint,
            @Value("${userService.endpoint.sendConfirmationEmail}") String sendConfirmationEmailEndpoint) {
        this.sendPostNotificationEndpoint = sendPostNoificationEndpoint;
        this.sendConfirmationEmailEndpoint = sendConfirmationEmailEndpoint;
    }

    public String getSendPostNotificationEndpoint() {
        return sendPostNotificationEndpoint;
    }

    public String getSendConfirmationEmailEndpoint() {
        return sendConfirmationEmailEndpoint;
    }
}
