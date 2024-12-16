package org.example.telegrambotnotificationservice.bot;

import org.example.telegrambotnotificationservice.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.MessageContext;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Component
public class SraBot extends AbilityBot {
    @Value("${bot.creator-id}")
    private String creatorId;

    private final TelegramUserService telegramUserService;

    public SraBot(@Value("${bot.token}") String botToken,
                  @Value("${bot.username}") String botUsername,
                  TelegramUserService telegramUserService) {
        super(botToken, botUsername);
        this.telegramUserService = telegramUserService;
    }

    @Override
    public long creatorId() {
        return Long.parseLong(creatorId);
    }

    public Ability start() {
        return Ability.builder()
                .name("start")
                .info("Подписка на уведомления")
                .locality(USER)
                .privacy(PUBLIC)
                .action(this::subscribeUser)
                .build();
    }

    public Ability stop() {
        return Ability.builder()
                .name("stop")
                .info("Отписка от уведомлений")
                .locality(USER)
                .privacy(PUBLIC)
                .action(this::unsubscribeUser)
                .build();
    }

    private void subscribeUser(MessageContext ctx) {
        long chatId = ctx.chatId();
        String username = ctx.user().getUserName();
        telegramUserService.subscribeUser(chatId, username);
        silent.send("Вы успешно подписаны на уведомления!", chatId);
    }

    private void unsubscribeUser(MessageContext ctx) {
        var username = ctx.user().getUserName();
        var chatId = ctx.chatId();
        telegramUserService.unsubscribeUser(username);
        silent.send("Вы успешно отписаны от уведомлений!", chatId);
    }
}


