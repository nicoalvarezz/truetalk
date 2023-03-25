package com.fyp.userservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hiveshared.api.helpers.ApiHelpers;
import com.fyp.hiveshared.api.helpers.JwtHelpers;
import com.fyp.hiveshared.api.responses.excpetion.NotFoundException;
import com.fyp.hiveshared.api.responses.excpetion.UnauthorizedException;
import com.fyp.userservice.config.AlethiaProperties;
import com.fyp.userservice.config.ProducerServiceProperties;
import com.fyp.userservice.dto.AlethiaRequest;
import com.fyp.userservice.dto.ConfirmationUser;
import com.fyp.userservice.dto.FollowRequest;
import com.fyp.userservice.dto.UnfollowRequest;
import com.fyp.userservice.dto.LoginUserRequest;
import com.fyp.userservice.dto.RegisterUserRequest;
import com.fyp.userservice.dto.UserProfile;
import com.fyp.userservice.helpers.CountryLanguages;
import com.fyp.userservice.model.ConfirmationToken;
import com.fyp.userservice.model.Followee;
import com.fyp.userservice.model.User;
import com.fyp.userservice.model.UserVerifiedProfile;
import com.fyp.userservice.repository.ConfirmationTokenRepository;
import com.fyp.userservice.repository.FolloweeeRepository;
import com.fyp.userservice.repository.UserRepository;
import com.fyp.userservice.repository.UserVerifiedProfileRepository;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Component
@RequiredArgsConstructor
public class UserService implements ConfirmUser {

    @Autowired
    private AlethiaProperties alethiaProperties;

    @Autowired
    private ProducerServiceProperties producerServiceProperties;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    EmailSender emailSender;

    JwtHelpers jwtHelpers = new JwtHelpers();

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserVerifiedProfileRepository userVerifiedProfileRepository;
    private final FolloweeeRepository followeeeRepository;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String EMPTY_ACCESS_TOKEN = "";
    private static final String INVALID_EMAIL_ERROR = "Invalid email";
    private static final String INVALID_EMAIL_PASSWORD = "Invalid password";
    private static final String INVALID_USER_USER = "Invalid user";
    private static final String USER_NOT_FOUND = "User not found";

    private final CountryLanguages countryLanguages = new CountryLanguages();
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
                ApiHelpers.postRequest(
                        alethiaProperties.getAlethiaTriggerVerificationEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(alethiaRequest), JSON),
                        EMPTY_ACCESS_TOKEN
                )
        );

    }

    @Deprecated
    public void triggerAlethiaVerification(RegisterUserRequest registerUserRequest) throws IOException {
        AlethiaRequest alethiaRequest = MAPPER.convertValue(registerUserRequest, AlethiaRequest.class);
        ApiHelpers.makeApiRequest(
                ApiHelpers.postRequest(
                        alethiaProperties.getAlethiaTriggerVerificationEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(alethiaRequest), JSON),
                        EMPTY_ACCESS_TOKEN
                )
        );
    }

    public void saveUserProfileInfo(UserProfile userProfileInfo) {
        User user = userRepository.findByEmail(userProfileInfo.getEmail())
                        .orElseThrow(() -> new UnauthorizedException(INVALID_EMAIL_ERROR));
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
                .selfieUrl(userProfileInfo.getSelfieUrl())
                .build();

        firstAndLastNameHandling(userVerifiedProfile);

        System.out.println("First Name: " + userVerifiedProfile.getFirstName());
        System.out.println("Last Name: " + userVerifiedProfile.getLastName());

        userVerifiedProfileRepository.save(userVerifiedProfile);
        LOGGER.info("User with uuid {}, successfully verified ", userVerifiedProfile.getUuid());
    }

    private void firstAndLastNameHandling(UserVerifiedProfile userVerifiedProfile) {
        String[] splitFirstName = userVerifiedProfile.getFirstName().split(" ");
        if (splitFirstName.length == 1) {
            return;
        }
        String newLastName = String.format("%s %s", userVerifiedProfile.getFirstName().split(" ", 2)[1], userVerifiedProfile.getLastName());
        System.out.println("New Last Name: " + newLastName);
        userVerifiedProfile.setFirstName(splitFirstName[0]);
        userVerifiedProfile.setLastName(newLastName);
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
        confirmationTokenRepository.save(
                ConfirmationToken.builder()
                        .token(token)
                        .user(user)
                        .build());
    }

    public void follow(FollowRequest followRequest) {
        followeeeRepository.save(
                Followee.builder()
                        .followeeId(UUID.fromString(followRequest.getFolloweeId()))
                        .followerId(UUID.fromString(followRequest.getFollowerId()))
                        .build());
    }

    public void unfollow(UnfollowRequest unfollowRequest) {
        followeeeRepository.delete(
                Followee.builder()
                        .followeeId(UUID.fromString(unfollowRequest.getFolloweeId()))
                        .followerId(UUID.fromString(unfollowRequest.getFollowerId()))
                        .build());
    }

    public List<UUID> getFollowees(String uuid) {
        return followeeeRepository.findByFollowerId(
                UUID.fromString(uuid))
                        .stream()
                        .map(Followee::getFolloweeId)
                        .collect(Collectors.toList());
    }

    private List<UUID> getFollowers(String uuid) {
        return followeeeRepository.findByFolloweeId(UUID.fromString(uuid))
                .stream()
                .map(Followee::getFollowerId)
                .collect(Collectors.toList());
    }

    public String generateToken(LoginUserRequest loginUserRequest) {
        return jwtHelpers.generateJwtTokenWithPayload(
                new HashMap<>(){{ put("uuid", validateUser(loginUserRequest));}}
        );
    }

    private String validateUser(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByEmail(loginUserRequest.getEmail())
                        .orElseThrow(() -> new UnauthorizedException(INVALID_EMAIL_ERROR));

        isUserServiceViable(loginUserRequest.getEmail());
        if (!user.getPassword().equals(loginUserRequest.getPassword())) {
            throw new UnauthorizedException(INVALID_EMAIL_PASSWORD);
        }
        return user.getId().toString();
    }

    public String getUserName(String uuid) {
        return getUserVerifiedProfile(uuid).getFirstName() + " " + getUserVerifiedProfile(uuid).getLastName();
    }

    public String getUserCountry(String uuid) {
        return getUserVerifiedProfile(uuid).getCountryOfBirth();
    }

    public String getUserLanguage(String uuid) {
        return countryLanguages.getLanguage(getUserCountry(uuid));
    }

    private UserVerifiedProfile getUserVerifiedProfile(String uuid) {
        return userVerifiedProfileRepository.findById(UUID.fromString(uuid))
                        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    public void sendPostNotification(String uuid) {
        String followeeName = getUserName(uuid);
        getFollowers(uuid)
                .forEach(follower -> {
                    User recipientUser = userRepository
                            .findById(follower)
                            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
                    emailSender.postNotification(followeeName, recipientUser.getEmail());
                });
    }

    public void publishConfirmationEmailEvent(String email) throws JsonProcessingException {
        ApiHelpers.makeApiRequest(
                ApiHelpers.postRequest(
                        producerServiceProperties.getConfirmationEmailEventEndpoint(),
                        RequestBody.create(MAPPER.writeValueAsString(ConfirmationUser.builder().email(email).build()), JSON),
                        EMPTY_ACCESS_TOKEN
                )
        );
    }

    public void sendConfirmationEmail(String email) {
        String token = UUID.randomUUID().toString();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException(INVALID_USER_USER));
        createVerificationToken(user, token);
        emailSender.confirmationEmail(email, token);
    }

    public String findUserByFirstAndLastName(String firstName, String lastName) {
        UserVerifiedProfile user = userVerifiedProfileRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return user.getUser().getId().toString();
    }

    private void isUserServiceViable(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        if (!user.isEnabled() || !user.isVerified()) {
            throw new UnauthorizedException(INVALID_USER_USER);
        }
    }

    public String getProfilePictureUrl(String uuid) {
        return userVerifiedProfileRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND))
                .getSelfieUrl();
    }
}
