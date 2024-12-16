package org.example.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.example.mainservice.entity.ResidenceNews;
import org.example.mainservice.kafka.NotificationProducer;
import org.example.mainservice.model.notification.CredentialsEmailNotificationMessage;
import org.example.mainservice.model.notification.EventNearResidenceEmailNotificationMessage;
import org.example.mainservice.model.notification.ResidenceNewsEmailNotificationMessage;
import org.example.mainservice.model.response.EventResponse;
import org.example.mainservice.model.response.ResidenceNewsResponse;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationProducer notificationProducer;
    private final ConversionService conversionService;
    private static final String CREDENTIALS_TYPE = "credentials";
    private static final String EVENT_NEAR_RESIDENCE_TYPE = "event_near_residence";
    private static final String RESIDENCE_NEWS_TYPE = "residence_news";

    public void sendCredentialsEmailNotification(String email,String username, String password) {
        notificationProducer.sendEmailNotification(
                CredentialsEmailNotificationMessage.builder()
                        .email(email)
                        .subject("Учётная запись для доступа к личному кабинету")
                        .body("Ваши учётные данные для доступа к личному кабинету")
                        .username(username)
                        .type(CREDENTIALS_TYPE)
                        .password(password)
                        .build()
        );
    }

    public void sendEventNearResidenceEmailNotificationMessage(String telegramUsername, String email, List<EventResponse> events) {
        notificationProducer.sendEmailNotification(
                EventNearResidenceEmailNotificationMessage.builder()
                        .email(email)
                        .telegramUsername(telegramUsername)
                        .subject("Подборка мероприятий рядом с вашим домом")
                        .body("Список мероприятий рядом с вашим домом")
                        .events(events)
                        .type(EVENT_NEAR_RESIDENCE_TYPE)
                        .build()
        );
    }

    public void sendResidenceNewsEmailNotification(String telegramUsername, String email, ResidenceNews residenceNews){
        notificationProducer.sendEmailNotification(
                ResidenceNewsEmailNotificationMessage.builder()
                        .email(email)
                        .telegramUsername(telegramUsername)
                        .subject("Важная новость вашего ЖК")
                        .body("Новость")
                        .type(RESIDENCE_NEWS_TYPE)
                        .residenceNews(conversionService.convert(residenceNews, ResidenceNewsResponse.class))
                        .build()
        );
    }

}
