package com.fyp.alethiaservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.alethiaservice.config.IdPalProperties;
import com.fyp.alethiaservice.config.UserServiceProperties;
import com.fyp.alethiaservice.dto.idpal.IDPalRequest;
import com.fyp.alethiaservice.dto.users.UserProfileInfo;
import com.fyp.alethiaservice.dto.users.UserRequest;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class AlethiaService {

    @Autowired
    private IdPalProperties idPalProperties;

    @Autowired
    private UserServiceProperties userServiceProperties;

    private static String INFORMATION_TYPE = "email"; // This is only temporal -> I need to decide whether the user can choose or not ???
    private static Logger LOGGER = LoggerFactory.getLogger(AlethiaService.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String EMPTY_ACCESS_TOKEN = "";
    private static String POST_METHOD = "POST";

    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public void triggerVerification(UserRequest registerUserData) throws IOException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(idPalProperties.getClientKey())
                .accessKey(idPalProperties.getAccessKey())
                .informationType(INFORMATION_TYPE)
                .contact(registerUserData.getEmail())
                .profileId(idPalProperties.getProfileId())
                .build();

        ApiHelpers.makeAPIRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        idPalProperties.getSendVerificationLink(),
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idPalProperties.getAccessToken()
                )
        );
    }

    public UserProfileInfo retrieveUserPersonalInfo(int submissionId) throws IOException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(idPalProperties.getClientKey())
                .accessKey(idPalProperties.getAccessKey())
                .submissionId(submissionId)
                .contentDisposition("inline")
                .build();

        Response response = ApiHelpers.makeAPIRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        idPalProperties.getGetSubmissionDetailsEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idPalProperties.getAccessToken()
                )
        );

        UserProfileInfo personalInfoResponse = MAPPER.readValue(response.body().string(), UserProfileInfo.class);

        LOGGER.info(personalInfoResponse.toString());
        return personalInfoResponse;
    }

    public void sendUserProfileToUserService(UserProfileInfo userProfileInfo) throws JsonProcessingException {
        ApiHelpers.makeAPIRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        userServiceProperties.getReceiveUserProfileEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(userProfileInfo), JSON),
                        EMPTY_ACCESS_TOKEN
                )
        );
    }
}
