package net.pm.mailservice.mailservice.kafka;

import net.pm.mailservice.mailservice.dto.EmailRequest;

import net.pm.mailservice.mailservice.service.SendMailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer{
    private final SendMailService sendMailService;

    public EmailConsumer(SendMailService sendMailService) {
        this.sendMailService = sendMailService;
    }

    @KafkaListener(topics = "order-paid-topic", groupId = "email-group")
    public void consume(EmailRequest emailRequest) {
        String email = emailRequest.getTo();
        System.out.println("Kafka Consumer received email: " + email);
        try {
            sendMailService.sendEmail(
                    email,
                    "Your order is confirmed!",
                    "Thank you for your purchase!"
            );
            System.out.println("Email sent successfully to: " + email);
        } catch (Exception e) {
            System.err.println("Failed to send email to: " + email);
            e.printStackTrace();
        }
    }
}