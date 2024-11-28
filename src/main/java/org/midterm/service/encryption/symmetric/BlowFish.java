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

public class BlowFish {

    public String generateKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

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

    public String encryptFile(String base64Iv, String baseSecretKey, String inputFile, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {
        return processFile(base64Iv, baseSecretKey, inputFile, mode, padding, Cipher.ENCRYPT_MODE);
    }

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

    public static BlowFish create() {
        return new BlowFish();
    }
}
