package org.example.emailnotificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.emailnotificationservice.model.EmailNotificationMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmailService emailService;
    private final HtmlTemplateService htmlTemplateService;

    @SneakyThrows
    public void sendEmail(EmailNotificationMessage message) {
        var htmlContent = htmlTemplateService.generateEventNearResidenceHtmlTemplate(message.getEvents());
        emailService.sendHtmlEmail(message.getEmail(), message.getSubject(), htmlContent);
    }
}
