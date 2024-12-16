package org.example.telegrambotnotificationservice.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.telegrambotnotificationservice.repository.TelegramUserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisTelegramUserRepository implements TelegramUserRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String USERNAME_KEY_PREFIX = "telegram_username:";

    @Override
    public void save(String username, long chatId) {
        String key = USERNAME_KEY_PREFIX + username;
        redisTemplate.opsForValue().set(key, String.valueOf(chatId));
    }

    @Override
    public void deleteByUsername(String username) {
        String key = USERNAME_KEY_PREFIX + username;
        redisTemplate.delete(key);
    }

    @Override
    public Optional<String> findByUsername(String username) {
        String key = USERNAME_KEY_PREFIX + username;
        String chatId = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(chatId);
    }
}
