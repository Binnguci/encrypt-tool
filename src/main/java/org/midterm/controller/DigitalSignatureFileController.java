package org.midterm.controller;

import org.midterm.service.encryption.digitalsignature.DSA;

public class DigitalSignatureFileController {
    public static String signData(String data, String privateKeyBase64, String algorithm) throws Exception {
        DSA dsa = DSA.create();
        return dsa.signData(data, privateKeyBase64, algorithm);
    }

    public static boolean verifySignature(String data, String signatureBase64, String publicKeyBase64, String algorithm) throws Exception {
        DSA dsa = DSA.create();
        return dsa.verifySignature(data, signatureBase64, publicKeyBase64, algorithm);
    }
}
