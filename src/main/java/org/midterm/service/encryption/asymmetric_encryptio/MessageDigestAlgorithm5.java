package org.midterm.service.encryption.asymmetric_encryptio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestAlgorithm5 {

    public String hash(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(plainText.getBytes());
        BigInteger number = new BigInteger(1, hashInBytes);
        return number.toString(16);
    }

    public String hashFile(String filePath) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        InputStream is = new BufferedInputStream(new FileInputStream(filePath));
        DigestInputStream dis = new DigestInputStream(is, messageDigest);
        byte[] buffer = new byte[1024];
        int read;
        do {
            read = dis.read(buffer);
        } while (read != -1);
        BigInteger bigInt = new BigInteger(1, messageDigest.digest());
        return bigInt.toString(16);
    }

    public static void main(String[] args) {
        MessageDigestAlgorithm5 messageDigestAlgorithm5 = new MessageDigestAlgorithm5();
        try {
            System.out.println(messageDigestAlgorithm5.hash("Hello!"));
            System.out.println(messageDigestAlgorithm5.hashFile("/home/binnguci/Documents/test-folder/file-text1.txt"));
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }
}
