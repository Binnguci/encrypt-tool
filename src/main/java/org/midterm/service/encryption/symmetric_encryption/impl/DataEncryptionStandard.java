package org.midterm.service.encryption.symmetric_encryption.impl;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
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

    public String encryptText(IvParameterSpec iv, SecretKey secretKey, String plainText, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        String transformation = "DES/" + mode + "/" + padding;
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        String encodedIv = Base64.getEncoder().encodeToString(iv.getIV());
        String encodedCipherText = Base64.getEncoder().encodeToString(cipherText);

        return encodedIv + ":" + encodedCipherText;
    }


    public String decryptText(String combinedCipherText, SecretKey secretKey, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        String[] parts = combinedCipherText.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid cipher text format");
        }
        byte[] ivBytes = Base64.getDecoder().decode(parts[0]);
        byte[] cipherTextBytes = Base64.getDecoder().decode(parts[1]);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        String transformation = "DES/" + mode + "/" + padding;
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] plainText = cipher.doFinal(cipherTextBytes);
        return new String(plainText);
    }


    public void encryptFile(IvParameterSpec iv, SecretKey secretKey, File inputFile, File outputFile, String mode, String padding)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {
        String transformation = "DES/" + mode + "/" + padding;
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
        String transformation = "DES/" + mode + "/" + padding;
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
