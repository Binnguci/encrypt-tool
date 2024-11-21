package org.midterm.service.encryption.classic;

public interface ClassicAlgorithm {

    String generateKey(String language);
    /**
     * Mã hóa văn bản đầu vào.
     * @param plainText Văn bản gốc cần mã hóa.
     * @param key Khóa để mã hóa (có thể là số, chuỗi, hoặc object phức tạp).
     * @return Văn bản đã được mã hóa.
     */
    String encrypt(String plainText,String language, Object key);

    /**
     * Giải mã văn bản đầu vào.
     * @param cipherText Văn bản mã hóa cần giải mã.
     * @param key Khóa để giải mã (phải tương ứng với mã hóa).
     * @return Văn bản gốc sau khi giải mã.
     */
    String decrypt(String cipherText, String language, Object key);
}
