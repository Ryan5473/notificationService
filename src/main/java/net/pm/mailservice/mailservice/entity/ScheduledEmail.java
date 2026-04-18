package net.pm.mailservice.mailservice.entity;

import java.time.Instant;
import java.util.List;

public class ScheduledEmail {

    private List<String> to;
    private String subject;
    private String body;
    private Instant scheduledAt;

    public ScheduledEmail() {}

    public List<String> getTo() { return to; }
    public void setTo(List<String> to) { this.to = to; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Instant getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(Instant scheduledAt) { this.scheduledAt = scheduledAt; }
}
