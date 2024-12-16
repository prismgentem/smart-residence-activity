package org.example.telegrambotnotificationservice.service;

import lombok.RequiredArgsConstructor;
import org.example.telegrambotnotificationservice.repository.TelegramUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;

    public void subscribeUser(long chatId, String username) {
        telegramUserRepository.findByUsername(username).ifPresent(user -> {
            throw new IllegalArgumentException("User with this chatId already exists");
        });

        telegramUserRepository.save(username, chatId);
    }

    public void unsubscribeUser(String username) {
        telegramUserRepository.deleteByUsername(username);
    }
}
