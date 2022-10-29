package com.fyp.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlethiaProperties {

    private final String alethiaTriggerVerificationEndpoint;

    public AlethiaProperties(
            @Value("${alethia.endpoint.triggerVerification}") String alethiaTriggerVerificationEndpoint
    ) {
        this.alethiaTriggerVerificationEndpoint = alethiaTriggerVerificationEndpoint;
    }

    public String getAlethiaTriggerVerificationEndpoint() {
        return alethiaTriggerVerificationEndpoint;
    }
}
