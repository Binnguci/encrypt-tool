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
 * Lớp BlowFish cung cấp các phương thức mã hóa và giải mã dữ liệu sử dụng thuật toán Blowfish.
 * Bao gồm các tính năng:
 * - Tạo khóa bí mật (secret key) và vector khởi tạo (IV).
 * - Mã hóa và giải mã chuỗi văn bản.
 * - Mã hóa và giải mã tệp.
 *
 * Các chế độ mã hóa được hỗ trợ:
 * - ECB: Electronic Codebook
 * - CBC: Cipher Block Chaining
 *
 * Padding được hỗ trợ:
 * - PKCS5Padding, NoPadding, ...
 *
 * Các phương thức trong lớp này sử dụng Base64 để biểu diễn dữ liệu nhị phân ở dạng chuỗi.
 */
public class BlowFish {

    /**
     * Tạo một khóa bí mật mới với kích thước xác định.
     *
     * @param keySize Kích thước khóa (ví dụ: 128, 192, hoặc 256 bit).
     * @return Chuỗi biểu diễn khóa bí mật dưới dạng Base64.
     * @throws NoSuchAlgorithmException Nếu thuật toán Blowfish không được hỗ trợ.
     */
    public String generateKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * Tạo một vector khởi tạo (IV) ngẫu nhiên.
     *
     * @return Chuỗi biểu diễn IV dưới dạng Base64.
     */
    public static String generateIv() {
        byte[] iv = new byte[8];
        new SecureRandom().nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }

    private static SecretKey convertBase64ToKey(String base64Key) {
        return new SecretKeySpec(Base64.getDecoder().decode(base64Key), "Blowfish");
    }

    private static IvParameterSpec convertBase64ToIv(String base64Iv) {
        return new IvParameterSpec(Base64.getDecoder().decode(base64Iv));
    }

    private static void checkKeyNotNullOrEmpty(String secretKeyBase64) {
        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }
    }

    private static void checkIvNotNullOrEmpty(String base64Iv, String mode) {
        if (!"ECB".equalsIgnoreCase(mode) && (base64Iv == null || base64Iv.isEmpty())) {
            throw new IllegalArgumentException("IV không được null hoặc rỗng với chế độ không phải ECB.");
        }
    }

    private static Cipher getCipherInstance(String mode, String secretKeyBase64, String base64Iv, String transformation)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance(transformation);
        if ("ECB".equalsIgnoreCase(mode)) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            IvParameterSpec iv = convertBase64ToIv(base64Iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        }
        return cipher;
    }

    /**
     * Mã hóa văn bản bằng thuật toán Blowfish.
     *
     * @param base64Iv Vector khởi tạo (IV) ở dạng Base64.
     * @param secretKeyBase64 Khóa bí mật ở dạng Base64.
     * @param plainText Văn bản gốc cần mã hóa.
     * @param mode Chế độ mã hóa (ECB, CBC, ...).
     * @param padding Kiểu padding (PKCS5Padding, NoPadding, ...).
     * @return Chuỗi văn bản đã được mã hóa dưới dạng Base64.
     * @throws Exception Nếu có lỗi trong quá trình mã hóa.
     */
    public String encryptText(String base64Iv, String secretKeyBase64, String plainText, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        checkKeyNotNullOrEmpty(secretKeyBase64);
        checkIvNotNullOrEmpty(base64Iv, mode);

        String transformation = "Blowfish/" + mode + "/" + padding;
        Cipher cipher = getCipherInstance(mode, secretKeyBase64, base64Iv, transformation);

        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Giải mã văn bản đã được mã hóa.
     *
     * @param base64Iv Vector khởi tạo (IV) ở dạng Base64.
     * @param secretKeyBase64 Khóa bí mật ở dạng Base64.
     * @param cipherTextBase64 Chuỗi văn bản đã được mã hóa ở dạng Base64.
     * @param mode Chế độ mã hóa (ECB, CBC, ...).
     * @param padding Kiểu padding (PKCS5Padding, NoPadding, ...).
     * @return Chuỗi văn bản gốc đã được giải mã.
     * @throws Exception Nếu có lỗi trong quá trình giải mã.
     */
    public String decryptText(String base64Iv, String secretKeyBase64, String cipherTextBase64, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        checkKeyNotNullOrEmpty(secretKeyBase64);
        checkIvNotNullOrEmpty(base64Iv, mode);

        String transformation = "Blowfish/" + mode + "/" + padding;
        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance(transformation);
        IvParameterSpec iv = null;
        if (!"ECB".equalsIgnoreCase(mode)) {
            iv = convertBase64ToIv(base64Iv);
        }

        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] cipherTextBytes = Base64.getDecoder().decode(cipherTextBase64);
        byte[] plainTextBytes = cipher.doFinal(cipherTextBytes);
        return new String(plainTextBytes);
    }

    /**
     * Mã hóa tệp bằng thuật toán Blowfish.
     *
     * @param base64Iv Vector khởi tạo (IV) ở dạng Base64.
     * @param baseSecretKey Khóa bí mật ở dạng Base64.
     * @param inputFile Đường dẫn tới tệp cần mã hóa.
     * @param mode Chế độ mã hóa (ECB, CBC, ...).
     * @param padding Kiểu padding (PKCS5Padding, NoPadding, ...).
     * @return Đường dẫn tới tệp đã được mã hóa.
     * @throws Exception Nếu có lỗi trong quá trình mã hóa tệp.
     */
    public String encryptFile(String base64Iv, String baseSecretKey, String inputFile, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {
        return processFile(base64Iv, baseSecretKey, inputFile, mode, padding, Cipher.ENCRYPT_MODE);
    }

    /**
     * Giải mã tệp đã được mã hóa.
     *
     * @param base64Iv Vector khởi tạo (IV) ở dạng Base64.
     * @param baseSecretKey Khóa bí mật ở dạng Base64.
     * @param inputFile Đường dẫn tới tệp đã được mã hóa.
     * @param mode Chế độ mã hóa (ECB, CBC, ...).
     * @param padding Kiểu padding (PKCS5Padding, NoPadding, ...).
     * @return Đường dẫn tới tệp đã được giải mã.
     * @throws Exception Nếu có lỗi trong quá trình giải mã tệp.
     */
    public String decryptFile(String base64Iv, String baseSecretKey, String inputFile, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {
        return processFile(base64Iv, baseSecretKey, inputFile, mode, padding, Cipher.DECRYPT_MODE);
    }

    private String processFile(String base64Iv, String baseSecretKey, String inputFile, String mode, String padding, int cipherMode)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {

        File inputFileObj = new File(inputFile);
        String outputFile = generateOutputFilePath(inputFileObj, cipherMode == Cipher.ENCRYPT_MODE ? "_encrypt" : "_decrypt");

        String transformation = "Blowfish/" + mode + "/" + padding;
        SecretKey secretKey = convertBase64ToKey(baseSecretKey);
        IvParameterSpec iv = convertBase64ToIv(base64Iv);

        Cipher cipher = Cipher.getInstance(transformation);
        if ("ECB".equalsIgnoreCase(mode)) {
            cipher.init(cipherMode, secretKey);
        } else {
            cipher.init(cipherMode, secretKey, iv);
        }

        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
             OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {

            if (!"ECB".equalsIgnoreCase(mode)) {
                os.write(iv.getIV());
            }

            writeFile(cipher, is, os);
        }
        return outputFile;
    }

    static void writeFile(Cipher cipher, InputStream is, OutputStream os) throws IOException, IllegalBlockSizeException, BadPaddingException {
        byte[] buffer = new byte[10240];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                os.write(output);
            }
        }

        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            os.write(outputBytes);
        }
    }

    private String generateOutputFilePath(File inputFileObj, String suffix) {
        String parentPath = inputFileObj.getParent();
        String fileName = inputFileObj.getName();
        String outputFile;
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex != -1) {
            outputFile = parentPath + File.separator + fileName.substring(0, dotIndex) + suffix + fileName.substring(dotIndex);
        } else {
            outputFile = parentPath + File.separator + fileName + suffix;
        }
        return outputFile;
    }

    /**
     * Tạo một đối tượng BlowFish mới.
     *
     * @return Đối tượng BlowFish.
     */
    public static BlowFish create() {
        return new BlowFish();
    }
}
