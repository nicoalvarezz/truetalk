package com.fyp.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry.addMapping("/**");
//        corsRegistration.allowedOrigins("http://localhost:3000");
//        corsRegistration.allowedOrigins("http://192.168.0.161:3000");
//        corsRegistration.allowedOrigins("http://143.42.26.143");
//        corsRegistration.allowedOrigins("http://143.42.26.143:80");
//        corsRegistration.allowedOrigins("http://172.105.75.93:8080");
//        corsRegistration.allowedOrigins("http://192.46.239.71:80");
        corsRegistration.allowedOrigins("*");
        corsRegistration.allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
