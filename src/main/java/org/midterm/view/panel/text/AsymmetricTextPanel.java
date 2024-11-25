package org.midterm.view.panel.text;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.controller.AsymmetricTextController;
import org.midterm.factory.EncryptionConfigFactory;
import org.midterm.model.AsymmetricAlgorithms;
import org.midterm.model.PairKey;
import org.midterm.service.KeyManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AsymmetricTextPanel extends JPanel {
    private JTextArea textArea;
    private JComboBox<String> algorithmComboBox;
    private JTextField publicKeyField, privateKeyField;
    private JTextArea resultArea;
    private JComboBox<String> modeComboBox;
    private JComboBox<String> paddingComboBox;
//    private JLabel keySizeLabel;
    private JComboBox<Integer> keySizeComboBox;
//    private JLabel ivSizeLabel;
//    private JComboBox<Integer> ivSizeComboBox;
//    private JTextField ivField;
//    private JButton generateButton;


    public static AsymmetricTextPanel create() {
        return new AsymmetricTextPanel();
    }

    public AsymmetricTextPanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)));
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Add components
        mainPanel.add(createAlgorithmSelectionPanel());
        mainPanel.add(createEncryptionConfigPanel());
        mainPanel.add(createTextInputPanel());
        mainPanel.add(createResultPanel());
        mainPanel.add(createActionButtons());

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        paddedPanel.add(mainPanel, BorderLayout.CENTER);

        add(paddedPanel, BorderLayout.NORTH);
        updateEncryptionConfig();
        loadKey();
    }

    private JPanel createFlowLeftPanel() {
        return new JPanel(new FlowLayout(FlowLayout.LEFT));
    }


    private JPanel createAlgorithmSelectionPanel() {
        JPanel panel = createFlowLeftPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Select Algorithm"));

        String[] algorithms = {
                AlgorithmsConstant.RSA};
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.addActionListener(e -> updateEncryptionConfig());

        panel.add(new JLabel("Algorithm:"));
        panel.add(algorithmComboBox);
        return panel;
    }

    private void updateEncryptionConfig() {
        String algorithm = (String) algorithmComboBox.getSelectedItem();

        if (algorithm != null) {
            // Cập nhật danh sách mode
            List<String> modes = EncryptionConfigFactory.getModesByAsymmetricAlgorithm(algorithm);
            modeComboBox.setModel(new DefaultComboBoxModel<>(modes.toArray(new String[0])));

            // Chọn mode đầu tiên theo mặc định
            if (!modes.isEmpty()) {
                modeComboBox.setSelectedIndex(0);
            }

            // Cập nhật padding theo mode đầu tiên
            String selectedMode = (String) modeComboBox.getSelectedItem();
            if (selectedMode != null) {
                List<String> paddings = EncryptionConfigFactory.getPaddingsByAsymmetricAlgorithmAndMode(algorithm, selectedMode);
                paddingComboBox.setModel(new DefaultComboBoxModel<>(paddings.toArray(new String[0])));

                // Chọn padding đầu tiên theo mặc định
                if (!paddings.isEmpty()) {
                    paddingComboBox.setSelectedIndex(0);
                }
            }

            // Cập nhật kích thước key
            List<Integer> keySizes = EncryptionConfigFactory.getKeySizesByAsymmetricAlgorithm(algorithm);
            keySizeComboBox.setModel(new DefaultComboBoxModel<>(keySizes.toArray(new Integer[0])));
            // Cập nhật key đã lưu (nếu có)

        }
    }

    private void loadKey(){
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        Integer keySize = (Integer) keySizeComboBox.getSelectedItem();
        if (keySize != null) {
            PairKey key = KeyManager.loadKeys(algorithm, keySize);
            publicKeyField.setText(key.getPublicKey());
            privateKeyField.setText(key.getPrivateKey());
        } else {
            System.err.println("Key size is null. No key pair loaded.");
            publicKeyField.setText("");
            privateKeyField.setText("");
        }
    }


    private JPanel createEncryptionConfigPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Encryption Configuration"));

        // Mode selection
        JPanel modePanel = createFlowLeftPanel();
        modePanel.add(new JLabel("Mode:"));
        modeComboBox = new JComboBox<>();
        modePanel.add(modeComboBox);

        // Padding selection
        JPanel paddingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paddingPanel.add(new JLabel("Padding:"));
        paddingComboBox = new JComboBox<>();
        paddingPanel.add(paddingComboBox);

        // Key size
        JPanel keySizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keySizePanel.add(new JLabel("Key Size:"));
        keySizeComboBox = new JComboBox<>();
        keySizePanel.add(keySizeComboBox);
        publicKeyField = new JTextField();
        privateKeyField = new JTextField();

        panel.add(modePanel);
        panel.add(paddingPanel);
        panel.add(keySizePanel);
        panel.add(createPublicKeyPanel());
        panel.add(createPrivateKeyPanel());
        panel.add(createButtonKeyPanel());
        return panel;
    }

    private JPanel createPublicKeyPanel() {
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyPanel.add(new JLabel("Public key:"));
        publicKeyField.setPreferredSize(new Dimension(300, 25));
        keyPanel.add(publicKeyField);
        return keyPanel;
    }

    private JPanel createPrivateKeyPanel() {
        JPanel privateKeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        privateKeyPanel.add(new JLabel("Private key:"));
        privateKeyField.setPreferredSize(new Dimension(300, 25));
        privateKeyPanel.add(privateKeyField);
        return privateKeyPanel;
    }

    private JPanel createButtonKeyPanel() {
        JPanel keyButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> {
            try {
                performGenKey();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> performReset(publicKeyField, privateKeyField));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> performSave(publicKeyField, privateKeyField));

        keyButtonPanel.add(generateButton);
        keyButtonPanel.add(resetButton);
        keyButtonPanel.add(saveButton);
        return keyButtonPanel;
    }

    private void performGenKey() throws Exception {
        int keySize = (Integer) keySizeComboBox.getSelectedItem();
        String algorithms = (String) algorithmComboBox.getSelectedItem();
        if (algorithms == null || algorithms.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select a valid algorithm!");
        }
        PairKey pairKey = AsymmetricTextController.generatePairKey(keySize, algorithms);
        publicKeyField.setText(pairKey.getPublicKey());
        privateKeyField.setText(pairKey.getPrivateKey());
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

    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());

        resultArea = new JTextArea(5, 30);
        resultArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        resultArea.setEditable(false);
        resultPanel.setBorder(BorderFactory.createTitledBorder("Result"));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        return resultPanel;
    }

    private JPanel createActionButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(e -> {
            try {
                performEncrypt();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        JButton decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(e -> {
            try {
                performDecrypt();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);

        return buttonPanel;
    }

    private AsymmetricAlgorithms collection() {
        return AsymmetricAlgorithms.builder().algorithm((String) algorithmComboBox.getSelectedItem()).inputText(textArea.getText()).mode((String) modeComboBox.getSelectedItem()).padding((String) paddingComboBox.getSelectedItem()).publicKey(publicKeyField.getText()).privateKey(privateKeyField.getText()).build();
    }

    private void performReset(JTextField publicKeyField, JTextField privateKeyField) {
        publicKeyField.setText("");
        privateKeyField.setText("");
    }

    private void performEncrypt() throws Exception {
        AsymmetricAlgorithms asymmetricAlgorithms = collection();
        String result = AsymmetricTextController.encypt(asymmetricAlgorithms);
        resultArea.setText(result);
    }

    private void performDecrypt() throws Exception {
        AsymmetricAlgorithms asymmetricAlgorithms = collection();
        String result = AsymmetricTextController.decrypt(asymmetricAlgorithms);
        resultArea.setText(result);
    }

    private void performSave(JTextField publicKey, JTextField privateKey) {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        Integer keySize = (Integer) keySizeComboBox.getSelectedItem();
        if (algorithm == null || keySize == null || publicKey.getText().isEmpty() || privateKey.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Failed");
        } else {
            KeyManager.saveKeys(algorithm, keySize, publicKey.getText(), privateKey.getText());
            JOptionPane.showMessageDialog(null, "Key save successfulll");
        }
    }

    private void addPlaceholder(JTextArea textArea, String placeholder) {
        textArea.setText(placeholder);
        textArea.setForeground(Color.GRAY);

        textArea.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setForeground(Color.GRAY);
                    textArea.setText(placeholder);
                }
            }
        });
    }

}
