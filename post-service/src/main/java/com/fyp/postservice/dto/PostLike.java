package com.fyp.postservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLike {

    @NotNull
    @NotBlank
    @JsonProperty("post_id")
    private String postId;

    @NotNull
    @NotBlank
    private String user;
}
