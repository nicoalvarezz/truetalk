package com.fyp.postservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostComment {

    @NotNull
    @NotBlank
    @JsonProperty("post_id")
    private String postId;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 150)
    private String text;

    @NotNull
    @NotBlank
    private String user;

    @NotNull
    @NotBlank
    private String profilePictureUrl;
}
