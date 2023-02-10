package com.fyp.postservice.repository;

import com.fyp.postservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUser(String user);
}
