package com.fyp.userservice.eventListener;

import com.fyp.userservice.model.User;
import com.fyp.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = messages.getMessage("message.subject", null, event.getLocale());
        String confirmationUrl = event.getAppUrl() + "/registration-confirm?token=" + token;
        String messageFirstPart = messages.getMessage("message.regSucc.firstPart", null, event.getLocale());
        String messageSecondPart = messages.getMessage("message.regSucc.secondPart", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(messageFirstPart + "\r\n" + "http://localhost:8080" + confirmationUrl + messageSecondPart);
        mailSender.send(email);
    }
}
