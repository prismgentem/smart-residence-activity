package org.example.emailnotificationservice.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.emailnotificationservice.model.notification.base.BaseEmailNotificationMessage;
import org.example.emailnotificationservice.model.residencenews.ResidenceNewsResponse;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class ResidenceNewsEmailNotificationMessage extends BaseEmailNotificationMessage {
    private ResidenceNewsResponse residenceNews;
}
