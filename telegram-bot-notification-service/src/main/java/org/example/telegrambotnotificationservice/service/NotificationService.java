package org.example.telegrambotnotificationservice.service;

import lombok.RequiredArgsConstructor;
import org.example.telegrambotnotificationservice.bot.SraBot;
import org.example.telegrambotnotificationservice.factory.NotificationMessageProcessorFactory;
import org.example.telegrambotnotificationservice.model.notification.EventNearResidenceNotificationForTelegramMessage;
import org.example.telegrambotnotificationservice.model.notification.EventNearResidenceNotificationMessage;
import org.example.telegrambotnotificationservice.model.notification.base.BaseEmailNotificationMessage;
import org.example.telegrambotnotificationservice.processor.EventNearResidenceForTelegramMessageProcessor;
import org.example.telegrambotnotificationservice.processor.NotificationMessageProcessor;
import org.example.telegrambotnotificationservice.repository.TelegramUserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SraBot bot;
    private final TelegramUserRepository telegramUserRepository;
    private final NotificationMessageProcessorFactory processorFactory;
    private static final Integer MAX_EVENTS_COUNT = 3;

    public <T extends BaseEmailNotificationMessage> void sendNotification(T message) {
        if (message instanceof EventNearResidenceNotificationMessage eventNearResidenceNotificationMessage) {
            handleEventNearResidenceNotification(eventNearResidenceNotificationMessage);
        } else {
            handleGenericNotification(message);
        }
    }

    private void handleEventNearResidenceNotification(EventNearResidenceNotificationMessage message) {
        List<EventNearResidenceNotificationForTelegramMessage> eventMessages = message.getEvents().stream()
                .limit(MAX_EVENTS_COUNT)
                .map(event -> EventNearResidenceNotificationForTelegramMessage.builder()
                        .event(event)
                        .email(message.getEmail())
                        .telegramUsername(message.getTelegramUsername())
                        .subject(message.getSubject())
                        .body(message.getBody())
                        .type(message.getType())
                        .build())
                .collect(Collectors.toList());

        var chatId = getChatIdByUsername(message.getTelegramUsername());

        sendMessage(chatId, "Подборка ближайших мероприятий рядом с вами");

        var processor = new EventNearResidenceForTelegramMessageProcessor();

        eventMessages.forEach(eventMessage -> {
            String text = processor.processMessage(eventMessage);
            sendMessage(chatId, text);
        });
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseEmailNotificationMessage> void handleGenericNotification(T message) {
        NotificationMessageProcessor<T> processor = (NotificationMessageProcessor<T>) processorFactory.getProcessor(message.getClass());

        if (processor == null) {
            throw new IllegalArgumentException("Процессор для сообщения типа " + message.getClass() + " не найден.");
        }

        var chatId = getChatIdByUsername(message.getTelegramUsername());
        String text = processor.processMessage(message);
        sendMessage(chatId, text);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.enableMarkdown(true);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка при отправке сообщения: " + e.getMessage(), e);
        }
    }

    private Long getChatIdByUsername(String username) {
        var chatId = telegramUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с username " + username + " не найден."));
        return Long.parseLong(chatId);
    }
}