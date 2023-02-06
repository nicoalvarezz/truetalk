package com.fyp.postservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Comment {

    @Id
    private String id;

    private String user;

    private String text;

    private String createdAt;
}
