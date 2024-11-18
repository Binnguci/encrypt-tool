package org.midterm.service.encryption.symmetric_encryption.normal;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class TripleDES {
    public static TripleDES create() {
        return new TripleDES();
    }

    public static String generateKey(int keySize) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static String generateIv(int ivSize) {
        byte[] iv = new byte[ivSize / 8];
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

    public String encryptText(String base64Iv, String secretKeyBase64, String plainText, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        if (plainText == null || plainText.isEmpty()) {
            throw new IllegalArgumentException("Plain text không được rỗng hoặc null.");
        }
        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }
        String transformation = "DESede";
        if (mode != null && !mode.isEmpty() && !mode.equalsIgnoreCase("None")) {
            transformation += "/" + mode;
            if (padding != null && !padding.isEmpty()) {
                transformation += "/" + padding;
            }
        }

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance(transformation);

        if ("ECB".equalsIgnoreCase(mode) || "None".equalsIgnoreCase(mode)) {
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

    public String decryptText(String base64Iv, String secretKeyBase64, String cipherText, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        if (cipherText == null || cipherText.isEmpty()) {
            throw new IllegalArgumentException("Cipher text không được rỗng hoặc null.");
        }
        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }
        String transformation = "DESede";
        if (mode != null && !mode.isEmpty() && !mode.equalsIgnoreCase("None")) {
            transformation += "/" + mode;
            if (padding != null && !padding.isEmpty()) {
                transformation += "/" + padding;
            }
        }

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance(transformation);

        if ("ECB".equalsIgnoreCase(mode) || "None".equalsIgnoreCase(mode)) {
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


    public String encryptFile(String base64Iv, String baseSecretKey, String inputFile, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {
        File inputFileObj = new File(inputFile);
        System.out.println(inputFileObj);
        String parentPath = inputFileObj.getParent(); // Lấy thư mục chứa file nguồn
        String fileName = inputFileObj.getName(); // Lấy tên file nguồn
        String outputFile = null;
        int dotIndex = fileName.lastIndexOf('.'); // Tìm dấu chấm cuối cùng (nếu có)
        if (dotIndex != -1) { // Nếu file có phần mở rộng
            outputFile = parentPath + File.separator + fileName.substring(0, dotIndex) + "_encrypt" + fileName.substring(dotIndex);
        } else { // Nếu file không có phần mở rộng
            outputFile = parentPath + File.separator + fileName + "_encrypt";
        }

        String transformation = "DESede";
        if (mode != null && !mode.isEmpty() && !mode.equalsIgnoreCase("None")) {
            transformation += "/" + mode;
            if (padding != null && !padding.isEmpty()) {
                transformation += "/" + padding;
            }
        }

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
        return outputFile;
    }

    public String decryptFile(String base64Iv, String baseSecretKey, String inputFile, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {

        File inputFileObj = new File(inputFile);
        String parentPath = inputFileObj.getParent(); // Thư mục chứa file nguồn
        String fileName = inputFileObj.getName(); // Tên file nguồn
        String outputFile = null;

        int dotIndex = fileName.lastIndexOf('.'); // Tìm dấu chấm cuối cùng (nếu có)
        if (dotIndex != -1) {
            outputFile = parentPath + File.separator + fileName.substring(0, dotIndex) + "_decrypt" + fileName.substring(dotIndex);
        } else {
            outputFile = parentPath + File.separator + fileName + "_decrypt";
        }

        String transformation = "Desede";
        if (mode != null && !mode.isEmpty() && !mode.equalsIgnoreCase("None")) {
            transformation += "/" + mode;
            if (padding != null && !padding.isEmpty()) {
                transformation += "/" + padding;
            }
        }

        SecretKey secretKey = convertBase64ToKey(baseSecretKey);
        IvParameterSpec iv = null;

        // Tạo IV nếu có base64Iv
        if (base64Iv != null && !base64Iv.isEmpty()) {
            iv = convertBase64ToIv(base64Iv);
        }

        Cipher cipher = Cipher.getInstance(transformation);

        // Khởi tạo Cipher
        if (iv != null && !mode.equalsIgnoreCase("ECB")) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        }

        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
             OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {

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
        return outputFile;
    }
}
