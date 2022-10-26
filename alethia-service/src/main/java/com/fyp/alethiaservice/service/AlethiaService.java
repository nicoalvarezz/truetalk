package com.fyp.alethiaservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.alethiaservice.config.IDPalProperties;
import com.fyp.alethiaservice.config.UserServiceProperties;
import com.fyp.alethiaservice.dto.TriggerVerification;
import com.fyp.alethiaservice.dto.idpal.IDPalRequest;
import com.fyp.alethiaservice.dto.users.UserProfileInfo;
import com.fyp.alethiaservice.dto.users.UserRequest;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class AlethiaService {

    private IDPalProperties idPalProperties = new IDPalProperties();
    private UserServiceProperties userServiceProperties = new UserServiceProperties();

    private static String INFORMATION_TYPE = "email"; // This is only temporal -> I need to decide whether the user can choose or not ???
    private static Logger LOGGER = LoggerFactory.getLogger(AlethiaService.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String EMPTY_ACCESS_TOKEN = "";
    private static String POST_METHOD = "POST";
    private static String CLIENT_KEY_PROPERTY_NAME = "clientKey";
    private static String ACCESS_KEY_PROPERTY_NAME = "accessKey";
    private static String PROFILE_NAME = "standard";
    private static String SEND_VERIFICATION_LINK_ENDPOINT = "sendVerificationLink";
    private static String GET_SUBMISSION_DETAILS_ENDPOINT = "getSubmissionDetails";
    private static String RECEIVE_SUBMISSION_DETAILS_ENDPOINT = "receiveUserProfile";

    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public TriggerVerification triggerVerification(UserRequest registerUserData) throws IOException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(idPalProperties.getApiAccess().get(CLIENT_KEY_PROPERTY_NAME))
                .accessKey(idPalProperties.getApiAccess().get(ACCESS_KEY_PROPERTY_NAME))
                .informationType(INFORMATION_TYPE)
                .contact(registerUserData.getEmail())
                .profileId(idPalProperties.getProfileId().get(PROFILE_NAME))
                .build();

        Response response = ApiHelpers.makeAPIRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        idPalProperties.getEndpoint().get(SEND_VERIFICATION_LINK_ENDPOINT),
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idPalProperties.getApiAccess().get(ACCESS_KEY_PROPERTY_NAME)
                )
        );

        TriggerVerification triggerVerificationResponse = MAPPER.readValue(response.body().string(), TriggerVerification.class);
        triggerVerificationResponse.setStatusCode(response.code());

        LOGGER.info(triggerVerificationResponse.toString());
        return triggerVerificationResponse;
    }

    public UserProfileInfo retrieveUserPersonalInfo(int submissionId) throws IOException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(idPalProperties.getApiAccess().get(CLIENT_KEY_PROPERTY_NAME))
                .accessKey(idPalProperties.getApiAccess().get(ACCESS_KEY_PROPERTY_NAME))
                .submissionId(submissionId)
                .contentDisposition("inline")
                .build();

        Response response = ApiHelpers.makeAPIRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        idPalProperties.getEndpoint().get(GET_SUBMISSION_DETAILS_ENDPOINT),
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idPalProperties.getApiAccess().get("accessToken")
                )
        );

        UserProfileInfo personalInfoResponse = MAPPER.readValue(response.body().string(), UserProfileInfo.class);

        LOGGER.info(personalInfoResponse.toString());
        return personalInfoResponse;
    }

    public void sendUserProfileToUserService(UserProfileInfo userProfileInfo) throws JsonProcessingException {
            Response response = ApiHelpers.makeAPIRequest(
                    ApiHelpers.generateRequest(
                            POST_METHOD,
                            userServiceProperties.getEndpoint().get(RECEIVE_SUBMISSION_DETAILS_ENDPOINT),
                            RequestBody.create(MAPPER.writeValueAsString(userProfileInfo), JSON),
                            EMPTY_ACCESS_TOKEN
                    )
            );
            LOGGER.info(response.toString());
    }
}
