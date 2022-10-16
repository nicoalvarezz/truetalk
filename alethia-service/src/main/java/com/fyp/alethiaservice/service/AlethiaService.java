package com.fyp.alethiaservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.alethiaservice.dto.AlethiaResponse;
import com.fyp.alethiaservice.dto.IDPalRequest;
import com.fyp.alethiaservice.dto.UserPersonalInfo;
import com.fyp.alethiaservice.dto.UserRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;


@Service
@Component
public class AlethiaService {

    @Value("${idpal.apiAccess.clientKey}")
    private String idpalClientKey;

    @Value("${idpal.apiAccess.accessKey}")
    private String idpalAccessKey;

    @Value("${idpal.apiAccess.clientId}")
    private String idpalClientId;

    @Value("${idpal.profileId.standard}")
    private int idpalProfileId;

    @Value("${idpal.endpoint.send}")
    private String idpalSendLinkEndpoint;

    @Value("${idpal.endpoint.getAccessToken}")
    private String idpalGetAccessTokenEndpoint;

    @Value("${idpal.apiAccess.accessToken}")
    private String idpalAccessToken;

    @Value("${idpal.apiAccess.refreshToken}")
    private String idpalRefreshToken;

    @Value("${idpal.endpoint.getSubmissionDetails}")
    private String idpalGetSubmissionDetailsEndpoint;

    private static String INFORMATION_TYPE = "email"; // This is only temporal -> I need to decide whether the user can choose or not ???
    private static ObjectMapper MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static Logger LOGGER = LoggerFactory.getLogger(AlethiaService.class);

    private final OkHttpClient httpClient = new OkHttpClient();

    public AlethiaResponse triggerVerification(UserRequest registerUserData) throws JsonProcessingException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(idpalClientKey)
                .accessKey(idpalAccessKey)
                .informationType(INFORMATION_TYPE)
                .contact(registerUserData.getEmail())
                .profileId(idpalProfileId)
                .build();

        HashMap<String, String> responseMap = makeIdPalRequest(
                generateIdPalRequest(idpalSendLinkEndpoint,
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON))
        );

        AlethiaResponse alethiaResponse = AlethiaResponse.builder()
                .message(responseMap.get("message"))
                .statusCode(Integer.parseInt(responseMap.get("status")))
                .uuid(responseMap.get("uuid")) // I don't think I should be returning the uuid in the response -> Follow-up stories in responses
                .build();

        LOGGER.info(alethiaResponse.toString());
        return alethiaResponse;
    }

    public UserPersonalInfo retrieveUserPersonalInfo(int submissionId) throws JsonProcessingException{
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(idpalClientKey)
                .accessKey(idpalAccessKey)
                .submissionId(submissionId)
                .contentDisposition("inline")
                .build();

        HashMap<String, String> responseMap = makeIdPalRequest(
                generateIdPalRequest(idpalGetSubmissionDetailsEndpoint,
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON))
        );

        return UserPersonalInfo.builder()
                .firstName(responseMap.get("firstname"))
                .lastName(responseMap.get("lastname"))
                .email(responseMap.get("email"))
                .accountId(responseMap.get("account_id"))
                .phoneCountryCode(responseMap.get("phone_country_code"))
                .phoneNumber(responseMap.get("phone"))
                .gender(responseMap.get("gender"))
                .dateOfBirth(responseMap.get("dob"))
                .countryOfBirth(responseMap.get("countryofbirth"))
                .address1(responseMap.get("address1"))
                .address2(responseMap.get("address2"))
                .city(responseMap.get("city"))
                .county(responseMap.get("county"))
                .countryName(responseMap.get("country_name"))
                .postalCode(responseMap.get("postalcode"))
                .build();
    }

    private void renewAccessToken() throws JsonProcessingException {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(idpalClientKey)
                .accessKey(idpalAccessKey)
                .clientId(idpalClientId)
                .refreshToken(idpalRefreshToken)
                .build();

        HashMap<String, String> responseMap = makeIdPalRequest(
                generateIdPalRequest(idpalGetAccessTokenEndpoint,
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON))
        );
    }

    private Request generateIdPalRequest(String endpoint, RequestBody body) {
        return new Request.Builder()
                .url(endpoint)
                .addHeader("Authorization", "Bearer " + idpalAccessToken)
                .addHeader("Accept", "application/json")
                .post(body)
                .build();
    }

    private HashMap<String, String> makeIdPalRequest(Request request) {
        try {
            Response response = httpClient.newCall(request).execute();
            HashMap<String, String> responseMap = MAPPER.readValue(response.body().string(), HashMap.class);
            responseMap.put("status", String.valueOf(response.code()));
            return responseMap;
        } catch (IOException e) {
            return new HashMap<>() {{
                put("message", "The request was valid. Something occurred with ID-Pal service");
                put("status", "200");
            }};
        }
    }
}
