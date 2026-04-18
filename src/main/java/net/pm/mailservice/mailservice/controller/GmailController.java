package net.pm.mailservice.mailservice.controller;

import net.pm.mailservice.mailservice.service.GmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/gmail")
public class GmailController {

    private final GmailService gmailService;

    @Autowired
    public GmailController(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    // ---------- SEND EMAIL ----------
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body
    ) {
        try {
            gmailService.sendEmail(to, subject, body);
            return ResponseEntity.ok("✅ Email sent to: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Failed to send email: " + e.getMessage());
        }
    }

    // ---------- READ EMAILS ----------
    @GetMapping("/inbox")
    public ResponseEntity<?> readInbox(@RequestParam(defaultValue = "0") int start) {
        try {
            List<String> emails = gmailService.readEmails(start, 10); // fetch 10 emails starting at offset
            return ResponseEntity.ok(emails);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Failed to read emails: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
