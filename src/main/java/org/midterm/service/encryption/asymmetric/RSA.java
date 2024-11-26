package org.midterm.service.encryption.asymmetric;

import org.midterm.model.AsymmetricAlgorithms;
import org.midterm.model.PairKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class RSA {

    public static RSA create() {
        return new RSA();
    }

    public PairKey generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PairKey pairKey = PairKey.create();
        pairKey.setPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        pairKey.setPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return pairKey;
    }
    public String encryptText(AsymmetricAlgorithms config) throws Exception {
        Cipher cipher = Cipher.getInstance(config.getAlgorithm() + "/" + config.getMode() + "/" + config.getPadding());
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(config));
        byte[] encryptedBytes = cipher.doFinal(config.getInputText().getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    public String decryptText(AsymmetricAlgorithms config) throws Exception {
        Cipher cipher = Cipher.getInstance(config.getAlgorithm() + "/" + config.getMode() + "/" + config.getPadding());
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(config));
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(config.getInputText()));
        return new String(decryptedBytes);
    }
    public String encryptFile(AsymmetricAlgorithms config) throws Exception {
        File inputFileObj = new File(config.getFileInputPath());
        String parentPath = inputFileObj.getParent();
        String fileName = inputFileObj.getName();
        String outputFile;
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            outputFile = parentPath + File.separator + fileName.substring(0, dotIndex) + "_encrypted" + fileName.substring(dotIndex);
        } else {
            outputFile = parentPath + File.separator + fileName + "_encrypted";
        }
        byte[] fileData = Files.readAllBytes(inputFileObj.toPath());
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // Đảm bảo JCE Unlimited Strength Jurisdiction Policy được cài đặt
        SecretKey aesKey = keyGen.generateKey();
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecureRandom random = new SecureRandom();
        byte[] ivBytes = new byte[16];
        random.nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);
        byte[] encryptedData = aesCipher.doFinal(fileData);
        Cipher rsaCipher = Cipher.getInstance(config.getAlgorithm() + "/" + config.getMode() + "/" + config.getPadding());
        rsaCipher.init(Cipher.ENCRYPT_MODE, getPublicKey(config));
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(ivBytes.length); // Lưu độ dài của IV (16 byte)
            fos.write(ivBytes);        // Lưu IV
            fos.write(encryptedAesKey.length);
            fos.write(encryptedAesKey);
            fos.write(encryptedData);
        }
        return outputFile;
    }
    public String decryptFile(AsymmetricAlgorithms config) throws Exception {
        File inputFileObj = new File(config.getFileInputPath());
        String parentPath = inputFileObj.getParent();
        String fileName = inputFileObj.getName();
        String outputFile;
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            outputFile = parentPath + File.separator + fileName.substring(0, dotIndex) + "_decrypted" + fileName.substring(dotIndex);
        } else {
            outputFile = parentPath + File.separator + fileName + "_decrypted";
        }

        byte[] fileBytes = Files.readAllBytes(inputFileObj.toPath());
        int ivLength = fileBytes[0];
        byte[] ivBytes = Arrays.copyOfRange(fileBytes, 1, 1 + ivLength);
        int aesKeyLength = fileBytes[1 + ivLength];
        byte[] encryptedAesKey = Arrays.copyOfRange(fileBytes, 1 + ivLength + 1, 1 + ivLength + 1 + aesKeyLength);
        byte[] encryptedData = Arrays.copyOfRange(fileBytes, 1 + ivLength + 1 + aesKeyLength, fileBytes.length);
        Cipher rsaCipher = Cipher.getInstance(config.getAlgorithm() + "/" + config.getMode() + "/" + config.getPadding());
        rsaCipher.init(Cipher.DECRYPT_MODE, getPrivateKey(config));
        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, iv);
        byte[] decryptedData = aesCipher.doFinal(encryptedData);
        Files.write(new File(outputFile).toPath(), decryptedData);
        return outputFile;
    }
    private PublicKey getPublicKey(AsymmetricAlgorithms config) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(config.getPublicKey());
        KeyFactory keyFactory = KeyFactory.getInstance(config.getAlgorithm());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }
    private PrivateKey getPrivateKey(AsymmetricAlgorithms config) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(config.getPrivateKey());
        KeyFactory keyFactory = KeyFactory.getInstance(config.getAlgorithm());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(keySpec);
    }
}

