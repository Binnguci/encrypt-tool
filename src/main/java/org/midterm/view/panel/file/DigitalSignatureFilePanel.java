package org.midterm.view.panel.file;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.controller.DigitalSignatureTextController;
import org.midterm.factory.EncryptionConfigFactory;
import org.midterm.model.PairKey;
import org.midterm.view.common.FileChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class DigitalSignatureFilePanel extends JPanel {
    private JTextArea textArea;
    private JComboBox<String> signatureCombobox;
    private JTextField publicKeyField, privateKeyField, filePathField;
    private JTextArea resultArea;
    private JComboBox<Integer> keySizeComboBox;

    public DigitalSignatureFilePanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)));
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(createDropPanel());
        mainPanel.add(createFilePathPanel());
        mainPanel.add(createAlgorithmSelectionPanel());
        mainPanel.add(createEncryptionConfigPanel());
        mainPanel.add(createResultPanel());
        mainPanel.add(createActionButtons());

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        paddedPanel.add(mainPanel, BorderLayout.CENTER);

        add(paddedPanel, BorderLayout.NORTH);
        updateEncryptionConfig();
    }

    public static DigitalSignatureFilePanel create() {
        return new DigitalSignatureFilePanel();
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
                    if (event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor) instanceof List<?> files && files.getFirst() instanceof File file) {
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
        saveButton.addActionListener(e -> performSavePairKey(publicKeyField, privateKeyField));
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> performLoadKey(publicKeyField, privateKeyField));
        keyButtonPanel.add(generateButton);
        keyButtonPanel.add(resetButton);
        keyButtonPanel.add(saveButton);
        keyButtonPanel.add(loadButton);
        return keyButtonPanel;
    }

    private void performReset(JTextField publicKeyField, JTextField privateKeyField) {
        publicKeyField.setText("");
        privateKeyField.setText("");
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
        }
    }

    private void performSavePairKey(JTextField publicKeyField, JTextField privateKeyField) {
        String algorithm = (String) signatureCombobox.getSelectedItem();
        String keySize = String.valueOf(keySizeComboBox.getSelectedItem());
        String publicKey = publicKeyField.getText();
        String privateKey = privateKeyField.getText();
        if (algorithm != null && (privateKey != null || publicKey != null) && (!publicKey.isEmpty() || !Objects.requireNonNull(privateKey).isEmpty())) {
            writeKey(algorithm, keySize, publicKey, privateKey);
        } else {
            JOptionPane.showMessageDialog(null, "Key hoặc thuật toán không hợp lệ!");
        }
    }

    static void writeKey(String algorithm, String keySize, String publicKey, String privateKey) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn để lưu Key");
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write("Thuật toán: " + algorithm + "-" + keySize + "\n");
                writer.write("PublicKey: " + publicKey + "\n");
                writer.write("PrivateKey: " + privateKey + "\n");
                JOptionPane.showMessageDialog(null, "Key đã được lưu thành công!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi lưu file: " + ex.getMessage());
            }
        }
    }

    private void performLoadKey(JTextField publicKeyField, JTextField privateKeyField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file chứa Key");
        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad))) {
                String line;
                String publicKey = null;
                String privateKey = null;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("PublicKey:")) {
                        publicKey = line.substring("PublicKey:".length()).trim();
                    } else if (line.startsWith("PrivateKey:")) {
                        privateKey = line.substring("PrivateKey:".length()).trim();
                    }
                }

                if (publicKey != null && privateKey != null) {
                    publicKeyField.setText(publicKey);
                    privateKeyField.setText(privateKey);
                    JOptionPane.showMessageDialog(null, "Key đã được load thành công!");
                } else {
                    JOptionPane.showMessageDialog(null, "File không chứa đủ thông tin PublicKey và PrivateKey!");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi đọc file: " + ex.getMessage());
            }
        }
    }

    private void generateKey() {
        try {
            int keySize = (int) keySizeComboBox.getSelectedItem();
            String algorithm = (String) signatureCombobox.getSelectedItem();

            PairKey pairKey = DigitalSignatureTextController.generatePairKey(keySize, algorithm);
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

            String signature = DigitalSignatureTextController.signData(text, privateKey, algorithm);
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

            boolean isValid = DigitalSignatureTextController.verifySignature(text, signature, publicKey, algorithm);
            JOptionPane.showMessageDialog(this, isValid ? "Signature is valid!" : "Signature is invalid!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error verifying signature: " + e.getMessage());
        }
    }
}
