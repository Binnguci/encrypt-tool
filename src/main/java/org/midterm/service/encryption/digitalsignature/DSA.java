package org.midterm.service.encryption.digitalsignature;

import org.midterm.model.PairKey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Lớp cung cấp các phương thức để tạo chữ ký số (Digital Signature)
 * và xác thực tính toàn vẹn của dữ liệu bằng thuật toán DSA (Digital Signature Algorithm).
 * <p>
 * Chức năng chính:
 * - Tạo cặp khóa (public/private) để sử dụng với chữ ký số.
 * - Ký số dữ liệu hoặc tệp tin bằng khóa riêng tư.
 * - Xác thực chữ ký số bằng khóa công khai.
 * </p>
 */
public class DSA {

    /**
     * Tạo một đối tượng DSA mới.
     *
     * @return đối tượng {@link DSA}.
     */
    public static DSA create() {
        return new DSA();
    }

    /**
     * Tạo cặp khóa DSA.
     *
     * @param keySize kích thước của khóa (ví dụ: 1024, 2048).
     * @return đối tượng {@link PairKey} chứa khóa công khai và khóa riêng tư được mã hóa Base64.
     * @throws Exception nếu xảy ra lỗi trong quá trình tạo khóa.
     */
    public PairKey generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PairKey pairKey = PairKey.create();
        pairKey.setPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        pairKey.setPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return pairKey;
    }

    /**
     * Ký số một chuỗi dữ liệu bằng khóa riêng tư.
     *
     * @param data chuỗi dữ liệu cần ký.
     * @param privateKeyBase64 khóa riêng tư (dạng mã hóa Base64).
     * @param algorithm thuật toán sử dụng để ký (ví dụ: "SHA256withDSA").
     * @return chuỗi chữ ký số (dạng mã hóa Base64).
     * @throws Exception nếu xảy ra lỗi trong quá trình ký.
     */
    public String signData(String data, String privateKeyBase64, String algorithm) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
        PrivateKey privateKey = KeyFactory.getInstance("DSA")
                .generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] digitalSignature = signature.sign();

        return Base64.getEncoder().encodeToString(digitalSignature);
    }

    /**
     * Ký số một tệp tin bằng khóa riêng tư.
     *
     * @param path đường dẫn tới tệp cần ký.
     * @param privateKeyBase64 khóa riêng tư (dạng mã hóa Base64).
     * @param algorithm thuật toán sử dụng để ký (ví dụ: "SHA256withDSA").
     * @return chuỗi chữ ký số (dạng mã hóa Base64).
     * @throws Exception nếu xảy ra lỗi trong quá trình ký.
     */
    public String signFile(String path, String privateKeyBase64, String algorithm) throws Exception {
        File file = new File(path);
        byte[] fileBytes = readFileToBytes(file);
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
        PrivateKey privateKey = KeyFactory.getInstance("DSA")
                .generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(fileBytes);
        byte[] digitalSignature = signature.sign();

        return Base64.getEncoder().encodeToString(digitalSignature);
    }

    /**
     * Xác thực chữ ký số của một chuỗi dữ liệu.
     *
     * @param data chuỗi dữ liệu cần xác thực.
     * @param signatureBase64 chữ ký số (dạng mã hóa Base64).
     * @param publicKeyBase64 khóa công khai (dạng mã hóa Base64).
     * @param algorithm thuật toán sử dụng để xác thực (ví dụ: "SHA256withDSA").
     * @return {@code true} nếu chữ ký hợp lệ, {@code false} nếu không hợp lệ.
     * @throws Exception nếu xảy ra lỗi trong quá trình xác thực.
     */
    public boolean verifySignature(String data, String signatureBase64, String publicKeyBase64, String algorithm) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        PublicKey publicKey = KeyFactory.getInstance("DSA")
                .generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        byte[] digitalSignature = Base64.getDecoder().decode(signatureBase64);

        return signature.verify(digitalSignature);
    }

    /**
     * Xác thực chữ ký số của một tệp tin.
     *
     * @param path đường dẫn tới tệp cần xác thực.
     * @param signatureBase64 chữ ký số (dạng mã hóa Base64).
     * @param publicKeyBase64 khóa công khai (dạng mã hóa Base64).
     * @param algorithm thuật toán sử dụng để xác thực (ví dụ: "SHA256withDSA").
     * @return {@code true} nếu chữ ký hợp lệ, {@code false} nếu không hợp lệ.
     * @throws Exception nếu xảy ra lỗi trong quá trình xác thực.
     */
    public boolean verifyFileSignature(String path, String signatureBase64, String publicKeyBase64, String algorithm) throws Exception {
        File file = new File(path);
        byte[] fileBytes = readFileToBytes(file);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        PublicKey publicKey = KeyFactory.getInstance("DSA")
                .generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(fileBytes);
        byte[] digitalSignature = Base64.getDecoder().decode(signatureBase64);

        return signature.verify(digitalSignature);
    }

    /**
     * Đọc tệp tin và chuyển đổi thành mảng byte.
     *
     * @param file đối tượng {@link File} cần đọc.
     * @return mảng byte chứa nội dung của tệp.
     * @throws IOException nếu xảy ra lỗi trong quá trình đọc tệp.
     */
    private byte[] readFileToBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

}
