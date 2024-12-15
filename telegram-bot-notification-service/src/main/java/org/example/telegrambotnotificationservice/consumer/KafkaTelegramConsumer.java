package org.example.telegrambotnotificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.telegrambotnotificationservice.model.notification.base.BaseEmailNotificationMessage;
import org.example.telegrambotnotificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class KafkaTelegramConsumer {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = "${kafka-config.topic}", groupId = "${kafka-config.group-id}")
    public void consume(String message) {
        var telegramNotificationMessage = objectMapper.readValue(message, BaseEmailNotificationMessage.class);
        if (StringUtils.hasText(telegramNotificationMessage.getTelegramUsername())) {
            notificationService.sendNotification(telegramNotificationMessage);
        }
    }
}
