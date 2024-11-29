package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.model.SymmetricAlgorithms;
import org.midterm.service.encryption.symmetric.*;

public class SymmetricTextController {
    public static String generateKey(String algorithm, String keySize) throws Exception {
        String result = "";
        int keySizeInt = Integer.parseInt(keySize);
        result = switch (algorithm) {
            case AlgorithmsConstant.AES -> {
                AdvancedEncryptionStandard aes = AdvancedEncryptionStandard.create();
                yield aes.generateKey(keySizeInt);
            }
            case AlgorithmsConstant.DES -> {
                DataEncryptionStandard des = DataEncryptionStandard.create();
                yield des.generateKey();
            }
            case AlgorithmsConstant.BLOWFISH -> {
                BlowFish blowFish = BlowFish.create();
                yield blowFish.generateKey(keySizeInt);
            }
            case AlgorithmsConstant.TRIPLEDES -> {
                TripleDES tripleDes = TripleDES.create();
                yield tripleDes.generateKey(keySizeInt);
            }
            case AlgorithmsConstant.RC4 -> {
                RC4 rc4 = RC4.create();
                yield rc4.generateKey();
            }
            case AlgorithmsConstant.SEED, AlgorithmsConstant.IDEA -> {
                LibSymetricCipher libSymetricCipher = new LibSymetricCipher();
                yield libSymetricCipher.generateKey(algorithm);
            }
            default -> result;
        };
        return result;
    }

    public static String generateIV(String algorithm) throws Exception {
        String result = "";
        switch (algorithm) {
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard aes = AdvancedEncryptionStandard.create();
                result = aes.generateIv();
                break;
            case AlgorithmsConstant.DES:
                DataEncryptionStandard des = DataEncryptionStandard.create();
                result = des.generateIv();
                break;
            case AlgorithmsConstant.BLOWFISH:
                BlowFish blowFish = BlowFish.create();
                result = blowFish.generateIv();
                break;
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDes = TripleDES.create();
                result = tripleDes.generateIv();
                break;
            case AlgorithmsConstant.SEED, AlgorithmsConstant.IDEA:
                LibSymetricCipher libSymetricCipher = new LibSymetricCipher();
                result = libSymetricCipher.generateIv(algorithm);
                break;
            default:
                break;
        }
        return result;
    }

    public static String encrypt(SymmetricAlgorithms symmetricAlgorithms) throws Exception {
        String algorithm = symmetricAlgorithms.getName();
        String mode = symmetricAlgorithms.getMode();
        String padding = symmetricAlgorithms.getPadding();
        String key = symmetricAlgorithms.getKey();
        String iv = symmetricAlgorithms.getIv();
        String inputText = symmetricAlgorithms.getInputText();
        String result = "";
        switch (algorithm) {
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard aes = AdvancedEncryptionStandard.create();
                result = aes.encryptText(iv, key, inputText, mode, padding);
                break;
            case AlgorithmsConstant.DES:
                DataEncryptionStandard des = DataEncryptionStandard.create();
                result = des.encryptText(iv, key, inputText, mode, padding);
                break;
            case AlgorithmsConstant.BLOWFISH:
                BlowFish blowFish = BlowFish.create();
                result = blowFish.encryptText(iv, key, inputText, mode, padding);
                break;
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDes = TripleDES.create();
                result = tripleDes.encryptText(iv, key, inputText, mode, padding);
                break;
            case AlgorithmsConstant.RC4:
                RC4 rc4 = RC4.create();
                result = rc4.encryptText(key, inputText);
                break;
            case AlgorithmsConstant.SEED, AlgorithmsConstant.IDEA:
                LibSymetricCipher libSymetricCipher = new LibSymetricCipher();
                result = libSymetricCipher.encryptText(iv, key, inputText, algorithm, mode, padding);
                break;
            default:
                break;
        }
        return result;
    }

    public static String decrypt(SymmetricAlgorithms symmetricAlgorithms) throws Exception {
        String algorithm = symmetricAlgorithms.getName();
        String mode = symmetricAlgorithms.getMode();
        String padding = symmetricAlgorithms.getPadding();
        String key = symmetricAlgorithms.getKey();
        String iv = symmetricAlgorithms.getIv();
        String inputText = symmetricAlgorithms.getInputText();
        String result = "";
        switch (algorithm) {
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard aes = AdvancedEncryptionStandard.create();
                result = aes.decryptText(iv, key, inputText, mode, padding);
                break;
            case AlgorithmsConstant.DES:
                DataEncryptionStandard des = DataEncryptionStandard.create();
                result = des.decryptText(iv, key, inputText, mode, padding);
                break;
            case AlgorithmsConstant.BLOWFISH:
                BlowFish blowFish = BlowFish.create();
                result = blowFish.decryptText(iv, key, inputText, mode, padding);
                break;
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDes = TripleDES.create();
                result = tripleDes.decryptText(iv, key, inputText, mode, padding);
                break;
            case AlgorithmsConstant.RC4:
                RC4 rc4 = RC4.create();
                result = rc4.decryptText(key, inputText);
                break;
            case AlgorithmsConstant.SEED, AlgorithmsConstant.IDEA:
                LibSymetricCipher libSymetricCipher = new LibSymetricCipher();
                result = libSymetricCipher.decryptText(iv, key, inputText, algorithm, mode, padding);
                break;
            default:
                break;
        }
        return result;
    }
}
