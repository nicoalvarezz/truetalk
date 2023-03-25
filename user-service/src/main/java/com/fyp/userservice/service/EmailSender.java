package com.fyp.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailContents mailContents;

    public void postNotification(String recipientName, String recipientAddress) {
        String subject = recipientName + " " + mailContents.getPostNotificationSubject();
        String emailContent =  mailContents.getPostNotificationFirstPart() + " " + recipientName + " " +
                               mailContents.getPostNotificationSecondPart() + " " + recipientName + " " +
                               mailContents.getPostNotificationThirdPart();
        sendEmail(recipientAddress, subject, emailContent);
    }

    public void confirmationEmail(String recipientAddress, String token) {
        String subject = mailContents.getConfirmationEmailSubject();
        String confirmationUrl = "/registration-confirm?token=" + token;
        String emailContent = mailContents.getConfirmationEmailFirstPart() + "\r\n" + "https://truetalk.ie:8000/api/users" + confirmationUrl + "\n\n" +
                              mailContents.getConfirmationEmailSecondPart();
        sendEmail(recipientAddress, subject, emailContent);
    }

    public void sendEmail(String address, String subject, String content) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(address);
        email.setSubject(subject);
        email.setText(content);
        mailSender.send(email);
    }
}
