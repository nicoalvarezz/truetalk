package com.fyp.alethiaservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.alethiaservice.dto.IDPalRequest;
import com.fyp.alethiaservice.dto.UserRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AlethiaServce {

    @Value("${idpal.clientKey}")
    private static String CLIENT_KEY;

    @Value("${idpal.accessKey}")
    private static String ACCESS_KEY;

    @Value("${idpal.profileId.standard}")
    private static int PROFILE_ID;

    @Value("${idpal.enpoint.send}")
    private static String IDPAL_SEND_ENDPOINT;

    private static String INFORMATION_TYPE = "email";
    private static ObjectMapper MAPPER = new ObjectMapper();
    private final OkHttpClient httpClient = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void triggerVerification(UserRequest registerUserData) throws IOException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(CLIENT_KEY)
                .accessKey(ACCESS_KEY)
                .informationType(INFORMATION_TYPE)
                .contact(registerUserData.getEmail())
                .profileId(PROFILE_ID)
                .build();

        RequestBody body = RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON);
        Request request = new Request.Builder()
                .url(IDPAL_SEND_ENDPOINT)
                // I need to investigate how access token work and how would be the best way to store them
                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImZkZjM2OGRkMmQzNzNjNmQ2MzIwZjUxNDJkYTc4ZDY2M2VkNWIzMTg0N2E1NzdiYjczNWY0Mzk0ZmE3N2FhNWFkZjE2ODliNTlmZmNlYzMzIn0")
                .addHeader("Accept", "application/json")
                .post(body)
                .build();

        Response response = httpClient.newCall(request).execute();
    }
}
