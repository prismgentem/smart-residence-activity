package org.example.telegrambotnotificationservice.processor;

import lombok.RequiredArgsConstructor;
import org.example.telegrambotnotificationservice.model.notification.EventNearResidenceNotificationForTelegramMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class EventNearResidenceForTelegramMessageProcessor implements NotificationMessageProcessor<EventNearResidenceNotificationForTelegramMessage> {
    @Override
    public String processMessage(EventNearResidenceNotificationForTelegramMessage message) {
        StringBuilder markdownMessage = new StringBuilder();

        var event = message.getEvent();

        markdownMessage.append(String.format("*%s*", event.getTitle())).append("\n\n");
        markdownMessage.append(String.format("_%s_", event.getDescription())).append("\n\n");

        if (event.getAgeRestriction() != null) {
            markdownMessage.append(String.format("*Возрастное ограничение:* _%s_", event.getAgeRestriction())).append("\n\n");
        }

        if (StringUtils.hasText(event.getPrice())) {
            markdownMessage.append(String.format("*Цена:* _%s_", event.getPrice())).append("\n\n");
        }

        if (event.getPlace() != null) {
            var place = event.getPlace();
            markdownMessage.append(String.format("*Место проведения:* _%s_", place.getTitle())).append("\n\n");

            if (StringUtils.hasText(place.getAddress())) {
                markdownMessage.append(String.format("*Адрес:* %s", place.getAddress())).append("\n");
            }

            if (StringUtils.hasText(place.getPhone())) {
                markdownMessage.append(String.format("*Телефон:* %s", place.getPhone())).append("\n");
            }
        }

        if (StringUtils.hasText(requireNonNull(event.getPlace()).getSiteUrl())) {
            markdownMessage.append("Ссылки:\n");
            markdownMessage.append(String.format("[сайт](%s)", event.getPlace().getSiteUrl())).append("\n");
        } else if (event.getImages() != null && !event.getImages().isEmpty()) {
            var image = event.getImages().stream().findFirst().get();
            markdownMessage.append(String.format("[изображение](%s)", image));
        }

        return markdownMessage.toString();
    }
}
