package org.midterm.view.panel.text;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.midterm.view.common.CustomColorButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AsymmetricEncryptTextPanel extends JPanel{

    public AsymmetricEncryptTextPanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)));
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JTextArea textArea = new JTextArea(5, 30);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        addPlaceholder(textArea, "Enter your text here...");
        textArea.setBorder(null);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Text"));
        scrollPane.setPreferredSize(new Dimension(500, 100));

        mainPanel.add(scrollPane);


        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        optionsPanel.add(new JLabel("Algorithm:"));
        JComboBox<String> algorithmComboBox = new JComboBox<>(new String[]{"AES", "DES", "Blowfish", "Hill", "Affine"});
        optionsPanel.add(algorithmComboBox);

        optionsPanel.add(new JLabel("Mode:"));
        JComboBox<String> modeComboBox = new JComboBox<>(new String[]{"ECB", "CBC", "CFB"});
        optionsPanel.add(modeComboBox);

        optionsPanel.add(new JLabel("Padding:"));
        JComboBox<String> paddingComboBox = new JComboBox<>(new String[]{"PKCS5", "NoPadding", "ISO10126"});
        optionsPanel.add(paddingComboBox);

        mainPanel.add(optionsPanel);


        JPanel keyPanel = new JPanel(new BorderLayout());
        JTextField keyField = new JTextField();
        keyField.setEditable(true);
        keyField.setBorder(BorderFactory.createTitledBorder("Key"));
        JPanel keyButtonPanel = new JPanel(new BorderLayout());
        JButton generateKeyButton = new JButton("Generate Key");
        JButton copyKeyButton = new JButton("Copy");
        CustomColorButton.setButtonPressColor(generateKeyButton, "#F26680", Color.WHITE);
        CustomColorButton.setButtonPressColor(copyKeyButton, "#F26680", Color.WHITE);
        keyButtonPanel.add(generateKeyButton, BorderLayout.WEST);
        keyButtonPanel.add(copyKeyButton, BorderLayout.EAST);
        keyPanel.add(keyButtonPanel, BorderLayout.EAST);
        keyPanel.add(keyField, BorderLayout.CENTER);
        mainPanel.add(keyPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonPanel);

        JTextArea resultArea = new JTextArea(5, 30);
        resultArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        resultArea.setEditable(false);
        addPlaceholder(resultArea, "Result will exist here...");
        resultArea.setBorder(null);
        resultArea.setBackground(Color.WHITE);

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Result"));
        resultScrollPane.setPreferredSize(new Dimension(500, 100));

        mainPanel.add(resultScrollPane);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel buttonCopy = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton copyBtn = new JButton("Copy");
        buttonCopy.add(copyBtn);

        buttonCopy.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonCopy);


        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        paddedPanel.add(mainPanel, BorderLayout.CENTER);

        add(paddedPanel, BorderLayout.NORTH);
    }

    private void addPlaceholder(JTextArea textArea, String placeholder) {
        textArea.setText(placeholder);
        textArea.setForeground(Color.GRAY);

        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setForeground(Color.GRAY);
                    textArea.setText(placeholder);
                }
            }
        });
    }

}
