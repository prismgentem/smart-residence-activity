package org.example.mainservice.model.notification.base;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
public abstract class BaseEmailNotificationMessage {
    private String email;
    private String telegramUsername;
    private String subject;
    private String body;
    private String type;
}
