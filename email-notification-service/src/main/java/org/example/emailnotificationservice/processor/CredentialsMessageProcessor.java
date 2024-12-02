package org.example.emailnotificationservice.processor;

import lombok.RequiredArgsConstructor;
import org.example.emailnotificationservice.model.notification.CredentialsEmailNotificationMessage;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class CredentialsMessageProcessor implements NotificationMessageProcessor<CredentialsEmailNotificationMessage> {
    private final TemplateEngine templateEngine;

    @Override
    public String generateHtmlContent(CredentialsEmailNotificationMessage message) {
        var context = new Context();
        context.setVariable("body", message.getBody());
        context.setVariable("username", message.getUsername());
        context.setVariable("password", message.getPassword());
        return templateEngine.process("credentials-template", context);
    }
}
