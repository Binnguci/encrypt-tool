package org.midterm.controller;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.model.AsymmetricAlgorithms;
import org.midterm.model.PairKey;
import org.midterm.service.encryption.asymmetric.RSA;

public class AsymmetricTextController {
    public static PairKey generatePairKey(int keySize, String algorithms) throws Exception {
        PairKey pairKey = PairKey.create();
        switch (algorithms) {
            case AlgorithmsConstant.RSA:
                RSA rsa = RSA.create();
                pairKey = rsa.generateKeyPair(keySize);
                break;
        }
        return pairKey;
    }

    public static String encypt(AsymmetricAlgorithms asymmetricAlgorithms) throws Exception {
        String algorithm = asymmetricAlgorithms.getAlgorithm();
        String result = "";
        switch (algorithm) {
            case AlgorithmsConstant.RSA:
                RSA rsa = RSA.create();
                result = rsa.encryptText(asymmetricAlgorithms);
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
                result = rsa.decryptText(asymmetricAlgorithms);
                break;
        }
        return result;
    }
}
