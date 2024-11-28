package org.midterm;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.midterm.constant.OptionConstant;
import org.midterm.view.UIManagerSetup;
import org.midterm.view.page.*;
import org.midterm.view.panel.SideMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp {@code Main} đại diện cho cửa sổ chính của ứng dụng mã hóa. Cửa sổ này bao gồm một thanh tiêu đề tùy chỉnh,
 * menu bên và khu vực nội dung chính, nơi các panel sẽ được chuyển đổi và hiển thị tùy theo lựa chọn của người dùng.
 * Lớp này cũng quản lý các hành động như thu nhỏ, phóng to và đóng cửa sổ, cũng như xử lý việc cập nhật nội dung
 * của cửa sổ chính khi người dùng chọn các tùy chọn khác nhau từ menu bên.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Main extends JFrame {

    /** Panel chứa nội dung chính của ứng dụng. */
    final JPanel mainContentPanel;

    /** Bản đồ chứa các panel tương ứng với từng tùy chọn trong ứng dụng. */
    final Map<String, JPanel> panelMap = new HashMap<>();

    /** Tọa độ X và Y của chuột khi kéo cửa sổ. */
    int mouseX = 0, mouseY = 0;

    /**
     * Khởi tạo cửa sổ chính với các thành phần như thanh tiêu đề tùy chỉnh và menu bên.
     * Sau khi khởi tạo, cửa sổ sẽ hiển thị nội dung mặc định.
     */
    public Main() {
        setUndecorated(true); // Ẩn thanh tiêu đề mặc định
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Thêm thanh tiêu đề tùy chỉnh vào cửa sổ
        add(createCustomTitleBar(), BorderLayout.NORTH);

        // Tạo menu bên
        SideMenu sideMenu = new SideMenu(this);
        sideMenu.setPreferredSize(new Dimension(180, 0));
        sideMenu.setBackground(Color.WHITE);
        add(sideMenu, BorderLayout.WEST);

        // Tạo panel nội dung chính và thêm các panel vào
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new CardLayout());

        for (Map.Entry<String, JPanel> entry : panelMap.entrySet()) {
            mainContentPanel.add(entry.getValue(), entry.getKey());
        }

        add(mainContentPanel, BorderLayout.CENTER);

        // Khởi tạo bản đồ các panel
        initializePanelMap();
        // Thêm panel mặc định vào
        mainContentPanel.add(new MainContentPanel(), OptionConstant.DEFAULT);

        setVisible(true);
    }

    /**
     * Tạo thanh tiêu đề tùy chỉnh cho cửa sổ với các nút chức năng như thu nhỏ, phóng to và đóng cửa sổ.
     *
     * @return {@code JPanel} đại diện cho thanh tiêu đề tùy chỉnh.
     */
    private JPanel createCustomTitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setLayout(new BorderLayout());
        titleBar.setBackground(Color.decode("#F4F4F4"));
        titleBar.setPreferredSize(new Dimension(getWidth(), 30));

        // Tạo các nút chức năng
        JButton closeButton = new JButton();
        closeButton.setPreferredSize(new Dimension(15, 15));
        closeButton.setBackground(Color.RED);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setOpaque(true);
        closeButton.addActionListener(e -> System.exit(0));

        JButton minimizeButton = new JButton();
        minimizeButton.setPreferredSize(new Dimension(15, 15));
        minimizeButton.setBackground(Color.ORANGE);
        minimizeButton.setFocusPainted(false);
        minimizeButton.setBorderPainted(false);
        minimizeButton.setOpaque(true);
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton maximizeButton = getJButton();

        JPanel macButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 7));
        macButtonPanel.setOpaque(false);
        macButtonPanel.add(closeButton);
        macButtonPanel.add(minimizeButton);
        macButtonPanel.add(maximizeButton);

        JLabel titleLabel = new JLabel("Cryptography Tool", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.BLACK);

        // Xử lý kéo cửa sổ
        titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });

        // Thêm các thành phần vào thanh tiêu đề
        titleBar.add(macButtonPanel, BorderLayout.WEST);
        titleBar.add(titleLabel, BorderLayout.CENTER);

        return titleBar;
    }

    /**
     * Tạo nút tối đa hóa cửa sổ với màu nền và hành động tùy chỉnh.
     *
     * @return {@code JButton} đại diện cho nút tối đa hóa cửa sổ.
     */
    private JButton getJButton() {
        JButton maximizeButton = new JButton();
        maximizeButton.setPreferredSize(new Dimension(15, 15));
        maximizeButton.setBackground(Color.GREEN);
        maximizeButton.setFocusPainted(false);
        maximizeButton.setBorderPainted(false);
        maximizeButton.setOpaque(true);
        maximizeButton.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        return maximizeButton;
    }

    /**
     * Cập nhật nội dung chính của cửa sổ khi người dùng chọn một tùy chọn.
     *
     * @param option Tùy chọn người dùng chọn để hiển thị nội dung tương ứng.
     */
    public void updateContent(String option) {
        JPanel panel = panelMap.getOrDefault(option, panelMap.get(OptionConstant.DEFAULT));
        mainContentPanel.add(panel, option);

        CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();
        cardLayout.show(mainContentPanel, option);
        revalidate();
        repaint();
    }

    /**
     * Khởi tạo bản đồ các panel cho các tùy chọn.
     */
    private void initializePanelMap() {
        panelMap.put(OptionConstant.CLASSIC_ENCRYPT, new ClassicEncryptPanel());
        panelMap.put(OptionConstant.SYMMETRIC_ENCRYPT, new SymmetricPanel());
        panelMap.put(OptionConstant.HASH, new HashPanel());
        panelMap.put(OptionConstant.ASYMMETRIC_ENCRYPT, new AsymmetricPanel());
        panelMap.put(OptionConstant.DIGITAL_SIGNATURE, new DigitalSignaturePanel());
        panelMap.put(OptionConstant.ABOUT, new AboutPanel());
        panelMap.put(OptionConstant.DEFAULT, new MainContentPanel());
    }

    /**
     * Phương thức {@code main} khởi tạo giao diện người dùng và bắt đầu ứng dụng.
     *
     * @param args Các đối số dòng lệnh (không sử dụng trong trường hợp này).
     */
    public static void main(String[] args) {
        UIManagerSetup.applyFlatLafTheme();
        SwingUtilities.invokeLater(Main::new);
    }
}
