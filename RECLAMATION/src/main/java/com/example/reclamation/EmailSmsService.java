package com.example.reclamation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSmsService {

    @Autowired
    private JavaMailSender mailSender;

    // Send email
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("chamekheya1@gmail.com");

        mailSender.send(email);
    }

    // Send SMS using email-to-SMS gateway
    public void sendSms(String phoneNumber, String message) {
        // Tunisian SMS Gateway (change if needed)
        String smsEmail = phoneNumber + "@sms.tunisietelecom.tn";

        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(smsEmail);
        sms.setSubject(""); // SMS gateway doesn't need a subject
        sms.setText(message);
        sms.setFrom("chamekheya1@gmail.com");

        mailSender.send(sms);
    }
}
