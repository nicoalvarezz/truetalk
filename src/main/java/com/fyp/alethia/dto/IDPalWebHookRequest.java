package com.fyp.alethia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IDPalWebHookRequest {

    @JsonProperty("event_id")
    private int eventId;

    @JsonProperty("event_type")
    private String eventType;

    private String uuid;

    @JsonProperty("submission_id")
    private int SubmissionID;

    private String source;
}
