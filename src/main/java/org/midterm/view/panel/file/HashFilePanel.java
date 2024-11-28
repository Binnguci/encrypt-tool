package org.midterm.view.panel.file;

import org.midterm.constant.AlgorithmsConstant;
import org.midterm.controller.HashController;
import org.midterm.view.common.FileChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.net.URL;

public class HashFilePanel extends JPanel {
    JTextField filePathField, resultFilePathField;
    JComboBox<String> algorithmComboBox;

    public static HashFilePanel create() {
        return new HashFilePanel();
    }

    public HashFilePanel() {
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
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createActionButtonPanel());
        mainPanel.add(createResultPathPanel());
        mainPanel.add(Box.createVerticalStrut(20));
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
                    if (event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor) instanceof java.util.List<?> files && files.getFirst() instanceof File file) {
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

        String[] algorithms = {AlgorithmsConstant.MD5,
                AlgorithmsConstant.SHA1,
                AlgorithmsConstant.SHA3,
                AlgorithmsConstant.SHA256,
                AlgorithmsConstant.SHA512,};
        algorithmComboBox = new JComboBox<>(algorithms);
        panel.add(new JLabel("Algorithm:"));
        panel.add(algorithmComboBox);
        return panel;
    }

    private JPanel createActionButtonPanel() {
        JButton encryptButton = new JButton("Hash");
        encryptButton.addActionListener(e -> {
            try {
                performHash();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        return createButtonPanel(new JButton[]{encryptButton});
    }

    private void performHash() {
        String path = filePathField.getText();
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        String result = HashController.hashFile(path, algorithm);
        resultFilePathField.setText(result);
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
        resultFilePathField.setBorder(BorderFactory.createTitledBorder("Result"));
        resultPathPanel.add(resultFilePathField, BorderLayout.CENTER);
        return resultPathPanel;
    }

}
