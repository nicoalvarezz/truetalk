package com.fyp.userservice.repository;

import com.fyp.userservice.model.Followee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface FolloweeeRepository extends JpaRepository<Followee, UUID> {

    List<Followee> findByFollowerId(UUID uuid);

    List<Followee> findByFolloweeId(UUID uuid);
}
