package com.fyp.alethiaservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class IdPalProperties {

    private final String clientKey;
    private final String accessKey;
    private final int profileId;
    private final String sendVerificationLink;
    private String accessToken;
    private String getSubmissionDetailsEndpoint;

    @Autowired
    public IdPalProperties(
            @Value("${idpal.apiAccess.clientKey}") String clientKey,
            @Value("${idpal.apiAccess.accessKey}") String accessKey,
            @Value("${idpal.profileId.standard}") int profileId,
            @Value("${idpal.endpoint.sendVerificationLink}") String sendVerificationLink,
            @Value("${idpal.apiAccess.accessToken}") String accessToken,
            @Value("${idpal.endpoint.getSubmissionDetails}") String getSubmissionDetailsEndpoint
    ) {
        this.clientKey = clientKey;
        this.accessKey = accessKey;
        this.profileId = profileId;
        this.sendVerificationLink = sendVerificationLink;
        this.accessToken = accessToken;
        this.getSubmissionDetailsEndpoint = getSubmissionDetailsEndpoint;
    }

    public String getClientKey() {
        return clientKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public int getProfileId() {
        return profileId;
    }

    public String getSendVerificationLink() {
        return sendVerificationLink;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getGetSubmissionDetailsEndpoint() {
        return getSubmissionDetailsEndpoint;
    }
}
