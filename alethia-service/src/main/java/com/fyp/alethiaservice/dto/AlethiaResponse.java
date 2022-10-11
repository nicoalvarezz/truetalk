package com.fyp.alethiaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlethiaResponse {

    private String message;
    private int statusCode;
    private String uuid;
}
