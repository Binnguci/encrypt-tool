package org.midterm.service.encryption.digitalsignature;

import org.midterm.model.PairKey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DSA {

    public static DSA create() {
        return new DSA();
    }

    public PairKey generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PairKey pairKey = PairKey.create();
        pairKey.setPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        pairKey.setPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return pairKey;
    }

    public String signData(String data, String privateKeyBase64, String algorithm) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
        PrivateKey privateKey = KeyFactory.getInstance("DSA")
                .generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] digitalSignature = signature.sign();

        return Base64.getEncoder().encodeToString(digitalSignature);
    }

    public String signFile(String path, String privateKeyBase64, String algorithm) throws Exception {
        File file = new File(path);
        byte[] fileBytes = readFileToBytes(file);
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
        PrivateKey privateKey = KeyFactory.getInstance("DSA")
                .generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(fileBytes);
        byte[] digitalSignature = signature.sign();

        return Base64.getEncoder().encodeToString(digitalSignature);
    }


    public boolean verifySignature(String data, String signatureBase64, String publicKeyBase64, String algorithm) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        PublicKey publicKey = KeyFactory.getInstance("DSA")
                .generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        byte[] digitalSignature = Base64.getDecoder().decode(signatureBase64);

        return signature.verify(digitalSignature);
    }

    public boolean verifyFileSignature(String path, String signatureBase64, String publicKeyBase64, String algorithm) throws Exception {
        File file = new File(path);
        byte[] fileBytes = readFileToBytes(file);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        PublicKey publicKey = KeyFactory.getInstance("DSA")
                .generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(fileBytes);
        byte[] digitalSignature = Base64.getDecoder().decode(signatureBase64);

        return signature.verify(digitalSignature);
    }


    private byte[] readFileToBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

    public static void main(String[] args) {
        try {
            DSA dsa = DSA.create();
            String signAlgorithm = "SHA1withDSA";

            int keySize = 1024;

            System.out.println("==== Generating Key Pair ====");
            PairKey pairKey = dsa.generateKeyPair(keySize);
            String publicKey = pairKey.getPublicKey();
            String privateKey = pairKey.getPrivateKey();
            System.out.println("Public Key: " + publicKey);
            System.out.println("Private Key: " + privateKey);

            String data = "This is a test message.";
            System.out.println("\n==== Signing Data ====");
            String signature = dsa.signData(data, privateKey, signAlgorithm);
            System.out.println("Digital Signature: " + signature);

            // Xác minh chữ ký
            System.out.println("\n==== Verifying Signature ====");
            boolean isVerified = dsa.verifySignature(data, signature, publicKey, signAlgorithm);
            System.out.println("Signature Verified: " + isVerified);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
