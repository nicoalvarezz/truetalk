package com.fyp.userservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.userservice.dto.AlethiaRequest;
import com.fyp.userservice.dto.TriggerVerificationResponse;
import com.fyp.userservice.dto.RegisterUserRequest;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Component
public class UserService {

    @Value("${alethia.endpoint.triggerVerification}")
    private String alethiaTriggerVerificationEndpoint;

    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static String REQUEST_ERROR = "The request was valid. Something occurred with the called service";

    private final OkHttpClient httpClient = new OkHttpClient();

    public TriggerVerificationResponse triggerAlethiaVerification(RegisterUserRequest registerUserRequest) throws IOException {
        AlethiaRequest alethiaRequest = AlethiaRequest.builder()
                .email(registerUserRequest.getEmail())
                .phoneNumber(registerUserRequest.getPhoneNumber())
                .build();

        Response response = makeRequest(
                generateAlethiaRequest(alethiaTriggerVerificationEndpoint,
                        RequestBody.create(MAPPER.writeValueAsString(alethiaRequest), JSON))
        );
        TriggerVerificationResponse triggerVerificationResponse = MAPPER.readValue(response.body().string(), TriggerVerificationResponse.class);
        triggerVerificationResponse.setStatusCode(response.code());

        LOGGER.info(triggerVerificationResponse.toString());
        return triggerVerificationResponse;
    }

    private Request generateAlethiaRequest(String endpoint, RequestBody body) {
        return new Request.Builder()
                .url(endpoint)
                .addHeader("Accept", "application/json")
                .post(body)
                .build();
    }

    private Response makeRequest(Request request) {
        try {
            return httpClient.newCall(request).execute();
        } catch (IOException e) {
            LOGGER.error(e.toString());
            return new Response.Builder()
                    .message(REQUEST_ERROR)
                    .code(HttpStatus.OK.value())
                    .build();
        }
    }
}
