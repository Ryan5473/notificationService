package net.pm.mailservice.mailservice.controller;

import net.pm.mailservice.mailservice.entity.ScheduledEmail;
import net.pm.mailservice.mailservice.service.ScheduledEmailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class ScheduledEmailController {

    private final ScheduledEmailService scheduledEmailService;

    public ScheduledEmailController(ScheduledEmailService scheduledEmailService) {
        this.scheduledEmailService = scheduledEmailService;
    }

    @PostMapping("/schedule-email")
    public String scheduleEmail(@RequestBody ScheduledEmail email) {
        scheduledEmailService.scheduleEmail(email);
        return "Email scheduled for " + email.getScheduledAt();
    }
}
