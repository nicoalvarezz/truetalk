package com.fyp.postservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "post")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Post {

    @Id
    private String id;

    private String createdAt;

    private String text;

    private String user;

    private String name;

    private String profilePictureUrl;

    private List<String> likes;

    private List<Comment> comments;
}
