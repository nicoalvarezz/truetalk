package com.fyp.userservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ApiHelpers {

    private static Logger LOGGER = LoggerFactory.getLogger(ApiHelpers.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final String REQUEST_EXCEPTION = "Something occurred down the service that has been called";
    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static Response makeAPIRequest(Request request) {
        try {
            return HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            LOGGER.error(e.toString());
            return new Response.Builder()
                    .message(REQUEST_EXCEPTION)
                    .code(HttpStatus.OK.value())
                    .build();
        }
    }

    public static Request generateRequest(String method, String endpoint, RequestBody body, String accessToken) {
        Headers headers = !accessToken.isEmpty()
                            ? new Headers.Builder()
                                .add("Accept", "application/json")
                                .add("Authorization", "Bearer " + accessToken)
                                .build()
                            : new Headers.Builder()
                                .add("Accept", "application/json")
                                .build();

        return new Request.Builder()
                .url(endpoint)
                .headers(headers)
                .method(method, body)
                .build();
    }
}