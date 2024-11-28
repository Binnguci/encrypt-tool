package org.midterm.service.encryption.symmetric;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Lớp RC4 cung cấp các chức năng mã hóa và giải mã văn bản, cũng như tệp tin bằng thuật toán RC4.
 */
public class RC4 {
    /**
     * Tạo một khóa RC4 ngẫu nhiên và trả về chuỗi mã hóa Base64 của khóa.
     *
     * @return Chuỗi Base64 của khóa RC4.
     * @throws Exception Nếu xảy ra lỗi trong quá trình tạo khóa.
     */
    public static String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("RC4");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }


    /**
     * Chuyển đổi chuỗi Base64 sang đối tượng SecretKey dùng cho RC4.
     *
     * @param base64Key Chuỗi khóa Base64.
     * @return Đối tượng SecretKey.
     */
    private static SecretKey convertBase64ToKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "RC4");
    }

    /**
     * Mã hóa văn bản bằng thuật toán RC4.
     *
     * @param secretKeyBase64 Chuỗi Base64 của khóa bí mật.
     * @param plainText       Văn bản gốc cần mã hóa.
     * @return Văn bản đã mã hóa dưới dạng chuỗi Base64.
     * @throws NoSuchPaddingException        Nếu thuật toán padding không tồn tại.
     * @throws NoSuchAlgorithmException      Nếu thuật toán RC4 không tồn tại.
     * @throws InvalidKeyException           Nếu khóa không hợp lệ.
     * @throws BadPaddingException           Nếu padding không hợp lệ.
     * @throws IllegalBlockSizeException     Nếu kích thước block không hợp lệ.
     */
    public String encryptText(String secretKeyBase64, String plainText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Giải mã văn bản đã mã hóa bằng thuật toán RC4.
     *
     * @param secretKeyBase64   Chuỗi Base64 của khóa bí mật.
     * @param cipherTextBase64  Văn bản đã mã hóa dưới dạng chuỗi Base64.
     * @return Văn bản gốc đã giải mã.
     * @throws NoSuchPaddingException        Nếu thuật toán padding không tồn tại.
     * @throws NoSuchAlgorithmException      Nếu thuật toán RC4 không tồn tại.
     * @throws InvalidKeyException           Nếu khóa không hợp lệ.
     * @throws BadPaddingException           Nếu padding không hợp lệ.
     * @throws IllegalBlockSizeException     Nếu kích thước block không hợp lệ.
     */
    public String decryptText(String secretKeyBase64, String cipherTextBase64)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        if (secretKeyBase64 == null || secretKeyBase64.isEmpty()) {
            throw new IllegalArgumentException("Secret key không được rỗng hoặc null.");
        }

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] cipherTextBytes = Base64.getDecoder().decode(cipherTextBase64);
        byte[] plainTextBytes = cipher.doFinal(cipherTextBytes);
        return new String(plainTextBytes);
    }

    /**
     * Mã hóa một tệp tin bằng thuật toán RC4.
     *
     * @param secretKeyBase64 Chuỗi Base64 của khóa bí mật.
     * @param inputFile       Đường dẫn đến tệp tin cần mã hóa.
     * @return Đường dẫn đến tệp tin đã mã hóa.
     * @throws NoSuchPaddingException        Nếu thuật toán padding không tồn tại.
     * @throws NoSuchAlgorithmException      Nếu thuật toán RC4 không tồn tại.
     * @throws InvalidKeyException           Nếu khóa không hợp lệ.
     * @throws IOException                   Nếu xảy ra lỗi trong việc đọc/ghi tệp.
     * @throws IllegalBlockSizeException     Nếu kích thước block không hợp lệ.
     * @throws BadPaddingException           Nếu padding không hợp lệ.
     */
    public String encryptFile(String secretKeyBase64, String inputFile)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {

        File inputFileObj = new File(inputFile);
        System.out.println(inputFileObj);
        String parentPath = inputFileObj.getParent();
        String fileName = inputFileObj.getName();
        String outputFile;
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            outputFile = parentPath + File.separator + fileName.substring(0, dotIndex) + "_encrypt" + fileName.substring(dotIndex);
        } else {
            outputFile = parentPath + File.separator + fileName + "_encrypt";
        }

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
             OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            BlowFish.writeFile(cipher, is, os);
        }
        return outputFile;
    }

    /**
     * Giải mã một tệp tin đã mã hóa bằng thuật toán RC4.
     *
     * @param secretKeyBase64 Chuỗi Base64 của khóa bí mật.
     * @param inputFile       Đường dẫn đến tệp tin đã mã hóa.
     * @return Đường dẫn đến tệp tin đã giải mã.
     * @throws NoSuchPaddingException        Nếu thuật toán padding không tồn tại.
     * @throws NoSuchAlgorithmException      Nếu thuật toán RC4 không tồn tại.
     * @throws InvalidKeyException           Nếu khóa không hợp lệ.
     * @throws IOException                   Nếu xảy ra lỗi trong việc đọc/ghi tệp.
     * @throws IllegalBlockSizeException     Nếu kích thước block không hợp lệ.
     * @throws BadPaddingException           Nếu padding không hợp lệ.
     */
    public String decryptFile(String secretKeyBase64, String inputFile)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IOException, IllegalBlockSizeException, BadPaddingException {

        File inputFileObj = new File(inputFile);
        String parentPath = inputFileObj.getParent();
        String fileName = inputFileObj.getName();
        String outputFile;

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            outputFile = parentPath + File.separator + fileName.substring(0, dotIndex) + "_decrypt" + fileName.substring(dotIndex);
        } else {
            outputFile = parentPath + File.separator + fileName + "_decrypt";
        }

        SecretKey secretKey = convertBase64ToKey(secretKeyBase64);
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        try (InputStream is = new BufferedInputStream(new FileInputStream(inputFile));
             OutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))) {

            BlowFish.writeFile(cipher, is, os);
        }
        return outputFile;
    }

    /**
     * Tạo một đối tượng RC4 mới.
     *
     * @return Đối tượng RC4.
     */
    public static RC4 create() {
        return new RC4();
    }
}
