package org.example.telegrambotnotificationservice;

import org.example.telegrambotnotificationservice.bot.SraBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TelegramBotNotificationServiceApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(TelegramBotNotificationServiceApplication.class, args);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(ctx.getBean(SraBot.class));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
