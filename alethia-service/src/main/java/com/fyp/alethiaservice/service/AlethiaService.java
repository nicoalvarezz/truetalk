package com.fyp.alethiaservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.alethiaservice.dto.TriggerVerification;
import com.fyp.alethiaservice.dto.idpal.IDPalRequest;
import com.fyp.alethiaservice.dto.users.UserProfileInfo;
import com.fyp.alethiaservice.dto.users.UserRequest;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@Component
public class AlethiaService {

    @Value("${idpal.apiAccess.clientKey}")
    private String idpalClientKey;

    @Value("${idpal.apiAccess.accessKey}")
    private String idpalAccessKey;

    @Value("${idpal.profileId.standard}")
    private int idpalProfileId;

    @Value("${idpal.endpoint.send}")
    private String idpalSendLinkEndpoint;

    @Value("${idpal.apiAccess.accessToken}")
    private String idpalAccessToken;

    @Value("${idpal.endpoint.getSubmissionDetails}")
    private String idpalGetSubmissionDetailsEndpoint;

    @Value("${users.endpoint.receive-user-profile}")
    private String usersReceiveUserProfile;

    private static String INFORMATION_TYPE = "email"; // This is only temporal -> I need to decide whether the user can choose or not ???
    private static Logger LOGGER = LoggerFactory.getLogger(AlethiaService.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String EMPTY_ACCESS_TOKEN = "";
    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public TriggerVerification triggerVerification(UserRequest registerUserData) throws IOException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(idpalClientKey)
                .accessKey(idpalAccessKey)
                .informationType(INFORMATION_TYPE)
                .contact(registerUserData.getEmail())
                .profileId(idpalProfileId)
                .build();

        Response response = APIHelpers.makeAPIRequest(
                APIHelpers.generateRequest(
                        "POST",
                        idpalSendLinkEndpoint,
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idpalAccessToken
                )
        );

        TriggerVerification triggerVerificationResponse = MAPPER.readValue(response.body().string(), TriggerVerification.class);
        triggerVerificationResponse.setStatusCode(response.code());

        LOGGER.info(triggerVerificationResponse.toString());
        return triggerVerificationResponse;
    }

    public UserProfileInfo retrieveUserPersonalInfo(int submissionId) throws IOException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(idpalClientKey)
                .accessKey(idpalAccessKey)
                .submissionId(submissionId)
                .contentDisposition("inline")
                .build();

        Response response = APIHelpers.makeAPIRequest(
                APIHelpers.generateRequest(
                        "POST",
                        idpalGetSubmissionDetailsEndpoint,
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idpalAccessToken
                )
        );

        UserProfileInfo personalInfoResponse = MAPPER.readValue(response.body().string(), UserProfileInfo.class);

        LOGGER.info(personalInfoResponse.toString());
        return personalInfoResponse;
    }

    public void sendUserProfileToUserService(UserProfileInfo userProfileInfo) throws JsonProcessingException {
            Response response = APIHelpers.makeAPIRequest(
                    APIHelpers.generateRequest(
                            "POST",
                            usersReceiveUserProfile,
                            RequestBody.create(MAPPER.writeValueAsString(userProfileInfo), JSON),
                            EMPTY_ACCESS_TOKEN
                    )
            );
            LOGGER.info(response.toString());
    }
}
