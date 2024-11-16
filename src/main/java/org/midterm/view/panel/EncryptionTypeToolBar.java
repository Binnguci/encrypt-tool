package org.midterm.view.panel;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.awt.*;

@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EncryptionTypeToolBar extends JToolBar {

    public interface ModeChangeListener {
        void onModeChange(String mode);
    }

    ModeChangeListener modeChangeListener;

    public EncryptionTypeToolBar() {
        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        setPreferredSize(new Dimension(getPreferredSize().width, 50));

        JRadioButton textRadioButton = new JRadioButton("Text", true);
        JRadioButton fileRadioButton = new JRadioButton("File");

        ButtonGroup group = new ButtonGroup();
        group.add(textRadioButton);
        group.add(fileRadioButton);

        JLabel label = new JLabel("Choose: ");
        add(label);
        add(textRadioButton);
        add(fileRadioButton);

        textRadioButton.addActionListener(e -> notifyModeChange("Text"));
        fileRadioButton.addActionListener(e -> notifyModeChange("File"));
    }


    private void notifyModeChange(String mode) {
        if (modeChangeListener != null) {
            modeChangeListener.onModeChange(mode);
        }
    }
}

