package org.example.telegrambotnotificationservice.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.telegrambotnotificationservice.model.kudago.EventResponse;
import org.example.telegrambotnotificationservice.model.notification.base.BaseEmailNotificationMessage;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventNearResidenceNotificationMessage extends BaseEmailNotificationMessage {
    private List<EventResponse> events;
}
