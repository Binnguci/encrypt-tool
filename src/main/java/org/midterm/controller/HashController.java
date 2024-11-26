package org.midterm.controller;

import org.midterm.service.encryption.Hash;

public class HashController {
    public static String hash(String input, String algorithm) {
        Hash hash = new Hash();
        return hash.hash(input, algorithm);
    }

    public static String hashFile(String path, String algorithm) {
        Hash hash = new Hash();
        return hash.hashFile(path, algorithm);
    }
}
