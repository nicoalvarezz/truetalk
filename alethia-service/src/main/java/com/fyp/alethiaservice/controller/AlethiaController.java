package com.fyp.alethiaservice.controller;

import com.fyp.alethiaservice.dto.TriggerVerificationResponse;
import com.fyp.alethiaservice.dto.IDPalWebhookRequest;
import com.fyp.alethiaservice.dto.PersonalInfoResponse;
import com.fyp.alethiaservice.dto.UserRequest;
import com.fyp.alethiaservice.service.AlethiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/alethia")
@RequiredArgsConstructor
public class AlethiaController {

    private final AlethiaService alethiaService;

    @PostMapping("/trigger-verification")
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public TriggerVerificationResponse triggerVerification(@RequestBody UserRequest userRequest) throws IOException {
        return alethiaService.triggerVerification(userRequest);
    }

    @PostMapping("/webhook-receiver")
    @ResponseStatus(code = HttpStatus.OK)
    public void webhookReceiver(@RequestBody IDPalWebhookRequest idPalWebhookRequest) throws IOException {
        PersonalInfoResponse userPersonalInfo = alethiaService.retrieveUserPersonalInfo(idPalWebhookRequest.getSubmissionId());
        // TODO:
        // Send info to user-service
    }
}
