package com.fyp.userservice.service;

import com.fyp.userservice.model.ConfirmationToken;
import com.fyp.userservice.model.User;

public interface ConfirmUser {

    User getUserBycConfirmationToken(String confirmationToken);

    ConfirmationToken getVerificationToken(String confirmationToken);

    void createVerificationToken(User user, String token);
}
