package org.example.emailnotificationservice.factory;

import lombok.RequiredArgsConstructor;
import org.example.emailnotificationservice.model.notification.base.BaseEmailNotificationMessage;
import org.example.emailnotificationservice.processor.NotificationMessageProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationMessageProcessorFactory {
    private final Map<Class<? extends BaseEmailNotificationMessage>, NotificationMessageProcessor<?>> processors = new HashMap<>();

    public NotificationMessageProcessor<?> getProcessor(Class<? extends BaseEmailNotificationMessage> messageClass) {
        return processors.get(messageClass);
    }

    public void registerProcessor(Class<? extends BaseEmailNotificationMessage> messageClass, NotificationMessageProcessor<?> processor) {
        processors.put(messageClass, processor);
    }
}
