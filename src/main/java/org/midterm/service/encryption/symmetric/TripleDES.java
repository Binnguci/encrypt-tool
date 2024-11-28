package org.midterm.service.encryption.symmetric;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Lớp hỗ trợ mã hóa và giải mã sử dụng thuật toán Triple DES (DESede).
 * Cung cấp các chức năng để mã hóa, giải mã chuỗi, tập tin, và các tiện ích
 * tạo khóa, tạo IV (Initialization Vector).
 */
public class TripleDES {

    /**
     * Tạo một đối tượng mới của TripleDES.
     *
     * @return đối tượng TripleDES mới.
     */
    public static TripleDES create() {
        return new TripleDES();
    }

    /**
     * Sinh khóa bí mật dùng cho thuật toán Triple DES.
     *
     * @param keySize kích thước khóa (112 hoặc 168 bit).
     * @return khóa bí mật dưới dạng chuỗi Base64.
     * @throws Exception nếu xảy ra lỗi trong quá trình tạo khóa.
     */
    public static String generateKey(int keySize) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * Sinh một IV (Initialization Vector) ngẫu nhiên.
     *
     * @return IV dưới dạng chuỗi Base64.
     */
    public static String generateIv() {
        byte[] iv = new byte[8];
        new SecureRandom().nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }

    private static SecretKey convertBase64ToKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "DESede");
    }

    private static IvParameterSpec convertBase64ToIv(String base64Iv) {
        byte[] decodedIv = Base64.getDecoder().decode(base64Iv);
        return new IvParameterSpec(decodedIv);
    }

    /**
     * Mã hóa một chuỗi văn bản.
     *
     * @param base64Iv         IV (Base64), có thể null nếu dùng chế độ ECB.
     * @param secretKeyBase64  khóa bí mật (Base64).
     * @param plainText        chuỗi văn bản gốc cần mã hóa.
     * @param mode             chế độ mã hóa (ECB, CBC, CFB, OFB).
     * @param padding          kiểu padding (PKCS5Padding, NoPadding, v.v.).
     * @return chuỗi mã hóa dưới dạng Base64.
     * @throws IllegalArgumentException nếu tham số không hợp lệ.
     */
    public String encryptText(String base64Iv, String secretKeyBase64, String plainText, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }
        String transformation = "DESede/" + mode + "/" + padding;

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance(transformation);

        if ("ECB".equalsIgnoreCase(mode)) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            if (base64Iv == null || base64Iv.isEmpty()) {
                throw new IllegalArgumentException("IV không được null hoặc rỗng với chế độ không phải ECB.");
            }
            IvParameterSpec iv = convertBase64ToIv(base64Iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        }
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Giải mã một chuỗi đã mã hóa.
     *
     * @param base64Iv         IV (Base64), có thể null nếu dùng chế độ ECB.
     * @param secretKeyBase64  khóa bí mật (Base64).
     * @param cipherText       chuỗi đã mã hóa (Base64).
     * @param mode             chế độ mã hóa (ECB, CBC, CFB, OFB).
     * @param padding          kiểu padding (PKCS5Padding, NoPadding, v.v.).
     * @return chuỗi văn bản gốc đã giải mã.
     * @throws IllegalArgumentException nếu tham số không hợp lệ.
     */
    public String decryptText(String base64Iv, String secretKeyBase64, String cipherText, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }
        String transformation = "DESede/" + mode + "/" + padding;
        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance(transformation);

        if ("ECB".equalsIgnoreCase(mode)) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } else {
            if (base64Iv == null || base64Iv.isEmpty()) {
                throw new IllegalArgumentException("IV không được null hoặc rỗng với chế độ không phải ECB.");
            }
            IvParameterSpec iv = convertBase64ToIv(base64Iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        }
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    /**
     * Mã hóa một tập tin.
     *
     * @param base64Iv         IV (Base64), có thể null nếu dùng chế độ ECB.
     * @param baseSecretKey    khóa bí mật (Base64).
     * @param inputFile        đường dẫn tập tin gốc.
     * @param mode             chế độ mã hóa (ECB, CBC, CFB, OFB).
     * @param padding          kiểu padding (PKCS5Padding, NoPadding, v.v.).
     * @return đường dẫn tập tin đã mã hóa.
     */
    public String encryptFile(String base64Iv, String baseSecretKey, String inputFile, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {
        File inputFileObj = new File(inputFile);
        System.out.println(inputFileObj);
        String parentPath = inputFileObj.getParent();
        String fileName = inputFileObj.getName();
        String outputFile;
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            outputFile = parentPath + File.separator + fileName.substring(0, dotIndex) + "_encrypt" + fileName.substring(dotIndex);
        } else {
            outputFile = parentPath + File.separator + fileName + "_encrypt";
        }

        String transformation = "DESede/" + mode + "/" + padding;

        SecretKey secretKey = convertBase64ToKey(baseSecretKey);
        IvParameterSpec iv = null;

        if (base64Iv != null && !base64Iv.isEmpty()) {
            iv = convertBase64ToIv(base64Iv);
        }

        Cipher cipher = Cipher.getInstance(transformation);

        if (iv != null && !mode.equalsIgnoreCase("ECB")) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        }

        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
             OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {

            if (iv != null && !mode.equalsIgnoreCase("ECB")) {
                os.write(iv.getIV());
            }

            BlowFish.writeFile(cipher, is, os);
        }
        return outputFile;
    }

    /**
     * Giải mã một tập tin.
     *
     * @param base64Iv         IV (Base64), có thể null nếu dùng chế độ ECB.
     * @param baseSecretKey    khóa bí mật (Base64).
     * @param inputFile        đường dẫn tập tin đã mã hóa.
     * @param mode             chế độ mã hóa (ECB, CBC, CFB, OFB).
     * @param padding          kiểu padding (PKCS5Padding, NoPadding, v.v.).
     * @return đường dẫn tập tin đã giải mã.
     */
    public String decryptFile(String base64Iv, String baseSecretKey, String inputFile, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {

        File inputFileObj = new File(inputFile);
        String parentPath = inputFileObj.getParent();
        String fileName = inputFileObj.getName();
        String outputFile;

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            outputFile = parentPath + File.separator + fileName.substring(0, dotIndex) + "_decrypt" + fileName.substring(dotIndex);
        } else {
            outputFile = parentPath + File.separator + fileName + "_decrypt";
        }

        String transformation = "DESede/" + mode + "/" + padding;

        SecretKey secretKey = convertBase64ToKey(baseSecretKey);
        IvParameterSpec iv = null;

        if (base64Iv != null && !base64Iv.isEmpty()) {
            iv = convertBase64ToIv(base64Iv);
        }

        Cipher cipher = Cipher.getInstance(transformation);

        if (iv != null && !mode.equalsIgnoreCase("ECB")) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        }

        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
             OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {

            BlowFish.writeFile(cipher, is, os);
        }
        return outputFile;
    }
}
