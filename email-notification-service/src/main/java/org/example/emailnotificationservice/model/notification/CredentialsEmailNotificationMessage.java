package org.example.emailnotificationservice.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.emailnotificationservice.model.notification.base.BaseEmailNotificationMessage;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class CredentialsEmailNotificationMessage extends BaseEmailNotificationMessage {
    private String username;
    private String password;
}
