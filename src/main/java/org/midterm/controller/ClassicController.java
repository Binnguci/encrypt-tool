package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.model.AffineKey;
import org.midterm.model.ClassicAlgorithm;
import org.midterm.service.encryption.classic.AffineCipher;
import org.midterm.service.encryption.classic.ShiftCipher;
import org.midterm.service.encryption.classic.SubstitutionCipher;
import org.midterm.service.encryption.classic.Vigenere;

/**
 * Lớp điều khiển cho các thuật toán mã hóa cổ điển.
 * Cung cấp các phương thức để tạo khóa, mã hóa và giải mã văn bản
 * sử dụng các thuật toán mã hóa cổ điển như Shift Cipher, Substitution Cipher, Affine Cipher, và Vigenere Cipher.
 */
public class ClassicController {

    /**
     * Tạo khóa mã hóa cho thuật toán Substitution Cipher dựa trên ngôn ngữ đầu vào.
     *
     * @param language ngôn ngữ được sử dụng để tạo khóa (ví dụ: "English", "Vietnamese").
     * @return khóa mã hóa được tạo.
     */
    public static String generateSubstitutionKey(String language) {
        SubstitutionCipher substitutionCipher = SubstitutionCipher.create();
        return substitutionCipher.generateKey(language);
    }

    /**
     * Tạo khóa cho thuật toán Vigenere Cipher dựa trên văn bản gốc và ngôn ngữ.
     *
     * @param language   ngôn ngữ được sử dụng (ví dụ: "English", "Vietnamese").
     * @param plaintext  văn bản gốc để tạo khóa.
     * @return khóa được tạo từ văn bản gốc.
     */
    public static String generateKey(String language, String plaintext) {
        Vigenere vigenere = Vigenere.create();
        return vigenere.generateKey(plaintext, language);
    }

    /**
     * Mã hóa văn bản đầu vào bằng một thuật toán mã hóa cổ điển được chỉ định.
     *
     * @param classicAlgorithm đối tượng chứa thông tin về thuật toán mã hóa, ngôn ngữ, và khóa.
     * @param inputText        văn bản cần mã hóa.
     * @return văn bản đã được mã hóa hoặc null nếu thuật toán không hợp lệ.
     */
    public static String encrypt(ClassicAlgorithm classicAlgorithm, String inputText) {
        String result;
        switch (classicAlgorithm.getName()) {
            case AlgorithmsConstant.SHIFT:
                ShiftCipher shiftCipher = ShiftCipher.create();
                result = shiftCipher.encrypt(inputText, classicAlgorithm.getLanguage(), classicAlgorithm.getShift());
                break;
            case AlgorithmsConstant.SUBSTITUTION:
                SubstitutionCipher substitutionCipher = SubstitutionCipher.create();
                result = substitutionCipher.encrypt(inputText, classicAlgorithm.getLanguage(), classicAlgorithm.getSubstitutionKey());
                break;
            case AlgorithmsConstant.AFFINE:
                AffineCipher affineCipher = AffineCipher.create();
                AffineKey affineKey = new AffineKey(classicAlgorithm.getAMultiplier(), classicAlgorithm.getBShift());
                result = affineCipher.encrypt(inputText, classicAlgorithm.getLanguage(), affineKey);
                break;
            case AlgorithmsConstant.VIGENERE:
                Vigenere vigenere = Vigenere.create();
                result = vigenere.encrypt(inputText, classicAlgorithm.getKey(), classicAlgorithm.getLanguage());
                break;
            default:
                return null;
        }
        return result;
    }

    /**
     * Giải mã văn bản đầu vào bằng một thuật toán mã hóa cổ điển được chỉ định.
     *
     * @param classicAlgorithm đối tượng chứa thông tin về thuật toán mã hóa, ngôn ngữ, và khóa.
     * @param inputText        văn bản cần giải mã.
     * @return văn bản đã được giải mã hoặc null nếu thuật toán không hợp lệ.
     */
    public static String decrypt(ClassicAlgorithm classicAlgorithm, String inputText) {
        String result;
        switch (classicAlgorithm.getName()) {
            case AlgorithmsConstant.SHIFT:
                ShiftCipher shiftCipher = ShiftCipher.create();
                result = shiftCipher.decrypt(inputText, classicAlgorithm.getLanguage(), classicAlgorithm.getShift());
                break;
            case AlgorithmsConstant.SUBSTITUTION:
                SubstitutionCipher substitutionCipher = SubstitutionCipher.create();
                result = substitutionCipher.decrypt(inputText, classicAlgorithm.getLanguage(), classicAlgorithm.getSubstitutionKey());
                break;
            case AlgorithmsConstant.AFFINE:
                AffineCipher affineCipher = AffineCipher.create();
                AffineKey affineKey = new AffineKey(classicAlgorithm.getAMultiplier(), classicAlgorithm.getBShift());
                result = affineCipher.decrypt(inputText, classicAlgorithm.getLanguage(), affineKey);
                break;
            case AlgorithmsConstant.VIGENERE:
                Vigenere vigenere = Vigenere.create();
                result = vigenere.decrypt(inputText, classicAlgorithm.getKey(), classicAlgorithm.getLanguage());
                break;
            default:
                return null;
        }
        return result;
    }


}
