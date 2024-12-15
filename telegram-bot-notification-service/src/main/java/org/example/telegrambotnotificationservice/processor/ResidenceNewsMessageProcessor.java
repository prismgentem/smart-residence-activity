package org.example.telegrambotnotificationservice.processor;

import lombok.RequiredArgsConstructor;
import org.example.telegrambotnotificationservice.model.notification.ResidenceNewsNotificationMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResidenceNewsMessageProcessor implements NotificationMessageProcessor<ResidenceNewsNotificationMessage> {
    @Override
    public String processMessage(ResidenceNewsNotificationMessage message) {
        var news = message.getResidenceNews();

        StringBuilder markdownMessage = new StringBuilder();
        markdownMessage.append("#НовостьЖК\n\n");

        markdownMessage.append(String.format("_%s_", news.getTitle())).append("\n\n");
        markdownMessage.append(String.format("%s", news.getContent())).append("\n\n");

        var admin = news.getCreatedBy();
        if (admin != null) {
            markdownMessage.append(String.format("*автор:* _%s %s %s._", admin.getLastName(), admin.getFirstName(), admin.getSecondName())).append("\n");
            markdownMessage.append(String.format("*email:* %s", admin.getEmail())).append("\n\n");
        }

        var residence = news.getResidence();
        if (residence != null) {
            markdownMessage.append(String.format("*ЖК:* _%s._ ", residence.getName())).append("\n");
            markdownMessage.append(String.format("*Адрес:* _%s._ ", residence.getAddress()));
        }

        return markdownMessage.toString();
    }
}
