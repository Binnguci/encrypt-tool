package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.constant.StringConstant;
import org.midterm.model.InformationData;
import org.midterm.service.encryption.symmetric_encryption.classic.ShiftCipher;
import org.midterm.service.encryption.symmetric_encryption.classic.SubstitutionCipher;
import org.midterm.service.encryption.symmetric_encryption.impl.DataEncryptionStandard;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;

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
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                key = dataEncryptionStandard.generateKey();
                keyField.setText(key);
        }
    }

    public static void generateIV(String algorithm, JTextField ivField) {
        String iv = "";
        switch (algorithm) {
            case AlgorithmsConstant.SHIFT:
                break;
            case AlgorithmsConstant.SUBSTITUTION:
                break;
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                iv = dataEncryptionStandard.generateIv();
                ivField.setText(iv);
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
        }

        resultArea.setText(result);
    }
}
