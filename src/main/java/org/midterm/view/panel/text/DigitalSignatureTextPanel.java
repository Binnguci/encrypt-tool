/*package org.midterm.view.panel.text;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.controller.AsymmetricTextController;
import org.midterm.controller.DigitalSignatureController;
import org.midterm.factory.EncryptionConfigFactory;
import org.midterm.model.PairKey;
import org.midterm.service.KeyManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DigitalSignatureTextPanel extends JPanel {
    private JTextArea textArea;
    private JComboBox<String> signatureCombobox;
    private JTextField publicKeyField, privateKeyField;
    private JTextArea resultArea;
    private JComboBox<Integer> keySizeComboBox;

    public static DigitalSignatureTextPanel create() {
        return new DigitalSignatureTextPanel();
    }

    public DigitalSignatureTextPanel() {
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
        panel.setBorder(BorderFactory.createTitledBorder("Select Signature"));

        String[] algorithms = {
                AlgorithmsConstant.SHA1WITHDSA,
                AlgorithmsConstant.SHA256WITHDSA,
                AlgorithmsConstant.SHA1WITHRSA,
                AlgorithmsConstant.SHA256WITHRSA,
                AlgorithmsConstant.SHA512WITHRSA
        };
        signatureCombobox = new JComboBox<>(algorithms);
        signatureCombobox.addActionListener(e -> updateEncryptionConfig());

        panel.add(new JLabel("Signature:"));
        panel.add(signatureCombobox);
        return panel;
    }

    private void updateEncryptionConfig() {
        String algorithm = (String) signatureCombobox.getSelectedItem();

        if (algorithm != null) {
            List<Integer> keySizes = EncryptionConfigFactory.getKeyByDigitalSignatureAlgorithm(algorithm);
            keySizeComboBox.setModel(new DefaultComboBoxModel<>(keySizes.toArray(new Integer[0])));
            loadKey();
        }
    }

    private void loadKey() {
        String algorithm = (String) signatureCombobox.getSelectedItem();
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

        JPanel keySizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keySizePanel.add(new JLabel("Key Size:"));
        keySizeComboBox = new JComboBox<>();
        keySizePanel.add(keySizeComboBox);
        publicKeyField = new JTextField();
        privateKeyField = new JTextField();

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
        String algorithms = (String) signatureCombobox.getSelectedItem();
        if (algorithms == null || algorithms.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select a valid algorithm!");
        }
        PairKey pairKey = DigitalSignatureController.generatePairKey(keySize, algorithms);
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

        JButton signButton = new JButton("Sign");
        signButton.addActionListener(e -> {
            performSign();
        });
        buttonPanel.add(signButton);

        return buttonPanel;
    }

    private void performSign() {
        String algorithm = (String) signatureCombobox.getSelectedItem();
        String text = textArea.getText();
        String privateKey = privateKeyField.getText();
        if (algorithm == null || text.isEmpty() || privateKey.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Failed");
        } else {
            try {
                String signature = DigitalSignatureController.signData(text, privateKey, algorithm);
                resultArea.setText(signature);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed");
            }
        }
    }

    private void performReset(JTextField publicKeyField, JTextField privateKeyField) {
        publicKeyField.setText("");
        privateKeyField.setText("");
    }

    private void performSave(JTextField publicKey, JTextField privateKey) {
        String algorithm = (String) signatureCombobox.getSelectedItem();
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

}*/
package org.midterm.view.panel.text;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.controller.DigitalSignatureController;
import org.midterm.factory.EncryptionConfigFactory;
import org.midterm.model.PairKey;
import org.midterm.service.KeyManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DigitalSignatureTextPanel extends JPanel {
    private JTextArea textArea;
    private JComboBox<String> signatureCombobox;
    private JTextField publicKeyField, privateKeyField;
    private JTextArea resultArea;
    private JComboBox<Integer> keySizeComboBox;

    public DigitalSignatureTextPanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)));
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

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
    }

    public static DigitalSignatureTextPanel create() {
        return new DigitalSignatureTextPanel();
    }

    private JPanel createAlgorithmSelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Select Signature"));

        String[] algorithms = {
                AlgorithmsConstant.SHA1WITHDSA,
                AlgorithmsConstant.SHA256WITHDSA,
                AlgorithmsConstant.SHA1WITHRSA,
                AlgorithmsConstant.SHA256WITHRSA,
        };
        signatureCombobox = new JComboBox<>(algorithms);
        signatureCombobox.addActionListener(e -> updateEncryptionConfig());

        panel.add(new JLabel("Signature:"));
        panel.add(signatureCombobox);
        return panel;
    }

    private JPanel createEncryptionConfigPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Encryption Configuration"));

        JPanel keySizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keySizePanel.add(new JLabel("Key Size:"));
        keySizeComboBox = new JComboBox<>(new Integer[]{2048, 4096});
        keySizePanel.add(keySizeComboBox);

        publicKeyField = new JTextField(30);
        privateKeyField = new JTextField(30);

        JPanel publicKeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        publicKeyPanel.add(new JLabel("Public Key:"));
        publicKeyPanel.add(publicKeyField);

        JPanel privateKeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        privateKeyPanel.add(new JLabel("Private Key:"));
        privateKeyPanel.add(privateKeyField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton generateButton = new JButton("Generate Key");
        generateButton.addActionListener(e -> generateKey());
        buttonPanel.add(generateButton);

        panel.add(keySizePanel);
        panel.add(publicKeyPanel);
        panel.add(privateKeyPanel);
//        panel.add(buttonPanel);
        panel.add(createButtonKeyPanel());
        return panel;
    }

    private JPanel createTextInputPanel() {
        JPanel textInputPanel = new JPanel(new BorderLayout());
        textArea = new JTextArea(5, 30);
        textInputPanel.setBorder(BorderFactory.createTitledBorder("Input Text"));
        textInputPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return textInputPanel;
    }

    private JPanel createButtonKeyPanel() {
        JPanel keyButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> {
            try {
                generateKey();
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

    private void performReset(JTextField publicKeyField, JTextField privateKeyField) {
        publicKeyField.setText("");
        privateKeyField.setText("");
    }

    private void performSave(JTextField publicKey, JTextField privateKey) {
        String algorithm = (String) signatureCombobox.getSelectedItem();
        Integer keySize = (Integer) keySizeComboBox.getSelectedItem();
        if (algorithm == null || keySize == null || publicKey.getText().isEmpty() || privateKey.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Failed");
        } else {
            KeyManager.saveKeys(algorithm, keySize, publicKey.getText(), privateKey.getText());
            JOptionPane.showMessageDialog(null, "Key save successfulll");
        }
    }

    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        resultPanel.setBorder(BorderFactory.createTitledBorder("Signature/Verification Result"));
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        return resultPanel;
    }

    private JPanel createActionButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton signButton = new JButton("Sign");
        signButton.addActionListener(e -> signData());
        buttonPanel.add(signButton);

        JButton verifyButton = new JButton("Verify");
        verifyButton.addActionListener(e -> verifySignature());
        buttonPanel.add(verifyButton);

        return buttonPanel;
    }

    private void updateEncryptionConfig() {
        String algorithm = (String) signatureCombobox.getSelectedItem();

        if (algorithm != null) {
            List<Integer> keySizes = EncryptionConfigFactory.getKeyByDigitalSignatureAlgorithm(algorithm);
            keySizeComboBox.setModel(new DefaultComboBoxModel<>(keySizes.toArray(new Integer[0])));
            loadKey();
        }
    }
    private void loadKey() {
        String algorithm = (String) signatureCombobox.getSelectedItem();
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

    private void generateKey() {
        try {
            int keySize = (int) keySizeComboBox.getSelectedItem();
            String algorithm = (String) signatureCombobox.getSelectedItem();

            PairKey pairKey = DigitalSignatureController.generatePairKey(keySize, algorithm);
            publicKeyField.setText(pairKey.getPublicKey());
            privateKeyField.setText(pairKey.getPrivateKey());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating keys: " + e.getMessage());
        }
    }

    private void signData() {
        try {
            String algorithm = (String) signatureCombobox.getSelectedItem();
            String text = textArea.getText();
            String privateKey = privateKeyField.getText();

            if (text.isEmpty() || privateKey.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Text and Private Key cannot be empty!");
                return;
            }

            String signature = DigitalSignatureController.signData(text, privateKey, algorithm);
            resultArea.setText(signature);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error signing data: " + e.getMessage());
        }
    }

    private void verifySignature() {
        try {
            String algorithm = (String) signatureCombobox.getSelectedItem();
            String text = textArea.getText();
            String signature = resultArea.getText();
            String publicKey = publicKeyField.getText();

            if (text.isEmpty() || signature.isEmpty() || publicKey.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Text, Signature, and Public Key cannot be empty!");
                return;
            }

            boolean isValid = DigitalSignatureController.verifySignature(text, signature, publicKey, algorithm);
            JOptionPane.showMessageDialog(this, isValid ? "Signature is valid!" : "Signature is invalid!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error verifying signature: " + e.getMessage());
        }
    }
}
