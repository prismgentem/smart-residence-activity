package org.example.emailnotificationservice.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.emailnotificationservice.model.kudago.EventResponse;
import org.example.emailnotificationservice.model.notification.base.BaseEmailNotificationMessage;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventNearResidenceEmailNotificationMessage extends BaseEmailNotificationMessage {
    private List<EventResponse> events;
}
