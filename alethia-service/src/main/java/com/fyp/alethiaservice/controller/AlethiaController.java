package com.fyp.alethiaservice.controller;

import com.fyp.alethiaservice.dto.idpal.IdpalWebhookRequest;
import com.fyp.alethiaservice.dto.users.UserProfileInfo;
import com.fyp.alethiaservice.dto.users.UserRequest;
import com.fyp.alethiaservice.service.AlethiaService;
import com.fyp.hiveshared.api.responses.ResponseHandlers;
import com.fyp.hiveshared.api.responses.excpetion.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/alethia")
@RequiredArgsConstructor
public class AlethiaController {

    private final AlethiaService alethiaService;
    private static final String SERVICE = "alethia-service";

    @PostMapping("/trigger-verification")
    public ResponseEntity<Map<String, Object>> triggerVerification(@Valid @RequestBody UserRequest userRequest) throws IOException, ServiceUnavailableException {
        alethiaService.triggerVerification(userRequest);
        return ResponseHandlers.responseBody("Verification link sent", HttpStatus.OK, SERVICE);
    }

    @PostMapping("/webhook-receiver")
    public ResponseEntity<Map<String, Object>> webhookReceiver(@Valid @RequestBody IdpalWebhookRequest idPalWebhookRequest) throws IOException {
        UserProfileInfo userPersonalInfo = alethiaService.retrieveUserPersonalInfo(idPalWebhookRequest.getSubmissionId());
        alethiaService.sendUserProfileToUserService(userPersonalInfo);
        return ResponseHandlers.responseBody("Webhook received", HttpStatus.OK, SERVICE);
    }
}
