package com.fyp.alethiaservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CloudinaryProperties {

    private final String cloudinaryURL;

    @Autowired
    public CloudinaryProperties(@Value("${cloudinary.url}") String cloudinaryURL) {
        this.cloudinaryURL = cloudinaryURL;
    }

    public String getCloudinaryURL() {
        return cloudinaryURL;
    }
}
