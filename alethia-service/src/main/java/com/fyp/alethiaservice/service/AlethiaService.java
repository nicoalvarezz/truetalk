package com.fyp.alethiaservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.alethiaservice.dto.IDPalRequest;
import com.fyp.alethiaservice.dto.UserRequest;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;


@Service
@Component
@PropertySource("classpath:application.properties")
public class AlethiaService {

    // There is a better way on how to do this -> Remember that this variables are not static,
    // so it should not be written in Upper Case
    // Use something that is global to the class (I don't remember the technical name, but that exists)
    @Value("${idpal.apiAccess.clientKey}")
    private String IDPAL_CLIENT_KEY;

    @Value("${idpal.apiAccess.accessKey}")
    private String IDPAL_ACCESS_KEY;

    @Value("${idpal.apiAccess.clientId}")
    private String IDPAL_CLIENT_ID;

    @Value("${idpal.profileId.standard}")
    private int IDPAL_PROFILE_ID;

    @Value("${idpal.endpoint.send}")
    private String IDPAL_SEND_ENDPOINT;

    @Value("${idpal.endpoint.getAccessToken}")
    private String IDPAL_GET_ACCESS_TOKEN_ENDPOINT;

    @Value("${idpal.apiAccess.accessToken}")
    private String IDPAL_ACCESS_TOKEN;

    @Value("${idpal.apiAccess.refreshToken}")
    private String IDPAL_REFRESH_TOKEN;

    private static String INFORMATION_TYPE = "email";
    private static ObjectMapper MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient httpClient = new OkHttpClient();

    // TODO:
    // Understand how ID-Pal access token work, how should it be stored? Does it expire? If so, how to renew access token?
    // What should happen with the response? 200 response right? -> That is what makes sense to me

    public void triggerVerification(UserRequest registerUserData) throws JsonProcessingException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(IDPAL_CLIENT_KEY)
                .accessKey(IDPAL_ACCESS_KEY)
                .informationType(INFORMATION_TYPE)
                .contact(registerUserData.getEmail())
                .profileId(IDPAL_PROFILE_ID)
                .build();

        RequestBody body = RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON);
        Response response = makeIdPalRequest(
                generateIdPalRequest(IDPAL_SEND_ENDPOINT, body)
        );

        // What does this return ?????
    }

    private void renewAccessToken() throws JsonProcessingException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(IDPAL_CLIENT_KEY)
                .accessKey(IDPAL_ACCESS_KEY)
                .clientId(IDPAL_CLIENT_ID)
                .refreshToken(IDPAL_REFRESH_TOKEN)
                .build();

        RequestBody body = RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON);
        Response response = makeIdPalRequest(
                generateIdPalRequest(IDPAL_GET_ACCESS_TOKEN_ENDPOINT, body)
        );
    }

    private Request generateIdPalRequest(String endpoint, RequestBody body) {
        return new Request.Builder()
                .url(endpoint)
                .addHeader("Authorization", "Bearer " + IDPAL_ACCESS_TOKEN)
                .addHeader("Accept", "application/json")
                .post(body)
                .build();
    }

    @SneakyThrows
    private Response makeIdPalRequest(Request request) {
        return httpClient.newCall(request).execute();
    }
}
