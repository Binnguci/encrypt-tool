package org.midterm.view.panel.text;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.controller.SymmetricTextController;
import org.midterm.factory.EncryptionConfigFactory;
import org.midterm.model.SymmetricAlgorithms;
import org.midterm.view.panel.file.SymmetricFilePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class SymmetricTextPanel extends JPanel {
    private JTextArea textArea;
    private JComboBox<String> algorithmComboBox;
    private JTextField keyField;
    private JTextArea resultArea;
    private JComboBox<String> modeComboBox;
    private JComboBox<String> paddingComboBox;
    private JLabel keySizeLabel;
    private JComboBox<Integer> keySizeComboBox;
    private JLabel ivSizeLabel;
    private JComboBox<Integer> ivSizeComboBox;
    private JTextField ivField;
    private JButton generateButton;
    private JButton generateIvButton;


    public static SymmetricTextPanel create() {
        return new SymmetricTextPanel();
    }

    public SymmetricTextPanel() {
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
    }

    private JPanel createAlgorithmSelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Select Algorithm"));

        String[] algorithms = {
                AlgorithmsConstant.BLOWFISH,
                AlgorithmsConstant.AES, AlgorithmsConstant.DES,
                AlgorithmsConstant.TRIPLEDES,
                AlgorithmsConstant.RC4,
        AlgorithmsConstant.SEED,
        AlgorithmsConstant.IDEA};
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.addActionListener(e ->
                updateEncryptionConfig());

        panel.add(new JLabel("Algorithm:"));
        panel.add(algorithmComboBox);
        return panel;
    }

    private void updateEncryptionConfig() {
        String algorithm = (String) algorithmComboBox.getSelectedItem();

        List<String> modes = EncryptionConfigFactory.getModes(algorithm);
        modeComboBox.setModel(new DefaultComboBoxModel<>(modes.toArray(new String[0])));

        if (!modes.isEmpty()) {
            updatePaddings(algorithm, modes.get(0));
        }

        modeComboBox.addActionListener(e -> {
            String selectedMode = (String) modeComboBox.getSelectedItem();
            updatePaddings(algorithm, selectedMode);
        });

        List<Integer> keySizes = EncryptionConfigFactory.getKeySizes(algorithm);
        keySizeComboBox.setModel(new DefaultComboBoxModel<>(keySizes.toArray(new Integer[0])));

        int ivSize = EncryptionConfigFactory.getIvSize(algorithm);
        ivSizeLabel.setText("IV Size: " + ivSize);

        // Nạp khóa đã lưu (nếu có)
//        String savedKey = KeyManager.loadKey(algorithm);
//        keyField.setText(savedKey != null ? savedKey : "");
        ivField.setText(""); // Đặt lại trường IV
    }

    private void updatePaddings(String algorithm, String mode) {
        List<String> paddings = EncryptionConfigFactory.getPaddings(algorithm, mode);
        paddingComboBox.setModel(new DefaultComboBoxModel<>(paddings.toArray(new String[0])));
    }


    private JPanel createEncryptionConfigPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Encryption Configuration"));

        // Mode selection
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        keyField = new JTextField();

        JPanel ivSizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ivSizeLabel = new JLabel("IV Size:");
        ivSizePanel.add(ivSizeLabel);
        ivField = new JTextField();

        panel.add(modePanel);
        panel.add(paddingPanel);
        panel.add(keySizePanel);
        panel.add(createKeyPanel());
        panel.add(ivSizePanel);
        panel.add(createIVPanel(ivField));

        return panel;
    }

    private JPanel createKeyPanel() {
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyPanel.add(new JLabel("Key:"));
        keyField.setPreferredSize(new Dimension(300, 25));
        keyPanel.add(keyField);
        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> {
            try {
                performGenerateKey();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> performReset(keyField));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> performSaveKey(keyField));
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> performLoadKey(keyField));

        keyPanel.add(generateButton);
        keyPanel.add(resetButton);
        keyPanel.add(saveButton);
        keyPanel.add(loadButton);
        return keyPanel;
    }

    private JPanel createIVPanel(JTextField ivField) {
        JPanel ivPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ivPanel.add(new JLabel("IV:"));
        ivField.setPreferredSize(new Dimension(300, 25));
        ivPanel.add(ivField);
        generateIvButton = new JButton("Generate");
        generateIvButton.addActionListener(e -> {
            try {
                performGenerateIV();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> performReset(ivField));
//        JButton saveButton = new JButton("Save");
        ivPanel.add(generateIvButton);
        ivPanel.add(resetButton);
//        ivPanel.add(saveButton);
        return ivPanel;
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
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    performEncryption();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JButton decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    performDecryption();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);

        return buttonPanel;
    }

    private void performEncryption() throws Exception {
        SymmetricAlgorithms symmetricAlgorithms = collection();
        String encryptedText = SymmetricTextController.encrypt(symmetricAlgorithms);
        resultArea.setText(encryptedText);
    }

    private void performDecryption() throws Exception {
        SymmetricAlgorithms symmetricAlgorithms = collection();
        String decryptedText = SymmetricTextController.decrypt(symmetricAlgorithms);
        resultArea.setText(decryptedText);
    }

    private void performGenerateKey() throws Exception {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        String keySize = keySizeComboBox.getSelectedItem().toString();
        String key = SymmetricTextController.generateKey(algorithm, keySize);
        keyField.setText(key);
    }

    private void performReset(JTextField field) {
        field.setText("");
    }



    private void performGenerateIV() throws Exception {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        String iv = SymmetricTextController.generateIV(algorithm);
        ivField.setText(iv);
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

    private SymmetricAlgorithms collection() {
        return SymmetricAlgorithms.builder()
                .name((String) algorithmComboBox.getSelectedItem())
                .key(keyField.getText())
                .iv(ivField.getText())
                .mode((String) modeComboBox.getSelectedItem())
                .padding((String) paddingComboBox.getSelectedItem())
                .inputText(textArea.getText())
                .build();
    }

    private class AlgorithmSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            String selectedMode = (String) modeComboBox.getSelectedItem();
            switch (selectedAlgorithm) {
                case AlgorithmsConstant.RC4:
                    ivField.setEnabled(false);
                default:
                    break;
            }
            revalidate();
            repaint();
        }
    }

    private void performSaveKey(JTextField field) {
        SymmetricFilePanel.writeKey(field, algorithmComboBox);
    }

    private void performLoadKey(JTextField field) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file chứa Key");

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad))) {
                String line;
                String algorithm = null;
                String key = null;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Thuật toán: ")) {
                        algorithm = line.substring(12).trim();
                    } else if (line.startsWith("Key: ")) {
                        key = line.substring(5).trim();
                    }
                }

                if (algorithm != null && key != null) {
                    algorithmComboBox.setSelectedItem(algorithm);
                    field.setText(key);
                } else {
                    JOptionPane.showMessageDialog(null, "File không hợp lệ hoặc không đầy đủ thông tin!");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi đọc file: " + ex.getMessage());
            }
        }
    }
}
