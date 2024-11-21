package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.model.AffineKey;
import org.midterm.model.ClassicAlgorithm;
import org.midterm.service.encryption.classic.AffineCipher;
import org.midterm.service.encryption.classic.ShiftCipher;
import org.midterm.service.encryption.classic.SubstitutionCipher;
import org.midterm.service.encryption.classic.Vigenere;

public class ClassicController {
    public static String generateSubstitutionKey(String language) {
        SubstitutionCipher substitutionCipher = SubstitutionCipher.create();
        return substitutionCipher.generateKey(language);
    }

    public static String generateKey(String language, String plaintext) {
        Vigenere vigenere = Vigenere.create();
        return vigenere.generateKey(plaintext, language);
    }

    public static String encrypt(ClassicAlgorithm classicAlgorithm, String inputText) {
        String result = "";
        switch (classicAlgorithm.getName()) {
            case AlgorithmsConstant.SHIFT:
                ShiftCipher shiftCipher = ShiftCipher.create();
                result = shiftCipher.encrypt(inputText, classicAlgorithm.getLanguage(), classicAlgorithm.getShift());
                break;
            case AlgorithmsConstant.SUBSTITUTION:
                SubstitutionCipher substitutionCipher = SubstitutionCipher.create();
                result = substitutionCipher.encrypt(inputText, classicAlgorithm.getLanguage(), classicAlgorithm.getSubstitutionKey());
                break;
            case AlgorithmsConstant.AFFINE:
                AffineCipher affineCipher = AffineCipher.create();
                AffineKey affineKey = new AffineKey(classicAlgorithm.getAMultiplier(), classicAlgorithm.getBShift());
                result = affineCipher.encrypt(inputText, classicAlgorithm.getLanguage(), affineKey);
                break;
            case AlgorithmsConstant.VIGENERE:
                Vigenere vigenere = Vigenere.create();
                result = vigenere.encrypt(inputText, classicAlgorithm.getKey(), classicAlgorithm.getLanguage());
                break;
            default:
                return null;
        }
        return result;
    }

    public static String decrypt(ClassicAlgorithm classicAlgorithm, String inputText) {
        String result = "";
        switch (classicAlgorithm.getName()) {
            case AlgorithmsConstant.SHIFT:
                ShiftCipher shiftCipher = ShiftCipher.create();
                result = shiftCipher.decrypt(inputText, classicAlgorithm.getLanguage(), classicAlgorithm.getShift());
                break;
            case AlgorithmsConstant.SUBSTITUTION:
                SubstitutionCipher substitutionCipher = SubstitutionCipher.create();
                result = substitutionCipher.decrypt(inputText, classicAlgorithm.getLanguage(), classicAlgorithm.getSubstitutionKey());
                break;
            case AlgorithmsConstant.AFFINE:
                AffineCipher affineCipher = AffineCipher.create();
                AffineKey affineKey = new AffineKey(classicAlgorithm.getAMultiplier(), classicAlgorithm.getBShift());
                result = affineCipher.decrypt(inputText, classicAlgorithm.getLanguage(), affineKey);
                break;
            case AlgorithmsConstant.VIGENERE:
                Vigenere vigenere = Vigenere.create();
                result = vigenere.decrypt(inputText, classicAlgorithm.getKey(), classicAlgorithm.getLanguage());
                break;
            default:
                return null;
        }
        return result;
    }


}
