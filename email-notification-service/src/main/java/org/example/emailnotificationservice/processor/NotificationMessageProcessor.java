package org.example.emailnotificationservice.processor;

import org.example.emailnotificationservice.model.notification.base.BaseEmailNotificationMessage;

public interface NotificationMessageProcessor<T extends BaseEmailNotificationMessage> {
    String generateHtmlContent(T message);
}
