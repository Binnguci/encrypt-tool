package org.midterm.service.encryption.asymmetric;

import org.midterm.model.AsymmetricAlgorithms;
import org.midterm.model.PairKey;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Lớp cung cấp các phương thức hỗ trợ mã hóa và giải mã bằng thuật toán RSA.
 * <p>
 * Chức năng chính bao gồm:
 * - Tạo cặp khóa RSA (public/private).
 * - Mã hóa văn bản sử dụng khóa công khai.
 * - Giải mã văn bản sử dụng khóa riêng tư.
 * </p>
 */
public class RSA {

    /**
     * Tạo một đối tượng RSA mới.
     *
     * @return đối tượng {@link RSA}.
     */
    public static RSA create() {
        return new RSA();
    }

    /**
     * Tạo cặp khóa RSA.
     *
     * @param keySize kích thước của khóa RSA (ví dụ: 2048, 4096).
     * @return đối tượng {@link PairKey} chứa khóa công khai và khóa riêng tư được mã hóa Base64.
     * @throws Exception nếu xảy ra lỗi trong quá trình tạo khóa.
     */
    public PairKey generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PairKey pairKey = PairKey.create();
        pairKey.setPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        pairKey.setPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return pairKey;
    }

    /**
     * Mã hóa văn bản bằng thuật toán RSA.
     *
     * @param config cấu hình mã hóa, bao gồm thuật toán, chế độ, padding, khóa công khai và văn bản đầu vào.
     * @return chuỗi văn bản đã mã hóa dưới dạng Base64.
     * @throws Exception nếu xảy ra lỗi trong quá trình mã hóa.
     */
    public String encryptText(AsymmetricAlgorithms config) throws Exception {
        Cipher cipher = Cipher.getInstance(config.getAlgorithm() + "/" + config.getMode() + "/" + config.getPadding());
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(config));
        byte[] encryptedBytes = cipher.doFinal(config.getInputText().getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Giải mã văn bản bằng thuật toán RSA.
     *
     * @param config cấu hình giải mã, bao gồm thuật toán, chế độ, padding, khóa riêng tư và văn bản đầu vào (dạng Base64).
     * @return chuỗi văn bản đã giải mã.
     * @throws Exception nếu xảy ra lỗi trong quá trình giải mã.
     */
    public String decryptText(AsymmetricAlgorithms config) throws Exception {
        Cipher cipher = Cipher.getInstance(config.getAlgorithm() + "/" + config.getMode() + "/" + config.getPadding());
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(config));
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(config.getInputText()));
        return new String(decryptedBytes);
    }

    /**
     * Lấy đối tượng {@link PublicKey} từ chuỗi khóa công khai.
     *
     * @param config cấu hình chứa thông tin thuật toán và khóa công khai (dạng Base64).
     * @return đối tượng {@link PublicKey}.
     * @throws Exception nếu xảy ra lỗi trong quá trình khôi phục khóa.
     */
    private PublicKey getPublicKey(AsymmetricAlgorithms config) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(config.getPublicKey());
        KeyFactory keyFactory = KeyFactory.getInstance(config.getAlgorithm());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * Lấy đối tượng {@link PrivateKey} từ chuỗi khóa riêng tư.
     *
     * @param config cấu hình chứa thông tin thuật toán và khóa riêng tư (dạng Base64).
     * @return đối tượng {@link PrivateKey}.
     * @throws Exception nếu xảy ra lỗi trong quá trình khôi phục khóa.
     */
    private PrivateKey getPrivateKey(AsymmetricAlgorithms config) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(config.getPrivateKey());
        KeyFactory keyFactory = KeyFactory.getInstance(config.getAlgorithm());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(keySpec);
    }
}

