package com.fyp.postservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserServiceProperties {

    private final String userListFollowees;

    public UserServiceProperties(
            @Value("${userservice.endpoint.listFollowees}") String userListFollowees) {
        this.userListFollowees = userListFollowees;
    }

    public String getUserListFollowees() {
        return userListFollowees;
    }
}
