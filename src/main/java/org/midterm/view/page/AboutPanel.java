package org.midterm.view.page;

import org.midterm.constant.StringConstant;

import javax.swing.*;
import java.awt.*;

public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("About the Project", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setPreferredSize(new Dimension(400, 50));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JTextArea textArea = new JTextArea(StringConstant.ABOUT_TEXT);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(getBackground());
        textArea.setPreferredSize(new Dimension(380, 200));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPanel.add(scrollPane);

        JLabel authorLabel = new JLabel("Author: Binnguci", JLabel.CENTER);
        authorLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        contentPanel.add(authorLabel);

         ImageIcon icon = new ImageIcon("/icon/binnguci.jpg");
         JLabel iconLabel = new JLabel(icon);
         contentPanel.add(iconLabel);

        add(contentPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("© 2024 Encrypt-Decrypt Tool. All rights reserved.");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel);

        add(footerPanel, BorderLayout.SOUTH);
    }
}
