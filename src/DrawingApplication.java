import dialog.language.SimpleLanguageDialog;
import frames.GMainFrame;
import language.LanguageManager;

import javax.swing.*;

public class DrawingApplication {

    public static void main(String[] args) {
        setSystemLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            showLanguageSelectionAndStart();
        });
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
    }

    private static void showLanguageSelectionAndStart() {
        String selectedLanguage = SimpleLanguageDialog.showDialog(null);

        if (selectedLanguage != null) {
            LanguageManager.getInstance().setLanguage(selectedLanguage);
            startApplication();
        } else {
            System.exit(0);
        }
    }

    private static void startApplication() {
        new GMainFrame();
    }
}