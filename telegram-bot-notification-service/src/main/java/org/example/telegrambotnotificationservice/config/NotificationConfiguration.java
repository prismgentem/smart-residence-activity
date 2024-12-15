package org.example.telegrambotnotificationservice.config;

import org.example.telegrambotnotificationservice.factory.NotificationMessageProcessorFactory;
import org.example.telegrambotnotificationservice.model.notification.EventNearResidenceNotificationForTelegramMessage;
import org.example.telegrambotnotificationservice.model.notification.ResidenceNewsNotificationMessage;
import org.example.telegrambotnotificationservice.processor.EventNearResidenceForTelegramMessageProcessor;
import org.example.telegrambotnotificationservice.processor.ResidenceNewsMessageProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfiguration {
    @Bean
    public NotificationMessageProcessorFactory processorFactory(
            ResidenceNewsMessageProcessor residenceNewsMessageProcessor,
            EventNearResidenceForTelegramMessageProcessor eventNearResidenceForTelegramMessageProcessor) {
        NotificationMessageProcessorFactory factory = new NotificationMessageProcessorFactory();

        factory.registerProcessor(ResidenceNewsNotificationMessage.class, residenceNewsMessageProcessor);
        factory.registerProcessor(EventNearResidenceNotificationForTelegramMessage.class, eventNearResidenceForTelegramMessageProcessor);
        return factory;
    }
}
