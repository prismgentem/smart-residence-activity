package org.example.telegrambotnotificationservice.model.notification.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.telegrambotnotificationservice.model.notification.EventNearResidenceNotificationMessage;
import org.example.telegrambotnotificationservice.model.notification.ResidenceNewsNotificationMessage;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EventNearResidenceNotificationMessage.class, name = "event_near_residence"),
        @JsonSubTypes.Type(value = ResidenceNewsNotificationMessage.class, name = "residence_news")
})
public abstract class BaseEmailNotificationMessage {
    private String email;
    private String telegramUsername;
    private String subject;
    private String body;
    private String type;
}
