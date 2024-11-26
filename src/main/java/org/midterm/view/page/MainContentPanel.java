package org.midterm.view.page;

import org.midterm.constant.StringConstant;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainContentPanel extends JPanel {

    public MainContentPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/parrotsec-logo.png")));

        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledIcon);
        logoLabel.setPreferredSize(new Dimension(100, 100));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel welcomeLabel = new JLabel("Welcome to the Cryptography Tool!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.decode("#333333"));

        JLabel descriptionLabel = new JLabel(StringConstant.GREETING);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionLabel.setForeground(Color.decode("#555555"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(logoLabel, gbc);

        gbc.gridy = 1;
        add(welcomeLabel, gbc);

        gbc.gridy = 2;
        add(descriptionLabel, gbc);
    }
}
