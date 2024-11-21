package org.midterm.service.encryption.classic;

import org.midterm.constant.StringConstant;
import org.midterm.model.AffineKey;

import java.util.HashMap;
import java.util.Map;

public class AffineCipher {

    public String encrypt(String plainText, String language, Object key) {
        if (!(key instanceof AffineKey affineKey)) {
            throw new IllegalArgumentException("Key must be an AffineKey.");
        }
        return encryptAffine(plainText, affineKey.getA(), affineKey.getB(), language);
    }

    public String decrypt(String cipherText, String language, Object key) {
        if (!(key instanceof AffineKey affineKey)) {
            throw new IllegalArgumentException("Key must be an AffineKey.");
        }
        return decryptAffine(cipherText, affineKey.getA(), affineKey.getB(), language);
    }

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
        throw new IllegalArgumentException("a and m are not coprime and cannot be inversed.");
    }

    public String encryptAffine(String plainText, int a, int b, String language) {
        String alphabet = getAlphabetByLanguage(language);
        int m = alphabet.length();

        if (gcd(a, m) != 1) {
            throw new IllegalArgumentException("a and m must be coprime.");
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

    public String decryptAffine(String cipherText, int a, int b, String language) {
        String alphabet = getAlphabetByLanguage(language);
        int m = alphabet.length();

        if (gcd(a, m) != 1) {
            throw new IllegalArgumentException("\n" + "a must be coprime with m.");
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

}

