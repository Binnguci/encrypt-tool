package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.model.InformationData;
import org.midterm.service.encryption.symmetric_encryption.normal.AdvancedEncryptionStandard;
import org.midterm.service.encryption.symmetric_encryption.normal.DataEncryptionStandard;

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
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard advancedEncryptionStandard = AdvancedEncryptionStandard.create();
                key = advancedEncryptionStandard.generateKey(keySize);
                keyField.setText(key);
        }
    }

    public static void generateIV(String algorithm, int ivSize, JTextField ivField) {
        String iv = "";
        switch (algorithm) {
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                iv = dataEncryptionStandard.generateIv(ivSize);
                ivField.setText(iv);
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard advancedEncryptionStandard = AdvancedEncryptionStandard.create();
                iv = advancedEncryptionStandard.generateIv(ivSize);
                ivField.setText(iv);
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
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard advancedEncryptionStandard = AdvancedEncryptionStandard.create();
                result = advancedEncryptionStandard.encryptFile(iv, key, filePath, mode, padding);
        }
        outputFile.setText(result);
    }

    public static void decrypt(InformationData informationData) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException {
        String algorithm = informationData.getAlgorithm();
        String mode = informationData.getMode();
        String padding = informationData.getPadding();
        String key = informationData.getKey();
        String filePath = informationData.getFilePath();
        String base64Iv = informationData.getIv();
        String resultFilePath = informationData.getResultFilePath();

        switch (algorithm) {
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                dataEncryptionStandard.decryptFile(base64Iv, key, filePath, mode, padding);
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard advancedEncryptionStandard = AdvancedEncryptionStandard.create();
                advancedEncryptionStandard.decryptFile(base64Iv, key, filePath, mode, padding);
        }
    }
}
