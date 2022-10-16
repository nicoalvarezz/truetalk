package com.fyp.userservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.userservice.dto.AlethiaRequest;
import com.fyp.userservice.dto.UserServiceResponse;
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
import java.util.HashMap;

@Service
@Component
public class UserService {

    @Value("${alethia.endpoint.triggerVerification}")
    private String alethiaTriggerVerificationEndpoint;

    private static ObjectMapper MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final OkHttpClient httpClient = new OkHttpClient();

    public UserServiceResponse triggerAlethiaVerification(RegisterUserRequest registerUserRequest) throws JsonProcessingException {
        AlethiaRequest alethiaRequest = AlethiaRequest.builder()
                .email(registerUserRequest.getEmail())
                .phoneNumber(registerUserRequest.getPhoneNumber())
                .build();

        HashMap<String, String> responseMap = makeAlethiaRequest(
                generateAlethiaRequest(alethiaTriggerVerificationEndpoint,
                        RequestBody.create(MAPPER.writeValueAsString(alethiaRequest), JSON))
        );

//        UserServiceResponse userServiceResponse = MAPPER.convertValue(responseMap, UserServiceResponse.class);

        return UserServiceResponse.builder()
                .message(responseMap.get("message"))
                .status(Integer.parseInt(responseMap.get("status_code")))
                .build();
    }

    private Request generateAlethiaRequest(String endpoint, RequestBody body) {
        return new Request.Builder()
                .url(endpoint)
                .addHeader("Accept", "application/json")
                .post(body)
                .build();
    }

    private HashMap<String, String> makeAlethiaRequest(Request request) {
        try {
            Response response = httpClient.newCall(request).execute();
            HashMap<String, String> responseMap = MAPPER.readValue(response.body().string(), HashMap.class);
            responseMap.put("status", String.valueOf(response.code()));
            return responseMap;
        } catch (IOException e) {
            LOGGER.error(e.toString());
            return new HashMap<>() {{
                put("message", "The request was valid. Something occurred with Alethia service");
                put("status_code", String.valueOf(HttpStatus.OK));
            }};
        }
    }
}
