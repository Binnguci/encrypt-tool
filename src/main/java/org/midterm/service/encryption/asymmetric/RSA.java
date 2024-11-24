package org.midterm.service.encryption.asymmetric;

import org.midterm.model.AsymmetricAlgorithms;
import org.midterm.model.PairKey;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class RSA {

    // Generate public and private keys based on the configuration
    public PairKey generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PairKey pairKey = PairKey.create();
        pairKey.setPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        pairKey.setPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return pairKey;
    }

    // Encrypt text
    public String encryptText(AsymmetricAlgorithms config) throws Exception {
        Cipher cipher = Cipher.getInstance(config.getAlgorithm() + "/" + config.getMode() + "/" + config.getPadding());
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(config));
        byte[] encryptedBytes = cipher.doFinal(config.getInputText().getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt text
    public String decryptText(AsymmetricAlgorithms config, String encryptedText) throws Exception {
        Cipher cipher = Cipher.getInstance(config.getAlgorithm() + "/" + config.getMode() + "/" + config.getPadding());
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(config));
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    // Encrypt file
    public void encryptFile(AsymmetricAlgorithms config, String inputFilePath, String outputFilePath) throws Exception {
        byte[] fileData = Files.readAllBytes(new File(inputFilePath).toPath());
        Cipher cipher = Cipher.getInstance(config.getAlgorithm() + "/" + config.getMode() + "/" + config.getPadding());
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(config));
        byte[] encryptedBytes = cipher.doFinal(fileData);
        Files.write(new File(outputFilePath).toPath(), encryptedBytes);
    }

    // Decrypt file
    public void decryptFile(AsymmetricAlgorithms config, String inputFilePath, String outputFilePath) throws Exception {
        byte[] encryptedData = Files.readAllBytes(new File(inputFilePath).toPath());
        Cipher cipher = Cipher.getInstance(config.getAlgorithm() + "/" + config.getMode() + "/" + config.getPadding());
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(config));
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        Files.write(new File(outputFilePath).toPath(), decryptedBytes);
    }

    // Convert public key from String to PublicKey
    private PublicKey getPublicKey(AsymmetricAlgorithms config) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(config.getPublicKey());
        KeyFactory keyFactory = KeyFactory.getInstance(config.getAlgorithm());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    // Convert private key from String to PrivateKey
    private PrivateKey getPrivateKey(AsymmetricAlgorithms config) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(config.getPrivateKey());
        KeyFactory keyFactory = KeyFactory.getInstance(config.getAlgorithm());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(keySpec);
    }
}

