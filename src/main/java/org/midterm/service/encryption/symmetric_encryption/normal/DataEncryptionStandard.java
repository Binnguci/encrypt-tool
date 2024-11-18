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

public class DataEncryptionStandard {

    public static DataEncryptionStandard create() {
        return new DataEncryptionStandard();
    }

    public String generateKey() throws NoSuchAlgorithmException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(56);
        return Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
    }

    public String generateIv() {
        byte[] iv = new byte[8];
        new SecureRandom().nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }

    public SecretKey convertBase64ToKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
    }

    public String encryptText(SecretKey secretKey, String plainText, String mode, String padding, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        String transformation = "DES";
        if (mode != null && !mode.isEmpty() && !mode.equalsIgnoreCase("None")) {
            transformation += "/" + mode;
            if (padding != null && !padding.isEmpty()) {
                transformation += "/" + padding;
            }
        }

        Cipher cipher = Cipher.getInstance(transformation);
        if (mode.equalsIgnoreCase("ECB")) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        }
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        String encodedCipherText = Base64.getEncoder().encodeToString(cipherText);

        return (mode.equalsIgnoreCase("ECB") ? "" : Base64.getEncoder().encodeToString(iv.getIV()) + ":") + encodedCipherText;
    }


    public String decryptText(String combinedCipherText, SecretKey secretKey, IvParameterSpec iv, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        String transformation = "DES";
        if (mode != null && !mode.isEmpty() && !mode.equalsIgnoreCase("None")) {
            transformation += "/" + mode;
            if (padding != null && !padding.isEmpty()) {
                transformation += "/" + padding;
            }
        }
        Cipher cipher = Cipher.getInstance(transformation);
        if (mode.equalsIgnoreCase("ECB")) {
            // ECB không sử dụng IV
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherTextBytes = Base64.getDecoder().decode(combinedCipherText);
            byte[] plainText = cipher.doFinal(cipherTextBytes);
            return new String(plainText);
        } else {
            // Với các chế độ khác cần sử dụng IV
            if (iv == null) {
                throw new IllegalArgumentException("IV must not be null for " + mode + " mode");
            }
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] cipherTextBytes = Base64.getDecoder().decode(combinedCipherText);
            byte[] plainText = cipher.doFinal(cipherTextBytes);
            return new String(plainText);
        }
    }


    public void encryptFile(IvParameterSpec iv, SecretKey secretKey, File inputFile, File outputFile, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {
        String transformation = "DES";
        if ((mode != null && !mode.isEmpty()) || (!mode.equalsIgnoreCase("None"))) {
            transformation += "/" + mode;
            if (padding != null && !padding.isEmpty()) {
                transformation += "/" + padding;
            }
        }

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
             OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            os.write(iv.getIV());
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
    }


    public void decryptFile(SecretKey secretKey, File inputFile, File outputFile, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {
        String transformation = "DES";

        if (mode != null && !mode.isEmpty()) {
            transformation += "/" + mode;
            if (padding != null && !padding.isEmpty()) {
                transformation += "/" + padding;
            }
        }

        Cipher cipher = Cipher.getInstance(transformation);
        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
             OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            byte[] ivBytes = new byte[8];
            is.read(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
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
    }
}
