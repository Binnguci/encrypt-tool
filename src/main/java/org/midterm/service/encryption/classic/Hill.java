package org.midterm.service.encryption.classic;

/**
 * Lớp `Hill` triển khai thuật toán mã hóa Hill, sử dụng ma trận khóa để mã hóa và giải mã văn bản.
 * Hỗ trợ ma trận khóa kích thước 2x2 hoặc 3x3.
 */
public class Hill {
    private final int keySize;

    /**
     * Khởi tạo một đối tượng `Hill` với kích thước ma trận khóa.
     *
     * @param keySize kích thước ma trận khóa (chỉ hỗ trợ 2 hoặc 3).
     */
    public Hill(int keySize) {
        this.keySize = keySize;
    }

    /**
     * Mã hóa văn bản gốc bằng ma trận khóa.
     *
     * @param plainText văn bản gốc cần mã hóa.
     * @param key       ma trận khóa kích thước {@code keySize x keySize}.
     * @return văn bản mã hóa.
     * @throws IllegalArgumentException nếu ma trận khóa không hợp lệ.
     */
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

    /**
     * Giải mã văn bản mã hóa bằng ma trận khóa.
     *
     * @param cipherText văn bản mã hóa cần giải mã.
     * @param key        ma trận khóa kích thước {@code keySize x keySize}.
     * @return văn bản gốc đã được giải mã.
     * @throws IllegalArgumentException nếu ma trận khóa không có nghịch đảo.
     */
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

    /**
     * Tính nghịch đảo của ma trận khóa.
     *
     * @param key ma trận khóa kích thước {@code keySize x keySize}.
     * @return ma trận nghịch đảo của khóa.
     * @throws IllegalArgumentException nếu ma trận không khả nghịch.
     */
    private int[][] invertKeyMatrix(int[][] key) {
        int determinant = calculateDeterminant(key);
        if (determinant == 0) {
            throw new IllegalArgumentException("Ma trận không có nghịch đảo!");
        }
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

    /**
     * Tính định thức của ma trận.
     *
     * @param matrix ma trận kích thước {@code keySize x keySize}.
     * @return định thức của ma trận (mod 26).
     * @throws IllegalArgumentException nếu kích thước ma trận không được hỗ trợ.
     */
    private int calculateDeterminant(int[][] matrix) {
        if (keySize == 2) {
            return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % 26;
        } else if (keySize == 3) {
            int a = matrix[0][0], b = matrix[0][1], c = matrix[0][2];
            int d = matrix[1][0], e = matrix[1][1], f = matrix[1][2];
            int g = matrix[2][0], h = matrix[2][1], i = matrix[2][2];
            return (a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g)) % 26;
        }
        throw new IllegalArgumentException("Chỉ hỗ trợ ma trận 2x2 hoặc 3x3.");
    }

    /**
     * Tính ma trận phụ hợp (adjugate) của một ma trận.
     *
     * @param matrix ma trận kích thước {@code keySize x keySize}.
     * @return ma trận phụ hợp.
     * @throws IllegalArgumentException nếu kích thước ma trận không được hỗ trợ.
     */
    private int[][] calculateAdjugate(int[][] matrix) {
        int[][] adjugate = new int[keySize][keySize];
        if (keySize == 2) {
            adjugate[0][0] = matrix[1][1];
            adjugate[0][1] = -matrix[0][1];
            adjugate[1][0] = -matrix[1][0];
            adjugate[1][1] = matrix[0][0];
        } else if (keySize == 3) {
            for (int i = 0; i < keySize; i++) {
                for (int j = 0; j < keySize; j++) {
                    int[][] minor = getMinor(matrix, i, j);
                    adjugate[j][i] = calculateDeterminant(minor) * ((i + j) % 2 == 0 ? 1 : -1);
                }
            }
        } else {
            throw new IllegalArgumentException("Chỉ hỗ trợ ma trận 2x2 hoặc 3x3.");
        }
        return adjugate;
    }

    /**
     * Tính ma trận con (minor) của một ma trận bằng cách loại bỏ một hàng và một cột.
     *
     * @param matrix ma trận gốc.
     * @param row    hàng cần loại bỏ.
     * @param col    cột cần loại bỏ.
     * @return ma trận con.
     */
    private int[][] getMinor(int[][] matrix, int row, int col) {
        int[][] minor = new int[keySize - 1][keySize - 1];
        int minorRow = 0, minorCol;
        for (int i = 0; i < keySize; i++) {
            if (i == row) continue;
            minorCol = 0;
            for (int j = 0; j < keySize; j++) {
                if (j == col) continue;
                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }
            minorRow++;
        }
        return minor;
    }

    /**
     * Tính nghịch đảo modular của một số theo modulo m.
     *
     * @param a số cần tìm nghịch đảo.
     * @param m modulo.
     * @return nghịch đảo modular của a theo modulo m, hoặc -1 nếu không tồn tại.
     */
    private int modularInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // Không tìm thấy nghịch đảo
    }
}
