package org.midterm.view.common;

import javax.swing.*;
import java.io.File;

public class FileChooser {

    private FileChooser() {
        throw new AssertionError();
    }

    public static void addBrowseButtonListener(JButton browseBtn, JTextField filePathField, JPanel parentPanel) {
        browseBtn.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(parentPanel);
            if (frame != null) {
                openFileChooser(filePathField, frame);
            } else {
                JOptionPane.showMessageDialog(parentPanel, "Không tìm thấy cửa sổ cha (JFrame).");
            }
        });
    }

    private static void openFileChooser(JTextField filePathField, JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }
}
