package org.midterm.service.encryption.classic;

import org.midterm.constant.StringConstant;

/**
 * Lớp {@code ShiftCipher} cung cấp các phương thức mã hóa và giải mã
 * sử dụng phương pháp mã hóa Caesar (còn gọi là mã hóa dịch chuyển), hỗ trợ cả
 * tiếng Anh và tiếng Việt.
 * <p>
 * Lớp này bao gồm các phương thức để mã hóa và giải mã văn bản sử dụng một giá trị dịch chuyển (khóa).
 * Phương pháp mã hóa dịch chuyển hoạt động bằng cách dịch chuyển từng ký tự trong văn bản đầu vào
 * theo một số lượng xác định trong bảng chữ cái (cả tiếng Anh và tiếng Việt).
 * </p>
 */
public class ShiftCipher {

    /**
     * Tạo một thể hiện mới của lớp {@code ShiftCipher}.
     *
     * @return một thể hiện mới của {@code ShiftCipher}
     */
    public static ShiftCipher create() {
        return new ShiftCipher();
    }

    /**
     * Mã hóa văn bản thuần túy đã cho sử dụng ngôn ngữ và khóa dịch chuyển được chỉ định.
     * <p>
     * Phương thức này kiểm tra ngôn ngữ được chỉ định và chuyển giao quá trình mã hóa
     * cho phương thức thích hợp cho tiếng Anh hoặc tiếng Việt. Nếu ngôn ngữ không hợp lệ,
     * phương thức sẽ trả về thông báo lỗi được định nghĩa trong {@link StringConstant#INPUT_INVALID}.
     * </p>
     *
     * @param plainText văn bản thuần túy cần mã hóa
     * @param language  ngôn ngữ của văn bản ("English" hoặc "Vietnamese")
     * @param key       khóa dịch chuyển (nên là một số nguyên)
     * @return văn bản đã mã hóa
     */
    public String encrypt(String plainText, String language, Object key) {
        if (key instanceof Integer) {
            int shift = (int) key;
            if (language.equalsIgnoreCase(StringConstant.LANGUAGE_ENGLISH)) {
                return encryptEnglish(plainText, shift);
            } else if (language.equalsIgnoreCase(StringConstant.LANGUAGE_VIETNAMESE)) {
                return encryptVietnamese(plainText, shift);
            } else {
                return StringConstant.INPUT_INVALID;
            }
        }
        return null;
    }

    /**
     * Giải mã văn bản mã hóa đã cho sử dụng ngôn ngữ và khóa dịch chuyển được chỉ định.
     * <p>
     * Tương tự như mã hóa, phương thức này kiểm tra ngôn ngữ và chuyển giao quá trình giải mã
     * cho phương thức thích hợp cho tiếng Anh hoặc tiếng Việt. Nếu ngôn ngữ không hợp lệ,
     * phương thức sẽ trả lại văn bản mã hóa mà không thay đổi.
     * </p>
     *
     * @param cipherText văn bản mã hóa cần giải mã
     * @param language   ngôn ngữ của văn bản ("English" hoặc "Vietnamese")
     * @param key        khóa dịch chuyển (nên là một số nguyên)
     * @return văn bản đã giải mã
     */
    public String decrypt(String cipherText, String language, Object key) {
        if (key instanceof Integer) {
            int shift = (int) key;
            if (language.equalsIgnoreCase(StringConstant.LANGUAGE_ENGLISH)) {
                return decryptEnglish(cipherText, shift);
            } else if (language.equalsIgnoreCase(StringConstant.LANGUAGE_VIETNAMESE)) {
                return decryptVietnamese(cipherText, shift);
            } else {
                return cipherText;
            }
        }
        return null;
    }

    /**
     * Mã hóa văn bản tiếng Anh sử dụng phương pháp mã hóa dịch chuyển với giá trị dịch chuyển được chỉ định.
     * <p>
     * Phương thức này lặp qua các ký tự của văn bản đầu vào và dịch chuyển mỗi ký tự chữ cái
     * theo giá trị dịch chuyển đã chỉ định, quấn lại bảng chữ cái nếu cần thiết.
     * Các chữ số cũng được dịch chuyển tương ứng. Các ký tự không phải chữ cái và không phải chữ số
     * sẽ không thay đổi.
     * </p>
     *
     * @param plainText văn bản thuần túy cần mã hóa
     * @param shift     số vị trí để dịch chuyển mỗi ký tự
     * @return văn bản đã mã hóa
     */
    public String encryptEnglish(String plainText, int shift) {
        StringBuilder cipherText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) (base + (c - base + shift) % 26);
            } else if (Character.isDigit(c)) {
                c = (char) ('0' + (c - '0' + shift) % 10);
            }
            cipherText.append(c);
        }
        return cipherText.toString();
    }

    /**
     * Mã hóa văn bản tiếng Việt sử dụng phương pháp mã hóa dịch chuyển với giá trị dịch chuyển được chỉ định.
     * <p>
     * Phương thức này lặp qua các ký tự của văn bản đầu vào và dịch chuyển mỗi ký tự trong
     * bảng chữ cái tiếng Việt (được định nghĩa trong {@link StringConstant#VIETNAMESE_ALPHABET})
     * theo giá trị dịch chuyển đã chỉ định. Các chữ số cũng được dịch chuyển tương ứng.
     * Các ký tự không phải chữ cái sẽ không thay đổi.
     * </p>
     *
     * @param plaintext văn bản thuần túy cần mã hóa
     * @param shift     số vị trí để dịch chuyển mỗi ký tự
     * @return văn bản đã mã hóa
     */
    public String encryptVietnamese(String plaintext, int shift) {
        StringBuilder cipherText = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            int index = StringConstant.VIETNAMESE_ALPHABET.indexOf(c);
            if (index != -1) {
                index = (index + shift) % StringConstant.VIETNAMESE_ALPHABET.length();
                cipherText.append(StringConstant.VIETNAMESE_ALPHABET.charAt(index));
            } else if (Character.isDigit(c)) {
                c = (char) ('0' + (c - '0' + shift) % 10);
                cipherText.append(c);
            } else {
                cipherText.append(c);
            }
        }
        return cipherText.toString();
    }

    /**
     * Giải mã văn bản tiếng Anh sử dụng phương pháp mã hóa dịch chuyển với giá trị dịch chuyển được chỉ định.
     * <p>
     * Phương thức này giải mã văn bản mã hóa bằng cách áp dụng dịch chuyển ngược (tức là 26 - shift)
     * và dịch chuyển mỗi ký tự chữ cái ngược lại theo số vị trí đó. Các chữ số cũng được đảo ngược tương ứng.
     * Các ký tự không phải chữ cái sẽ không thay đổi.
     * </p>
     *
     * @param cipherText văn bản mã hóa cần giải mã
     * @param shift      số vị trí để dịch chuyển mỗi ký tự ngược lại
     * @return văn bản đã giải mã
     */
    public String decryptEnglish(String cipherText, int shift) {
        return encryptEnglish(cipherText, 26 - shift);
    }

    /**
     * Giải mã văn bản tiếng Việt sử dụng phương pháp mã hóa dịch chuyển với giá trị dịch chuyển được chỉ định.
     * <p>
     * Tương tự như giải mã tiếng Anh, phương thức này giải mã văn bản mã hóa tiếng Việt bằng cách áp dụng
     * dịch chuyển ngược trong bảng chữ cái tiếng Việt (tức là dịch chuyển theo chiều dài của bảng chữ cái trừ đi shift).
     * Các chữ số cũng được đảo ngược tương ứng. Các ký tự không phải chữ cái sẽ không thay đổi.
     * </p>
     *
     * @param cipherText văn bản mã hóa cần giải mã
     * @param shift      số vị trí để dịch chuyển mỗi ký tự ngược lại
     * @return văn bản đã giải mã
     */
    public String decryptVietnamese(String cipherText, int shift) {
        return encryptVietnamese(cipherText, StringConstant.VIETNAMESE_ALPHABET.length() - shift);
    }
}
