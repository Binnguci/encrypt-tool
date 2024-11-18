package org.midterm.service.encryption.symmetric_encryption.classic;

import org.midterm.constant.StringConstant;

import java.util.*;

public class SubstitutionCipher {

    public static SubstitutionCipher create() {
        return new SubstitutionCipher();
    }


    public String generateRandomKey(String language) {
        String alphabet = getAlphabetByLanguage(language);

        Set<Character> uniqueCharacters = new LinkedHashSet<>();
        for (char c : alphabet.toCharArray()) {
            uniqueCharacters.add(c);
        }
        if (uniqueCharacters.size() != alphabet.length()) {
            throw new IllegalStateException("Key generation error: mismatched alphabet size.");
        }
        List<Character> characters = new ArrayList<>(uniqueCharacters);
        Collections.shuffle(characters);
        StringBuilder keyBuilder = new StringBuilder();
        for (char c : characters) {
            keyBuilder.append(c);
        }
        return keyBuilder.toString();
    }


    public String encrypt(String plainText, String key, String language) {
        String alphabet = getAlphabetByLanguage(language);
        Map<Character, Character> encryptionMap = createEncryptionMap(key, alphabet);
        StringBuilder cipherText = new StringBuilder();
        for (char c : plainText.toCharArray()) { // Giữ nguyên ký tự
            cipherText.append(encryptionMap.getOrDefault(c, c));
        }
        return cipherText.toString();
    }

    public String decrypt(String cipherText, String key, String language) {
        String alphabet = getAlphabetByLanguage(language);
        Map<Character, Character> decryptionMap = createDecryptionMap(key, alphabet);
        StringBuilder plainText = new StringBuilder();
        for (char c : cipherText.toCharArray()) { // Giữ nguyên ký tự
            plainText.append(decryptionMap.getOrDefault(c, c));
        }
        return plainText.toString();
    }

    private String getAlphabetByLanguage(String language) {
        switch (language.toUpperCase()) {
            case StringConstant.LANGUAGE_VIETNAMESE:
                return StringConstant.VIETNAMESE_ALPHABET;
            case StringConstant.LANGUAGE_ENGLISH:
            default:
                return StringConstant.ENGLISH_ALPHABET;
        }
    }

    private Map<Character, Character> createEncryptionMap(String key, String alphabet) {
        if (key.length() != alphabet.length()) {
            throw new IllegalArgumentException("Key length must match the alphabet length.");
        }
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            map.put(alphabet.charAt(i), key.charAt(i));
        }
        return map;
    }


    private Map<Character, Character> createDecryptionMap(String key, String alphabet) {
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            map.put(key.charAt(i), alphabet.charAt(i));
        }
        return map;
    }

    public static void checkDuplicateCharacters(String alphabet) {
        Set<Character> uniqueCharacters = new HashSet<>();
        for (char c : alphabet.toCharArray()) {
            if (!uniqueCharacters.add(c)) {
                System.out.println("Duplicate character: " + c);
            }
        }
        System.out.println("Total unique characters: " + uniqueCharacters.size());
        System.out.println("Total characters in alphabet: " + alphabet.length());
    }
}

