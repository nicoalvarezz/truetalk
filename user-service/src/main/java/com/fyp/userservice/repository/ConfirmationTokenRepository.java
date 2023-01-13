package com.fyp.userservice.repository;

import com.fyp.userservice.model.ConfirmationToken;
import com.fyp.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    ConfirmationToken findByToken(String token);

    ConfirmationToken findByUser(User user);
}
