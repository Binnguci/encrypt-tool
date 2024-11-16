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
    JButton decryptButton;
    JTextArea resultArea;
    JComboBox<Integer> keySizeField;
    JComboBox<Integer> ivSizeField;
    JButton generateKeyButton;
    JButton generateIvButton;
    JButton copyKeyButton;
    JButton copyIvButton;
    JButton saveKeyButton;

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
        algorithmComboBox = new JComboBox<>(new String[]{AlgorithmsConstant.SHIFT, AlgorithmsConstant.SUBSTITUTION, AlgorithmsConstant.DES});
        optionsPanel.add(algorithmComboBox);

        optionsPanel.add(new JLabel("Mode:"));
        modeComboBox = new JComboBox<>(new String[]{"CBC", "ECB", "CFB", "OFB", "CTR"});
        optionsPanel.add(modeComboBox);

        optionsPanel.add(new JLabel("Padding:"));
        paddingComboBox = new JComboBox<>(new String[]{"PKCS5Padding", "PKCS7Padding", "NoPadding"});
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
        copyIvButton = new JButton("Copy");
        CustomColorButton.setButtonPressColor(generateIvButton, "#F26680", Color.WHITE);
        CustomColorButton.setButtonPressColor(copyIvButton, "#F26680", Color.WHITE);
        ivButtonPanel.add(generateIvButton);
        ivButtonPanel.add(copyIvButton);
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
            SymmetricTextController.encrypt(data, resultArea);
        });
        JPanel buttonCopy = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        decryptButton.addActionListener(e -> {
            InformationData data = collectionData();
            SymmetricTextController.decrypt(data, resultArea);
        });

        generateKeyButton.addActionListener(e -> {
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            String language = (String) languageComboBox.getSelectedItem();
            try {
                SymmetricTextController.generateKey(algorithm, language, keyField);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });

        generateIvButton.addActionListener(e -> {
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            SymmetricTextController.generateIV(algorithm, ivField);
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
            if (selectedAlgorithm != null) {
                String savedKey = KeyManager.loadKey(selectedAlgorithm);
                keyField.setText(savedKey);
                updateKeySize(selectedAlgorithm, keySizeField);
            }
        });

        modeComboBox.addActionListener(e -> {
            String selectedAlgorithm = (String) modeComboBox.getSelectedItem();
            updateIvSize(selectedAlgorithm, ivSizeField);
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


        JButton copyBtn = new JButton("Copy");
        buttonCopy.add(copyBtn);

        buttonCopy.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buttonCopy);

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        paddedPanel.add(mainPanel, BorderLayout.CENTER);

        add(paddedPanel, BorderLayout.NORTH);
    }

    private void toggleShiftField(String algorithm) {

        String[] symmetricAlgorithms = {AlgorithmsConstant.SHIFT, AlgorithmsConstant.SUBSTITUTION, AlgorithmsConstant.DES};
        boolean isSymmetric = false;
        for (String symAlgo : symmetricAlgorithms) {
            if (algorithm.equalsIgnoreCase(symAlgo)) {
                isSymmetric = true;
                break;
            }
        }
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
        copyIvButton.setEnabled(false);


        if (isSymmetric) {
            switch (algorithm) {
                case AlgorithmsConstant.SHIFT -> shiftField.setEnabled(true);
                case AlgorithmsConstant.SUBSTITUTION -> {
                    generateKeyButton.setEnabled(true);
                    copyKeyButton.setEnabled(true);
                    keyField.setEditable(true);
                }
                case AlgorithmsConstant.DES -> {
                    modeComboBox.setEnabled(true);
                    paddingComboBox.setEnabled(true);
                    keySizeField.setEnabled(true);
                    ivSizeField.setEnabled(true);
                    keyField.setEditable(true);
                    ivField.setEditable(true);
                    generateKeyButton.setEnabled(true);
                    generateIvButton.setEnabled(true);
                    copyKeyButton.setEnabled(true);
                    copyIvButton.setEnabled(true);
                }
            }

        } else {
            modeComboBox.setEnabled(true);
            paddingComboBox.setEnabled(true);
            keySizeField.setEnabled(true);
            ivSizeField.setEnabled(true);
            keyField.setEditable(true);
            ivField.setEditable(true);
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
                paddingComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"PKCS5Padding", "PKCS7Padding", "ISO10126Padding", "ZeroPadding", "NoPadding"}));
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
            case "AES" -> keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{128, 192, 256}));
            case "DES" -> keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{56}));
            case "Blowfish" -> keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{32, 64, 128, 192, 256}));
            case "RC2" -> keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{40, 56, 64, 128, 192, 256}));
            case "IDEA" -> keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{128}));
            case "RC4" -> keySizeField.setModel(new DefaultComboBoxModel<>(new Integer[]{128}));
        }
    }

    private static void updateIvSize(String mode, JComboBox<Integer> ivSizeField) {
        switch (mode) {
            case "ECB" -> ivSizeField.setEnabled(false);
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
                .build();
    }

    public void setEncryptButtonActionListener(ActionListener actionListener) {
        encryptButton.addActionListener(
                e -> actionListener.actionPerformed(new ActionEvent(this, 0, "encrypt"))
        );
    }


}