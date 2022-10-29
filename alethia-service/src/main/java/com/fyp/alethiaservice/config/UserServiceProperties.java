package com.fyp.alethiaservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserServiceProperties {

    private final String receiveUserProfileEndpoint;

    public UserServiceProperties(
            @Value("${users.endpoint.receive-user-profile}") String receiveUserProfileEndpoint
    ) {
        this.receiveUserProfileEndpoint = receiveUserProfileEndpoint;
    }

    public String getReceiveUserProfileEndpoint() {
        return receiveUserProfileEndpoint;
    }
}
