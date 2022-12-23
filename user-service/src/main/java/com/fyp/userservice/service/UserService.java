package com.fyp.userservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hiveshared.api.helpers.ApiHelpers;
import com.fyp.userservice.config.AlethiaProperties;
import com.fyp.userservice.dto.AlethiaRequest;
import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.dto.UserProfile;
import com.fyp.userservice.model.UserVerifiedProfile;
import com.fyp.userservice.repository.UserRepository;
import com.fyp.userservice.repository.UserVerifiedProfileRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Component
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private AlethiaProperties alethiaProperties;

    private final UserRepository userRepository;
    private final UserVerifiedProfileRepository userVerifiedProfileRepository;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String POST_METHOD = "POST";
    private static final String EMPTY_ACCESS_TOKEN = "";

    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public void triggerAlethiaVerification(RegisterUserRequest registerUserRequest) throws IOException {
        AlethiaRequest alethiaRequest = AlethiaRequest.builder()
                .email(registerUserRequest.getEmail())
                .phoneNumber(registerUserRequest.getPhoneNumber())
                .build();

        ApiHelpers.makeApiRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        alethiaProperties.getAlethiaTriggerVerificationEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(alethiaRequest), JSON),
                        EMPTY_ACCESS_TOKEN
                )
        );
    }

    public void saveUserProfileInfo(UserProfile userProfileInfo) {
        UserVerifiedProfile userVerifiedProfile = UserVerifiedProfile.builder()
                .firstName(userProfileInfo.getFirstName())
                .lastName(userProfileInfo.getLastName())
                .phoneCountryCode(userProfileInfo.getPhoneCountryCode())
                .phoneNumber(userProfileInfo.getPhoneNumber())
                .dateOfBirth(userProfileInfo.getDateOfBirth())
                .countryOfBirth(userProfileInfo.getCountryOfBirth())
                .address1(userProfileInfo.getAddress1())
                .address2(userProfileInfo.getAddress2())
                .city(userProfileInfo.getCity())
                .county(userProfileInfo.getCounty())
                .countryName(userProfileInfo.getCountryName())
                .postalCode(userProfileInfo.getPostalCode())
                .build();


        userVerifiedProfileRepository.save(userVerifiedProfile);
        LOGGER.info("User {} is saved in the db successfully", userVerifiedProfile.getId());
    }
}
