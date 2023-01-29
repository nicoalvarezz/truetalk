package com.fyp.postservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "post")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Post {

    @Id
    private String id;

    private String cratedAt;

    private String text;

    private String user;

    private int likes;

    private Comment[] comments;
}

@AllArgsConstructor
@NoArgsConstructor
class Comment {

    @Id
    private String id;

    private String user;

    private String text;

    private String createdAt;
}
