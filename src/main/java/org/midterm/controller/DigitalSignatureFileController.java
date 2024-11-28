package org.midterm.controller;

import org.midterm.service.encryption.digitalsignature.DSA;

public class DigitalSignatureFileController {
    public static String signFile(String path, String privateKeyBase64, String algorithm) throws Exception {
        DSA dsa = DSA.create();
        return dsa.signFile(path, privateKeyBase64, algorithm);
    }

    public static boolean verifySignatureFile(String file, String signatureBase64, String publicKeyBase64, String algorithm) throws Exception {
        DSA dsa = DSA.create();
        return dsa.verifyFileSignature(file, signatureBase64, publicKeyBase64, algorithm);
    }
}
