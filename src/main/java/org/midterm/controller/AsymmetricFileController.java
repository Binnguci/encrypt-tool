package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.model.AsymmetricAlgorithms;
import org.midterm.service.encryption.asymmetric.RSA;

public class AsymmetricFileController {
    public static String encrypt(AsymmetricAlgorithms asymmetricAlgorithms) throws Exception {
        String algorithm = asymmetricAlgorithms.getAlgorithm();
        String result = "";
        switch (algorithm) {
            case AlgorithmsConstant.RSA:
                RSA rsa = RSA.create();
                result = rsa.encryptFile(asymmetricAlgorithms);
                break;
        }
        return result;
    }

    public static String decrypt(AsymmetricAlgorithms asymmetricAlgorithms) throws Exception {
        String algorithm = asymmetricAlgorithms.getAlgorithm();
        String result = "";
        switch (algorithm) {
            case AlgorithmsConstant.RSA:
                RSA rsa = RSA.create();
                result = rsa.decryptFile(asymmetricAlgorithms);
                break;
        }
        return result;
    }
}
