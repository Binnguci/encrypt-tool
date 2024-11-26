package org.midterm.view.panel;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.midterm.Main;
import org.midterm.constant.OptionConstant;

import javax.swing.*;
import java.awt.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SideMenu extends JPanel {
    JButton selectedButton = null;
    final Main mainFrame;

    public SideMenu(Main mainFrame) {
        this.mainFrame = mainFrame;
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(211, 211, 211)));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension(180, 40);
        JButton btnClassicEncrypt = createButton(OptionConstant.CLASSIC_ENCRYPT, buttonSize);
        JButton btnSymmetricEncrypt = createButton(OptionConstant.SYMMETRIC_ENCRYPT, buttonSize);
        JButton btnAsymmetricEncrypt = createButton(OptionConstant.ASYMMETRIC_ENCRYPT, buttonSize);
        JButton btnHash = createButton(OptionConstant.HASH, buttonSize);
        JButton btnDigitalSignature = createButton(OptionConstant.DIGITAL_SIGNATURE, buttonSize);
        JButton btnAbout = createButton(OptionConstant.ABOUT, buttonSize);

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

    private JButton createButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        button.setPreferredSize(size);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(null);
        button.setFocusPainted(false);

        button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        button.addActionListener(e -> {
            if (selectedButton != null) {
                selectedButton.setBackground(Color.WHITE);
                selectedButton.setForeground(Color.BLACK);
            }
            button.setBackground(Color.decode("#F26680"));
            button.setForeground(Color.WHITE);
            selectedButton = button;
            mainFrame.updateContent(text);
        });
        return button;
    }
}