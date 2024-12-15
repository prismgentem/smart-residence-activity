package org.example.emailnotificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.emailnotificationservice.model.notification.base.BaseEmailNotificationMessage;
import org.example.emailnotificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEmailConsumer {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = "email-notifications-topic", groupId = "email-notification-consumer-group")
    public void consume(String message) {
        var emailNotificationMessage = objectMapper.readValue(message, BaseEmailNotificationMessage.class);
        notificationService.sendEmail(emailNotificationMessage);
    }
}

