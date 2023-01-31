package com.fyp.userservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hiveshared.api.helpers.ApiHelpers;
import com.fyp.hiveshared.api.responses.excpetion.UnauthorizedException;
import com.fyp.userservice.config.AlethiaProperties;
import com.fyp.userservice.dto.AlethiaRequest;
import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.dto.UserProfile;
import com.fyp.userservice.model.ConfirmationToken;
import com.fyp.userservice.model.User;
import com.fyp.userservice.model.UserVerifiedProfile;
import com.fyp.userservice.repository.ConfirmationTokenRepository;
import com.fyp.userservice.repository.UserRepository;
import com.fyp.userservice.repository.UserVerifiedProfileRepository;
import com.fyp.userservice.eventListener.OnRegistrationCompleteEvent;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@Component
@RequiredArgsConstructor
public class UserService implements ConfirmUser {

    @Autowired
    private AlethiaProperties alethiaProperties;

    @Autowired
    ApplicationEventPublisher eventPublisher;


    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserVerifiedProfileRepository userVerifiedProfileRepository;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String POST_METHOD = "POST";
    private static final String EMPTY_ACCESS_TOKEN = "";

    private static ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public void registerUser(RegisterUserRequest registerUserRequest) {
        // I am not missing the phone number
        User user = User.builder()
                .email(registerUserRequest.getEmail())
                .password(registerUserRequest.getPassword())
                .build();

        userRepository.save(user);
        LOGGER.info("User with uuid {}, successfully registered ", user.getId());
    }

    public void confirmUser(String token) throws UnauthorizedException {
        if (token.isEmpty()) {
            throw new UnauthorizedException("Confirmation token cannot be null");
        }

        ConfirmationToken confirmationToken = getVerificationToken(token);
        User user = confirmationToken.getUser();

        // if now() is greater than expiry date the comparator value will be positive
        if (LocalDateTime.now().compareTo(confirmationToken.getExpiryDate()) > 0) {
            throw new UnauthorizedException("Confirmation token has expired");
        }

        user.setEnabled(true);
        userRepository.save(user);
    }

    public void triggerAlethiaVerification(String token) throws JsonProcessingException {
        User user = getVerificationToken(token).getUser();
        AlethiaRequest alethiaRequest = MAPPER.convertValue(user, AlethiaRequest.class);
        ApiHelpers.makeApiRequest(
                ApiHelpers.generateRequest(
                        POST_METHOD,
                        alethiaProperties.getAlethiaTriggerVerificationEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(alethiaRequest), JSON),
                        EMPTY_ACCESS_TOKEN
                )
        );

    }

    public void triggerAlethiaVerification(RegisterUserRequest registerUserRequest) throws IOException {
        AlethiaRequest alethiaRequest = MAPPER.convertValue(registerUserRequest, AlethiaRequest.class);
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
        // TODO:
        // Make sure that the if the user does not exist or already has been verified throws an error.
        User user = userRepository.findByEmail(userProfileInfo.getEmail());
        user.setVerified(true);

        UserVerifiedProfile userVerifiedProfile = UserVerifiedProfile.builder()
                .user(user)
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
        LOGGER.info("User with uuid {}, successfully verified ", userVerifiedProfile.getUuid());
    }

    public void publishConfirmationEvent(RegisterUserRequest userRequest, Locale locale, String appUrl) {
        User user = userRepository.findByEmail(userRequest.getEmail());
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, locale, appUrl));
    }

    @Override
    public User getUserBycConfirmationToken(String confirmationToken) {
        return confirmationTokenRepository.findByToken(confirmationToken).getUser();
    }

    @Override
    public ConfirmationToken getVerificationToken(String confirmationToken) {
        return confirmationTokenRepository.findByToken(confirmationToken);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        confirmationTokenRepository.save(ConfirmationToken.builder()
                                            .token(token)
                                            .user(user)
                                            .build());
    }
}
