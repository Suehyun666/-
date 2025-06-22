package util;

import javax.swing.*;
import java.awt.*;

public class UIUtils {
    public static Frame getParentFrame(Component component) {
        return (Frame) SwingUtilities.getWindowAncestor(component);
    }

    public static void showErrorMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfoMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showConfirmDialog(Component parent, String message, String title) {
        return JOptionPane.showConfirmDialog(parent, message, title,
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
