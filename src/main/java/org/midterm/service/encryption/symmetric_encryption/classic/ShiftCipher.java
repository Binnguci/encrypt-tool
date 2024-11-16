package org.midterm.service.encryption.symmetric_encryption.classic;

import org.midterm.constant.StringConstant;

public class ShiftCipher {

    public static ShiftCipher create() {
        return new ShiftCipher();
    }

    public String encryptEnglish(String plainText, int shift) {
        StringBuilder cipherText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) (base + (c - base + shift) % 26);
            } else if (Character.isDigit(c)) {
                c = (char) ('0' + (c - '0' + shift) % 10);
            }
            cipherText.append(c);
        }
        return cipherText.toString();
    }

    public String encryptVietnamese(String plaintext, int shift) {
        StringBuilder cipherText = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            int index = StringConstant.VIETNAMESE_ALPHABET.indexOf(c);
            if (index != -1) {
                index = (index + shift) % StringConstant.VIETNAMESE_ALPHABET.length();
                cipherText.append(StringConstant.VIETNAMESE_ALPHABET.charAt(index));
            } else if (Character.isDigit(c)) {
                c = (char) ('0' + (c - '0' + shift) % 10);
                cipherText.append(c);
            } else {
                cipherText.append(c);
            }
        }
        return cipherText.toString();
    }

    public String decryptEnglish(String cipherText, int shift) {
        return encryptEnglish(cipherText, 26 - shift);
    }

    public String decryptVietnamese(String cipherText, int shift) {
        return encryptVietnamese(cipherText, StringConstant.VIETNAMESE_ALPHABET.length() - shift);
    }
}
