package net.pm.mailservice.mailservice.service;

import net.pm.mailservice.mailservice.entity.ScheduledEmail;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ScheduledEmailService {

    private final SendMailService sendMailService;
    private final List<ScheduledEmail> scheduledEmails = new ArrayList<>();

    public ScheduledEmailService(SendMailService sendMailService) {
        this.sendMailService = sendMailService;
    }

    // Add email to schedule
    public void scheduleEmail(ScheduledEmail email) {
        scheduledEmails.add(email);
        System.out.println("Email scheduled for: " + email.getScheduledAt());
    }

    // Run every minute to send due emails
    @Scheduled(fixedRate = 60000)
    public void sendDueEmails() {
        Instant now = Instant.now();
        Iterator<ScheduledEmail> iterator = scheduledEmails.iterator();

        while (iterator.hasNext()) {
            ScheduledEmail email = iterator.next();
            if (!email.getScheduledAt().isAfter(now)) {
                for (String recipient : email.getTo()) {
                    sendMailService.sendEmail(recipient, email.getSubject(), email.getBody());
                }
                iterator.remove();
            }
        }
    }
}
