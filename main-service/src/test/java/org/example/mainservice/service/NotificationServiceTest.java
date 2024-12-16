package org.example.mainservice.service;

import org.example.mainservice.kafka.NotificationProducer;
import org.example.mainservice.model.notification.CredentialsEmailNotificationMessage;
import org.example.mainservice.model.notification.EventNearResidenceEmailNotificationMessage;
import org.example.mainservice.model.notification.ResidenceNewsEmailNotificationMessage;
import org.example.mainservice.model.notification.base.BaseEmailNotificationMessage;
import org.example.mainservice.model.response.EventResponse;
import org.example.mainservice.model.response.ResidenceNewsResponse;
import org.example.mainservice.entity.ResidenceNews;
import org.example.mainservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.convert.ConversionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationProducer notificationProducer;

    @Mock
    private ConversionService conversionService;

    private String email = "test@example.com";
    private String telegramUsername = "testTelegram";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendCredentialsEmailNotification_ShouldCallNotificationProducer() {
        // Arrange
        String email = "test@example.com";
        String username = "testuser";
        String password = "testpassword";

        CredentialsEmailNotificationMessage expectedMessage = CredentialsEmailNotificationMessage.builder()
                .email(email)
                .subject("Учётная запись для доступа к личному кабинету")
                .body("Ваши учётные данные для доступа к личному кабинету")
                .username(username)
                .type("credentials")
                .password(password)
                .build();

        // Act
        notificationService.sendCredentialsEmailNotification(email, username, password);

        ArgumentCaptor<BaseEmailNotificationMessage> captor = ArgumentCaptor.forClass(BaseEmailNotificationMessage.class);
        // Verify
        verify(notificationProducer, times(1)).sendEmailNotification(captor.capture());

        // Assert
        BaseEmailNotificationMessage actualMessage = captor.getValue();
        assertEquals(expectedMessage.getEmail(), actualMessage.getEmail());
        assertEquals(expectedMessage.getSubject(), actualMessage.getSubject());
        assertEquals(expectedMessage.getBody(), actualMessage.getBody());
        assertEquals(expectedMessage.getUsername(), username);
        assertEquals(expectedMessage.getType(), actualMessage.getType());
        assertEquals(expectedMessage.getPassword(), password);
    }

    @Test
    void sendEventNearResidenceEmailNotificationMessage_ShouldCallNotificationProducer() {
        // Arrange
        EventResponse eventResponse = new EventResponse();
        String email = "test@example.com";
        String telegramUsername = "testTelegram";

        EventNearResidenceEmailNotificationMessage expectedMessage = EventNearResidenceEmailNotificationMessage.builder()
                .email(email)
                .telegramUsername(telegramUsername)
                .subject("Подборка мероприятий рядом с вашим домом")
                .body("Список мероприятий рядом с вашим домом")
                .events(List.of(eventResponse))
                .type("event_near_residence")
                .build();

        // Act
        notificationService.sendEventNearResidenceEmailNotificationMessage(telegramUsername, email, List.of(eventResponse));

        ArgumentCaptor<EventNearResidenceEmailNotificationMessage> captor = ArgumentCaptor.forClass(EventNearResidenceEmailNotificationMessage.class);

        // Verify
        verify(notificationProducer, times(1)).sendEmailNotification(captor.capture());

        // Assert
        EventNearResidenceEmailNotificationMessage capturedMessage = captor.getValue();
        assertEquals(expectedMessage.getEmail(), capturedMessage.getEmail());
        assertEquals(expectedMessage.getTelegramUsername(), capturedMessage.getTelegramUsername());
        assertEquals(expectedMessage.getSubject(), capturedMessage.getSubject());
        assertEquals(expectedMessage.getBody(), capturedMessage.getBody());
        assertEquals(expectedMessage.getEvents(), capturedMessage.getEvents());
        assertEquals(expectedMessage.getType(), capturedMessage.getType());
    }

    @Test
    void sendResidenceNewsEmailNotification_ShouldCallNotificationProducer() {
        // Arrange
        ResidenceNews residenceNews = new ResidenceNews();
        ResidenceNewsResponse residenceNewsResponse = new ResidenceNewsResponse();

        Mockito.when(conversionService.convert(residenceNews, ResidenceNewsResponse.class)).thenReturn(residenceNewsResponse);

        ResidenceNewsEmailNotificationMessage expectedMessage = ResidenceNewsEmailNotificationMessage.builder()
                .email(email)
                .telegramUsername(telegramUsername)
                .subject("Важная новость вашего ЖК")
                .body("Новость")
                .type("residence_news")
                .residenceNews(residenceNewsResponse)
                .build();

        // Act
        notificationService.sendResidenceNewsEmailNotification(telegramUsername, email, residenceNews);

        ArgumentCaptor<ResidenceNewsEmailNotificationMessage> captor = ArgumentCaptor.forClass(ResidenceNewsEmailNotificationMessage.class);

        // Verify
        verify(notificationProducer, times(1)).sendEmailNotification(captor.capture());

        // Assert
        ResidenceNewsEmailNotificationMessage capturedMessage = captor.getValue();
        assertEquals(expectedMessage.getEmail(), capturedMessage.getEmail());
        assertEquals(expectedMessage.getTelegramUsername(), capturedMessage.getTelegramUsername());
        assertEquals(expectedMessage.getSubject(), capturedMessage.getSubject());
        assertEquals(expectedMessage.getBody(), capturedMessage.getBody());
        assertEquals(expectedMessage.getType(), capturedMessage.getType());
        assertEquals(expectedMessage.getResidenceNews(), capturedMessage.getResidenceNews());
    }

}
