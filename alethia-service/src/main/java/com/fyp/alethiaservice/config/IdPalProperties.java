package com.fyp.alethiaservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class IdPalProperties {

    private final String clientKey;
    private final String accessKey;
    private final String clientId;
    private String refreshToken;
    private final int profileId;
    private String accessToken;
    private final String sendVerificationLinkEndpoint;
    private String getSubmissionDetailsEndpoint;
    private String getAccessTokenEndpoint;

    @Autowired
    public IdPalProperties(
            @Value("${idpal.apiAccess.clientKey}") String clientKey,
            @Value("${idpal.apiAccess.accessKey}") String accessKey,
            @Value("${idpal.apiAccess.clientId}") String clientId,
            @Value("${idpal.profileId.standard}") int profileId,
            @Value("${idpal.apiAccess.refreshToken}") String refreshToken,
            @Value("${idpal.apiAccess.accessToken}") String accessToken,
            @Value("${idpal.endpoint.sendVerificationLink}") String sendVerificationLinkEndpoint,
            @Value("${idpal.endpoint.getSubmissionDetails}") String getSubmissionDetailsEndpoint,
            @Value("${idpal.endpoint.getAccessToken}") String getAccessTokenEndpoint
    ) {
        this.clientKey = clientKey;
        this.accessKey = accessKey;
        this.clientId = clientId;
        this.refreshToken = refreshToken;
        this.profileId = profileId;
        this.accessToken = accessToken;
        this.sendVerificationLinkEndpoint = sendVerificationLinkEndpoint;
        this.getSubmissionDetailsEndpoint = getSubmissionDetailsEndpoint;
        this.getAccessTokenEndpoint = getAccessTokenEndpoint;
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

    public String getSendVerificationLinkEndpoint() {
        return sendVerificationLinkEndpoint;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getGetSubmissionDetailsEndpoint() {
        return getSubmissionDetailsEndpoint;
    }

    public String getAccessTokenEndpoint() {
        return getAccessTokenEndpoint;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setNewTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
