package com.fyp.alethiaservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class TriggerVerificationResponse {

        private String message;

        @JsonProperty("status")
        @JsonIgnore
        private int statusCode;

        private String uuid;
    }
