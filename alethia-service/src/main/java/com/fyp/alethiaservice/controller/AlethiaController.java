package com.fyp.alethiaservice.controller;

import com.fyp.alethiaservice.dto.idpal.IDPalWebhookRequest;
import com.fyp.alethiaservice.dto.users.UserProfileInfo;
import com.fyp.alethiaservice.dto.users.UserRequest;
import com.fyp.alethiaservice.response.ResponseHandler;
import com.fyp.alethiaservice.service.AlethiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/alethia")
@RequiredArgsConstructor
public class AlethiaController {

    private final AlethiaService alethiaService;

    @PostMapping("/trigger-verification")
    @ResponseBody
    public ResponseEntity<Object> triggerVerification(@Valid @RequestBody UserRequest userRequest) throws IOException {
        alethiaService.triggerVerification(userRequest);
        return ResponseHandler.generateSimpleResponse("Verification link sent", HttpStatus.OK);
    }

    @PostMapping("/webhook-receiver")
    public ResponseEntity<Object> webhookReceiver(@Valid @RequestBody IDPalWebhookRequest idPalWebhookRequest) throws IOException {
        UserProfileInfo userPersonalInfo = alethiaService.retrieveUserPersonalInfo(idPalWebhookRequest.getSubmissionId());
        alethiaService.sendUserProfileToUserService(userPersonalInfo);
        return ResponseHandler.generateSimpleResponse("Webhook received", HttpStatus.OK);
    }
}
