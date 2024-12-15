package org.example.mainservice.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%&*|";
    private static final String ALL_CHARACTERS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generatePassword(Integer length) {
        if (length < 8) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 8 символов.");
        }

        var password = new StringBuilder(length);

        password.append(randomCharacter(LOWERCASE));
        password.append(randomCharacter(UPPERCASE));
        password.append(randomCharacter(DIGITS));
        password.append(randomCharacter(SPECIAL_CHARACTERS));

        for (int i = password.length(); i < length; i++) {
            password.append(randomCharacter(ALL_CHARACTERS));
        }

        return shuffleString(password.toString());
    }

    private char randomCharacter(String characters) {
        return characters.charAt(SECURE_RANDOM.nextInt(characters.length()));
    }

    private String shuffleString(String input) {
        var shuffled = new StringBuilder(input);
        for (int i = 0; i < shuffled.length(); i++) {
            int j = SECURE_RANDOM.nextInt(shuffled.length());
            char temp = shuffled.charAt(i);
            shuffled.setCharAt(i, shuffled.charAt(j));
            shuffled.setCharAt(j, temp);
        }
        return shuffled.toString();
    }
}

