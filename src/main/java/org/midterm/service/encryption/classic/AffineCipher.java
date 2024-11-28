package org.midterm.service.encryption.classic;

import org.midterm.constant.StringConstant;
import org.midterm.model.AffineKey;

import java.util.HashMap;
import java.util.Map;

/**
 * Lớp {@code AffineCipher} triển khai thuật toán mã hóa và giải mã Affine cho văn bản.
 * Thuật toán này sử dụng một hệ số khóa affine (a, b) để mã hóa và giải mã văn bản.
 * Các chế độ mã hóa có thể áp dụng cho nhiều ngôn ngữ khác nhau thông qua bảng chữ cái tương ứng.
 */
public class AffineCipher {

    /**
     * Mã hóa văn bản thuần (plain text) sử dụng thuật toán Affine với khóa affine (a, b).
     *
     * @param plainText Văn bản thuần cần mã hóa.
     * @param language Ngôn ngữ của văn bản, có thể là "English" hoặc "Vietnamese".
     * @param key Khóa affine (a, b) dùng cho thuật toán mã hóa.
     * @return Văn bản đã mã hóa.
     * @throws IllegalArgumentException Nếu khóa không phải là thể loại {@link AffineKey}.
     */
    public String encrypt(String plainText, String language, Object key) {
        if (!(key instanceof AffineKey affineKey)) {
            throw new IllegalArgumentException("Khóa phải là AffineKey.");
        }
        return encryptAffine(plainText, affineKey.getA(), affineKey.getB(), language);
    }

    /**
     * Giải mã văn bản đã mã hóa (cipher text) sử dụng thuật toán Affine với khóa affine (a, b).
     *
     * @param cipherText Văn bản đã mã hóa cần giải mã.
     * @param language Ngôn ngữ của văn bản, có thể là "English" hoặc "Vietnamese".
     * @param key Khóa affine (a, b) dùng cho thuật toán giải mã.
     * @return Văn bản đã giải mã.
     * @throws IllegalArgumentException Nếu khóa không phải là thể loại {@link AffineKey}.
     */
    public String decrypt(String cipherText, String language, Object key) {
        if (!(key instanceof AffineKey affineKey)) {
            throw new IllegalArgumentException("Khóa phải là AffineKey.");
        }
        return decryptAffine(cipherText, affineKey.getA(), affineKey.getB(), language);
    }

    /** Bảng chữ cái cho các ngôn ngữ hỗ trợ. */
    private final Map<String, String> alphabets = new HashMap<>();

    /**
     * Tạo một thể hiện {@code AffineCipher} mới.
     *
     * @return Một thể hiện {@code AffineCipher} mới.
     */
    public static AffineCipher create() {
        return new AffineCipher();
    }

    /**
     * Khởi tạo {@code AffineCipher} và cấu hình bảng chữ cái cho các ngôn ngữ hỗ trợ.
     */
    public AffineCipher() {
        alphabets.put(StringConstant.LANGUAGE_ENGLISH, StringConstant.ENGLISH_ALPHABET);
        alphabets.put(StringConstant.LANGUAGE_VIETNAMESE, StringConstant.VIETNAMESE_ALPHABET);
    }

    /**
     * Tìm kiếm nghịch đảo modulo của một số nguyên trong phạm vi [1, m-1].
     *
     * @param a Số cần tìm nghịch đảo.
     * @param m Mô-đun để tính nghịch đảo.
     * @return Nghịch đảo modulo của a.
     * @throws IllegalArgumentException Nếu a và m không là các số nguyên tố cùng nhau và không thể tính nghịch đảo.
     */
    private int modularInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        throw new IllegalArgumentException("a và m không phải là các số nguyên tố cùng nhau và không thể tính nghịch đảo.");
    }

    /**
     * Mã hóa văn bản thuần sử dụng thuật toán Affine.
     *
     * @param plainText Văn bản thuần cần mã hóa.
     * @param a Hệ số "a" của khóa affine.
     * @param b Hệ số "b" của khóa affine.
     * @param language Ngôn ngữ của văn bản.
     * @return Văn bản đã mã hóa.
     * @throws IllegalArgumentException Nếu a và m không phải là các số nguyên tố cùng nhau.
     */
    public String encryptAffine(String plainText, int a, int b, String language) {
        String alphabet = getAlphabetByLanguage(language);
        int m = alphabet.length();

        if (gcd(a, m) != 1) {
            throw new IllegalArgumentException("a và m phải là các số nguyên tố cùng nhau.");
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

    /**
     * Giải mã văn bản đã mã hóa sử dụng thuật toán Affine.
     *
     * @param cipherText Văn bản đã mã hóa cần giải mã.
     * @param a Hệ số "a" của khóa affine.
     * @param b Hệ số "b" của khóa affine.
     * @param language Ngôn ngữ của văn bản.
     * @return Văn bản đã giải mã.
     * @throws IllegalArgumentException Nếu a và m không phải là các số nguyên tố cùng nhau.
     */
    public String decryptAffine(String cipherText, int a, int b, String language) {
        String alphabet = getAlphabetByLanguage(language);
        int m = alphabet.length();

        if (gcd(a, m) != 1) {
            throw new IllegalArgumentException("a phải là số nguyên tố cùng nhau với m.");
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

    /**
     * Lấy bảng chữ cái tương ứng với ngôn ngữ đã chọn.
     *
     * @param language Ngôn ngữ của văn bản (ví dụ: "English", "Vietnamese").
     * @return Bảng chữ cái tương ứng với ngôn ngữ.
     */
    private String getAlphabetByLanguage(String language) {
        return alphabets.getOrDefault(language.toUpperCase(), StringConstant.ENGLISH_ALPHABET);
    }

    /**
     * Tính ước chung lớn nhất (GCD) của hai số nguyên.
     *
     * @param a Số nguyên thứ nhất.
     * @param b Số nguyên thứ hai.
     * @return Ước chung lớn nhất của a và b.
     */
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
