package com.eventmanager.event_management.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOrderConfirmationEmail(String to, String orderDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Potwierdzenie zamówienia");
        message.setText("Dziękujemy za złożenie zamówienia. Prosimy o dokonanie płatności. Szczegóły zamówienia:\n\n" + orderDetails + "\n\nEvents App");
        message.setFrom("eventsappuwb123@gmail.com");

        javaMailSender.send(message);
    }
}
