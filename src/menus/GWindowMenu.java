package menus;

import controller.AppController;
import language.LanguageManager;
import language.LanguageSupport;
import menus.GMenuConstants.EWindowMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GWindowMenu extends JMenu implements LanguageSupport {
    private static final long serialVersionUID = 1L;
    private AppController appController;

    public GWindowMenu(String text) {
        super(text);
        ActionHandler actionHandler = new ActionHandler();

        for (EWindowMenuItem eWindowMenuItem : EWindowMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eWindowMenuItem.getText());
            menuItem.setActionCommand(eWindowMenuItem.name());
            menuItem.addActionListener(actionHandler);
            this.add(menuItem);
        }

        LanguageManager.getInstance().addLanguageChangeListener(() -> updateLanguage());
    }

    @Override
    public void updateLanguage() {
        setText(LanguageManager.getInstance().getText("window.menu"));

        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (command != null) {
                    try {
                        EWindowMenuItem menuItem = EWindowMenuItem.valueOf(command);
                        item.setText(menuItem.getText());
                    } catch (IllegalArgumentException e) {
                        // 무시
                    }
                }
            }
        }
    }

    public void setController(AppController controller) {
        this.appController = controller;
    }

    public void initialize() {
        updateMenuState();
    }

    public void updateMenuState() {
        if (appController == null) return;

        // Window 메뉴는 항상 활성화
        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item != null) {
                item.setEnabled(true);
            }
        }
    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                EWindowMenuItem eMenuItem = EWindowMenuItem.valueOf(e.getActionCommand());

                switch (eMenuItem) {
                    case eLanguage:
                        showLanguageDialog();
                        break;
                    default:
                        invokeMethod(eMenuItem.getMethodName());
                        break;
                }

                updateMenuState();
            } catch (IllegalArgumentException ex) {
                System.err.println("Unknown menu command: " + e.getActionCommand());
            }
        }
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "한국어", "日本語"};
        String[] languageCodes = {"en", "ko", "ja"};

        String selected = (String) JOptionPane.showInputDialog(
                this,
                LanguageManager.getInstance().getText("language.selectMessage"),
                LanguageManager.getInstance().getText("language.selectTitle"),
                JOptionPane.QUESTION_MESSAGE,
                null,
                languages,
                languages[0]
        );

        if (selected != null) {
            for (int i = 0; i < languages.length; i++) {
                if (languages[i].equals(selected)) {
                    LanguageManager.getInstance().setLanguage(languageCodes[i]);
                    break;
                }
            }
        }
    }

    private void invokeMethod(String methodName) {
        try {
            if (appController != null) {
                appController.getClass().getMethod(methodName).invoke(appController);
            }
        } catch (Exception e) {
            System.err.println("Failed to invoke method: " + methodName);
            e.printStackTrace();
        }
    }
}