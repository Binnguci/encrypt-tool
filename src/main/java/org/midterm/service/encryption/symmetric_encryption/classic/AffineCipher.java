package org.midterm.service.encryption.symmetric_encryption.classic;

import org.midterm.constant.StringConstant;

import java.util.HashMap;
import java.util.Map;

public class AffineCipher {
    private final Map<String, String> alphabets = new HashMap<>();

    public static AffineCipher create() {
        return new AffineCipher();
    }

    public AffineCipher() {
        alphabets.put(StringConstant.LANGUAGE_ENGLISH, StringConstant.ENGLISH_ALPHABET);
        alphabets.put(StringConstant.LANGUAGE_VIETNAMESE, StringConstant.VIETNAMESE_ALPHABET);
    }

    private int modularInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        throw new IllegalArgumentException("a và m không nguyên tố cùng nhau, không thể tính nghịch đảo.");
    }

    public String encrypt(String plainText, int a, int b, String language) {
        String alphabet = getAlphabetByLanguage(language);
        int m = alphabet.length();

        if (gcd(a, m) != 1) {
            throw new IllegalArgumentException("a phải nguyên tố cùng nhau với m.");
        }

        StringBuilder cipherText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            int index = alphabet.indexOf(c);
            if (index == -1) {
                cipherText.append(c);
            } else {
                int encryptedIndex = (a * index + b) % m;
                cipherText.append(alphabet.charAt(encryptedIndex));
            }
        }
        return cipherText.toString();
    }

    public String decrypt(String cipherText, int a, int b, String language) {
        String alphabet = getAlphabetByLanguage(language);
        int m = alphabet.length();

        if (gcd(a, m) != 1) {
            throw new IllegalArgumentException("a phải nguyên tố cùng nhau với m.");
        }

        int aInverse = modularInverse(a, m);
        StringBuilder plainText = new StringBuilder();
        for (char c : cipherText.toCharArray()) {
            int index = alphabet.indexOf(c);
            if (index == -1) {
                plainText.append(c);
            } else {
                int decryptedIndex = (aInverse * (index - b + m)) % m;
                plainText.append(alphabet.charAt(decryptedIndex));
            }
        }
        return plainText.toString();
    }

    private String getAlphabetByLanguage(String language) {
        return alphabets.getOrDefault(language.toUpperCase(), StringConstant.ENGLISH_ALPHABET);
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static void main(String[] args) {
        AffineCipher cipher = new AffineCipher();

        String plainTextEnglish = "Hello, World!";
        String plainTextVietnamese = "Xin chào, Thế giới!";

        int a = 5; // Hệ số nhân
        int b = 8; // Hệ số dịch

        // Mã hóa và giải mã tiếng Anh
        String encryptedEnglish = cipher.encrypt(plainTextEnglish, a, b, StringConstant.LANGUAGE_ENGLISH);
        String decryptedEnglish = cipher.decrypt(encryptedEnglish, a, b, StringConstant.LANGUAGE_ENGLISH);

        System.out.println("Original English: " + plainTextEnglish);
        System.out.println("Encrypted English: " + encryptedEnglish);
        System.out.println("Decrypted English: " + decryptedEnglish);

        // Mã hóa và giải mã tiếng Việt
        String encryptedVietnamese = cipher.encrypt(plainTextVietnamese, a, b, StringConstant.LANGUAGE_VIETNAMESE);
        String decryptedVietnamese = cipher.decrypt(encryptedVietnamese, a, b, StringConstant.LANGUAGE_VIETNAMESE);

        System.out.println("Original Vietnamese: " + plainTextVietnamese);
        System.out.println("Encrypted Vietnamese: " + encryptedVietnamese);
        System.out.println("Decrypted Vietnamese: " + decryptedVietnamese);
    }
}

