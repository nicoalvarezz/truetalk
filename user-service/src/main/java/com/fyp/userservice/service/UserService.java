package com.fyp.userservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.userservice.dto.AlethiaRequest;
import com.fyp.userservice.dto.TriggerVerificationResponse;
import com.fyp.userservice.dto.RegisterUserRequest;
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
public class UserService {

    @Value("${alethia.endpoint.triggerVerification}")
    private String alethiaTriggerVerificationEndpoint;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static String POST_METHOD = "POST";
    private static String EMPTY_ACCESS_TOKEN = "";
    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public TriggerVerificationResponse triggerAlethiaVerification(RegisterUserRequest registerUserRequest) throws IOException {
        AlethiaRequest alethiaRequest = AlethiaRequest.builder()
                .email(registerUserRequest.getEmail())
                .phoneNumber(registerUserRequest.getPhoneNumber())
                .build();

        Response response = ApiHelpers.makeAPIRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        alethiaTriggerVerificationEndpoint,
                        RequestBody.create(MAPPER.writeValueAsString(alethiaRequest), JSON),
                        EMPTY_ACCESS_TOKEN
                )
        );

        TriggerVerificationResponse triggerVerificationResponse = MAPPER.readValue(response.body().string(), TriggerVerificationResponse.class);
        triggerVerificationResponse.setStatusCode(response.code());

        LOGGER.info(triggerVerificationResponse.toString());
        return triggerVerificationResponse;
    }
}
