package org.example.emailnotificationservice.model.notification.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.emailnotificationservice.model.notification.CredentialsEmailNotificationMessage;
import org.example.emailnotificationservice.model.notification.EventNearResidenceEmailNotificationMessage;

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
        @JsonSubTypes.Type(value = CredentialsEmailNotificationMessage.class, name = "credentials"),
        @JsonSubTypes.Type(value = EventNearResidenceEmailNotificationMessage.class, name = "event_near_residence")
})
public abstract class BaseEmailNotificationMessage {
    private String email;
    private String subject;
    private String body;
    private String type;
}
