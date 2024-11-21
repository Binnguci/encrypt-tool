package org.midterm;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.midterm.constant.OptionConstant;
import org.midterm.view.UIManagerSetup;
import org.midterm.view.page.*;
import org.midterm.view.panel.SideMenu;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Main extends JFrame {
    JPanel mainContentPanel;
    Map<String, JPanel> panelMap = new HashMap<>();

    public Main() {
        setTitle("Cryptography Tool");
        setSize(1000, 800);
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

        for (Map.Entry<String, JPanel> entry : panelMap.entrySet()) {
            mainContentPanel.add(entry.getValue(), entry.getKey());
        }

        add(mainContentPanel, BorderLayout.CENTER);
        initializePanelMap();
        mainContentPanel.add(new MainContentPanel(), OptionConstant.DEFAULT);
        setVisible(true);
    }



    public void updateContent(String option) {
        JPanel panel = panelMap.getOrDefault(option, panelMap.get(OptionConstant.DEFAULT));
        mainContentPanel.add(panel, option);

        CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();
        cardLayout.show(mainContentPanel, option);
        revalidate();
        repaint();
    }


    private void initializePanelMap() {
        panelMap.put(OptionConstant.CLASSIC_ENCRYPT, new ClassicEncryptPanel());
//        panelMap.put(OptionConstant.SYMMETRIC_ENCRYPT, new SymmetricEncryptPanel());
//        panelMap.put(OptionConstant.ASYMMETRIC_ENCRYPT, new AsymmetricEncryptPanel());
//        panelMap.put(OptionConstant.HASH, new HashPanel());
//        panelMap.put(OptionConstant.DIGITAL_SIGNATURE, new DigitalSignaturePanel());
        panelMap.put(OptionConstant.ABOUT, new AboutPanel());
        panelMap.put(OptionConstant.DEFAULT, new MainContentPanel());
    }


    public static void main(String[] args) {
        UIManagerSetup.applyFlatLafTheme();
        SwingUtilities.invokeLater(Main::new);
    }

}


//    public void updateContent(String option) {
//        mainContentPanel.removeAll();
//        switch (option) {
//            case OptionConstant.SYMMETRIC_ENCRYPT:
//                mainContentPanel.add(new SymmetricEncryptPanel(), OptionConstant.SYMMETRIC_ENCRYPT);
//                break;
//            case OptionConstant.ASYMMETRIC_ENCRYPT:
//                mainContentPanel.add(new AsymmetricEncryptPanel(), OptionConstant.ASYMMETRIC_ENCRYPT);
//                break;
//            case OptionConstant.HASH:
//                mainContentPanel.add(new HashPanel(), OptionConstant.HASH);
//                break;
//            case OptionConstant.DIGITAL_SIGNATURE:
//                mainContentPanel.add(new DigitalSignaturePanel(), OptionConstant.DIGITAL_SIGNATURE);
//                break;
//            case OptionConstant.ABOUT:
//                mainContentPanel.add(new AboutPanel(), OptionConstant.ABOUT);
//                break;
//            default:
//                mainContentPanel.add(new MainContentPanel(), OptionConstant.DEFAULT);
//                break;
//        }
//
//        CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();
//        cardLayout.show(mainContentPanel, option);
//        revalidate();
//        repaint();
//    }

//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(new FlatMacLightLaf());
//
//            UIManager.put("Panel.background", Color.WHITE);
//            UIManager.put("TextField.background", Color.WHITE);
//            UIManager.put("Button.background", Color.WHITE);
//            UIManager.put("ComboBox.background", Color.WHITE);
//            UIManager.put("ToolBar.background", Color.WHITE);
//        } catch (UnsupportedLookAndFeelException e) {
//            System.err.println("Unsupported Look and Feel");
//        }
//        SwingUtilities.invokeLater(Main::new);
//    }