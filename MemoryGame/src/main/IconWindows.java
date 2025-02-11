package main;

import javax.swing.*;

public class IconWindows {
    public static void setWindowIcon(JFrame frame) {
        ImageIcon icon = new ImageIcon("images/icono.png");
        frame.setIconImage(icon.getImage());
    }
}
