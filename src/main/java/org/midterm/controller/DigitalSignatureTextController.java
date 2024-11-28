package org.midterm.controller;

import org.midterm.model.PairKey;
import org.midterm.service.encryption.digitalsignature.DSA;

public class DigitalSignatureTextController {
    public static PairKey generatePairKey(int keySize, String algorithms) throws Exception {
        PairKey pairKey = PairKey.create();
        DSA rsa = DSA.create();
        pairKey = rsa.generateKeyPair(keySize);
        return pairKey;
    }

    public static String signData(String data, String privateKeyBase64, String algorithm) throws Exception {
        DSA rsa = DSA.create();
        return rsa.signData(data, privateKeyBase64, algorithm);
    }

    public static boolean verifySignature(String data, String signatureBase64, String publicKeyBase64, String algorithm) throws Exception {
        DSA rsa = DSA.create();
        return rsa.verifySignature(data, signatureBase64, publicKeyBase64, algorithm);
    }
}
