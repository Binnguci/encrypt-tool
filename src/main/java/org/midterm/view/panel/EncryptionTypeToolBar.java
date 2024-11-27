package org.midterm.view.panel;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.awt.*;

/**
 * Lớp {@code EncryptionTypeToolBar} là một thanh công cụ tùy chỉnh cho phép người dùng chọn giữa
 * các chế độ mã hóa khác nhau (Văn bản hoặc Tệp). Nó bao gồm một cặp nút radio trong một thanh công cụ
 * và thông báo cho một listener khi chế độ được chọn thay đổi.
 * <p>
 * Thanh công cụ bao gồm hai nút radio: một để chọn chế độ "Văn bản" và một để chọn chế độ "Tệp".
 * Nó cũng có một nhãn yêu cầu người dùng chọn một chế độ.
 * </p>
 * <p>
 * Chế độ được chọn sẽ được thông báo qua {@link ModeChangeListener}, sẽ được gọi khi người dùng
 * chuyển đổi giữa các chế độ.
 * </p>
 */
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EncryptionTypeToolBar extends JToolBar {

    /** Listener được thông báo khi chế độ mã hóa thay đổi. */
    ModeChangeListener modeChangeListener;

    /**
     * Interface lắng nghe sự thay đổi chế độ. Các lớp triển khai giao diện này có thể được sử dụng
     * để xử lý khi người dùng chọn chế độ mã hóa khác.
     */
    public interface ModeChangeListener {
        /**
         * Phương thức này được gọi khi chế độ mã hóa thay đổi.
         *
         * @param mode Chế độ mới được người dùng chọn (có thể là "Văn bản" hoặc "Tệp").
         */
        void onModeChange(String mode);
    }

    /**
     * Tạo một {@code EncryptionTypeToolBar} mới và khởi tạo thanh công cụ với các nút radio
     * để chọn giữa các chế độ mã hóa "Văn bản" và "Tệp". Một nhãn được thêm vào để yêu cầu người dùng
     * chọn chế độ. Kích thước và bố cục ưu tiên của thanh công cụ cũng được thiết lập trong quá trình khởi tạo.
     */
    public EncryptionTypeToolBar() {
        // Đặt thanh công cụ không thể nổi và cấu hình kích thước ưu tiên
        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        setPreferredSize(new Dimension(getPreferredSize().width, 50));

        // Tạo các nút radio để chọn chế độ mã hóa
        JRadioButton textRadioButton = new JRadioButton("Text", true);
        JRadioButton fileRadioButton = new JRadioButton("File");

        // Nhóm các nút radio để chỉ có một nút có thể được chọn tại một thời điểm
        ButtonGroup group = new ButtonGroup();
        group.add(textRadioButton);
        group.add(fileRadioButton);

        // Thêm nhãn và các nút radio vào thanh công cụ
        JLabel label = new JLabel("Choose: ");
        add(label);
        add(textRadioButton);
        add(fileRadioButton);

        // Thêm action listener cho các nút radio để thông báo thay đổi chế độ
        textRadioButton.addActionListener(e -> notifyModeChange("Text"));
        fileRadioButton.addActionListener(e -> notifyModeChange("File"));
    }

    /**
     * Thông báo cho {@link ModeChangeListener} về sự thay đổi chế độ mã hóa.
     *
     * @param mode Chế độ mới được chọn ("Văn bản" hoặc "Tệp").
     */
    private void notifyModeChange(String mode) {
        if (modeChangeListener != null) {
            modeChangeListener.onModeChange(mode);
        }
    }
}
