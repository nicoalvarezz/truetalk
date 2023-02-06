package com.fyp.merkurioservice.repository;

import com.fyp.merkurioservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
