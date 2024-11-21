package org.midterm.view;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;

public class UIManagerSetup {
    private UIManagerSetup() {
        throw new AssertionError("Can't create instance of this class");
    }
    public static void applyFlatLafTheme() {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ToolBar.background", Color.WHITE);
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Unsupported Look and Feel");
        }
    }
}
