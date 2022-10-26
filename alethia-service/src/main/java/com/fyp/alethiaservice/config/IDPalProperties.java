package com.fyp.alethiaservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.HashMap;

@ConfigurationProperties(prefix = "idpal")
@ConfigurationPropertiesScan
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IDPalProperties {
    private HashMap<String, String> apiAccess;
    private HashMap<String, Integer> profileId;
    private HashMap<String, String> endpoint;
}
