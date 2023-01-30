package com.fyp.userservice.repository;

import com.fyp.userservice.model.Followers;
import com.fyp.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;


public interface FollowersRepository extends JpaRepository<Followers, User> {
    Set<Followers> findByUser(User user);
}
