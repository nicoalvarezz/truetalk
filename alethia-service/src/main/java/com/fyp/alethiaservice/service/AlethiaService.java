package com.fyp.alethiaservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.alethiaservice.config.CloudinaryProperties;
import com.fyp.alethiaservice.config.IdPalProperties;
import com.fyp.alethiaservice.config.UserServiceProperties;
import com.fyp.alethiaservice.dto.idpal.IdpalAccessToken;
import com.fyp.alethiaservice.dto.idpal.IdpalRequest;
import com.fyp.alethiaservice.dto.idpal.IdpalRenewAccessToken;
import com.fyp.alethiaservice.dto.users.UserProfileInfo;
import com.fyp.alethiaservice.dto.users.UserRequest;
import com.fyp.hiveshared.api.helpers.ApiHelpers;
import com.fyp.hiveshared.api.helpers.JwtHelpers;
import com.fyp.hiveshared.api.responses.excpetion.ServiceUnavailableException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;


@Service
public class AlethiaService {

    @Autowired
    private IdPalProperties idPalProperties;

    @Autowired
    private UserServiceProperties userServiceProperties;

    @Autowired
    private CloudinaryProperties cloudinaryProperties;

    JwtHelpers jwtHelpers = new JwtHelpers();

    private static final String IDPAL_EXCEPTION_MESSAGE = "An error has occurred with identity verification service services";
    private static final String INFORMATION_TYPE = "email"; // This is only temporal -> I need to decide whether the user can choose or not ???
    private static final Logger LOGGER = LoggerFactory.getLogger(AlethiaService.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String EMPTY_ACCESS_TOKEN = "";

    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    Cloudinary cloudinary = new Cloudinary(cloudinaryProperties.getCloudinaryURL());


    public void triggerVerification(UserRequest registerUserData) throws IOException, ServiceUnavailableException {
//        renewIdpalAccessToken();

        IdpalRequest idPalRequest = IdpalRequest.builder()
                .clientKey(idPalProperties.getClientKey())
                .accessKey(idPalProperties.getAccessKey())
                .informationType(INFORMATION_TYPE)
                .contact(registerUserData.getEmail())
                .profileId(idPalProperties.getProfileId())
                .build();

        Response response = ApiHelpers.makeApiRequest(
                ApiHelpers.postRequest(
                        idPalProperties.getSendVerificationLinkEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idPalProperties.getAccessToken()
                )
        );

        if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
            throw new ServiceUnavailableException(IDPAL_EXCEPTION_MESSAGE);
        }
    }

    public UserProfileInfo retrieveUserPersonalInfo(int submissionId) throws IOException {
//        renewIdpalAccessToken();

        IdpalRequest idPalRequest = IdpalRequest.builder()
                .clientKey(idPalProperties.getClientKey())
                .accessKey(idPalProperties.getAccessKey())
                .submissionId(submissionId)
                .contentDisposition("inline")
                .build();

        Response response = ApiHelpers.makeApiRequest(
                ApiHelpers.postRequest(
                        idPalProperties.getGetSubmissionDetailsEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idPalProperties.getAccessToken()
                )
        );

        UserProfileInfo userProfileInfo = MAPPER.readValue(response.body().string(), UserProfileInfo.class);
        return userProfileInfo;
    }

    public void sendUserProfileToUserService(UserProfileInfo userProfileInfo) throws JsonProcessingException {
        ApiHelpers.makeApiRequest(
                ApiHelpers.postRequest(
                        userServiceProperties.getReceiveUserProfileEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(userProfileInfo), JSON),
                        EMPTY_ACCESS_TOKEN
                )
        );
    }

    public String saveSelfieInCloudinary(int submissionId) throws IOException {
        File fileToUpload = constructTempSelfieImage(retrieveSubmissionSelfie(submissionId), String.valueOf(submissionId));
        String url = uploadPictureAtCloudinary(fileToUpload);
        fileToUpload.delete();
        return url;
    }

    private byte[] retrieveSubmissionSelfie(int submissionId) throws IOException {
        IdpalRequest idPalRequest = IdpalRequest.builder()
                .clientKey(idPalProperties.getClientKey())
                .accessKey(idPalProperties.getAccessKey())
                .submissionId(submissionId)
                .documentType("selfie")
                .build();

        Response response = ApiHelpers.makeApiRequest(
                ApiHelpers.postRequest(
                        idPalProperties.getGetDocumentEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON),
                        idPalProperties.getAccessToken()
                )
        );

        return response.body().byteStream().readAllBytes();
    }

    private File constructTempSelfieImage(byte[] imageData, String fileName) throws IOException {
        File file = new File(fileName + ".jpg");
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(imageData);
        outputStream.close();
        return file;
    }

    private String uploadPictureAtCloudinary(File file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        System.out.println("URL:  " + uploadResult.get("url"));
        return uploadResult.get("url").toString();
    }

    public void renewIdpalAccessToken() throws IOException {
        String accessToken = idPalProperties.getAccessToken();
        if (jwtHelpers.isAccessTokenExpired(accessToken)) {
            return;
        } else {
            Response response = ApiHelpers.makeApiRequest(
                    ApiHelpers.postRequest(
                            idPalProperties.getAccessTokenEndpoint(),
                            RequestBody.create(MAPPER.writeValueAsString(getRenewTokenBody()), JSON),
                            accessToken
                    )
            );
            IdpalAccessToken tokens = MAPPER.readValue(response.body().string(), IdpalAccessToken.class);
            idPalProperties.setNewTokens(tokens.getAccessToken(), tokens.getRefreshToken());
        }
    }

    private IdpalRenewAccessToken getRenewTokenBody() {
        return IdpalRenewAccessToken.builder()
                .clientKey(idPalProperties.getClientKey())
                .accessKey(idPalProperties.getAccessKey())
                .clientId(idPalProperties.getClientId())
                .refreshToken(idPalProperties.getRefreshToken())
                .build();
    }
}
