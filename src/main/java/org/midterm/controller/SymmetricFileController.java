package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.model.InformationData;
import org.midterm.service.encryption.symmetric_encryption.normal.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricFileController {
    private SymmetricFileController() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static void generateKey(String algorithm, int keySize, JTextField keyField) throws Exception {
        String key = "";
        switch (algorithm) {
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                key = dataEncryptionStandard.generateKey();
                keyField.setText(key);
                break;
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard advancedEncryptionStandard = AdvancedEncryptionStandard.create();
                key = advancedEncryptionStandard.generateKey(keySize);
                keyField.setText(key);
                break;
            case AlgorithmsConstant.BLOWFISH:
                BlowFish blowFish = BlowFish.create();
                key = blowFish.generateKey(keySize);
                keyField.setText(key);
                break;
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDES = TripleDES.create();
                key = tripleDES.generateKey(keySize);
                keyField.setText(key);
                break;
            case AlgorithmsConstant.RC4:
                RC4 rc4 = RC4.create();
                key = rc4.generateKey(keySize);
                keyField.setText(key);
                break;
        }
    }

    public static void generateIV(String algorithm, int ivSize, JTextField ivField) {
        String iv = "";
        switch (algorithm) {
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                iv = dataEncryptionStandard.generateIv(ivSize);
                ivField.setText(iv);
                break;
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard advancedEncryptionStandard = AdvancedEncryptionStandard.create();
                iv = advancedEncryptionStandard.generateIv(ivSize);
                ivField.setText(iv);
                break;
            case AlgorithmsConstant.BLOWFISH:
                BlowFish blowFish = BlowFish.create();
                iv = blowFish.generateIv();
                ivField.setText(iv);
                break;
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDES = TripleDES.create();
                iv = tripleDES.generateIv(ivSize);
                ivField.setText(iv);
                break;
        }
    }

    public static void encrypt(InformationData informationData, JTextField outputFile) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException {
        String algorithm = informationData.getAlgorithm();
        String mode = informationData.getMode();
        String padding = informationData.getPadding();
        String key = informationData.getKey();
        String iv = informationData.getIv();
        String filePath = informationData.getFilePath();
        String resultFilePath = informationData.getResultFilePath();
        System.out.println(informationData.toString());
        String result = "";
        switch (algorithm) {
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                result = dataEncryptionStandard.encryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard advancedEncryptionStandard = AdvancedEncryptionStandard.create();
                result = advancedEncryptionStandard.encryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.BLOWFISH:
                BlowFish blowFish = BlowFish.create();
                result = blowFish.encryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDES = TripleDES.create();
                result = tripleDES.encryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.RC4:
                RC4 rc4 = RC4.create();
                result = rc4.encryptFile(key, filePath);
                break;
        }
        outputFile.setText(result);
    }

    public static void decrypt(InformationData informationData, JTextField outputFile) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException {
        String algorithm = informationData.getAlgorithm();
        String mode = informationData.getMode();
        String padding = informationData.getPadding();
        String key = informationData.getKey();
        String filePath = informationData.getFilePath();
        String base64Iv = informationData.getIv();
        String resultFilePath = informationData.getResultFilePath();
        String result = "";

        switch (algorithm) {
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                result = dataEncryptionStandard.decryptFile(base64Iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard advancedEncryptionStandard = AdvancedEncryptionStandard.create();
                result = advancedEncryptionStandard.decryptFile(base64Iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.BLOWFISH:
                BlowFish blowFish = BlowFish.create();
                result = blowFish.decryptFile(base64Iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDES = TripleDES.create();
                result = tripleDES.decryptFile(base64Iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.RC4:
                RC4 rc4 = RC4.create();
                result = rc4.decryptFile(key, filePath);
                break;
        }
        outputFile.setText(result);
    }
}
