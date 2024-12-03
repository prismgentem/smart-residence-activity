package org.example.emailnotificationservice.config;

import org.example.emailnotificationservice.factory.NotificationMessageProcessorFactory;
import org.example.emailnotificationservice.model.notification.CredentialsEmailNotificationMessage;
import org.example.emailnotificationservice.model.notification.EventNearResidenceEmailNotificationMessage;
import org.example.emailnotificationservice.processor.CredentialsMessageProcessor;
import org.example.emailnotificationservice.processor.EventNearResidenceMessageProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfiguration {

    @Bean
    public NotificationMessageProcessorFactory processorFactory(
            EventNearResidenceMessageProcessor eventProcessor,
            CredentialsMessageProcessor credentialsProcessor) {
        NotificationMessageProcessorFactory factory = new NotificationMessageProcessorFactory();

        factory.registerProcessor(EventNearResidenceEmailNotificationMessage.class, eventProcessor);
        factory.registerProcessor(CredentialsEmailNotificationMessage.class, credentialsProcessor);

        return factory;
    }
}

