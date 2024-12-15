package org.example.mainservice.model.notification;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.mainservice.model.notification.base.BaseEmailNotificationMessage;
import org.example.mainservice.model.response.EventResponse;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventNearResidenceEmailNotificationMessage extends BaseEmailNotificationMessage {
    private List<EventResponse> events;
}
