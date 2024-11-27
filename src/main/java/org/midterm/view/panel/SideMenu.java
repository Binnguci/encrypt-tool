package org.midterm.view.panel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.midterm.Main;
import org.midterm.constant.OptionConstant;

import javax.swing.*;
import java.awt.*;

/**
 * Lớp {@code SideMenu} đại diện cho bảng điều khiển menu bên trong cửa sổ ứng dụng chính.
 * Nó chứa các nút cho phép người dùng điều hướng giữa các tùy chọn mã hóa khác nhau và các tính năng.
 * Mỗi nút trong menu tương ứng với một tùy chọn cụ thể trong khu vực nội dung chính, và sẽ được cập nhật
 * động dựa trên tùy chọn được chọn.
 * <p>
 * Menu bên sử dụng {@link BoxLayout} cho bố cục theo chiều dọc và cho phép tạo các nút với kích thước
 * và hành vi tùy chỉnh. Khi một nút được nhấn, nó sẽ kích hoạt việc cập nhật nội dung trong cửa sổ chính
 * thông qua phương thức {@link Main#updateContent(String)}.
 * </p>
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SideMenu extends JPanel {

    /** Nút hiện tại được chọn trong menu bên. */
    JButton selectedButton = null;

    /** Khung ứng dụng chính mà menu bên này sẽ cập nhật. */
    final Main mainFrame;

    /**
     * Tạo mới một {@code SideMenu} và khởi tạo các nút tương ứng với các tùy chọn khác nhau.
     * Các nút sẽ được sắp xếp theo chiều dọc trong bảng điều khiển.
     *
     * @param mainFrame Khung ứng dụng chính, được sử dụng để cập nhật nội dung chính.
     */
    public SideMenu(Main mainFrame) {
        this.mainFrame = mainFrame;

        // Cài đặt biên và bố cục cho menu bên
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(211, 211, 211)));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Định nghĩa kích thước nút
        Dimension buttonSize = new Dimension(180, 40);

        // Tạo các nút cho các tùy chọn menu khác nhau
        JButton btnClassicEncrypt = createButton(OptionConstant.CLASSIC_ENCRYPT, buttonSize);
        JButton btnSymmetricEncrypt = createButton(OptionConstant.SYMMETRIC_ENCRYPT, buttonSize);
        JButton btnAsymmetricEncrypt = createButton(OptionConstant.ASYMMETRIC_ENCRYPT, buttonSize);
        JButton btnHash = createButton(OptionConstant.HASH, buttonSize);
        JButton btnDigitalSignature = createButton(OptionConstant.DIGITAL_SIGNATURE, buttonSize);
        JButton btnAbout = createButton(OptionConstant.ABOUT, buttonSize);

        // Thêm các nút với khoảng cách giữa chúng
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(btnClassicEncrypt);
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(btnSymmetricEncrypt);
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(btnAsymmetricEncrypt);
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(btnHash);
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(btnDigitalSignature);
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(btnAbout);
    }

    /**
     * Tạo một nút với văn bản và kích thước đã chỉ định, và thêm một trình lắng nghe sự kiện để xử lý sự kiện nhấn nút.
     * Khi một nút được nhấn, nó thay đổi màu nền của nút đã chọn, cập nhật nội dung chính,
     * và làm nổi bật nút đã chọn.
     *
     * @param text Văn bản hiển thị trên nút.
     * @param size Kích thước ưu tiên của nút.
     * @return Một {@link JButton} đã được cấu hình với văn bản và kích thước đã chỉ định.
     */
    private JButton createButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        button.setPreferredSize(size);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Thêm trình lắng nghe sự kiện để cập nhật nội dung khi nút được nhấn
        button.addActionListener(e -> {
            // Đặt lại màu nền của nút đã chọn trước đó
            if (selectedButton != null) {
                selectedButton.setBackground(Color.WHITE);
                selectedButton.setForeground(Color.BLACK);
            }
            // Làm nổi bật nút đã chọn và cập nhật nội dung
            button.setBackground(Color.decode("#0583F2"));
            button.setForeground(Color.WHITE);
            selectedButton = button;
            mainFrame.updateContent(text);
        });
        return button;
    }
}
