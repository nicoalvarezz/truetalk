package com.fyp.alethiaservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.alethiaservice.config.IdPalProperties;
import com.fyp.alethiaservice.config.UserServiceProperties;
import com.fyp.alethiaservice.dto.idpal.IdpalAccessToken;
import com.fyp.alethiaservice.dto.idpal.IdpalRequest;
import com.fyp.alethiaservice.dto.idpal.RenewIdpalAccessToken;
import com.fyp.alethiaservice.dto.users.UserProfileInfo;
import com.fyp.alethiaservice.dto.users.UserRequest;
import com.fyp.hiveshared.api.helpers.ApiHelpers;
import com.fyp.hiveshared.api.responses.excpetion.ServiceUnavailableException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class AlethiaService {

    @Autowired
    private IdPalProperties idPalProperties;

    @Autowired
    private UserServiceProperties userServiceProperties;

    private static final String IDPAL_EXCEPTION_MESSAGE = "An error has occurred with identity verification service services";
    private static final String INFORMATION_TYPE = "email"; // This is only temporal -> I need to decide whether the user can choose or not ???
    private static final Logger LOGGER = LoggerFactory.getLogger(AlethiaService.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String EMPTY_ACCESS_TOKEN = "";
    private static final String POST_METHOD = "POST";

    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public void triggerVerification(UserRequest registerUserData) throws IOException, ServiceUnavailableException {
        renewIdpalAccessToken();

        IdpalRequest idPalRequest = IdpalRequest.builder()
                .clientKey(idPalProperties.getClientKey())
                .accessKey(idPalProperties.getAccessKey())
                .informationType(INFORMATION_TYPE)
                .contact(registerUserData.getEmail())
                .profileId(idPalProperties.getProfileId())
                .build();

        Response response = ApiHelpers.makeApiRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        idPalProperties.getSendVerificationLinkEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idPalProperties.getAccessToken()
                )
        );

        // TODO:
        // Create a class in the service layer that is in charge of checking the status of the response
        // And throw the error
        // Less clutter in this class
        if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
            throw new ServiceUnavailableException(IDPAL_EXCEPTION_MESSAGE);
        }
    }

    public UserProfileInfo retrieveUserPersonalInfo(int submissionId) throws IOException {
        renewIdpalAccessToken();

        IdpalRequest idPalRequest = IdpalRequest.builder()
                .clientKey(idPalProperties.getClientKey())
                .accessKey(idPalProperties.getAccessKey())
                .submissionId(submissionId)
                .contentDisposition("inline")
                .build();

        Response response = ApiHelpers.makeApiRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        idPalProperties.getGetSubmissionDetailsEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idPalProperties.getAccessToken()
                )
        );

        UserProfileInfo userProfileInfo = MAPPER.readValue(response.body().string(), UserProfileInfo.class);

        return userProfileInfo;
    }

    public void sendUserProfileToUserService(UserProfileInfo userProfileInfo) throws JsonProcessingException {
        ApiHelpers.makeApiRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        userServiceProperties.getReceiveUserProfileEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(userProfileInfo), JSON),
                        EMPTY_ACCESS_TOKEN
                )
        );
    }

    private void renewIdpalAccessToken() throws IOException {
        String accessToken = idPalProperties.getAccessToken();
        if (!ApiHelpers.isAccessTokenExpired(accessToken)) {
            return;
        } else {
            Response response = ApiHelpers.makeApiRequest(
                    ApiHelpers.generateRequest(
                            POST_METHOD,
                            idPalProperties.getAccessTokenEndpoint(),
                            RequestBody.create(MAPPER.writeValueAsString(getRenewTokenBody()), JSON),
                            accessToken
                    )
            );
            IdpalAccessToken tokens = MAPPER.readValue(response.body().string(), IdpalAccessToken.class);
            idPalProperties.setNewTokens(tokens.getAccessToken(), tokens.getRefreshToken());
        }
    }

    private RenewIdpalAccessToken getRenewTokenBody() {
        return RenewIdpalAccessToken.builder()
                .clientKey(idPalProperties.getClientKey())
                .accessKey(idPalProperties.getAccessKey())
                .clientId(idPalProperties.getClientId())
                .refreshToken(idPalProperties.getRefreshToken())
                .build();
    }
}
