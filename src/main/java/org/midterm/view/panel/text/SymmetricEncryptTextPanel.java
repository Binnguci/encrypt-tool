package org.midterm.view.panel.text;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.midterm.constant.AlgorithmsConstant;
import org.midterm.constant.StringConstant;
import org.midterm.controller.SymmetricTextController;
import org.midterm.model.InformationData;
import org.midterm.service.KeyManager;
import org.midterm.view.common.CustomColorButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SymmetricEncryptTextPanel extends JPanel {
    JComboBox<Integer> shiftField;
    JComboBox<String> modeComboBox;
    JComboBox<String> paddingComboBox;
    JComboBox<String> algorithmComboBox;
    JComboBox<String> languageComboBox;
    JTextArea textArea;
    JTextField keyField;
    JTextField ivField;
    JButton encryptButton;
    JButton resetIvButton;
    JButton decryptButton;
    JTextArea resultArea;
    JComboBox<Integer> keySizeField;
    JComboBox<Integer> ivSizeField;
    JButton generateKeyButton;
    JButton generateIvButton;
    JButton copyKeyButton;
    JButton saveKeyButton;
    JComboBox<String> aComboBox;
    JComboBox<String> bComboBox;

    public SymmetricEncryptTextPanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)));
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        languageComboBox = new JComboBox<>(new String[]{StringConstant.LANGUAGE_ENGLISH, StringConstant.LANGUAGE_VIETNAMESE});
        inputPanel.add(new JLabel("Language:"));
        inputPanel.add(languageComboBox);
        mainPanel.add(inputPanel);

        textArea = new JTextArea(5, 30);
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
        algorithmComboBox = new JComboBox<>(new String[]{"None", AlgorithmsConstant.AFFINE, AlgorithmsConstant.SHIFT, AlgorithmsConstant.SUBSTITUTION, AlgorithmsConstant.VIGENERE, AlgorithmsConstant.DES, AlgorithmsConstant.AES, AlgorithmsConstant.BLOWFISH, AlgorithmsConstant.TRIPLEDES, AlgorithmsConstant.RC4});
        optionsPanel.add(algorithmComboBox);

        optionsPanel.add(new JLabel("Mode:"));
        modeComboBox = new JComboBox<>(new String[]{"CBC", "ECB", "CFB", "OFB", "CTR"});
        optionsPanel.add(modeComboBox);

        optionsPanel.add(new JLabel("Padding:"));
        paddingComboBox = new JComboBox<>(new String[]{"None", "PKCS5Padding", "PKCS7Padding", "NoPadding"});
        optionsPanel.add(paddingComboBox);

        optionsPanel.add(new JLabel("Shift:"));
        shiftField = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        shiftField.setEnabled(false);
        optionsPanel.add(shiftField);

        mainPanel.add(optionsPanel);

        algorithmComboBox.addActionListener(e -> {
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            assert selectedAlgorithm != null;
            updateModeAndPadding(selectedAlgorithm, modeComboBox, paddingComboBox);
            toggleShiftField(selectedAlgorithm);
        });

        updateModeAndPadding("AES", modeComboBox, paddingComboBox);

        JPanel keyPanel = new JPanel(new BorderLayout());
        keyField = new JTextField();
        keyField.setEditable(true);
        keyField.setBorder(BorderFactory.createTitledBorder("Key"));

        String[] validA = {"1", "3", "5", "7", "9", "11"}; // Các giá trị a hợp lệ
        String[] validB = {"0", "5", "10", "15", "20"};    // Giá trị b
        aComboBox = new JComboBox<>(validA);
        bComboBox = new JComboBox<>(validB);
        JPanel affinePanel = new JPanel(new FlowLayout());
        affinePanel.add(new JLabel("Choose a for multiplication:"));
        affinePanel.add(aComboBox);
        affinePanel.add(new JLabel("Choose b for shift:"));
        affinePanel.add(bComboBox);
        affinePanel.setVisible(false);
        mainPanel.add(affinePanel, BorderLayout.CENTER);

        keySizeField = new JComboBox<>(new Integer[]{128, 192, 256});
        JPanel keySizePanel = new JPanel(new BorderLayout());
        keySizePanel.add(new JLabel("Key Size:"));
        keySizePanel.add(keySizeField, BorderLayout.EAST);
        keyPanel.add(keySizePanel, BorderLayout.EAST);
        keyPanel.add(keyField, BorderLayout.CENTER);
        mainPanel.add(keyPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        JPanel keyButtonPanel = new JPanel(new BorderLayout());
        keyButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        generateKeyButton = new JButton("Generate Key");
        copyKeyButton = new JButton("Copy");
        saveKeyButton = new JButton("Save");
        CustomColorButton.setButtonPressColor(saveKeyButton, "#F26680", Color.WHITE);
        CustomColorButton.setButtonPressColor(generateKeyButton, "#F26680", Color.WHITE);
        CustomColorButton.setButtonPressColor(copyKeyButton, "#F26680", Color.WHITE);
        keyButtonPanel.add(generateKeyButton);
        keyButtonPanel.add(copyKeyButton);
        keyButtonPanel.add(saveKeyButton);
        mainPanel.add(keyButtonPanel, BorderLayout.CENTER);

        JPanel ivPanel = new JPanel(new BorderLayout());
        ivField = new JTextField();
        ivField.setEditable(true);
        ivField.setBorder(BorderFactory.createTitledBorder("Initialization Vector"));
        ivSizeField = new JComboBox<>(new Integer[]{128, 192, 256});
        JPanel ivSizePanel = new JPanel(new BorderLayout());
        ivSizePanel.add(new JLabel("IV Size:"));
        ivSizePanel.add(ivSizeField, BorderLayout.EAST);
        ivPanel.add(ivSizePanel, BorderLayout.EAST);
        ivPanel.add(ivField, BorderLayout.CENTER);
        mainPanel.add(ivPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        JPanel ivButtonPanel = new JPanel(new BorderLayout());
        ivButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        generateIvButton = new JButton("Generate IV");
        resetIvButton = new JButton("Reset");
        CustomColorButton.setButtonPressColor(generateIvButton, "#F26680", Color.WHITE);
        CustomColorButton.setButtonPressColor(resetIvButton, "#F26680", Color.WHITE);
        ivButtonPanel.add(generateIvButton);
        ivButtonPanel.add(resetIvButton);
        mainPanel.add(ivButtonPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");


        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonPanel);

        resultArea = new JTextArea(5, 30);
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

        encryptButton.addActionListener(e -> {
            InformationData data = collectionData();
            String selectMode = (String) modeComboBox.getSelectedItem();
            String selectPadding = (String) paddingComboBox.getSelectedItem();
            if ("None".equalsIgnoreCase(selectMode) && "None".equalsIgnoreCase(selectPadding)) {
            } else if (selectMode != null && "None".equalsIgnoreCase(selectPadding)) {
                JOptionPane.showMessageDialog(null, "Padding không được để trống!");
            } else if ("None".equalsIgnoreCase(selectMode) && selectPadding != null) {
                JOptionPane.showMessageDialog(null, "Sai chế độ mã hóa!");
            }


            SymmetricTextController.encrypt(data, resultArea);
        });

        decryptButton.addActionListener(e -> {
            InformationData data = collectionData();
            SymmetricTextController.decrypt(data, resultArea);
        });

        resetIvButton.addActionListener(e -> {
            ivField.setText("");
        });

        generateKeyButton.addActionListener(e -> {
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            String language = (String) languageComboBox.getSelectedItem();
            Integer keySize = (Integer) keySizeField.getSelectedItem();
            try {
                SymmetricTextController.generateKey(algorithm, language, keySize, keyField);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


        generateIvButton.addActionListener(e -> {
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            Integer ivSize = (Integer) ivSizeField.getSelectedItem();
            SymmetricTextController.generateIV(algorithm, ivSize, ivField);
        });

        copyKeyButton.addActionListener(e -> {
            String keyText = keyField.getText();
            if (keyText != null && !keyText.isEmpty()) {
                copyToClipboard(keyText);
                String originalText = copyKeyButton.getText();
                copyKeyButton.setText("Copied!");
                Timer timer = new Timer(2000, evt -> copyKeyButton.setText(originalText));
                timer.setRepeats(false);
                timer.start();
            } else {
                JOptionPane.showMessageDialog(null, "Key trống, không thể sao chép!");
            }
        });

        algorithmComboBox.addActionListener(e -> {
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            if (selectedAlgorithm.equalsIgnoreCase(AlgorithmsConstant.AFFINE)) {
                affinePanel.setVisible(true);
                keyPanel.setVisible(false);
                keyButtonPanel.setVisible(false);
                ivPanel.setVisible(false);
                ivButtonPanel.setVisible(false);
            } else {
                affinePanel.setVisible(false);
                keyPanel.setVisible(true);
                keyButtonPanel.setVisible(true);
                ivPanel.setVisible(true);
                ivButtonPanel.setVisible(true);
            }
            if (selectedAlgorithm != null) {
                String savedKey = KeyManager.loadKey(selectedAlgorithm);
                keyField.setText(savedKey);
                updateKeySize(selectedAlgorithm, keySizeField);
            }
        });


        modeComboBox.addActionListener(e -> {
            String selectedMode = (String) modeComboBox.getSelectedItem();
            updateIvSize(getSelectedMode(), ivSizeField, generateIvButton, resetIvButton);
            if (selectedMode.equalsIgnoreCase("ECB")) {
                ivField.setText("");
                ivField.setEditable(false);
                ivSizeField.setEnabled(false);
                generateIvButton.setEnabled(false);
                resetIvButton.setEnabled(false);
            }
        });

        saveKeyButton.addActionListener(e -> {
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            String key = keyField.getText();
            if (selectedAlgorithm != null && key != null && !key.isEmpty()) {
                KeyManager.saveKey(selectedAlgorithm, key);
                JOptionPane.showMessageDialog(null, "Key đã được lưu thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Key hoặc thuật toán không hợp lệ!");
            }
        });

        JPanel buttonCopy = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton copyBtn = new JButton("Copy");
        JButton resetButton = new JButton("Reset");
        CustomColorButton.setButtonPressColor(copyBtn, "#F26680", Color.WHITE);
        CustomColorButton.setButtonPressColor(resetButton, "#F26680", Color.WHITE);
        buttonCopy.add(copyBtn);
        buttonCopy.add(resetButton);

        resetButton.addActionListener(e -> {
            textArea.setText("");
            resultArea.setText("");
            keyField.setText("");
            ivField.setText("");
            algorithmComboBox.setSelectedIndex(0);
            modeComboBox.setSelectedIndex(0);
            paddingComboBox.setSelectedIndex(0);
        });
        copyBtn.addActionListener(e -> {
            String resultText = resultArea.getText();
            if (resultText != null && !resultText.isEmpty()) {
                copyToClipboard(resultText);
                String originalText = copyBtn.getText();
                copyBtn.setText("Copied!");
                Timer timer = new Timer(2000, evt -> copyBtn.setText(originalText));
                timer.setRepeats(false);
                timer.start();
            } else {
                JOptionPane.showMessageDialog(null, "Kết quả trống, không thể sao chép!");
            }
        });

        buttonCopy.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonCopy);

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        paddedPanel.add(mainPanel, BorderLayout.CENTER);

        add(paddedPanel, BorderLayout.NORTH);
    }

    private void toggleShiftField(String algorithm) {
        Set<String> symmetricAlgorithms = Set.of(
                AlgorithmsConstant.SHIFT,
                AlgorithmsConstant.SUBSTITUTION,
                AlgorithmsConstant.VIGENERE,
                AlgorithmsConstant.DES,
                AlgorithmsConstant.AES,
                AlgorithmsConstant.AFFINE,
                AlgorithmsConstant.BLOWFISH,
                AlgorithmsConstant.TRIPLEDES,
                AlgorithmsConstant.RC4
        );

        shiftField.setEnabled(false);
        modeComboBox.setEnabled(false);
        paddingComboBox.setEnabled(false);
        keySizeField.setEnabled(false);
        ivSizeField.setEnabled(false);
        keyField.setEditable(false);
        ivField.setEditable(false);
        generateKeyButton.setEnabled(false);
        generateIvButton.setEnabled(false);
        copyKeyButton.setEnabled(false);
        resetIvButton.setEnabled(false);
        languageComboBox.setEnabled(true);
        saveKeyButton.setEnabled(false);

        if (symmetricAlgorithms.contains(algorithm)) {
            switch (algorithm) {
                case AlgorithmsConstant.SHIFT -> shiftField.setEnabled(true);

                case AlgorithmsConstant.SUBSTITUTION -> {
                    generateKeyButton.setEnabled(true);
                    copyKeyButton.setEnabled(true);
                    keyField.setEditable(true);
                }
                case AlgorithmsConstant.AFFINE -> {
                    modeComboBox.setEnabled(false);
                    paddingComboBox.setEnabled(false);
                }
                case AlgorithmsConstant.VIGENERE -> {
                    keyField.setEditable(true);
                    generateKeyButton.setEnabled(true);
                    copyKeyButton.setEnabled(true);
                }

                case AlgorithmsConstant.DES, AlgorithmsConstant.AES, AlgorithmsConstant.BLOWFISH,
                     AlgorithmsConstant.TRIPLEDES -> {
                    languageComboBox.setEnabled(false);
                    modeComboBox.setEnabled(true);
                    paddingComboBox.setEnabled(true);
                    keySizeField.setEnabled(true);
                    ivSizeField.setEnabled(true);
                    keyField.setEditable(true);
                    ivField.setEditable(true);
                    generateKeyButton.setEnabled(true);
                    generateIvButton.setEnabled(true);
                    copyKeyButton.setEnabled(true);
                    resetIvButton.setEnabled(true);
                }
                case AlgorithmsConstant.RC4 -> {
                    keySizeField.setEnabled(true);
                    keyField.setEditable(true);
                    generateKeyButton.setEnabled(true);
                    copyKeyButton.setEnabled(true);
                    saveKeyButton.setEnabled(true);
                }
                default -> {
                }
            }
        } else {
            modeComboBox.setEnabled(true);
            paddingComboBox.setEnabled(true);
            keySizeField.setEnabled(true);
            ivSizeField.setEnabled(true);
            keyField.setEditable(true);
            ivField.setEditable(true);
            languageComboBox.setEnabled(true);
        }
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

    private void copyToClipboard(String text) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, null);
    }


    private static void updateModeAndPadding(String algorithm, JComboBox<String> modeComboBox, JComboBox<String> paddingComboBox) {
        switch (algorithm) {
            case "AES", "DES", "Blowfish", "RC2", "IDEA" -> {
                modeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"None", "ECB", "CBC", "CFB", "OFB", "CTR", "GCM"}));
                paddingComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"None", "PKCS5Padding", "PKCS7Padding", "ISO10126Padding", "ZeroPadding", "NoPadding"}));
            }
            case "RC4" -> {
                modeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"ECB"}));
                paddingComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"NoPadding"}));
            }
            case "Hill", "Affine", "Caesar", "SubstitutionCipher" -> {
                modeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"None"}));
                paddingComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"None"}));

            }
        }
    }

    private static void updateKeySize(String algorithm, JComboBox<Integer> keySizeField) {
        switch (algorithm) {
            case AlgorithmsConstant.AES ->
                    keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{128, 192, 256}));
            case AlgorithmsConstant.DES -> keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{56}));
            case AlgorithmsConstant.BLOWFISH ->
                    keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{32, 64, 128, 192, 256}));
            case AlgorithmsConstant.TRIPLEDES ->
                    keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{112, 168}));
            case "RC2" -> keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{40, 56, 64, 128, 192, 256}));
            case "IDEA" -> keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{128}));
            case "RC4" -> keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{128}));
        }
    }

    private static void updateIvSize(String mode, JComboBox<Integer> ivSizeField, JButton generateIvButton, JButton copyIvButton) {
        if (mode == null || mode.isEmpty() || mode.equalsIgnoreCase("None")) {
            ivSizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{}));
            ivSizeField.setEnabled(false);
            generateIvButton.setEnabled(false);
            copyIvButton.setEnabled(false);
            return;
        }

        switch (mode) {
            case "CBC", "CFB", "OFB", "CTR", "GCM" -> {
                ivSizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{128}));
                ivSizeField.setEnabled(true);
                generateIvButton.setEnabled(true);
                copyIvButton.setEnabled(true);
            }
            default -> {
                ivSizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{}));
                ivSizeField.setEnabled(false);
            }
        }
    }


    public String getInputText() {
        return textArea.getText();
    }

    public String getSelectedAlgorithm() {
        return (String) algorithmComboBox.getSelectedItem();
    }

    public String getSelectedMode() {
        if (modeComboBox.isEnabled()) {
            return (String) modeComboBox.getSelectedItem();
        } else {
            return null;
        }
    }

    public Integer getSelectedMultiplier() {
        return Integer.parseInt((String) aComboBox.getSelectedItem());
    }

    public Integer getSelectedShift() {
        return Integer.parseInt((String) bComboBox.getSelectedItem());
    }


    public String getSelectedPadding() {
        if (paddingComboBox.isEnabled()) {
            return (String) paddingComboBox.getSelectedItem();
        } else {
            return null;
        }
    }

    public String getKey() {
        return keyField.getText();
    }

    public Integer getShift() {
        if (!shiftField.isEnabled()) {
            return null;
        }
        return (Integer) shiftField.getSelectedItem();
    }

    public String getLanguage() {
        return (String) languageComboBox.getSelectedItem();
    }

    public String getIV() {
        if (ivField.getText().isEmpty()) {
            return null;
        }
        return ivField.getText();
    }

    private InformationData collectionData() {
        return InformationData.builder()
                .algorithm(getSelectedAlgorithm())
                .mode(getSelectedMode())
                .padding(getSelectedPadding())
                .shift(getShift())
                .language(getLanguage())
                .key(getKey())
                .iv(getIV())
                .inputText(getInputText())
                .aMultiplier(getSelectedMultiplier())
                .bShift(getSelectedShift())
                .build();
    }

    public void setEncryptButtonActionListener(ActionListener actionListener) {
        encryptButton.addActionListener(
                e -> actionListener.actionPerformed(new ActionEvent(this, 0, "encrypt"))
        );
    }

}