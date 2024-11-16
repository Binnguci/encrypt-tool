package org.midterm.view.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomColorButton {
    private CustomColorButton(){
        throw new AssertionError();
    }

    public static void setButtonPressColor(JButton button, String pressedColorHex, Color defaultColor) {
        button.setBackground(defaultColor);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(Color.decode(pressedColorHex));
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(defaultColor);
                button.setForeground(Color.BLACK);

            }
        });
    }

}
