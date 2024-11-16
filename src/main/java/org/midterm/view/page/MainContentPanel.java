package org.midterm.view.page;

import org.midterm.constant.StringConstant;

import javax.swing.*;
import java.awt.*;

public class MainContentPanel extends JPanel {

    public MainContentPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        // Load the image from resources
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/parrotsec-logo.png"));

        // Resize the image to the desired size
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);  // Adjust the size
        ImageIcon scaledIcon = new ImageIcon(scaledImage);  // Create a new ImageIcon from the resized image

        // Create a JLabel with the resized image
        JLabel logoLabel = new JLabel(scaledIcon);
        logoLabel.setPreferredSize(new Dimension(100, 100)); // Set the size of the image in JLabel
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER); // Align the image to the center of the label

        // Set up other JLabels
        JLabel welcomeLabel = new JLabel("Welcome to the Information Security Tool!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.decode("#333333"));

        JLabel descriptionLabel = new JLabel(StringConstant.GREETING);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionLabel.setForeground(Color.decode("#555555"));

        // Set up GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(logoLabel, gbc); // Add the image at the top of the panel

        gbc.gridy = 1;
        add(welcomeLabel, gbc);

        gbc.gridy = 2;
        add(descriptionLabel, gbc);
    }
}
