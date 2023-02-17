package com.fyp.hiveshared.api.helpers;

import com.auth0.jwt.JWT;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class ApiHelpers {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiHelpers.class);
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final String REQUEST_EXCEPTION = "Something occurred down the service that has been called";
    private static final String TOKEN_TYPE = "Bearer ";

    public static Response makeApiRequest(Request request) {
        try {
            return HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            LOGGER.error(e.toString());
            return new Response.Builder()
                    .message(REQUEST_EXCEPTION)
                    .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                    .build();
        }
    }

    public static Request postRequest(String endpoint, RequestBody body, String accessToken) {
        return new Request.Builder()
                .url(endpoint)
                .headers(getHeaders(accessToken))
                .method("POST", body)
                .build();
    }

    public static Request getRequest(String endpoint, String accessToken) {
        return new Request.Builder()
                .url(endpoint)
                .headers(getHeaders(accessToken))
                .build();
    }

    public static Request getRequest(String endpoint, Map<String, String> parameters, String accessToken) {
        HttpUrl.Builder urlBuilder= HttpUrl.parse(endpoint).newBuilder();
        parameters.forEach((key, value) -> urlBuilder.addQueryParameter(key, value));

        return new Request.Builder()
                .url(urlBuilder.build().toString())
                .headers(getHeaders(accessToken))
                .build();
    }

    private static Headers getHeaders(String accessToken) {
        return !accessToken.isEmpty()
                ? new Headers.Builder()
                    .add("Accept", "application/json")
                    .add("Authorization", TOKEN_TYPE + accessToken)
                    .build()
                : new Headers.Builder()
                    .add("Accept", "application/json")
                    .build();
    }
}
