package org.midterm.service.encryption.classic;

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

//public class Hill {
//    private int keySize;
//
//    public Hill(int keySize) {
//        this.keySize = keySize;
//    }
//
//    public String encrypt(String plainText, int[][] key) {
//        plainText = plainText.toUpperCase().replaceAll("[^A-Z]", "");
//        while (plainText.length() % keySize != 0) {
//            plainText += "X";
//        }
//
//        StringBuilder cipherText = new StringBuilder();
//        for (int i = 0; i < plainText.length(); i += keySize) {
//            int[] block = new int[keySize];
//            for (int j = 0; j < keySize; j++) {
//                block[j] = plainText.charAt(i + j) - 'A';
//            }
//
//            int[] encryptedBlock = multiplyMatrixVector(key, block);
//            for (int value : encryptedBlock) {
//                cipherText.append((char) (value + 'A'));
//            }
//        }
//        return cipherText.toString();
//    }
//
//    private int[] multiplyMatrixVector(int[][] matrix, int[] vector) {
//        int[] result = new int[vector.length];
//        for (int i = 0; i < matrix.length; i++) {
//            for (int j = 0; j < vector.length; j++) {
//                result[i] += matrix[i][j] * vector[j];
//            }
//            result[i] %= 26;
//        }
//        return result;
//    }
//
//    public String decrypt(String cipherText, int[][] key) {
//        int[][] inverseKey = invertKeyMatrix(key);
//        cipherText = cipherText.toUpperCase().replaceAll("[^A-Z]", "");
//
//        StringBuilder plainText = new StringBuilder();
//        for (int i = 0; i < cipherText.length(); i += keySize) {
//            int[] block = new int[keySize];
//            for (int j = 0; j < keySize; j++) {
//                block[j] = cipherText.charAt(i + j) - 'A';
//            }
//
//            int[] decryptedBlock = multiplyMatrixVector(inverseKey, block);
//            for (int value : decryptedBlock) {
//                plainText.append((char) (value + 'A'));
//            }
//        }
//        return plainText.toString();
//    }
//
//    private int[][] invertKeyMatrix(int[][] key) {
//        int determinant = calculateDeterminant(key);
//        determinant = (determinant % 26 + 26) % 26;
//        int determinantInverse = modularInverse(determinant, 26);
//        int[][] adjugate = calculateAdjugate(key);
//        int[][] inverse = new int[keySize][keySize];
//        for (int i = 0; i < keySize; i++) {
//            for (int j = 0; j < keySize; j++) {
//                inverse[i][j] = (adjugate[i][j] * determinantInverse) % 26;
//                if (inverse[i][j] < 0) {
//                    inverse[i][j] += 26;
//                }
//            }
//        }
//        return inverse;
//    }
//
//    private int calculateDeterminant(int[][] matrix) {
//        if (keySize == 2) {
//            return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % 26;
//        } else if (keySize == 3) {
//            int a = matrix[0][0], b = matrix[0][1], c = matrix[0][2];
//            int d = matrix[1][0], e = matrix[1][1], f = matrix[1][2];
//            int g = matrix[2][0], h = matrix[2][1], i = matrix[2][2];
//            return (a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g)) % 26;
//        }
//        throw new IllegalArgumentException("Chỉ hỗ trợ ma trận 2x2 hoặc 3x3.");
//    }
//
//
//    private int[][] calculateAdjugate(int[][] matrix) {
//        int[][] adjugate = new int[keySize][keySize];
//        if (keySize == 2) {
//            adjugate[0][0] = matrix[1][1];
//            adjugate[0][1] = -matrix[0][1];
//            adjugate[1][0] = -matrix[1][0];
//            adjugate[1][1] = matrix[0][0];
//        } else if (keySize == 3) {
//            for (int i = 0; i < keySize; i++) {
//                for (int j = 0; j < keySize; j++) {
//                    int[][] minor = getMinor(matrix, i, j);
//                    adjugate[j][i] = calculateDeterminant(minor) * ((i + j) % 2 == 0 ? 1 : -1);
//                }
//            }
//        } else {
//            throw new IllegalArgumentException("Chỉ hỗ trợ ma trận 2x2 hoặc 3x3.");
//        }
//        return adjugate;
//    }
//
//    private int[][] getMinor(int[][] matrix, int row, int col) {
//        int[][] minor = new int[keySize - 1][keySize - 1];
//        int minorRow = 0, minorCol;
//        for (int i = 0; i < keySize; i++) {
//            if (i == row) continue;
//            minorCol = 0;
//            for (int j = 0; j < keySize; j++) {
//                if (j == col) continue;
//                minor[minorRow][minorCol] = matrix[i][j];
//                minorCol++;
//            }
//            minorRow++;
//        }
//        return minor;
//    }
//
//
//    private int modularInverse(int a, int m) {
//        for (int x = 1; x < m; x++) {
//            if ((a * x) % m == 1) {
//                return x;
//            }
//        }
//        throw new ArithmeticException("Không tìm thấy nghịch đảo modular!");
//    }
//
//}
