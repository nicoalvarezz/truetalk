package com.fyp.postservice.repository;

import com.fyp.postservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
