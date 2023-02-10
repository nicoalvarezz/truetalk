package com.fyp.userservice.dto;

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
public class FollowRequest {

    @NotNull
    @NotBlank
    @JsonProperty("follower_id")
    private String followerId;

    @NotNull
    @NotBlank
    @JsonProperty("followee_id")
    private String followeeId;
}
