package org.example.emailnotificationservice.processor;

import lombok.RequiredArgsConstructor;
import org.example.emailnotificationservice.model.notification.ResidenceNewsEmailNotificationMessage;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class ResidenceNewsMessageProcessor implements NotificationMessageProcessor<ResidenceNewsEmailNotificationMessage>{
    private final TemplateEngine templateEngine;

    @Override
    public String generateHtmlContent(ResidenceNewsEmailNotificationMessage message) {
        var context = new Context();
        context.setVariable("residenceNews", message.getResidenceNews());
        return templateEngine.process("residence-news-template", context);
    }
}
