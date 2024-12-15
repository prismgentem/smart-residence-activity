package org.example.mainservice.model.notification;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.mainservice.model.notification.base.BaseEmailNotificationMessage;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class CredentialsEmailNotificationMessage extends BaseEmailNotificationMessage {
    private String username;
    private String password;
}
