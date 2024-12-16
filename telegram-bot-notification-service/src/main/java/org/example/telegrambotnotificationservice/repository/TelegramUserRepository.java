package org.example.telegrambotnotificationservice.repository;

import java.util.Optional;

public interface TelegramUserRepository {

    void save(String username, long chatId);

    void deleteByUsername(String username);

    Optional<String> findByUsername(String username);
}
