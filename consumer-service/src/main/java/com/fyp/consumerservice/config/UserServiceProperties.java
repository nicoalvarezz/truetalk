package com.fyp.consumerservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserServiceProperties {

    private final String sendPostNoificationEndpoint;

    public UserServiceProperties(
            @Value("${userService.endpoint.sendPostNotification}") String sendPostNoificationEndpoint) {
        this.sendPostNoificationEndpoint = sendPostNoificationEndpoint;
    }

    public String getSendPostNoificationEndpoint() {
        return sendPostNoificationEndpoint;
    }
}
