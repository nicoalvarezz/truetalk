package com.fyp.postservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProducerServiceProperties {

    private final String notifyFollowersEndpoint;

    public ProducerServiceProperties(
            @Value("${producerService.endpoint.notifyFollowers}") String notifyFollowersEndpoint) {
        this.notifyFollowersEndpoint = notifyFollowersEndpoint;
    }

    public String getNotifyFollowersEndpoint() {
        return notifyFollowersEndpoint;
    }
}
