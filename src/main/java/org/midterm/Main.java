package org.midterm;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.midterm.constant.OptionConstant;
import org.midterm.view.panel.SideMenu;
import org.midterm.view.page.*;

import javax.swing.*;
import java.awt.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Main extends JFrame {
    JPanel mainContentPanel;

    public Main() {
        setTitle("Cryptography Tool");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        SideMenu sideMenu = new SideMenu(this);
        sideMenu.setPreferredSize(new Dimension(180, 0));
        sideMenu.setBackground(Color.WHITE);
        add(sideMenu, BorderLayout.WEST);

        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new CardLayout());
        add(mainContentPanel, BorderLayout.CENTER);
        mainContentPanel.add(new MainContentPanel(), OptionConstant.DEFAULT);
        setVisible(true);
    }


    public void updateContent(String option) {
        mainContentPanel.removeAll();
        switch (option) {
            case "Symmetric Encrypt":
                mainContentPanel.add(new SymmetricEncryptPanel(), OptionConstant.SYMMETRIC_ENCRYPT);
                break;
            case "Asymmetric Encrypt":
                mainContentPanel.add(new AsymmetricEncryptPanel(), OptionConstant.ASYMMETRIC_ENCRYPT);
                break;
            case "Hash":
                mainContentPanel.add(new HashPanel(), OptionConstant.HASH);
                break;
            case "Digital Signature":
                mainContentPanel.add(new DigitalSignaturePanel(), OptionConstant.DIGITAL_SIGNATURE);
                break;
            case "About":
                mainContentPanel.add(new AboutPanel(), OptionConstant.ABOUT);
                break;
            default:
                mainContentPanel.add(new MainContentPanel(), OptionConstant.DEFAULT);
                break;
        }

        CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();
        cardLayout.show(mainContentPanel, option);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());

            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ToolBar.background", Color.WHITE);
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Unsupported Look and Feel");
        }
        SwingUtilities.invokeLater(Main::new);
    }
}
