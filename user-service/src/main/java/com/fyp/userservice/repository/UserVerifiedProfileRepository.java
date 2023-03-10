package com.fyp.userservice.repository;

import com.fyp.userservice.model.UserVerifiedProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserVerifiedProfileRepository extends JpaRepository<UserVerifiedProfile, UUID> {

    Optional<UserVerifiedProfile> findByFirstNameAndLastName(String firstName, String lastName);
}
