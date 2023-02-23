package com.fyp.postservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserServiceProperties {

    private final String listFolloweesEndpoint;
    private final String userProfileEndpoint;

    public UserServiceProperties(
            @Value("${userservice.endpoint.listFollowees}") String listFolloweesEndpoint,
            @Value("${userservice.endpoint.userProfile}") String userProfileEndpoint) {
        this.listFolloweesEndpoint = listFolloweesEndpoint;
        this.userProfileEndpoint = userProfileEndpoint;
    }

    public String getListFolloweesEndpoint() {
        return listFolloweesEndpoint;
    }

    public String getUserProfileEndpoint() {
        return userProfileEndpoint;
    }
}
