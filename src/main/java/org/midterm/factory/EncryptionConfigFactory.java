package org.midterm.factory;

import org.midterm.constant.AlgorithmsConstant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EncryptionConfigFactory {
    private static final Map<String, List<String>> MODES_BY_ALGORITHM = Map.of(
            AlgorithmsConstant.AES, Arrays.asList("GCM", "ECB", "KWP", "CBC", "OFB", "KW", "CFB"),
            AlgorithmsConstant.DES, Arrays.asList("CBC", "ECB", "CFB", "OFB", "PCBC", "CTR", "CTS"),
            AlgorithmsConstant.BLOWFISH, Arrays.asList("CBC", "ECB", "CFB", "OFB", "PCBC", "CTR", "CTS"),
            AlgorithmsConstant.TRIPLEDES, Arrays.asList("CBC", "ECB", "CFB", "OFB", "PCBC"),
            AlgorithmsConstant.RC4, List.of("None"),
            AlgorithmsConstant.SHIFT, List.of("None"),
            AlgorithmsConstant.SUBSTITUTION, List.of("None"),
            AlgorithmsConstant.VIGENERE, List.of("None"),
            AlgorithmsConstant.HILL, List.of("None"),
            AlgorithmsConstant.AFFINE, List.of("None")
    );

    private static final Map<String, List<String>> PADDING_BY_MODE = Map.of(
            "CBC", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
            "ECB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
            "GCM", List.of("NoPadding"),
            "KWP", List.of("NoPadding"),
            "OFB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
            "CFB", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
            "PCBC", Arrays.asList("PKCS5Padding", "NoPadding", "ISO10126Padding"),
            "CTR", List.of("NoPadding"),
            "CTS", List.of("NoPadding"),
            "None", Arrays.asList("PKCS1Padding", "OAEPWithSHA-256AndMGF1Padding")
    );

    private static final Map<String, List<Integer>> KEY_SIZES_BY_ALGORITHM = Map.of(
            "AES", Arrays.asList(128, 192, 256),
            "DES", Arrays.asList(56),
            "Blowfish", Arrays.asList(32, 64, 128, 192, 256),
            "3DES", Arrays.asList(112, 168),
            "RC2", Arrays.asList(40, 56, 64, 128, 192, 256),
            "IDEA", Arrays.asList(128),
            "RC4", Arrays.asList(128)
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

    public static List<String> getPaddings(String mode) {
        return PADDING_BY_MODE.getOrDefault(mode, List.of());
    }

    public static List<Integer> getKeySizes(String algorithm) {
        return KEY_SIZES_BY_ALGORITHM.getOrDefault(algorithm, List.of());
    }

    public static int getIvSize(String algorithm) {
        return IV_SIZE_MAP.getOrDefault(algorithm, 0);
    }

}
