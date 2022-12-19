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
public class RenewIdpalAccessToken {

    @JsonProperty("client_key")
    private String clientKey;

    @JsonProperty("access_key")
    private String accessKey;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
