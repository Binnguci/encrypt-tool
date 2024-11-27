package org.midterm.service.encryption.classic;

import org.midterm.constant.StringConstant;

import java.util.*;

/**
 * Lớp thực hiện thuật toán mã hóa và giải mã Substitution Cipher.
 * Thuật toán Substitution Cipher thay thế mỗi ký tự trong văn bản gốc bằng một ký tự tương ứng
 * từ một phiên bản xáo trộn của bảng chữ cái dựa trên khóa đã cho.
 * Lớp này hỗ trợ mã hóa và giải mã cho các ngôn ngữ khác nhau, bao gồm tiếng Việt và tiếng Anh.
 */
public class SubstitutionCipher {

    /**
     * Tạo và trả về một đối tượng mới của lớp SubstitutionCipher.
     *
     * @return Một đối tượng mới của SubstitutionCipher.
     */
    public static SubstitutionCipher create() {
        return new SubstitutionCipher();
    }

    /**
     * Tạo một khóa ngẫu nhiên cho thuật toán Substitution Cipher dựa trên ngôn ngữ đã chỉ định.
     * Khóa là một phiên bản xáo trộn của bảng chữ cái.
     *
     * @param language Ngôn ngữ của bảng chữ cái (ví dụ: Tiếng Anh, Tiếng Việt).
     * @return Khóa xáo trộn ngẫu nhiên dưới dạng chuỗi.
     * @throws IllegalStateException Nếu có lỗi trong quá trình tạo khóa, chẳng hạn như kích thước bảng chữ cái không phù hợp.
     */
    public String generateKey(String language) {
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

    /**
     * Trả về bảng chữ cái tương ứng với ngôn ngữ đã cho.
     *
     * @param language Mã ngôn ngữ (ví dụ: "Tiếng Việt", "Tiếng Anh").
     * @return Bảng chữ cái dưới dạng chuỗi.
     */
    private String getAlphabetByLanguage(String language) {
        if (language.equalsIgnoreCase(StringConstant.LANGUAGE_VIETNAMESE)) {
            return StringConstant.VIETNAMESE_ALPHABET;
        }
        return StringConstant.ENGLISH_ALPHABET;
    }

    /**
     * Mã hóa văn bản gốc bằng thuật toán Substitution Cipher.
     *
     * @param plainText Văn bản gốc cần mã hóa.
     * @param language Ngôn ngữ của bảng chữ cái được sử dụng.
     * @param key Khóa thay thế dưới dạng chuỗi.
     * @return Văn bản mã hóa.
     * @throws IllegalArgumentException Nếu khóa không phải là một chuỗi hợp lệ.
     */
    public String encrypt(String plainText, String language, Object key) {
        if (!(key instanceof String keyStr)) {
            throw new IllegalArgumentException("Key must be a String.");
        }
        System.out.println("keyStirng: " + keyStr.length());
        return encryptSubstitution(plainText, keyStr, language);
    }

    /**
     * Giải mã văn bản đã mã hóa bằng thuật toán Substitution Cipher.
     *
     * @param cipherText Văn bản đã mã hóa cần giải mã.
     * @param language Ngôn ngữ của bảng chữ cái được sử dụng.
     * @param key Khóa thay thế dưới dạng chuỗi.
     * @return Văn bản gốc sau khi giải mã.
     * @throws IllegalArgumentException Nếu khóa không phải là một chuỗi hợp lệ.
     */
    public String decrypt(String cipherText, String language, Object key) {
        if (!(key instanceof String keyStr)) {
            throw new IllegalArgumentException("Key must be a String.");
        }
        return decryptSubstitution(cipherText, keyStr, language);
    }


    /**
     * Mã hóa văn bản gốc bằng khóa và ngôn ngữ đã cho, thực hiện thay thế ký tự theo khóa.
     *
     * @param plainText Văn bản gốc cần mã hóa.
     * @param key Khóa thay thế.
     * @param language Ngôn ngữ của bảng chữ cái.
     * @return Văn bản mã hóa.
     */
    public String encryptSubstitution(String plainText, String key, String language) {
        String alphabet = getAlphabetByLanguage(language);
        Map<Character, Character> encryptionMap = createEncryptionMap(key, alphabet);

        StringBuilder cipherText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            cipherText.append(encryptionMap.getOrDefault(c, c));
        }
        return cipherText.toString();
    }

    /**
     * Giải mã văn bản đã mã hóa bằng khóa và ngôn ngữ đã cho, thực hiện thay thế ký tự theo khóa.
     *
     * @param cipherText Văn bản đã mã hóa cần giải mã.
     * @param key Khóa thay thế.
     * @param language Ngôn ngữ của bảng chữ cái.
     * @return Văn bản gốc sau khi giải mã.
     */
    public String decryptSubstitution(String cipherText, String key, String language) {
        String alphabet = getAlphabetByLanguage(language);
        Map<Character, Character> decryptionMap = createDecryptionMap(key, alphabet);

        StringBuilder plainText = new StringBuilder();
        for (char c : cipherText.toCharArray()) {
            // Giữ nguyên ký tự nếu không nằm trong bảng chữ cái
            plainText.append(decryptionMap.getOrDefault(c, c));
        }
        return plainText.toString();
    }

    /**
     * Tạo một bản đồ ánh xạ giữa các ký tự của bảng chữ cái và khóa để mã hóa.
     *
     * @param key Khóa thay thế.
     * @param alphabet Bảng chữ cái được sử dụng.
     * @return Bản đồ ánh xạ mỗi ký tự của bảng chữ cái với ký tự tương ứng từ khóa.
     * @throws IllegalArgumentException Nếu độ dài khóa không khớp với độ dài bảng chữ cái.
     */
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

    /**
     * Tạo một bản đồ ánh xạ giữa các ký tự của khóa và bảng chữ cái để giải mã.
     *
     * @param key Khóa thay thế.
     * @param alphabet Bảng chữ cái được sử dụng.
     * @return Bản đồ ánh xạ mỗi ký tự từ khóa với ký tự tương ứng từ bảng chữ cái.
     */
    private Map<Character, Character> createDecryptionMap(String key, String alphabet) {
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            map.put(key.charAt(i), alphabet.charAt(i));
        }
        return map;
    }
}
