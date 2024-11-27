package org.midterm.factory;

import org.midterm.constant.AlgorithmsConstant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EncryptionConfigFactory {

    private static final Map<String, List<String>> MODES_BY_ALGORITHM = Map.of(
            AlgorithmsConstant.AES, Arrays.asList("ECB", "CBC", "CFB", "OFB", "CTR", "GCM", "KWP", "KW"),
            AlgorithmsConstant.DES, Arrays.asList("CBC", "ECB", "CFB", "OFB", "PCBC", "CTR", "CTS"),
            AlgorithmsConstant.BLOWFISH, Arrays.asList("CBC", "ECB", "CFB", "OFB", "PCBC", "CTR", "CTS"),
            AlgorithmsConstant.TRIPLEDES, Arrays.asList("CBC", "ECB", "CFB", "OFB", "PCBC"),
            AlgorithmsConstant.RC4, List.of("None")
    );

    private static final Map<String, List<String>> MODES_BY_ASYMMETRIC_ALGORITHM = Map.of(
            AlgorithmsConstant.RSA, List.of("ECB")
    );

    private static final Map<String, Map<String, List<String>>> PADDING_BY_ALGORITHM_AND_MODE = Map.of(
            AlgorithmsConstant.AES, Map.of(
                    "CBC", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "ECB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "GCM", List.of("NoPadding"),
                    "OFB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "CFB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "PCBC", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "CTR", List.of("NoPadding"),
                    "CTS", List.of("NoPadding")
            ),
            AlgorithmsConstant.DES, Map.of(
                    "CBC", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "ECB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "CFB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "OFB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "PCBC", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "CTR", List.of("NoPadding"),
                    "CTS", List.of("NoPadding")
            ),
            AlgorithmsConstant.BLOWFISH, Map.of(
                    "ECB", Arrays.asList("PKCS5Padding", "ISO10126Padding"),
                    "CBC", Arrays.asList("PKCS5Padding", "ISO10126Padding"),
                    "CFB", Arrays.asList("PKCS5Padding", "ISO10126Padding"),
                    "OFB", Arrays.asList("PKCS5Padding", "ISO10126Padding"),
                    "PCBC", Arrays.asList("PKCS5Padding", "ISO10126Padding"),
                    "CTR", List.of("NoPadding")
            ),
            AlgorithmsConstant.TRIPLEDES, Map.of(
                    "CBC", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "ECB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "CFB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "OFB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
                    "PCBC", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding")
            ),
            AlgorithmsConstant.RC4, Map.of(
                    "None", Arrays.asList("PKCS1Padding", "OAEPWithSHA-256AndMGF1Padding")
            )
    );

    private static final Map<String, Map<String, List<String>>> PADDING_BY_ASYMMETRIC_ALGORITHM_AND_MODE = Map.of(
            AlgorithmsConstant.RSA, Map.of(
                    "ECB", Arrays.asList("PKCS1Padding", "NoPadding", "OAEPWithMD5AndMGF1Padding", "OAEPWithSHA-256AndMGF1Padding", "OAEPWithSHA-384AndMGF1Padding",
                            "OAEPWithSHA-512AndMGF1Padding")
            )
    );

    private static final Map<String, List<Integer>> KEY_SIZES_BY_ALGORITHM = Map.of(
            AlgorithmsConstant.AES, Arrays.asList(128, 192, 256),
            AlgorithmsConstant.DES, List.of(56),
            AlgorithmsConstant.BLOWFISH, Arrays.asList(32, 64, 128, 192, 256),
            AlgorithmsConstant.TRIPLEDES, Arrays.asList(112, 168),
            AlgorithmsConstant.RC4, List.of(128)
    );

    private static final Map<String, List<Integer>> KEY_SIZES_BY_ASYMMETRIC_ALGORITHM = Map.of(
            AlgorithmsConstant.RSA, Arrays.asList(2048, 3072, 4096)
    );

    private static final Map<String, List<Integer>> KEY_SIZES_BY_DIGITAL_SIGNATURE_ALGORITHM = Map.of(
            AlgorithmsConstant.SHA1WITHDSA, List.of(1024),
            AlgorithmsConstant.SHA256WITHDSA, Arrays.asList(1024, 2048, 3072, 4096),
            AlgorithmsConstant.SHA1WITHRSA, Arrays.asList(1024, 2048, 3072, 4096),
            AlgorithmsConstant.SHA256WITHRSA, Arrays.asList(1024, 2048, 3072, 4096),
            AlgorithmsConstant.SHA512WITHRSA, Arrays.asList(1024, 2048, 3072, 4096)
    );

    private static final Map<String, Integer> IV_SIZE_MAP = Map.of(
            AlgorithmsConstant.AES, 16,
            AlgorithmsConstant.DES, 8,
            AlgorithmsConstant.BLOWFISH, 8,
            AlgorithmsConstant.TRIPLEDES, 8,
            AlgorithmsConstant.RC4, 0
    );

    public static List<String> getModes(String algorithm) {
        return MODES_BY_ALGORITHM.getOrDefault(algorithm, List.of());
    }

    public static List<String> getModesByAsymmetricAlgorithm(String algorithm) {
        return MODES_BY_ASYMMETRIC_ALGORITHM.getOrDefault(algorithm, List.of());
    }

    public static List<String> getPaddings(String algorithm, String mode) {
        Map<String, List<String>> paddingsByMode = PADDING_BY_ALGORITHM_AND_MODE.get(algorithm);
        if (paddingsByMode != null) {
            return paddingsByMode.getOrDefault(mode, List.of());
        }
        return List.of();
    }

    public static List<String> getPaddingsByAsymmetricAlgorithmAndMode(String algorithm, String mode) {
        Map<String, List<String>> paddingsByMode = PADDING_BY_ASYMMETRIC_ALGORITHM_AND_MODE.get(algorithm);
        if (paddingsByMode != null) {
            return paddingsByMode.getOrDefault(mode, List.of());
        }
        return List.of();
    }

    public static List<Integer> getKeySizes(String algorithm) {
        return KEY_SIZES_BY_ALGORITHM.getOrDefault(algorithm, List.of());
    }

    public static List<Integer> getKeySizesByAsymmetricAlgorithm(String algorithm) {
        return KEY_SIZES_BY_ASYMMETRIC_ALGORITHM.getOrDefault(algorithm, List.of());
    }

    public static List<Integer> getKeyByDigitalSignatureAlgorithm(String algorithm) {
        return KEY_SIZES_BY_DIGITAL_SIGNATURE_ALGORITHM.getOrDefault(algorithm, List.of());
    }

    public static int getIvSize(String algorithm) {
        return IV_SIZE_MAP.getOrDefault(algorithm, 0);
    }

}
