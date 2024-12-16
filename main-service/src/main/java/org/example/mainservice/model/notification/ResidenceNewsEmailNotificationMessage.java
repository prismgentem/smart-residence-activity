package org.example.mainservice.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.mainservice.model.notification.base.BaseEmailNotificationMessage;
import org.example.mainservice.model.response.ResidenceNewsResponse;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class ResidenceNewsEmailNotificationMessage extends BaseEmailNotificationMessage {
    private ResidenceNewsResponse residenceNews;
}
