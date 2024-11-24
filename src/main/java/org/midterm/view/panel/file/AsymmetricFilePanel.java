package org.midterm.view.panel.file;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.controller.AsymmetricFileController;
import org.midterm.controller.AsymmetricTextController;
import org.midterm.factory.EncryptionConfigFactory;
import org.midterm.model.AsymmetricAlgorithms;
import org.midterm.model.PairKey;
import org.midterm.service.KeyManager;
import org.midterm.view.common.FileChooser;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.net.URL;
import java.util.List;

public class AsymmetricFilePanel extends JPanel {
    JTextField filePathField, resultFilePathField, publicKeyField, privateKeyField;
    JComboBox<String> algorithmComboBox, modeComboBox, paddingComboBox;
    JComboBox<Integer> keySizeComboBox;
    public static AsymmetricFilePanel create() {
        return new AsymmetricFilePanel();
    }

    public AsymmetricFilePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)));

        JPanel mainPanel = createMainPanel();
        JPanel paddedPanel = createPaddedPanel(mainPanel);

        add(paddedPanel, BorderLayout.NORTH);
    }

    private JPanel createPaddedPanel(JPanel mainPanel) {
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        paddedPanel.add(mainPanel, BorderLayout.NORTH);
        return paddedPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(createDropPanel());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createFilePathPanel());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createAlgorithmSelectionPanel());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createEncryptionConfigPanel());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createActionButtonPanel());
        mainPanel.add(createResultPathPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createResetButton());
        updateEncryptionConfig();
        return mainPanel;
    }

    private JPanel createDropPanel() {
        JPanel dropPanel = new JPanel(new BorderLayout());
        dropPanel.setPreferredSize(new Dimension(500, 150));
        dropPanel.setBackground(Color.WHITE);
        dropPanel.setBorder(BorderFactory.createTitledBorder("Drag and Drop File Here"));

        JLabel dropLabel = createDropLabel();
        dropPanel.add(dropLabel, BorderLayout.CENTER);

        setupDropTarget(dropPanel);
        return dropPanel;
    }

    private JLabel createDropLabel() {
        JLabel dropLabel = new JLabel("Drag & Drop your file here", SwingConstants.CENTER);
        URL iconURL = getClass().getResource("/img/icons8-file-64.png");
        if (iconURL != null) {
            dropLabel.setIcon(new ImageIcon(iconURL));
        } else {
            System.err.println("Warning: Icon file not found at /img/icons8-file-64.png");
        }
        dropLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        dropLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        return dropLabel;
    }

    private void setupDropTarget(JPanel dropPanel) {
        new DropTarget(dropPanel, new DropTargetListener() {
            @Override
            public void drop(DropTargetDropEvent event) {
                event.acceptDrop(DnDConstants.ACTION_COPY);
                try {
                    if (event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor) instanceof java.util.List<?> files && files.get(0) instanceof File file) {
                        filePathField.setText(file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }

            public void dragEnter(DropTargetDragEvent event) {
            }

            public void dragExit(DropTargetEvent event) {
            }

            public void dragOver(DropTargetDragEvent event) {
            }

            public void dropActionChanged(DropTargetDragEvent event) {
            }
        });
    }

    private JPanel createFilePathPanel() {
        JPanel filePathPanel = new JPanel(new BorderLayout());
        filePathField = new JTextField();
        filePathField.setEditable(true);
        filePathField.setBorder(BorderFactory.createTitledBorder("File Path"));

        JButton browseBtn = createBrowseButton();
        filePathPanel.add(filePathField, BorderLayout.CENTER);
        filePathPanel.add(browseBtn, BorderLayout.EAST);
        return filePathPanel;
    }

    private JButton createBrowseButton() {
        JButton browseBtn = new JButton("Browse");
        FileChooser.addBrowseButtonListener(browseBtn, filePathField, this);
        return browseBtn;
    }

    private JPanel createAlgorithmSelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Select Algorithm"));

        String[] algorithms = {AlgorithmsConstant.RSA};
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
                performGenerateKey();
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
        privateKeyField.setText("");
        publicKeyField.setText("");
    }

    private JPanel createActionButtonPanel() {
        JButton encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(e -> {
            try {
                performEncryption();
            } catch (BadPaddingException ex) {
                System.err.println("Padding error: " + ex.getMessage());
            } catch (IllegalBlockSizeException ex) {
                System.err.println("Block size error: " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("General decryption error: " + ex.getMessage());
            }
        });
        JButton decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(e -> {
            try {
                performDecryption();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return createButtonPanel(new JButton[]{encryptButton, decryptButton});
    }

    private JPanel createButtonPanel(JButton[] buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (JButton button : buttons) {
            panel.add(button);
        }
        return panel;
    }

    private JPanel createResultPathPanel() {
        JPanel resultPathPanel = new JPanel(new BorderLayout());
        resultFilePathField = new JTextField();
        resultFilePathField.setEditable(false);
        resultFilePathField.setBackground(Color.WHITE);
        resultFilePathField.setBorder(BorderFactory.createTitledBorder("Result File Path"));
        resultPathPanel.add(resultFilePathField, BorderLayout.CENTER);
        return resultPathPanel;
    }

    private JButton createResetButton() {
        JButton resetButton = new JButton("Reset All Fields");
        resetButton.addActionListener(e -> resetFields());
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        return resetButton;
    }

    private void resetFields() {
        filePathField.setText("");
        resultFilePathField.setText("");
        algorithmComboBox.setSelectedIndex(0);
        modeComboBox.setSelectedIndex(0);
        paddingComboBox.setSelectedIndex(0);
        privateKeyField.setText("");
        publicKeyField.setText("");
    }

    private void performEncryption() throws Exception {
        AsymmetricAlgorithms asymmetricAlgorithms = collection();
        String encryptedText = AsymmetricFileController.encrypt(asymmetricAlgorithms);
        resultFilePathField.setText(encryptedText);
    }

    private void performDecryption() throws Exception {
        AsymmetricAlgorithms asymmetricAlgorithms = collection();
        String decryptedText = AsymmetricFileController.decrypt(asymmetricAlgorithms);
        resultFilePathField.setText(decryptedText);
    }

    private void performSave(JTextField publicKey, JTextField privateKey) {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        Integer keySize = (Integer) keySizeComboBox.getSelectedItem();
        if (algorithm == null || keySize == null || publicKey.getText().isEmpty() || privateKey.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Failed");
        } else {
            KeyManager.saveKeys(algorithm, keySize, publicKey.getText(), privateKey.getText());
        }
    }



    private void performGenerateKey() throws Exception {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        int keySize = (int) keySizeComboBox.getSelectedItem();
        PairKey key = AsymmetricTextController.generatePairKey(keySize, algorithm);
        publicKeyField.setText(key.getPublicKey());
        privateKeyField.setText(key.getPrivateKey());
    }

    private AsymmetricAlgorithms collection() {
        return AsymmetricAlgorithms.builder()
                .algorithm((String) algorithmComboBox.getSelectedItem())
                .fileInputPath(filePathField.getText())
                .mode((String) modeComboBox.getSelectedItem())
                .padding((String) paddingComboBox.getSelectedItem())
                .publicKey(publicKeyField.getText())
                .privateKey(privateKeyField.getText())
                .build();
    }


}
