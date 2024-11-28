package org.midterm.view.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Lớp tiện ích để tùy chỉnh giao diện của nút JButton khi nhấn và thả chuột.
 * <p>
 * Lớp này cung cấp phương thức tĩnh để thiết lập màu sắc tùy chỉnh cho các thành phần JButton,
 * cho phép thay đổi động màu nền và màu chữ của nút trong các sự kiện nhấn và thả chuột.
 */
public class CustomColorButton {
    /**
     * Constructor riêng tư để ngăn tạo đối tượng từ lớp này.
     * <p>
     * Đây là một lớp tiện ích, không nên được khởi tạo.
     */
    private CustomColorButton(){
        throw new AssertionError();
    }

    /**
     * Thiết lập màu sắc tùy chỉnh cho JButton khi nhấn và thả chuột.
     *
     * @param button          JButton cần tùy chỉnh.
     * @param pressedColorHex Mã màu hex được sử dụng khi nút bị nhấn.
     * @param defaultColor    Màu sắc mặc định của nút khi không có sự kiện.
     */
    public static void setButtonPressColor(JButton button, String pressedColorHex, Color defaultColor) {
        // Thiết lập màu nền ban đầu cho nút
        button.setBackground(defaultColor);

        // Thêm bộ lắng nghe sự kiện click chuột
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(Color.decode(pressedColorHex));
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Khôi phục màu nền và màu chữ về trạng thái mặc định khi nhả nút
                button.setBackground(defaultColor);
                button.setForeground(Color.BLACK);

            }
        });
    }

}
