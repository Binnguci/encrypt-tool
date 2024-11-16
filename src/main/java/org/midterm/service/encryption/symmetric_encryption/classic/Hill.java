package org.midterm.service.encryption.symmetric_encryption.classic;

public class Hill {
    private int keySize;

    public Hill(int keySize) {
        this.keySize = keySize;
    }

    public String encrypt(String plainText, int[][] key) {
        plainText = plainText.toUpperCase().replaceAll("[^A-Z]", "");
        while (plainText.length() % keySize != 0) {
            plainText += "X";
        }

        StringBuilder cipherText = new StringBuilder();
        for (int i = 0; i < plainText.length(); i += keySize) {
            int[] block = new int[keySize];
            for (int j = 0; j < keySize; j++) {
                block[j] = plainText.charAt(i + j) - 'A';
            }

            int[] encryptedBlock = multiplyMatrixVector(key, block);
            for (int value : encryptedBlock) {
                cipherText.append((char) (value + 'A'));
            }
        }
        return cipherText.toString();
    }

    private int[] multiplyMatrixVector(int[][] matrix, int[] vector) {
        int[] result = new int[vector.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < vector.length; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
            result[i] %= 26;
        }
        return result;
    }

    public String decrypt(String cipherText, int[][] key) {
        int[][] inverseKey = invertKeyMatrix(key);
        cipherText = cipherText.toUpperCase().replaceAll("[^A-Z]", "");

        StringBuilder plainText = new StringBuilder();
        for (int i = 0; i < cipherText.length(); i += keySize) {
            int[] block = new int[keySize];
            for (int j = 0; j < keySize; j++) {
                block[j] = cipherText.charAt(i + j) - 'A';
            }

            int[] decryptedBlock = multiplyMatrixVector(inverseKey, block);
            for (int value : decryptedBlock) {
                plainText.append((char) (value + 'A'));
            }
        }
        return plainText.toString();
    }

    private int[][] invertKeyMatrix(int[][] key) {
        int determinant = calculateDeterminant(key);
        determinant = (determinant % 26 + 26) % 26;
        int determinantInverse = modularInverse(determinant, 26);
        int[][] adjugate = calculateAdjugate(key);
        int[][] inverse = new int[keySize][keySize];
        for (int i = 0; i < keySize; i++) {
            for (int j = 0; j < keySize; j++) {
                inverse[i][j] = (adjugate[i][j] * determinantInverse) % 26;
                if (inverse[i][j] < 0) {
                    inverse[i][j] += 26;
                }
            }
        }
        return inverse;
    }

    private int calculateDeterminant(int[][] matrix) {
        // Giả sử kích thước là 2x2 hoặc 3x3
        // Viết logic tính định thức tương ứng
        return 0; // Placeholder
    }

    private int[][] calculateAdjugate(int[][] matrix) {
        // Viết logic tính ma trận adjugate
        return new int[keySize][keySize]; // Placeholder
    }

    private int modularInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        throw new ArithmeticException("Không tìm thấy nghịch đảo modular!");
    }

}
