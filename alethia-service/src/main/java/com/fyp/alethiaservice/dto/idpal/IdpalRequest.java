package com.fyp.alethiaservice.dto.idpal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdpalRequest {

    @JsonProperty("client_key")
    private String clientKey;

    @JsonProperty("access_key")
    private String accessKey;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("information_type")
    private String informationType;

    private String contact;

    @JsonProperty("profile_id")
    private int profileId;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("submission_id")
    private int submissionId;

    @JsonProperty("content_disposition")
    private String contentDisposition;

    @JsonProperty("document_type")
    private String documentType;
}
