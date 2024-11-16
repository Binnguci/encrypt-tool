package org.midterm.view.page;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.midterm.constant.OptionConstant;
import org.midterm.view.panel.EncryptionTypeToolBar;
import org.midterm.view.panel.file.SymmetricEncryptFilePanel;
import org.midterm.view.panel.text.SymmetricEncryptTextPanel;

import javax.swing.*;
import java.awt.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SymmetricEncryptPanel extends JPanel {
    CardLayout cardLayout = new CardLayout();
    JPanel contentPanel = new JPanel(cardLayout);

    public SymmetricEncryptPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Symmetric Encryption Generation"), BorderLayout.CENTER);

        EncryptionTypeToolBar toolBar = new EncryptionTypeToolBar();
        toolBar.setModeChangeListener(mode -> {
            if (mode.equals("Text")) {
                cardLayout.show(contentPanel, OptionConstant.TEXT_PANEL);
            } else {
                cardLayout.show(contentPanel, OptionConstant.FILE_PANEL);
            }
        });

        SymmetricEncryptTextPanel textEncryptPanel = new SymmetricEncryptTextPanel();
        SymmetricEncryptFilePanel fileEncryptPanel = new SymmetricEncryptFilePanel();



        contentPanel.add(textEncryptPanel, OptionConstant.TEXT_PANEL);
        contentPanel.add(fileEncryptPanel, OptionConstant.FILE_PANEL);

        add(toolBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, OptionConstant.TEXT_PANEL);
    }
}
