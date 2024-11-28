package org.midterm.service.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * Lớp `Hash` cung cấp các phương thức để băm chuỗi và tập tin sử dụng các thuật toán băm như MD5, SHA-1, SHA-256.
 */
public class Hash {

    /**
     * Băm một chuỗi văn bản sử dụng thuật toán băm được chỉ định.
     *
     * @param input      chuỗi văn bản cần băm.
     * @param algorithm  tên thuật toán băm (ví dụ: "MD5", "SHA-1", "SHA-256").
     * @return chuỗi băm dưới dạng biểu diễn hexa (hexadecimal).
     * @throws RuntimeException nếu xảy ra lỗi khi tạo băm.
     */
    public String hash(String input, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(input.getBytes());
            return bytesToHex(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Băm một tập tin sử dụng thuật toán băm được chỉ định.
     *
     * @param path       đường dẫn đến tập tin cần băm.
     * @param algorithm  tên thuật toán băm (ví dụ: "MD5", "SHA-1", "SHA-256").
     * @return chuỗi băm của tập tin dưới dạng biểu diễn hexa (hexadecimal).
     * @throws RuntimeException nếu xảy ra lỗi trong quá trình đọc tập tin hoặc băm.
     */
    public String hashFile(String path, String algorithm) {
        File file = new File(path);
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            byte[] hashBytes = digest.digest();
            return bytesToHex(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing file", e);
        }
    }

    /**
     * Chuyển đổi một mảng byte thành chuỗi biểu diễn hexa (hexadecimal).
     *
     * @param bytes mảng byte cần chuyển đổi.
     * @return chuỗi biểu diễn hexa.
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
