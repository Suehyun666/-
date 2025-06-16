package menus;

import controller.AppController;
import language.LanguageManager;
import language.LanguageSupport;
import menus.GMenuConstants.EGraphicMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GGraphicMenu extends JMenu implements LanguageSupport {
    private static final long serialVersionUID = 1L;
    private AppController appController;

    public GGraphicMenu(String text) {
        super(text);
        ActionHandler actionHandler = new ActionHandler();

        for (EGraphicMenuItem eGraphicMenuItem : EGraphicMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eGraphicMenuItem.getText());
            menuItem.setActionCommand(eGraphicMenuItem.name());
            menuItem.addActionListener(actionHandler);
            this.add(menuItem);
        }

        // 언어 변경 리스너 등록
        LanguageManager.getInstance().addLanguageChangeListener(() -> updateLanguage());
    }

    @Override
    public void updateLanguage() {
        setText(LanguageManager.getInstance().getText("graphic.menu"));

        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (command != null) {
                    try {
                        EGraphicMenuItem menuItem = EGraphicMenuItem.valueOf(command);
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

        boolean hasActiveTab = appController.hasActiveTab();
        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item != null) {
                item.setEnabled(hasActiveTab);
            }
        }
    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (appController == null) {
                System.err.println("AppController not set!");
                return;
            }

            try {
                EGraphicMenuItem eMenuItem = EGraphicMenuItem.valueOf(e.getActionCommand());
                invokeMethod(eMenuItem.getMethodName());
                updateMenuState();
            } catch (IllegalArgumentException ex) {
                System.err.println("Unknown menu command: " + e.getActionCommand());
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