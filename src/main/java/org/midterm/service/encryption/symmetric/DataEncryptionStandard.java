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
 * Lớp DataEncryptionStandard cung cấp các phương thức mã hóa và giải mã
 * sử dụng thuật toán DES (Data Encryption Standard). Lớp hỗ trợ mã hóa/giải mã
 * cho văn bản và tệp, với nhiều chế độ (mode) và kiểu padding khác nhau.
 */
public class DataEncryptionStandard {

    /**
     * Tạo một đối tượng DataEncryptionStandard mới.
     *
     * @return Đối tượng DataEncryptionStandard.
     */
    public static DataEncryptionStandard create() {
        return new DataEncryptionStandard();
    }

    /**
     * Tạo một khóa DES ngẫu nhiên.
     *
     * @return Khóa DES được mã hóa dưới dạng chuỗi Base64.
     * @throws NoSuchAlgorithmException Nếu thuật toán DES không được hỗ trợ.
     */
    public String generateKey() throws NoSuchAlgorithmException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(56);
        return Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
    }

    /**
     * Tạo một IV (Initialization Vector) ngẫu nhiên.
     *
     * @return IV được mã hóa dưới dạng chuỗi Base64.
     */
    public String generateIv() {
        byte[] iv = new byte[8];
        new SecureRandom().nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }


    private SecretKey convertBase64ToKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
    }

    private IvParameterSpec convertBase64ToIv(String base64Iv) {
        byte[] decodedIv = Base64.getDecoder().decode(base64Iv);
        return new IvParameterSpec(decodedIv);
    }

    /**
     * Mã hóa một văn bản bằng thuật toán DES.
     *
     * @param base64Iv        IV được mã hóa Base64 (hoặc null nếu sử dụng ECB mode).
     * @param secretKeyBase64 Khóa bí mật được mã hóa Base64.
     * @param plainText       Văn bản gốc cần mã hóa.
     * @param mode            Chế độ mã hóa (ví dụ: ECB, CBC).
     * @param padding         Kiểu padding (ví dụ: PKCS5Padding).
     * @return Chuỗi Base64 chứa văn bản đã được mã hóa.
     * @throws NoSuchPaddingException             Nếu padding không được hỗ trợ.
     * @throws NoSuchAlgorithmException           Nếu thuật toán DES không được hỗ trợ.
     * @throws InvalidAlgorithmParameterException Nếu tham số thuật toán không hợp lệ.
     * @throws InvalidKeyException                Nếu khóa không hợp lệ.
     * @throws BadPaddingException                Nếu xảy ra lỗi padding.
     * @throws IllegalBlockSizeException          Nếu kích thước khối không hợp lệ.
     */
    public String encryptText(String base64Iv, String secretKeyBase64, String plainText, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }
        String transformation = "DES/" + mode + "/" + padding;

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
     * Giải mã một văn bản bằng thuật toán DES.
     *
     * @param base64Iv        IV được mã hóa Base64 (hoặc null nếu sử dụng ECB mode).
     * @param secretKeyBase64 Khóa bí mật được mã hóa Base64.
     * @param cipherTextBase64 Chuỗi Base64 chứa văn bản đã được mã hóa.
     * @param mode            Chế độ mã hóa (ví dụ: ECB, CBC).
     * @param padding         Kiểu padding (ví dụ: PKCS5Padding).
     * @return Văn bản gốc sau khi giải mã.
     * @throws NoSuchPaddingException             Nếu padding không được hỗ trợ.
     * @throws NoSuchAlgorithmException           Nếu thuật toán DES không được hỗ trợ.
     * @throws InvalidAlgorithmParameterException Nếu tham số thuật toán không hợp lệ.
     * @throws InvalidKeyException                Nếu khóa không hợp lệ.
     * @throws BadPaddingException                Nếu xảy ra lỗi padding.
     * @throws IllegalBlockSizeException          Nếu kích thước khối không hợp lệ.
     */
    public String decryptText(String base64Iv, String secretKeyBase64, String cipherTextBase64,   String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key doesn't null or empty.");
        }

        String transformation = "DES/" + mode + "/" + padding;

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance(transformation);

        if ("ECB".equalsIgnoreCase(mode)) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } else {
            if (base64Iv == null || base64Iv.isEmpty()) {
                throw new IllegalArgumentException("IV don't null or empty with mode not ECB.");
            }
            IvParameterSpec iv = convertBase64ToIv(base64Iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        }

        byte[] cipherTextBytes = Base64.getDecoder().decode(cipherTextBase64);
        byte[] plainTextBytes = cipher.doFinal(cipherTextBytes);
        return new String(plainTextBytes);
    }

    /**
     * Mã hóa một tệp bằng thuật toán DES.
     *
     * @param base64Iv       IV được mã hóa Base64 (hoặc null nếu sử dụng ECB mode).
     * @param baseSecretKey  Khóa bí mật được mã hóa Base64.
     * @param inputFile      Đường dẫn đến tệp gốc.
     * @param mode           Chế độ mã hóa (ví dụ: ECB, CBC).
     * @param padding        Kiểu padding (ví dụ: PKCS5Padding).
     * @return Đường dẫn đến tệp đã được mã hóa.
     * @throws IOException                     Nếu xảy ra lỗi IO.
     * @throws NoSuchPaddingException          Nếu padding không được hỗ trợ.
     * @throws NoSuchAlgorithmException        Nếu thuật toán DES không được hỗ trợ.
     * @throws InvalidAlgorithmParameterException Nếu tham số thuật toán không hợp lệ.
     * @throws InvalidKeyException             Nếu khóa không hợp lệ.
     * @throws BadPaddingException             Nếu xảy ra lỗi padding.
     * @throws IllegalBlockSizeException       Nếu kích thước khối không hợp lệ.
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
        if (dotIndex != -1) { // Nếu file có phần mở rộng
            outputFile = parentPath + File.separator + fileName.substring(0, dotIndex) + "_encrypt" + fileName.substring(dotIndex);
        } else { // Nếu file không có phần mở rộng
            outputFile = parentPath + File.separator + fileName + "_encrypt";
        }

        String transformation = "DES/" + mode + "/"+ padding;
        SecretKey secretKey = convertBase64ToKey(baseSecretKey);
        IvParameterSpec iv = null;
        if (base64Iv != null && !base64Iv.isEmpty()) {
            iv = convertBase64ToIv(base64Iv);
        }

        Cipher cipher = Cipher.getInstance(transformation);

        // Kiểm tra mode, nếu là ECB thì không sử dụng IV
        if (iv != null && !mode.equalsIgnoreCase("ECB")) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        }

        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
             OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {

            if (iv != null && !mode.equalsIgnoreCase("ECB")) {
                os.write(iv.getIV()); // Ghi IV vào file đầu tiên
            }

            BlowFish.writeFile(cipher, is, os);
        }
        return outputFile;
    }

    /**
     * Giải mã một tệp bằng thuật toán DES.
     *
     * @param base64Iv       IV được mã hóa Base64 (hoặc null nếu sử dụng ECB mode).
     * @param baseSecretKey  Khóa bí mật được mã hóa Base64.
     * @param inputFile      Đường dẫn đến tệp đã được mã hóa.
     * @param mode           Chế độ mã hóa (ví dụ: ECB, CBC).
     * @param padding        Kiểu padding (ví dụ: PKCS5Padding).
     * @return Đường dẫn đến tệp sau khi giải mã.
     * @throws IOException                     Nếu xảy ra lỗi IO.
     * @throws NoSuchPaddingException          Nếu padding không được hỗ trợ.
     * @throws NoSuchAlgorithmException        Nếu thuật toán DES không được hỗ trợ.
     * @throws InvalidAlgorithmParameterException Nếu tham số thuật toán không hợp lệ.
     * @throws InvalidKeyException             Nếu khóa không hợp lệ.
     * @throws BadPaddingException             Nếu xảy ra lỗi padding.
     * @throws IllegalBlockSizeException       Nếu kích thước khối không hợp lệ.
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

        String transformation = "DES/" + mode + "/"+ padding;

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
