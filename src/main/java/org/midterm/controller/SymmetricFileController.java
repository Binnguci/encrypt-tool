package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.model.SymmetricAlgorithms;
import org.midterm.service.encryption.symmetric_encryption.normal.*;

public class SymmetricFileController {
    public static String encrypt(SymmetricAlgorithms symmetricAlgorithms) throws Exception {
        String algorithm = symmetricAlgorithms.getName();
        String mode = symmetricAlgorithms.getMode();
        String padding = symmetricAlgorithms.getPadding();
        String key = symmetricAlgorithms.getKey();
        String iv = symmetricAlgorithms.getIv();
        String filePath = symmetricAlgorithms.getFilePath();
        String result = "";
        switch (algorithm) {
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard aes = AdvancedEncryptionStandard.create();
                result = aes.encryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.DES:
                DataEncryptionStandard des = DataEncryptionStandard.create();
                result = des.encryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.BLOWFISH:
                BlowFish blowFish = BlowFish.create();
                result = blowFish.encryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDes = TripleDES.create();
                result = tripleDes.encryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.RC4:
                RC4 rc4 = RC4.create();
                result = rc4.encryptFile(key, filePath);
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
        String filePath = symmetricAlgorithms.getFilePath();
        String result = "";
        switch (algorithm) {
            case AlgorithmsConstant.AES:
                AdvancedEncryptionStandard aes = AdvancedEncryptionStandard.create();
                result = aes.decryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.DES:
                DataEncryptionStandard des = DataEncryptionStandard.create();
                result = des.decryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.BLOWFISH:
                BlowFish blowFish = BlowFish.create();
                result = blowFish.decryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.TRIPLEDES:
                TripleDES tripleDes = TripleDES.create();
                result = tripleDes.decryptFile(iv, key, filePath, mode, padding);
                break;
            case AlgorithmsConstant.RC4:
                RC4 rc4 = RC4.create();
                result = rc4.decryptFile(key, filePath);
                break;
            default:
                break;
        }
        return result;
    }
}
