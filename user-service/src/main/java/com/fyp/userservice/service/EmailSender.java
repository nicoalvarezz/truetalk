package com.fyp.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public void postNotification(String followeeName, String recipientAddress) {
        String subject = followeeName + "Just Posted!";
        String emailContent = "We wanted to give you a heads up that " + followeeName + " just posted something new on our platform! It's always exciting to see new content from the people we follow, right?\n" +
                "\n" +
                "So, what are you waiting for? Head on over to our platform and check out what " + followeeName + " has been up to. Don't forget to like, comment, and share their post to show your support.\nBest,\n" +
                "TrueTalk Team";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(emailContent);
        mailSender.send(email);
    }
}
