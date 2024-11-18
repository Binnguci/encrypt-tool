package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.constant.StringConstant;
import org.midterm.model.InformationData;
import org.midterm.service.encryption.symmetric_encryption.classic.AffineCipher;
import org.midterm.service.encryption.symmetric_encryption.classic.ShiftCipher;
import org.midterm.service.encryption.symmetric_encryption.classic.SubstitutionCipher;
import org.midterm.service.encryption.symmetric_encryption.classic.Vigenere;
import org.midterm.service.encryption.symmetric_encryption.normal.AdvancedEncryptionStandard;
import org.midterm.service.encryption.symmetric_encryption.normal.BlowFish;
import org.midterm.service.encryption.symmetric_encryption.normal.DataEncryptionStandard;
import org.midterm.service.encryption.symmetric_encryption.normal.TripleDES;

import javax.swing.*;

public class SymmetricTextController {

    private SymmetricTextController() {
        throw new AssertionError("Cannot be instantiated");
    }

    public static void generateKey(String algorithm, String language, Integer keySize, JTextField keyField) throws Exception {
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
                break;
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                key = dataEncryptionStandard.generateKey();
                keyField.setText(key);
                break;
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard AES = AdvancedEncryptionStandard.create();
                key = AES.generateKey(keySize);
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
        }
    }

    public static void generateIV(String algorithm, int sizeKey, JTextField ivField) {
        String iv = "";
        switch (algorithm) {
            case AlgorithmsConstant.DES:
                DataEncryptionStandard dataEncryptionStandard = DataEncryptionStandard.create();
                iv = dataEncryptionStandard.generateIv(sizeKey);
                ivField.setText(iv);
                break;
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard AES = AdvancedEncryptionStandard.create();
                iv = AES.generateIv(sizeKey);
                ivField.setText(iv);
                break;
            case AlgorithmsConstant.BLOWFISH:
                BlowFish blowFish = BlowFish.create();
                iv = blowFish.generateIv();
                ivField.setText(iv);
                break;
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDES = TripleDES.create();
                iv = tripleDES.generateIv(sizeKey);
                ivField.setText(iv);
                break;
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
        System.out.println(informationData.toString());
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
                    result = dataEncryptionStandard.encryptText(iv, key, inputText, mode, padding);
                } catch (Exception e) {
                    System.err.println("Error encrypting text: " + e.getMessage());
                }
                break;
            case AlgorithmsConstant.AES:
                try {
                    AdvancedEncryptionStandard AES = AdvancedEncryptionStandard.create();
                    result = AES.encryptText(iv, key, inputText, mode, padding);
                } catch (Exception e) {
                    System.err.println("Error encrypting text: " + e.getMessage());
                }
                break;
            case AlgorithmsConstant.BLOWFISH:
                try {
                    BlowFish blowFish = BlowFish.create();
                    result = blowFish.encryptText(iv, key, inputText, mode, padding);
                } catch (Exception e) {
                    System.err.println("Error encrypting text: " + e.getMessage());
                }
                break;
            case AlgorithmsConstant.TRIPLEDES:
                try {
                    TripleDES tripleDES = TripleDES.create();
                    result = tripleDES.encryptText(iv, key, inputText, mode, padding);
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
            case AlgorithmsConstant.AES:
                try {
                    AdvancedEncryptionStandard AES = AdvancedEncryptionStandard.create();
                    result = AES.decryptText(iv, key, inputText, mode, padding);
                } catch (Exception e) {
                    System.err.println("Error decrypting text: " + e.getMessage());
                }
            case AlgorithmsConstant.BLOWFISH:
                try {
                    BlowFish blowFish = BlowFish.create();
                    result = blowFish.decryptText(iv, key, inputText, mode, padding);
                } catch (Exception e) {
                    System.err.println("Error decrypting text: " + e.getMessage());
                }
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDES = TripleDES.create();
                try {
                    result = tripleDES.decryptText(iv, key, inputText, mode, padding);
                } catch (Exception e) {
                    System.err.println("Error decrypting text: " + e.getMessage());
                }
        }
        resultArea.setText(result);
    }
}
