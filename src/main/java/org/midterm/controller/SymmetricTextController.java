package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.constant.StringConstant;
import org.midterm.model.InformationData;
import org.midterm.service.encryption.symmetric_encryption.classic.AffineCipher;
import org.midterm.service.encryption.symmetric_encryption.classic.ShiftCipher;
import org.midterm.service.encryption.symmetric_encryption.classic.SubstitutionCipher;
import org.midterm.service.encryption.symmetric_encryption.classic.Vigenere;
import org.midterm.service.encryption.symmetric_encryption.normal.DataEncryptionStandard;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SymmetricTextController {

    private SymmetricTextController() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static void generateKey(String algorithm, String language, JTextField keyField) throws NoSuchAlgorithmException {
        String key = "";
        switch (algorithm) {
            case AlgorithmsConstant.SHIFT:
                break;
            case AlgorithmsConstant.SUBSTITUTION:
                SubstitutionCipher substitutionCipher = SubstitutionCipher.create();
                key = substitutionCipher.generateRandomKey(language);
                keyField.setText(key);
                break;
            case AlgorithmsConstant.VIGENERE:
                Vigenere vigenere = Vigenere.create();
                key = vigenere.generateRandomKey(language, 10);
                keyField.setText(key);
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                key = dataEncryptionStandard.generateKey();
                keyField.setText(key);
        }
    }

    public static void generateIV(String algorithm, int sizeKey, JTextField ivField) {
        String iv = "";
        switch (algorithm) {
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                iv = dataEncryptionStandard.generateIv(sizeKey);
                ivField.setText(iv);
            default:
                break;
        }
    }

    public static void encrypt(InformationData informationData, JTextArea resultArea) {
        String algorithm = informationData.getAlgorithm();
        String mode = informationData.getMode();
        String padding = informationData.getPadding();
        Integer shift = informationData.getShift();
        String key = informationData.getKey();
        String iv = informationData.getIv();
        String language = informationData.getLanguage();
        String inputText = informationData.getInputText();
        String result = "";

        switch (algorithm) {
            case AlgorithmsConstant.SHIFT:
                ShiftCipher shiftCipher = ShiftCipher.create();
                if (informationData.getLanguage().equals(StringConstant.LANGUAGE_ENGLISH)) {
                    result = shiftCipher.encryptEnglish(inputText, shift);
                } else {
                    result = shiftCipher.encryptVietnamese(inputText, shift);
                }
                break;
            case AlgorithmsConstant.SUBSTITUTION:
                SubstitutionCipher substitutionCipher = SubstitutionCipher.create();
                result = substitutionCipher.encrypt(inputText, key, language);
                break;
            case AlgorithmsConstant.AFFINE:
                AffineCipher affineCipher = AffineCipher.create();
                result = affineCipher.encrypt(inputText, informationData.getAMultiplier(), informationData.getBShift(), language);
                break;
            case AlgorithmsConstant.VIGENERE:
                Vigenere vigenere = Vigenere.create();
                result = vigenere.encrypt(inputText, key, language);
                break;
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                try {
                    result = dataEncryptionStandard.encryptText(key, inputText, mode, padding, iv);
                } catch (Exception e) {
                    System.err.println("Error encrypting text: " + e.getMessage());
                }
                break;
        }
        resultArea.setText(result);
    }

    public static void decrypt(InformationData informationData, JTextArea resultArea) {
        String algorithm = informationData.getAlgorithm();
        String mode = informationData.getMode();
        String padding = informationData.getPadding();
        Integer shift = informationData.getShift();
        String key = informationData.getKey();
        String language = informationData.getLanguage();
        String iv = informationData.getIv();
        String inputText = informationData.getInputText();
        String result = "";

        switch (algorithm) {
            case AlgorithmsConstant.SHIFT:
                ShiftCipher shiftCipher = ShiftCipher.create();
                if (informationData.getLanguage().equals(StringConstant.LANGUAGE_ENGLISH)) {
                    result = shiftCipher.decryptEnglish(inputText, shift);
                } else {
                    result = shiftCipher.decryptVietnamese(inputText, shift);
                }
                break;
            case AlgorithmsConstant.SUBSTITUTION:
                SubstitutionCipher substitutionCipher = SubstitutionCipher.create();
                result = substitutionCipher.decrypt(inputText, key, language);
                break;
            case AlgorithmsConstant.AFFINE:
                AffineCipher affineCipher = AffineCipher.create();
                result = affineCipher.decrypt(inputText, informationData.getAMultiplier(), informationData.getBShift(), language);
                break;
            case AlgorithmsConstant.VIGENERE:
                Vigenere vigenere = Vigenere.create();
                result = vigenere.decrypt(inputText, key, language);
                break;
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                try {
                    result = dataEncryptionStandard.decryptText(inputText, key, iv, mode, padding);
                } catch (Exception e) {
                    System.err.println("Error decrypting text: " + e.getMessage());
                }
                break;
        }

        resultArea.setText(result);
    }
}
