package org.midterm.view.page;

import org.midterm.constant.OptionConstant;
import org.midterm.view.panel.EncryptionTypeToolBar;
import org.midterm.view.panel.file.HashFilePanel;
import org.midterm.view.panel.text.HashTextPanel;

import javax.swing.*;
import java.awt.*;

public class HashPanel extends JPanel {
    CardLayout cardLayout = new CardLayout();
    JPanel contentPanel = new JPanel(cardLayout);

    public HashPanel() {
        setLayout(new BorderLayout());

        EncryptionTypeToolBar toolBar = new EncryptionTypeToolBar();
        toolBar.setModeChangeListener(mode -> {
            if (mode.equals("Text")) {
                cardLayout.show(contentPanel, OptionConstant.TEXT_PANEL);
            } else {
                cardLayout.show(contentPanel, OptionConstant.FILE_PANEL);
            }
        });

        HashTextPanel hashTextPanel = new HashTextPanel();
        HashFilePanel hashFilePanel = new HashFilePanel();
        contentPanel.add(hashTextPanel, OptionConstant.TEXT_PANEL);
        contentPanel.add(hashFilePanel, OptionConstant.FILE_PANEL);

        add(toolBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, OptionConstant.TEXT_PANEL);
    }
}