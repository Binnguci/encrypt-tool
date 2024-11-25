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
        panelMap.put(OptionConstant.SYMMETRIC_ENCRYPT, new SymmetricPanel());
        panelMap.put(OptionConstant.HASH, new HashPanel());
        panelMap.put(OptionConstant.ASYMMETRIC_ENCRYPT, new AsymmetricPanel());
        panelMap.put(OptionConstant.DIGITAL_SIGNATURE, new DigitalSignaturePanel());
        panelMap.put(OptionConstant.ABOUT, new AboutPanel());
        panelMap.put(OptionConstant.DEFAULT, new MainContentPanel());
    }


    public static void main(String[] args) {
        UIManagerSetup.applyFlatLafTheme();
        SwingUtilities.invokeLater(Main::new);
    }

}