package org.example.emailnotificationservice.processor;

import lombok.RequiredArgsConstructor;
import org.example.emailnotificationservice.model.notification.EventNearResidenceEmailNotificationMessage;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EventNearResidenceMessageProcessor implements NotificationMessageProcessor<EventNearResidenceEmailNotificationMessage> {
    private final TemplateEngine templateEngine;

    @Override
    public String generateHtmlContent(EventNearResidenceEmailNotificationMessage message) {
        var context = new Context();
        context.setVariable("events", message.getEvents());
        return templateEngine.process("events-near-residence-template", context);
    }

}

