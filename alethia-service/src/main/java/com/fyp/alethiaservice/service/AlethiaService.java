package com.fyp.alethiaservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;


@Service
@Component
@PropertySource("classpath:application.properties")
public class AlethiaService {

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
    private final OkHttpClient httpClient = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // TODO:
    // Understand how ID-Pal access token work, how should it be stored? Does it expire? If so, how to renew access token?
    // Better understand SneakyThrows, how does it work? How does it affect performance?
    // What should happen with the response? 200 response right? -> That is what makes sense to me

    public void triggerVerification(UserRequest registerUserData) throws JsonProcessingException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(IDPAL_CLIENT_KEY)
                .accessKey(IDPAL_ACCESS_KEY)
                .informationType(INFORMATION_TYPE)
                .contact(registerUserData.getEmail())
                .profileId(IDPAL_PROFILE_ID)
                .build();

//        System.out.println(CLIENT_KEY);
//        System.out.println(IDPAL_SEND_ENDPOINT);
//        System.out.println(PROFILE_ID);

        RequestBody body = RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON);
        Request request = new Request.Builder()
                .url(IDPAL_SEND_ENDPOINT)
                // I need to investigate how access token work and how would be the best way to store them
                .addHeader("Authorization", "Bearer " + IDPAL_ACCESS_TOKEN)
                .addHeader("Accept", "application/json")
                .post(body)
                .build();

        getAccessToken();
//        do {
//            try {
//                Response response = httpClient.newCall(request).execute();
////                System.out.println(response.body().string());
//                Map<String, String> responseMap = MAPPER.readValue(response.body().toString(), Map.class);
//                if (responseMap.containsKey("error")) {
//                    getAccessToken();
//                }
//
//            } catch (IOException e) {
//                // What status code should this return -> I should never return a 5xx
//                // This will happen due to cancellation, a connectivity problem or a timeout
//                e.printStackTrace();
//            }
//        } while( access_token is expired is the condition);
    }

    private void getAccessToken() throws JsonProcessingException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(IDPAL_CLIENT_KEY)
                .accessKey(IDPAL_ACCESS_KEY)
                .clientId(IDPAL_CLIENT_ID)
                .refreshToken(IDPAL_REFRESH_TOKEN)
                .build();

        RequestBody body = RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON);
        System.out.println(MAPPER.writeValueAsString(idPalRequest));

        Request request = new Request.Builder()
                .url(IDPAL_GET_ACCESS_TOKEN_ENDPOINT)
                // I need to investigate how access token work and how would be the best way to store them
                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjA3OTliY2JhMDViZjBiMmM0MmJiMjY2ZDVkODhmODdmMTEwZWM0MTYxZjIxMTVhYjQ3NzMxOTUxNjMwYjA1NjZiMWRkMmFjODYzNGJjYTVkIn0") // Do not forget about Bearer
                .addHeader("Accept", "application/json")
                .post(body)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            Map<String, String> responseMap = MAPPER.readValue(response.body().string(), Map.class);
            IDPAL_ACCESS_TOKEN = responseMap.get("access_token");
            IDPAL_REFRESH_TOKEN = responseMap.get("refresh_token");
        } catch (IOException e) {
            // What status code should this return -> I should never return a 5xx
            // This will happen due to cancellation, a connectivity problem or a timeout
            e.printStackTrace();
        }
    }
}
