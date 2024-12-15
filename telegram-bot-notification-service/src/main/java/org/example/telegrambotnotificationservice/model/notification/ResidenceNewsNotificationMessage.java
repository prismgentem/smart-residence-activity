package org.example.telegrambotnotificationservice.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.telegrambotnotificationservice.model.notification.base.BaseEmailNotificationMessage;
import org.example.telegrambotnotificationservice.model.residencenews.ResidenceNewsResponse;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class ResidenceNewsNotificationMessage extends BaseEmailNotificationMessage {
    private ResidenceNewsResponse residenceNews;
}
