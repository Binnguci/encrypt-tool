package org.midterm.view.panel.text;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.constant.StringConstant;
import org.midterm.controller.ClassicController;
import org.midterm.model.ClassicAlgorithm;
import org.midterm.service.KeyManager;
import org.midterm.view.common.CustomColorButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassicEncryptTextPanel extends JPanel {
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

    public ClassicEncryptTextPanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)));
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(createLanguagePanel());
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

    private JPanel createLanguagePanel() {
        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        languageComboBox = new JComboBox<>(new String[]{
                StringConstant.LANGUAGE_ENGLISH,
                StringConstant.LANGUAGE_VIETNAMESE
        });
        languagePanel.add(new JLabel("Language:"));
        languagePanel.add(languageComboBox);
        return languagePanel;
    }

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
                AlgorithmsConstant.NONE,
                AlgorithmsConstant.SHIFT,
                AlgorithmsConstant.AFFINE,
                AlgorithmsConstant.SUBSTITUTION,
                AlgorithmsConstant.VIGENERE,
                AlgorithmsConstant.HILL
        });
        algorithmComboBox.addActionListener(new AlgorithmSelectionListener());

        JPanel algorithmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        algorithmPanel.add(new JLabel("Algorithm:"));
        algorithmPanel.add(algorithmComboBox);

        optionsPanel.add(algorithmPanel);

        shiftField = createComboBox(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        keyField = new JTextField(20);
        keyField.setToolTipText("Enter the encryption key here.");
        substitutionField = new JTextField(20);
        hillKeyArea = new JTextArea(3, 20);
        hillKeyArea.setBorder(BorderFactory.createTitledBorder("Hill Key Matrix"));
        hillKeyArea.setLineWrap(true);

        aComboBox = createComboBox(new String[]{"1", "3", "5", "7", "9", "11", "15", "17", "19", "21", "23", "25"});
        bComboBox = createComboBox(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});

        optionsPanel.add(createLabeledPanel("Shift:", shiftField));
//        optionsPanel.add(createLabeledPanel("Key:", keyField));
        JPanel keyPanel = createKeyPanel(keyField);
        optionsPanel.add(keyPanel, BorderLayout.NORTH);
        optionsPanel.add(createSubstitutionPanel());
        optionsPanel.add(createLabeledPanel("Affine multiplication:", aComboBox));
        optionsPanel.add(createLabeledPanel("Affine shift:", bComboBox));
        optionsPanel.add(hillKeyArea);

        // Initially hide all fields
        hideAllParameterFields();

        return optionsPanel;
    }

    private JPanel createKeyPanel(JTextField keyField){
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyPanel.add(new JLabel("Key:"));
        keyPanel.add(keyField);
        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> {
            String language = (String) languageComboBox.getSelectedItem();
            String plaintext = textArea.getText();
            String generatedKey = ClassicController.generateKey(plaintext, language);
            keyField.setText(generatedKey);
        });

        // Nút Reset
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> keyField.setText("")); // Xóa nội dung keyField

        // Nút Save
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            String key = substitutionField.getText();
            if (algorithm != null && key != null && !key.isEmpty()) {
                KeyManager.saveKey(algorithm, key);
                JOptionPane.showMessageDialog(null, "Key đã được lưu thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Key hoặc thuật toán không hợp lệ!");
            }

        });

        keyPanel.add(generateButton);
        keyPanel.add(resetButton);
        keyPanel.add(saveButton);
        return keyPanel;
    }

    private JPanel createSubstitutionPanel() {
        // Panel chứa Substitution Table và các nút
        JPanel substitutionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        substitutionPanel.add(new JLabel("Substitution Table:"));

        substitutionField = new JTextField(20); // Trường nhập Substitution Table
        substitutionPanel.add(substitutionField);

        // Nút Generate
        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> {
            // Thay đổi ngôn ngữ nếu cần
            String language = (String) languageComboBox.getSelectedItem();
            String generatedKey = ClassicController.generateSubstitutionKey(language);
            substitutionField.setText(generatedKey);
        });
        substitutionPanel.add(generateButton);

        // Nút Reset
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> substitutionField.setText("")); // Xóa nội dung
        substitutionPanel.add(resetButton);

        //Nút Lưu
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            String key = substitutionField.getText();
            if (algorithm != null && key != null && !key.isEmpty()) {
                KeyManager.saveKey(algorithm, key);
                JOptionPane.showMessageDialog(null, "Key đã được lưu thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Key hoặc thuật toán không hợp lệ!");
            }

        });
        substitutionPanel.add(saveButton);

        return substitutionPanel;
    }


    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(e -> handleEncryptAction());
        JButton decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(e -> handleDecryptAction());
        CustomColorButton.setButtonPressColor(encryptButton, "#F26680", Color.WHITE);
        CustomColorButton.setButtonPressColor(decryptButton, "#F26680", Color.WHITE);
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        return buttonPanel;
    }

    // xử lý sự kiện mã hóa
    private void handleEncryptAction() {
        ClassicAlgorithm classicAlgorithm = collection();
        String inputText = textArea.getText();
        if (inputText == null || inputText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Input text cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String resultText = ClassicController.encrypt(classicAlgorithm, inputText);
            resultArea.setText(resultText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // Xu ly su kien giai ma
    private void handleDecryptAction() {
        ClassicAlgorithm classicAlgorithm = collection();
        String inputText = textArea.getText();
        if (inputText == null || inputText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Input text cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String resultText = ClassicController.decrypt(classicAlgorithm, inputText);
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



    private JPanel createLabeledPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(labelText));
        panel.add(component);
        return panel;
    }

    private <T> JComboBox<T> createComboBox(T[] items) {
        return new JComboBox<>(items);
    }

    private void hideAllParameterFields() {
        shiftField.setEnabled(false);
        keyField.setEnabled(false);
        substitutionField.setEnabled(false);
        hillKeyArea.setEnabled(false);
        aComboBox.setEnabled(false);
        bComboBox.setEnabled(false);

    }

    private class AlgorithmSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            hideAllParameterFields();
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            switch (selectedAlgorithm) {
                case AlgorithmsConstant.AFFINE:
                    aComboBox.setEnabled(true);
                    bComboBox.setEnabled(true);
                    break;
                case AlgorithmsConstant.SHIFT:
                    shiftField.setEnabled(true);
                    break;
                case AlgorithmsConstant.VIGENERE:
                    keyField.setEnabled(true);
                    break;
                case AlgorithmsConstant.SUBSTITUTION:
                    substitutionField.setEnabled(true);
                    substitutionField.setText(KeyManager.loadKey(selectedAlgorithm));
                    break;
                case AlgorithmsConstant.HILL:
                    hillKeyArea.setEnabled(true);
                    break;
                case null:
                default:
                    break;
            }
            revalidate();
            repaint();
        }
    }

    private List<List<Integer>> parseHillMatrix(String input) {
        List<List<Integer>> matrix = new ArrayList<>();
        String[] rows = input.split("\n");
        for (String row : rows) {
            String[] numbers = row.trim().split("\\s+");
            List<Integer> rowValues = new ArrayList<>();
            for (String number : numbers) {
                try {
                    rowValues.add(Integer.parseInt(number));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number in matrix: " + number);
                }
            }
            matrix.add(rowValues);
        }

        // Kiểm tra ma trận vuông (nếu cần thiết)
        int rowSize = matrix.getFirst().size();
        for (List<Integer> row : matrix) {
            if (row.size() != rowSize) {
                throw new IllegalArgumentException("Matrix is not square!");
            }
        }

        return matrix;
    }


    private ClassicAlgorithm collection() {
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        String hillMatrixText = hillKeyArea.getText();

        ClassicAlgorithm.ClassicAlgorithmBuilder builder = ClassicAlgorithm.builder()
                .name(selectedAlgorithm)
                .shift(shiftField.isVisible() ? (Integer) shiftField.getSelectedItem() : null)
                .key(keyField.isVisible() ? keyField.getText() : null)
                .substitutionKey(substitutionField.isVisible() ? substitutionField.getText() : null)
                .language((String) languageComboBox.getSelectedItem())
                .aMultiplier(aComboBox.isVisible() ? Integer.parseInt((String) Objects.requireNonNull(aComboBox.getSelectedItem())) : null)
                .bShift(bComboBox.isVisible() ? Integer.parseInt((String) Objects.requireNonNull(bComboBox.getSelectedItem())) : null);

        if (AlgorithmsConstant.HILL.equals(selectedAlgorithm)) {
            builder.hillMatrix(parseHillMatrix(hillMatrixText));
        }

        return builder.build();
    }

}
