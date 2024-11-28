package org.midterm.view.page;

import org.midterm.constant.OptionConstant;
import org.midterm.view.panel.EncryptionTypeToolBar;
import org.midterm.view.panel.text.AsymmetricTextPanel;

import javax.swing.*;
import java.awt.*;

public class AsymmetricPanel extends JPanel {
    CardLayout cardLayout = new CardLayout();
    JPanel contentPanel = new JPanel(cardLayout);

    public AsymmetricPanel() {
        setLayout(new BorderLayout());

        EncryptionTypeToolBar toolBar = new EncryptionTypeToolBar();
        toolBar.setModeChangeListener(mode -> {
            if (mode.equals("Text")) {
                cardLayout.show(contentPanel, OptionConstant.TEXT_PANEL);
            } else {
                cardLayout.show(contentPanel, OptionConstant.FILE_PANEL);
            }
        });

        AsymmetricTextPanel asymmetricTextPanel = AsymmetricTextPanel.create();

        contentPanel.add(asymmetricTextPanel, OptionConstant.TEXT_PANEL);

        add(toolBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, OptionConstant.TEXT_PANEL);
    }
}
