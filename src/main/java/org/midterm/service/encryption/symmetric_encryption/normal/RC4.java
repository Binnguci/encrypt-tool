package org.midterm.service.encryption.symmetric_encryption.normal;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class RC4 {
    public static String generateKey(int keySize) throws Exception {
        if (keySize < 40 || keySize > 2048) {
            throw new IllegalArgumentException("Key size phải nằm trong khoảng từ 40 đến 2048 bit.");
        }
        KeyGenerator keyGenerator = KeyGenerator.getInstance("RC4");
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    private static SecretKey convertBase64ToKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "RC4");
    }

    public String encryptText(String secretKeyBase64, String plainText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        if (plainText == null || plainText.isEmpty()) {
            throw new IllegalArgumentException("Plain text không được rỗng hoặc null.");
        }
        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public String decryptText(String secretKeyBase64, String cipherTextBase64)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        if (cipherTextBase64 == null || cipherTextBase64.isEmpty()) {
            throw new IllegalArgumentException("Cipher text không được rỗng hoặc null.");
        }
        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] cipherTextBytes = Base64.getDecoder().decode(cipherTextBase64);
        byte[] plainTextBytes = cipher.doFinal(cipherTextBytes);
        return new String(plainTextBytes);
    }

    public String encryptFile(String secretKeyBase64, String inputFile)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {

        File inputFileObj = new File(inputFile);
        String parentPath = inputFileObj.getParent();
        String fileName = inputFileObj.getName();
        String outputFile = parentPath + File.separator + fileName + "_encrypt";

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

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

    public String decryptFile(String secretKeyBase64, String inputFile)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {

        File inputFileObj = new File(inputFile);
        String parentPath = inputFileObj.getParent();
        String fileName = inputFileObj.getName();
        String outputFile = parentPath + File.separator + fileName + "_decrypt";

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

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

    public static RC4 create() {
        return new RC4();
    }
}
