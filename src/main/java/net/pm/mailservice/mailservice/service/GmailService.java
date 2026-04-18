package net.pm.mailservice.mailservice.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class GmailService {

    private final String email = "rayenbelkahla219@gmail.com";
    private final String appPassword = "ymdu qtdr zdiq ltkf";

    private Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, appPassword);
            }
        });
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(getSession());
        message.setFrom(new InternetAddress(email));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }

    // New method with start and limit
    public List<String> readEmails(int start, int limit) throws MessagingException, IOException {
        List<String> emails = new ArrayList<>();

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");

        Session session = Session.getInstance(props, null);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", email, appPassword);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();

        int end = Math.min(messages.length, start + limit);
        for (int i = messages.length - 1 - start; i >= messages.length - end; i--) {
            Message msg = messages[i];
            String from = msg.getFrom()[0].toString();
            String subject = msg.getSubject();
            String content = getPlainTextFromMessage(msg);

            emails.add("From: " + from + " | Subject: " + subject + " | Body: " + content);
        }

        inbox.close(false);
        store.close();

        return emails;
    }

    private String getPlainTextFromMessage(Message message) throws MessagingException, IOException {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getPlainTextFromMimeMultipart(mimeMultipart);
        }
        return "";
    }

    private String getPlainTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getPlainTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
}
