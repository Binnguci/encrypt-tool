package org.midterm.service;

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

}
