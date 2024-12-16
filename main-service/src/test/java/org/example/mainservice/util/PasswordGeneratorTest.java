package org.example.mainservice.util;

import org.example.mainservice.util.PasswordGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    @Test
    void testGeneratePassword_LengthShouldBeAtLeastEight() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PasswordGenerator.generatePassword(7);
        });
        assertEquals("Пароль должен содержать минимум 8 символов.", exception.getMessage());
    }

    @Test
    void testGeneratePassword_CorrectLength() {
        String password = PasswordGenerator.generatePassword(12);
        assertEquals(12, password.length());
    }

    @Test
    void testGeneratePassword_ContainsRequiredCharacters() {
        String password = PasswordGenerator.generatePassword(12);

        assertTrue(password.chars().anyMatch(Character::isLowerCase), "Пароль должен содержать хотя бы одну строчную букву");
        assertTrue(password.chars().anyMatch(Character::isUpperCase), "Пароль должен содержать хотя бы одну заглавную букву");
        assertTrue(password.chars().anyMatch(Character::isDigit), "Пароль должен содержать хотя бы одну цифру");
        assertTrue(password.chars().anyMatch(ch -> "!@#$%^&*()-_+=<>?/|".indexOf(ch) >= 0), "Пароль должен содержать хотя бы один специальный символ");
    }

    @Test
    void testGeneratePassword_ValidPasswordWithDifferentLengths() {
        String password8 = PasswordGenerator.generatePassword(8);
        String password16 = PasswordGenerator.generatePassword(16);
        String password32 = PasswordGenerator.generatePassword(32);

        assertEquals(8, password8.length());
        assertEquals(16, password16.length());
        assertEquals(32, password32.length());
    }
}

