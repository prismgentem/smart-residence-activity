package org.example.telegrambotnotificationservice.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.telegrambotnotificationservice.model.kudago.EventResponse;
import org.example.telegrambotnotificationservice.model.notification.base.BaseEmailNotificationMessage;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventNearResidenceNotificationForTelegramMessage extends BaseEmailNotificationMessage {
    private EventResponse event;
}
