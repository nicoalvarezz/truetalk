package com.fyp.postservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProducerServaiceProperties {

    private final String notifyFollowersEndpoint;

    public ProducerServaiceProperties(
            @Value("${producerService.endpoint.notifyFollowers}") String notifyFollowersEndpoint) {
        this.notifyFollowersEndpoint = notifyFollowersEndpoint;
    }

    public String getNotifyFollowersEndpoint() {
        return notifyFollowersEndpoint;
    }
}
