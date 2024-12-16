package org.example.emailnotificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.emailnotificationservice.factory.NotificationMessageProcessorFactory;
import org.example.emailnotificationservice.model.notification.base.BaseEmailNotificationMessage;
import org.example.emailnotificationservice.processor.NotificationMessageProcessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmailService emailService;
    private final NotificationMessageProcessorFactory processorFactory;

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <T extends BaseEmailNotificationMessage> void sendEmail(T message){
        NotificationMessageProcessor<T> processor = (NotificationMessageProcessor<T>) processorFactory.getProcessor(message.getClass());

        if (processor != null) {
            var htmlContent = processor.generateHtmlContent(message);
            emailService.sendHtmlEmail(message.getEmail(), message.getSubject(), htmlContent);
        } else {
            throw new IllegalArgumentException("No processor found for message type: " + message.getClass());
        }
    }
}
