package org.midterm.view;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;

/**
 * Lớp {@code UIManagerSetup} là một lớp tiện ích áp dụng giao diện người dùng (Look and Feel) cụ thể cho ứng dụng
 * bằng cách sử dụng chủ đề FlatMacLightLaf từ thư viện FlatLaf. Lớp này được thiết kế để cấu hình giao diện cho
 * các ứng dụng Swing.
 * <p>
 * Lớp {@code UIManagerSetup} ngăn cản việc tạo đối tượng bằng cách ném ra một {@link AssertionError} nếu có
 * nỗ lực tạo một thể hiện của lớp. Nó cung cấp một phương thức tĩnh để áp dụng chủ đề FlatMacLightLaf và cấu
 * hình màu nền cho các thành phần UI khác nhau như panel, trường văn bản, nút bấm, hộp kết hợp và thanh công cụ.
 * </p>
 */
public class UIManagerSetup {

    /**
     * Hàm tạo riêng tư để ngăn cản việc tạo thể hiện của lớp tiện ích này.
     * Ném ra một {@link AssertionError} để chỉ ra rằng lớp này không thể được khởi tạo.
     */
    private UIManagerSetup() {
        throw new AssertionError("Không thể tạo đối tượng của lớp này");
    }

    /**
     * Áp dụng chủ đề FlatMacLightLaf cho ứng dụng Swing và thiết lập màu nền cho các thành phần UI khác nhau.
     * <p>
     * Các thành phần UI sau được cấu hình với màu nền trắng:
     * <ul>
     *     <li>Panel</li>
     *     <li>Trường văn bản (Text fields)</li>
     *     <li>Nút bấm (Buttons)</li>
     *     <li>Hộp kết hợp (Combo boxes)</li>
     *     <li>Thanh công cụ (Toolbars)</li>
     * </ul>
     * </p>
     * <p>
     * Nếu giao diện không được hỗ trợ, một thông báo lỗi sẽ được in ra luồng lỗi chuẩn.
     * </p>
     */
    public static void applyFlatLafTheme() {
        try {
            // Đặt giao diện FlatMacLightLaf
            UIManager.setLookAndFeel(new FlatMacLightLaf());

            // Đặt màu nền cho các thành phần khác nhau
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ToolBar.background", Color.WHITE);
        } catch (UnsupportedLookAndFeelException e) {
            // In ra thông báo lỗi nếu giao diện không được hỗ trợ
            System.err.println("Giao diện không được hỗ trợ");
        }
    }
}
