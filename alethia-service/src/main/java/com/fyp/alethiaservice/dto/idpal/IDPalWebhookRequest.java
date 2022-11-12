package com.fyp.alethiaservice.dto.idpal;

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
public class IDPalWebhookRequest {

    @NotNull
    @JsonProperty("event_id")
    private int eventId;

    @NotNull
    @NotBlank
    @JsonProperty("event_type")
    private String eventType;

    @NotNull
    @NotBlank
    private String uuid;

    @NotNull
    @JsonProperty("submission_id")
    private int submissionId;

    @NotNull
    @NotBlank
    private String source;
}
