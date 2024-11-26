package org.midterm.view.panel.text;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.controller.ClassicController;
import org.midterm.controller.HashController;
import org.midterm.model.ClassicAlgorithm;
import org.midterm.view.common.CustomColorButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HashTextPanel extends JPanel {
    private JComboBox<String> algorithmComboBox;
    private JComboBox<Integer> shiftField;
    private JTextField keyField;
    private JTextField substitutionField;
    private JTextArea hillKeyArea;
    private JComboBox<String> aComboBox;
    private JComboBox<String> bComboBox;
    private JComboBox<String> languageComboBox;

    private JTextArea textArea;
    private JTextArea resultArea;
    public static HashTextPanel create() {
        return new HashTextPanel();
    }
    public HashTextPanel(){
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)));
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

//        mainPanel.add(createLanguagePanel());
        mainPanel.add(createTextInputPanel());
        mainPanel.add(createOptionsPanel());
        mainPanel.add(createButtonPanel());
        mainPanel.add(createResultPanel());
        mainPanel.add(createButtonEndPanel());

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        paddedPanel.add(mainPanel, BorderLayout.CENTER);

        add(paddedPanel, BorderLayout.NORTH);
    }


//    private JPanel createLanguagePanel() {
//        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//        languageComboBox = new JComboBox<>(new String[]{
//                StringConstant.LANGUAGE_ENGLISH,
//                StringConstant.LANGUAGE_VIETNAMESE
//        });
//        languagePanel.add(new JLabel("Language:"));
//        languagePanel.add(languageComboBox);
//        return languagePanel;
//    }

    private JPanel createTextInputPanel() {
        JPanel textInputPanel = new JPanel();
        textInputPanel.setLayout(new BorderLayout());

        textArea = new JTextArea(5, 30);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        addPlaceholder(textArea, "Enter your text here...");
        textInputPanel.setBorder(BorderFactory.createTitledBorder("Input Text"));

        JScrollPane scrollPane = new JScrollPane(textArea);
        textInputPanel.add(scrollPane, BorderLayout.CENTER);

        return textInputPanel;
    }

    private JPanel createOptionsPanel() {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        algorithmComboBox = new JComboBox<>(new String[]{
                AlgorithmsConstant.MD5,
                AlgorithmsConstant.SHA1,
                AlgorithmsConstant.SHA3,
                AlgorithmsConstant.SHA256,
                AlgorithmsConstant.SHA512,
                AlgorithmsConstant.SHA384,
                AlgorithmsConstant.SHA224,
                AlgorithmsConstant.SHA3_224,
                AlgorithmsConstant.SHA3_256,
                AlgorithmsConstant.SHA3_384,
                AlgorithmsConstant.SHA3_512,
                AlgorithmsConstant.SHAKE126,
                AlgorithmsConstant.SHAKE256,
                AlgorithmsConstant.MD2,
                AlgorithmsConstant.HMACSHA1,
                AlgorithmsConstant.HMACSHA3_224,
                AlgorithmsConstant.HmacSH3_256
        });

        JPanel algorithmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        algorithmPanel.add(new JLabel("Algorithm:"));
        algorithmPanel.add(algorithmComboBox);
        optionsPanel.add(algorithmPanel);
        return optionsPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton encryptButton = new JButton("Hash");
        encryptButton.addActionListener(e -> handleHashAction());
        CustomColorButton.setButtonPressColor(encryptButton, "#F26680", Color.WHITE);
        buttonPanel.add(encryptButton);
        return buttonPanel;
    }

    private void handleHashAction() {
        String inputText = textArea.getText();
        String algorithm = Objects.requireNonNull(algorithmComboBox.getSelectedItem()).toString();
        if (inputText == null || inputText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Input text cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String resultText = HashController.hash(inputText, algorithm);
            resultArea.setText(resultText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createResultPanel() {
        resultArea = new JTextArea(5, 30);
        resultArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        resultArea.setEditable(false);
        addPlaceholder(resultArea, "Result will appear here...");

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Result"));

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);
        return resultPanel;
    }

    private JPanel createButtonEndPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton copyButton = new JButton("Copy");
        JButton resetButton = new JButton("Reset");
        CustomColorButton.setButtonPressColor(copyButton, "#F26680", Color.WHITE);
        CustomColorButton.setButtonPressColor(resetButton, "#F26680", Color.WHITE);
        buttonPanel.add(copyButton);
        buttonPanel.add(resetButton);
        return buttonPanel;
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


    private <T> JComboBox<T> createComboBox(T[] items) {
        return new JComboBox<>(items);
    }
}
