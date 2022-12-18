package com.fyp.hiveshared.api.helpers;

import com.auth0.jwt.JWT;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Date;

public class ApiHelpers {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiHelpers.class);
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final String REQUEST_EXCEPTION = "Something occurred down the service that has been called";

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

    public static boolean isAccessTokenExpired(String accessToken) {
        return JWT.decode(accessToken).getExpiresAt().before(new Date());
    }
}
