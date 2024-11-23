package org.midterm.view.panel.file;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.midterm.constant.AlgorithmsConstant;
import org.midterm.controller.SymmetricFileController;
import org.midterm.controller.SymmetricTextController;
import org.midterm.factory.EncryptionConfigFactory;
import org.midterm.model.SymmetricAlgorithms;
import org.midterm.service.KeyManager;
import org.midterm.view.common.FileChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.net.URL;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SymmetricFilePanel extends JPanel {
    JTextField filePathField, resultFilePathField, keyField, ivField;
    JComboBox<String> algorithmComboBox, modeComboBox, paddingComboBox;
    JLabel ivSizeLabel;
    JComboBox<Integer> keySizeComboBox, ivSizeCombox;
    JButton generateIvButton, resetIvButton, generateKeyButton, copyKeyButton, saveKeyButton;

    public static SymmetricFilePanel create() {
        return new SymmetricFilePanel();
    }

    public SymmetricFilePanel() {
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
                    if (event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor) instanceof List<?> files && files.get(0) instanceof File file) {
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

        String[] algorithms = {AlgorithmsConstant.AES, AlgorithmsConstant.DES, AlgorithmsConstant.BLOWFISH, AlgorithmsConstant.TRIPLEDES, AlgorithmsConstant.RC4};
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.addActionListener(e -> updateEncryptionConfig());

        panel.add(new JLabel("Algorithm:"));
        panel.add(algorithmComboBox);
        return panel;
    }

    private void updateEncryptionConfig() {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        // Update mode options
        List<String> modes = EncryptionConfigFactory.getModes(algorithm);
        modeComboBox.setModel(new DefaultComboBoxModel<>(modes.toArray(new String[0])));

        paddingComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"NoPadding"}));

        modeComboBox.addActionListener(e -> {
            String selectedMode = (String) modeComboBox.getSelectedItem();
            List<String> paddings = EncryptionConfigFactory.getPaddings(algorithm, selectedMode);
            paddingComboBox.setModel(new DefaultComboBoxModel<>(paddings.toArray(new String[0])));
        });

        // Update key sizes
        List<Integer> keySizes = EncryptionConfigFactory.getKeySizes(algorithm);
        keySizeComboBox.setModel(new DefaultComboBoxModel<>(keySizes.toArray(new Integer[0])));

        // Update IV size
        int ivSize = EncryptionConfigFactory.getIvSize(algorithm);
        ivSizeLabel.setText("IV Size: " + ivSize);

        String savedKey = KeyManager.loadKey(algorithm);
        keyField.setText(savedKey);
        ivField.setText("");
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

    private JPanel createIVPanel(JTextField ivField) {
        JPanel ivPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ivPanel.add(new JLabel("IV:"));
        ivField.setPreferredSize(new Dimension(300, 25));
        ivPanel.add(ivField);
        generateIvButton = new JButton("Generate");
        generateIvButton.addActionListener(e -> {
            try {
                performGenerateIv();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> ivField.setText(""));
//        JButton saveButton = new JButton("Save");
        ivPanel.add(generateIvButton);
        ivPanel.add(resetButton);
//        ivPanel.add(saveButton);
        return ivPanel;
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
        resetButton.addActionListener(e -> keyField.setText(""));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> performSave(keyField));
        keyPanel.add(generateButton);
        keyPanel.add(resetButton);
        keyPanel.add(saveButton);
        return keyPanel;
    }

    private JPanel createActionButtonPanel() {
        JButton encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(e -> {
            try {
                performEncryption();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
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
        keyField.setText("");
        ivField.setText("");
    }

    private void performEncryption() throws Exception {
        SymmetricAlgorithms symmetricAlgorithms = collection();
        String encryptedText = SymmetricFileController.encrypt(symmetricAlgorithms);
        resultFilePathField.setText(encryptedText);
    }

    private void performDecryption() throws Exception {
        SymmetricAlgorithms symmetricAlgorithms = collection();
        String decryptedText = SymmetricFileController.decrypt(symmetricAlgorithms);
        resultFilePathField.setText(decryptedText);
    }

    private void performGenerateIv() throws Exception {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        String iv = SymmetricTextController.generateIV(algorithm);
        ivField.setText(iv);
    }

    private void performSave(JTextField field) {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        String key = field.getText();
        if (algorithm != null && key != null && !key.isEmpty()) {
            KeyManager.saveKey(algorithm, key);
            JOptionPane.showMessageDialog(null, "Key saved successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Key cannot be empty!");
        }

    }

    private void performGenerateKey() throws Exception {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        String keySize = keySizeComboBox.getSelectedItem().toString();
        String key = SymmetricTextController.generateKey(algorithm, keySize);
        keyField.setText(key);
    }

    private SymmetricAlgorithms collection() {
        return SymmetricAlgorithms.builder()
                .name((String) algorithmComboBox.getSelectedItem())
                .key(keyField.getText())
                .iv(ivField.getText())
                .filePath(filePathField.getText())
                .mode((String) modeComboBox.getSelectedItem())
                .padding((String) paddingComboBox.getSelectedItem())
                .build();
    }
}
