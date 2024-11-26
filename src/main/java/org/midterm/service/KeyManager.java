package org.midterm.service;

import org.midterm.model.PairKey;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class KeyManager {
    private KeyManager() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    private static final String FILE_PATH = "keys.properties";

    public static void saveKey(String algorithm, String key) {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream(FILE_PATH)) {
            properties.load(input);
        } catch (IOException ignored) {
            System.err.println("File not found. Creating new file..." + ignored.getMessage());
        }

        properties.setProperty(algorithm, key);

        try (FileOutputStream output = new FileOutputStream(FILE_PATH)) {
            properties.store(output, "Keys for encryption algorithms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveKeys(String algorithm, int keySize, String publicKey, String privateKey) {
        Properties properties = new Properties();
        String publicKeyLabel = algorithm + "_" + keySize + "_public";
        String privateKeyLabel = algorithm + "_" + keySize + "_private";

        try (FileInputStream input = new FileInputStream(FILE_PATH)) {
            properties.load(input);
        } catch (IOException ignored) {
            System.err.println("File not found. Creating new file..." + ignored.getMessage());
        }

        properties.setProperty(publicKeyLabel, publicKey);
        properties.setProperty(privateKeyLabel, privateKey);

        try (FileOutputStream output = new FileOutputStream(FILE_PATH)) {
            properties.store(output, "Keys for encryption algorithms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String loadKey(String algorithm) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(FILE_PATH)) {
            properties.load(input);
            return properties.getProperty(algorithm, "");
        } catch (IOException e) {
            System.err.println("Error loading key: " + e.getMessage());
            return "";
        }
    }

    public static PairKey loadKeys(String algorithm, int keySize) {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream(FILE_PATH)) {
            properties.load(input);

            String keyPrefix = algorithm + "_" + keySize;
            String publicKey = properties.getProperty(keyPrefix + "_public", "");
            String privateKey = properties.getProperty(keyPrefix + "_private", "");

//            if (publicKey.isEmpty() || privateKey.isEmpty()) {
//                throw new IllegalArgumentException("Keys not found for algorithm: " + algorithm + " and keySize: " + keySize);
//            }

            return new PairKey(publicKey, privateKey);

        } catch (IOException e) {
            throw new RuntimeException("Error loading keys: " + e.getMessage(), e);
        }
    }

}
