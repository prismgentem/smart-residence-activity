package org.example.emailnotificationservice.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String emailFrom;
    private final JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MailException, MessagingException {
        var mimeMessage = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(mimeMessage, false);

        messageHelper.setFrom(emailFrom);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    public void sendHtmlEmailWithAttachment(String to, String subject, String text, File attachment) throws MailException, MessagingException {
        var mimeMessage = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(mimeMessage, true);

        messageHelper.setFrom(emailFrom);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(text, true);
        messageHelper.addAttachment(attachment.getName(), attachment);

        mailSender.send(mimeMessage);
    }
}


