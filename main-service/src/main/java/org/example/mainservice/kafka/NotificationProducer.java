package org.example.mainservice.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.mainservice.model.notification.base.BaseEmailNotificationMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {
    @Value("${kafka-topic.email-notifications}")
    private String notificationsTopic;
    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @SneakyThrows
    public void sendEmailNotification(BaseEmailNotificationMessage message) {
        var jsonMessage = objectMapper.writeValueAsString(message);
        kafkaTemplate.send(notificationsTopic, jsonMessage);
    }
}
