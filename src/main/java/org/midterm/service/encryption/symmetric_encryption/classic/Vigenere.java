package org.midterm.service.encryption.symmetric_encryption.classic;

import org.midterm.constant.StringConstant;

import java.util.Random;

public class Vigenere {

    public static String encrypt(String plaintext, String key, String language) {
        String alphabet = getAlphabetByLanguage(language);
        StringBuilder ciphertext = new StringBuilder();
        String extendedKey = extendKey(key, plaintext.length(), alphabet);

        for (int i = 0; i < plaintext.length(); i++) {
            char plainChar = plaintext.charAt(i);
            char keyChar = extendedKey.charAt(i);

            int plainIndex = alphabet.indexOf(plainChar);
            int keyIndex = alphabet.indexOf(keyChar);

            if (plainIndex == -1 || keyIndex == -1) {
                // Ký tự không nằm trong bảng chữ cái, giữ nguyên
                ciphertext.append(plainChar);
            } else {
                int encryptedIndex = (plainIndex + keyIndex) % alphabet.length();
                ciphertext.append(alphabet.charAt(encryptedIndex));
            }
        }

        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext, String key, String language) {
        String alphabet = getAlphabetByLanguage(language);
        StringBuilder plaintext = new StringBuilder();
        String extendedKey = extendKey(key, ciphertext.length(), alphabet);

        for (int i = 0; i < ciphertext.length(); i++) {
            char cipherChar = ciphertext.charAt(i);
            char keyChar = extendedKey.charAt(i);

            int cipherIndex = alphabet.indexOf(cipherChar);
            int keyIndex = alphabet.indexOf(keyChar);

            if (cipherIndex == -1 || keyIndex == -1) {
                // Ký tự không nằm trong bảng chữ cái, giữ nguyên
                plaintext.append(cipherChar);
            } else {
                int decryptedIndex = (cipherIndex - keyIndex + alphabet.length()) % alphabet.length();
                plaintext.append(alphabet.charAt(decryptedIndex));
            }
        }

        return plaintext.toString();
    }

    public static String generateRandomKey(String language, int keyLength) {
        String alphabet = getAlphabetByLanguage(language);
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < keyLength; i++) {
            int index = random.nextInt(alphabet.length());
            key.append(alphabet.charAt(index));
        }

        return key.toString();
    }

    private static String getAlphabetByLanguage(String language) {
        switch (language.toUpperCase()) {
            case StringConstant.LANGUAGE_VIETNAMESE:
                return StringConstant.VIETNAMESE_ALPHABET;
            case StringConstant.LANGUAGE_ENGLISH:
            default:
                return StringConstant.ENGLISH_ALPHABET;
        }
    }

    private static String extendKey(String key, int textLength, String alphabet) {
        StringBuilder extendedKey = new StringBuilder();
        int keyLength = key.length();

        for (int i = 0; i < textLength; i++) {
            char keyChar = key.charAt(i % keyLength);
            if (alphabet.indexOf(keyChar) != -1) {
                extendedKey.append(keyChar);
            } else {
                extendedKey.append(alphabet.charAt(i % alphabet.length()));
            }
        }
        return extendedKey.toString();
    }

    public static Vigenere create() {
        return new Vigenere();
    }
}
