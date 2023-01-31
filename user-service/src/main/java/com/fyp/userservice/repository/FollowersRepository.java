package com.fyp.userservice.repository;

import com.fyp.userservice.model.Follower;
import com.fyp.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface FollowersRepository extends JpaRepository<Follower, User> {
    Set<Follower> findByUser(User user);
}
