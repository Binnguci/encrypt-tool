package org.midterm.view.panel.file;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.midterm.view.common.CustomColorButton;
import org.midterm.view.common.FileChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.net.URL;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AsymmetricEncryptFilePanel extends JPanel{

    private JTextField filePathField;
    private JTextField resultFilePathField;
    private JComboBox<String> algorithmComboBox, modeComboBox, paddingComboBox;
    private JTextField keyField;

    public AsymmetricEncryptFilePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel dropPanel = new JPanel(new BorderLayout());
        dropPanel.setPreferredSize(new Dimension(500, 150));
        dropPanel.setBackground(Color.WHITE);
        dropPanel.setBorder(BorderFactory.createTitledBorder("Drag and Drop File Here"));

        JLabel dropLabel = new JLabel("Drag & Drop your file here", SwingConstants.CENTER);
        URL iconURL = getClass().getResource("/img/icons8-file-64.png");
        if (iconURL != null) {
            dropLabel.setIcon(new ImageIcon(iconURL));
        } else {
            System.err.println("Warning: Icon file not found at /img/icons8-file-64.png");
        }

        dropLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        dropLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        dropPanel.add(dropLabel, BorderLayout.CENTER);

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


        mainPanel.add(dropPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        JPanel filePathPanel = new JPanel(new BorderLayout());
        filePathField = new JTextField();
        filePathField.setEditable(true);
        filePathField.setBorder(BorderFactory.createTitledBorder("File Path"));
        filePathPanel.setBackground(Color.WHITE);
        filePathPanel.add(filePathField, BorderLayout.CENTER);

        JButton browseBtn = new JButton("Browse");
        FileChooser.addBrowseButtonListener(browseBtn, filePathField, mainPanel);
        filePathPanel.add(browseBtn, BorderLayout.EAST);

        mainPanel.add(filePathPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        optionsPanel.add(new JLabel("Algorithm:"));
        algorithmComboBox = new JComboBox<>(new String[]{"AES", "DES", "Blowfish", "Hill", "Affine"});
        optionsPanel.add(algorithmComboBox);

        optionsPanel.add(new JLabel("Mode:"));
        modeComboBox = new JComboBox<>(new String[]{"ECB", "CBC", "CFB"});
        optionsPanel.add(modeComboBox);

        optionsPanel.add(new JLabel("Padding:"));
        paddingComboBox = new JComboBox<>(new String[]{"PKCS5", "NoPadding", "ISO10126"});
        optionsPanel.add(paddingComboBox);

        mainPanel.add(optionsPanel);

        JPanel keyPanel = new JPanel(new BorderLayout());
        keyField = new JTextField();
        keyField.setEditable(true);
        keyField.setBorder(BorderFactory.createTitledBorder("Key"));
        keyPanel.add(keyField, BorderLayout.CENTER);
        JPanel keyButtonPanel = new JPanel(new BorderLayout());
        JButton generateKeyButton = new JButton("Generate Key");
        JButton copyKeyButton = new JButton("Copy");
        CustomColorButton.setButtonPressColor(generateKeyButton, "#F26680", Color.WHITE);
        CustomColorButton.setButtonPressColor(copyKeyButton, "#F26680", Color.WHITE);
        keyButtonPanel.add(generateKeyButton, BorderLayout.WEST);
        keyButtonPanel.add(copyKeyButton, BorderLayout.EAST);
        keyPanel.add(keyButtonPanel, BorderLayout.EAST);
        mainPanel.add(keyPanel);
        mainPanel.add(Box.createVerticalStrut(10));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");
        CustomColorButton.setButtonPressColor(encryptButton, "#F26680", Color.WHITE);
        CustomColorButton.setButtonPressColor(decryptButton, "#F26680", Color.WHITE);
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        mainPanel.add(buttonPanel);

        resultFilePathField = new JTextField();
        resultFilePathField.setEditable(false);
        resultFilePathField.setBackground(Color.WHITE);
        resultFilePathField.setBorder(BorderFactory.createTitledBorder("Result File Path"));
        mainPanel.add(resultFilePathField);
        mainPanel.add(Box.createVerticalStrut(20));

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetFields());
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(resetButton);
        mainPanel.add(Box.createVerticalStrut(10));

        encryptButton.addActionListener(e -> updateResultFilePath(true));
        decryptButton.addActionListener(e -> updateResultFilePath(false));

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        paddedPanel.add(mainPanel, BorderLayout.NORTH);

        add(paddedPanel, BorderLayout.NORTH);
    }

    private void updateResultFilePath(boolean isEncrypt) {
        String originalFilePath = filePathField.getText();
        if (originalFilePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please drop a file first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String resultFilePath;
        if (isEncrypt) {
            resultFilePath = originalFilePath + "-encrypt";
        } else {
            resultFilePath = originalFilePath + "-decrypt";
        }

        resultFilePathField.setText(resultFilePath);
    }

    private void resetFields() {
        filePathField.setText("");
        resultFilePathField.setText("");
        algorithmComboBox.setSelectedIndex(0);
        modeComboBox.setSelectedIndex(0);
        paddingComboBox.setSelectedIndex(0);
        keyField.setText("");
    }
}
