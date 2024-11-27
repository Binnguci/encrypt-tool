package org.midterm.service.encryption.classic;

import org.midterm.constant.StringConstant;

import java.util.Random;

/**
 * Lớp này triển khai thuật toán mã hóa và giải mã Vigenère.
 * Thuật toán mã hóa Vigenère là một phương pháp mã hóa văn bản chữ cái bằng cách sử dụng một dạng thay thế đa thức đơn giản.
 * Quá trình mã hóa và giải mã được thực hiện bằng cách sử dụng một khóa được lặp lại sao cho phù hợp với độ dài của văn bản đầu vào.
 */
public class Vigenere {

    /**
     * Tạo một phiên bản mới của dịch vụ mã hóa Vigenère.
     *
     * @return Một đối tượng {@link Vigenere} mới.
     */
    public static Vigenere create() {
        return new Vigenere();
    }

    /**
     * Sinh khóa ngẫu nhiên để sử dụng trong việc mã hóa hoặc giải mã.
     * Khóa được tạo ra bằng cách chọn các ký tự ngẫu nhiên từ bảng chữ cái cho ngôn ngữ đã chỉ định.
     * Độ dài của khóa xấp xỉ hai phần ba độ dài của văn bản gốc.
     *
     * @param plaintext Văn bản gốc để sinh khóa.
     * @param language Ngôn ngữ cho khóa sẽ được tạo.
     * @return Khóa đã tạo.
     */
    public String generateKey(String plaintext, String language) {
        String alphabet = getAlphabetByLanguage(language);
        StringBuilder key = new StringBuilder();
        Random random = new Random();
        int keyLength = Math.max(1, (2 * plaintext.length()) / 3);
        for (int i = 0; i < keyLength; i++) {
            int index = random.nextInt(alphabet.length());
            key.append(alphabet.charAt(index));
        }

        return key.toString();
    }

    /**
     * Mã hóa văn bản gốc sử dụng thuật toán Vigenère với khóa đã cung cấp.
     * Quá trình mã hóa thực hiện bằng cách dịch chuyển từng ký tự trong văn bản gốc theo ký tự tương ứng trong khóa.
     *
     * @param plaintext Văn bản gốc cần mã hóa.
     * @param key Khóa sử dụng để mã hóa.
     * @param language Ngôn ngữ của văn bản gốc (Tiếng Anh hoặc Tiếng Việt).
     * @return Văn bản mã hóa (ciphertext).
     */
    public String encrypt(String plaintext, String key, String language) {
        String alphabet = getAlphabetByLanguage(language);
        StringBuilder ciphertext = new StringBuilder();
        String extendedKey = extendKey(key, plaintext.length(), alphabet);

        for (int i = 0; i < plaintext.length(); i++) {
            char plainChar = plaintext.charAt(i);
            char keyChar = extendedKey.charAt(i);

            int plainIndex = alphabet.indexOf(plainChar);
            int keyIndex = alphabet.indexOf(keyChar);

            if (plainIndex == -1 || keyIndex == -1) {
                ciphertext.append(plainChar);
            } else {
                int encryptedIndex = (plainIndex + keyIndex) % alphabet.length();
                ciphertext.append(alphabet.charAt(encryptedIndex));
            }
        }

        return ciphertext.toString();
    }

    /**
     * Giải mã văn bản mã hóa sử dụng thuật toán Vigenère với khóa đã cung cấp.
     * Quá trình giải mã thực hiện bằng cách đảo ngược phép dịch chuyển đã dùng trong mã hóa.
     *
     * @param ciphertext Văn bản mã hóa cần giải mã.
     * @param key Khóa sử dụng để giải mã.
     * @param language Ngôn ngữ của văn bản mã hóa (Tiếng Anh hoặc Tiếng Việt).
     * @return Văn bản giải mã (plaintext).
     */
    public String decrypt(String ciphertext, String key, String language) {
        String alphabet = getAlphabetByLanguage(language);
        StringBuilder plaintext = new StringBuilder();
        String extendedKey = extendKey(key, ciphertext.length(), alphabet);

        for (int i = 0; i < ciphertext.length(); i++) {
            char cipherChar = ciphertext.charAt(i);
            char keyChar = extendedKey.charAt(i);

            int cipherIndex = alphabet.indexOf(cipherChar);
            int keyIndex = alphabet.indexOf(keyChar);

            if (cipherIndex == -1 || keyIndex == -1) {
                plaintext.append(cipherChar);
            } else {
                int decryptedIndex = (cipherIndex - keyIndex + alphabet.length()) % alphabet.length();
                plaintext.append(alphabet.charAt(decryptedIndex));
            }
        }

        return plaintext.toString();
    }

    /**
     * Trả về bảng chữ cái tương ứng với ngôn ngữ đã chỉ định.
     * Các ngôn ngữ hỗ trợ là Tiếng Anh và Tiếng Việt.
     *
     * @param language Ngôn ngữ cần bảng chữ cái.
     * @return Bảng chữ cái cho ngôn ngữ đã chỉ định.
     */
    private static String getAlphabetByLanguage(String language) {
        if (language.equalsIgnoreCase(StringConstant.LANGUAGE_VIETNAMESE)) {
            return StringConstant.VIETNAMESE_ALPHABET;
        }
        return StringConstant.ENGLISH_ALPHABET;
    }

    /**
     * Mở rộng khóa để phù hợp với độ dài của văn bản. Nếu khóa ngắn hơn văn bản,
     * khóa sẽ được lặp lại để khớp với độ dài văn bản.
     *
     * @param key Khóa gốc.
     * @param textLength Độ dài của văn bản để khớp với độ dài của khóa.
     * @param alphabet Bảng chữ cái được sử dụng để mở rộng khóa.
     * @return Khóa đã mở rộng.
     */
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
}
