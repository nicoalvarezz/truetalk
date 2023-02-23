package com.fyp.postservice.repository;

import com.fyp.postservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUser(String user);
    Optional<Post> findById(String postId);
}
