package com.fyp.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProducerServiceProperties {

    private final String confirmationEmailEventEndpoint;

    public ProducerServiceProperties(
            @Value("${producerService.endpoint.confirmationEmailEvent}") String confirmationEmailEventEndpoint) {
        this.confirmationEmailEventEndpoint = confirmationEmailEventEndpoint;
    }

    public String getConfirmationEmailEventEndpoint() {
        return confirmationEmailEventEndpoint;
    }
}
