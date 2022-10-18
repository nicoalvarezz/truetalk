package com.fyp.alethiaservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.alethiaservice.dto.idpal.IDPalRequest;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class APIHelpers {

    @Value("${idpal.apiAccess.clientKey}")
    private String idpalClientKey;

    @Value("${idpal.apiAccess.accessKey}")
    private String idpalAccessKey;

    @Value("${idpal.apiAccess.clientId}")
    private String idpalClientId;

    @Value("${idpal.apiAccess.refreshToken}")
    private String idpalRefreshToken;

    @Value("${idpal.endpoint.getAccessToken}")
    private String idpalGetAccessTokenEndpoint;

    @Value("${idpal.apiAccess.accessToken}")
    private String idpalAccessToken;

    private static Logger LOGGER = LoggerFactory.getLogger(APIHelpers.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final OkHttpClient httpClient = new OkHttpClient();

    public static Response makeAPIRequest(Request request) {
        try {
            return httpClient.newCall(request).execute();
        } catch (IOException e) {
            LOGGER.error(e.toString());
            return new Response.Builder()
                    .message("Something occurred down the serviced you called")
                    .code(HttpStatus.OK.value())
                    .build();
        }
    }

    public static Request generateRequest(String endpoint, RequestBody body, String accessToken) {
        if (!accessToken.isEmpty()) {
            // TODO:
            // Add access token to header
            // Have some sort of Header()
        }

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json")
                .method("POST", body)
                .build();


        return request;
    }

    private void renewAccessToken() throws JsonProcessingException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(idpalClientKey)
                .accessKey(idpalAccessKey)
                .clientId(idpalClientId)
                .refreshToken(idpalRefreshToken)
                .build();

        Response responseMap = makeAPIRequest(
                generateRequest(idpalGetAccessTokenEndpoint,
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idpalAccessToken
                )
        );
    }
}
