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
 * Lớp AdvancedEncryptionStandard cung cấp các phương thức để mã hóa và giải mã dữ liệu văn bản
 * và tệp tin bằng thuật toán AES (Advanced Encryption Standard).
 * Lớp này hỗ trợ nhiều chế độ mã hóa khác nhau (ECB, CBC, ...) và các phương thức padding.
 */
public class AdvancedEncryptionStandard {

    /**
     * Tạo một đối tượng AdvancedEncryptionStandard mới.
     *
     * @return đối tượng AdvancedEncryptionStandard.
     */
    public static AdvancedEncryptionStandard create() {
        return new AdvancedEncryptionStandard();
    }

    /**
     * Sinh một khóa AES với độ dài chỉ định.
     *
     * @param keySize độ dài khóa (128, 192, hoặc 256 bit).
     * @return khóa được mã hóa dưới dạng Base64.
     * @throws Exception nếu có lỗi xảy ra trong quá trình sinh khóa.
     */
    public String generateKey(int keySize) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        return Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
    }

    /**
     * Sinh một chuỗi IV (Initialization Vector) ngẫu nhiên.
     *
     * @return IV được mã hóa dưới dạng Base64.
     */
    public String generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }


    /**
     * Mã hóa văn bản với khóa bí mật và chế độ AES chỉ định.
     *
     * @param base64Iv           IV được mã hóa Base64 (nếu cần).
     * @param secretKeyBase64    Khóa bí mật được mã hóa Base64.
     * @param plainText          Văn bản gốc cần mã hóa.
     * @param mode               Chế độ mã hóa (ví dụ: ECB, CBC).
     * @param padding            Phương pháp padding (ví dụ: PKCS5Padding).
     * @return Văn bản đã mã hóa dưới dạng Base64.
     */
    public String encryptText(String base64Iv, String secretKeyBase64, String plainText, String mode, String padding) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }
        String transformation = "AES/" + mode + "/" + padding;

        secretKeyBase64 = secretKeyBase64.trim();
        if (base64Iv != null) {
            base64Iv = base64Iv.trim();
        }

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);

        Cipher cipher = Cipher.getInstance(transformation);

        if ("ECB".equalsIgnoreCase(mode)) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            if (base64Iv == null || base64Iv.isEmpty()) {
                throw new IllegalArgumentException("IV not found.");
            }
            IvParameterSpec iv = convertBase64ToIv(base64Iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        }
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Giải mã văn bản mã hóa với khóa bí mật và chế độ AES chỉ định.
     *
     * @param base64Iv           IV được mã hóa Base64 (nếu cần).
     * @param secretKeyBase64    Khóa bí mật được mã hóa Base64.
     * @param cipherTextBase64   Văn bản mã hóa dưới dạng Base64.
     * @param mode               Chế độ mã hóa (ví dụ: ECB, CBC).
     * @param padding            Phương pháp padding (ví dụ: PKCS5Padding).
     * @return Văn bản gốc sau khi giải mã.
     */
    public String decryptText(String base64Iv, String secretKeyBase64, String cipherTextBase64, String mode, String padding) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }

        String transformation = "AES/" + mode + "/" + padding;

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

        byte[] cipherTextBytes = Base64.getDecoder().decode(cipherTextBase64);
        byte[] plainTextBytes = cipher.doFinal(cipherTextBytes);
        return new String(plainTextBytes);
    }

    /**
     * Mã hóa tệp tin với khóa bí mật và chế độ AES chỉ định.
     *
     * @param base64Iv        IV được mã hóa Base64 (nếu cần).
     * @param baseSecretKey   Khóa bí mật được mã hóa Base64.
     * @param inputFile       Đường dẫn tệp tin gốc.
     * @param mode            Chế độ mã hóa (ví dụ: ECB, CBC).
     * @param padding         Phương pháp padding (ví dụ: PKCS5Padding).
     * @return Đường dẫn tệp tin đã mã hóa.
     */
    public String encryptFile(String base64Iv, String baseSecretKey, String inputFile, String mode, String padding) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
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

        String transformation = "AES/" + mode + "/" + padding;
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

        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile)); OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {

            if (iv != null && !mode.equalsIgnoreCase("ECB")) {
                os.write(iv.getIV());
            }

            BlowFish.writeFile(cipher, is, os);
        }
        return outputFile;
    }

    /**
     * Giải mã tệp tin mã hóa với khóa bí mật và chế độ AES chỉ định.
     *
     * @param base64Iv        IV được mã hóa Base64 (nếu cần).
     * @param baseSecretKey   Khóa bí mật được mã hóa Base64.
     * @param inputFile       Đường dẫn tệp tin mã hóa.
     * @param mode            Chế độ mã hóa (ví dụ: ECB, CBC).
     * @param padding         Phương pháp padding (ví dụ: PKCS5Padding).
     * @return Đường dẫn tệp tin sau khi giải mã.
     */
    public String decryptFile(String base64Iv, String baseSecretKey, String inputFile, String mode, String padding) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {

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

        String transformation = "AES/" + mode + "/" + padding;

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

        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile)); OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {

            BlowFish.writeFile(cipher, is, os);
        }
        return outputFile;
    }

    /**
     * Chuyển đổi một chuỗi Base64 thành SecretKey.
     *
     * @param base64Key chuỗi Base64 chứa khóa AES.
     * @return đối tượng SecretKey.
     */
    private SecretKey convertBase64ToKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }

    /**
     * Chuyển đổi một chuỗi Base64 thành IvParameterSpec.
     *
     * @param base64Iv chuỗi Base64 chứa IV.
     * @return đối tượng IvParameterSpec.
     */
    private IvParameterSpec convertBase64ToIv(String base64Iv) {
        byte[] decodedIv = Base64.getDecoder().decode(base64Iv);
        return new IvParameterSpec(decodedIv);
    }

}
