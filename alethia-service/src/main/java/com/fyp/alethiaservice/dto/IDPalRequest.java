package com.fyp.alethiaservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IDPalRequest {

    @JsonProperty("client_key")
    private String clientKey;

    @JsonProperty("access_key")
    private String accessKey;

    @JsonProperty("information_type")
    private String informationType;

    private String contact;

    @JsonProperty("profile_id")
    private int profileId;
}
