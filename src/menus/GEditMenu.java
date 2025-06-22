package menus;

import controller.AppController;
import language.LanguageManager;
import language.LanguageSupport;
import menus.GMenuConstants.EEditMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GEditMenu extends JMenu implements LanguageSupport {
    private static final long serialVersionUID = 1L;
    private AppController appController;

    public GEditMenu(String label) {
        super(label);
        ActionHandler actionHandler = new ActionHandler();

        for (EEditMenuItem eEditMenuItem : EEditMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eEditMenuItem.getText());
            menuItem.setActionCommand(eEditMenuItem.name());
            menuItem.addActionListener(actionHandler);

            setAccelerator(menuItem, eEditMenuItem);
            this.add(menuItem);

            if (eEditMenuItem == EEditMenuItem.eProperty) {
                this.addSeparator();
            }
        }

        // 언어 변경 리스너 등록
        LanguageManager.getInstance().addLanguageChangeListener(() -> updateLanguage());
    }

    @Override
    public void updateLanguage() {
        setText(LanguageManager.getInstance().getText("edit.menu"));

        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (command != null) {
                    try {
                        EEditMenuItem menuItem = EEditMenuItem.valueOf(command);
                        item.setText(menuItem.getText());
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        }
    }

    private void setAccelerator(JMenuItem menuItem, EEditMenuItem eEditMenuItem) {
        switch (eEditMenuItem) {
            case eUndo:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eRedo:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eCut:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eCopy:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
                break;
            case ePaste:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eClear:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
                break;
            case eFill:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eForward:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eBackward:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, KeyEvent.CTRL_DOWN_MASK));
                break;
            default:
                break;
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
        boolean hasSelection = appController.hasSelection();
        boolean canUndo = appController.canUndo();
        boolean canRedo = appController.canRedo();

        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (command != null) {
                    try {
                        EEditMenuItem menuItem = EEditMenuItem.valueOf(command);
                        switch (menuItem) {
                            case eUndo:
                                item.setEnabled(hasActiveTab && canUndo);
                                break;
                            case eRedo:
                                item.setEnabled(hasActiveTab && canRedo);
                                break;
                            case eCut:
                            case eCopy:
                            case eClear:
                            case eFill:
                            case eProperty:
                            case eForward:
                            case eBackward:
                            case eFade:
                                item.setEnabled(hasActiveTab && hasSelection);
                                break;
                            case ePaste:
                                item.setEnabled(hasActiveTab);
                                break;
                            case eColorSetting:
                                item.setEnabled(hasActiveTab);
                                break;
                            default:
                                item.setEnabled(hasActiveTab);
                                break;
                        }
                    } catch (IllegalArgumentException e) {
                        item.setEnabled(hasActiveTab);
                    }
                }
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
                EEditMenuItem eMenuItem = EEditMenuItem.valueOf(e.getActionCommand());

                switch (eMenuItem) {
                    case eProperty:
                        appController.showProperties();
                        break;
                    case eUndo:
                        appController.undo();
                        break;
                    case eRedo:
                        appController.redo();
                        break;
                    case eForward:
                        appController.bringForward();
                        break;
                    case eBackward:
                        appController.sendBackward();
                        break;
                    case eToForward:
                        appController.bringToFront();
                        break;
                    case eToBackward:
                        appController.sendToBack();
                    break;
                    case eFade:
                        // 추후 구현
                        break;
                    case eCut:
                        appController.cut();
                        break;
                    case eCopy:
                        appController.copy();
                        break;
                    case ePaste:
                        appController.paste();
                        break;
                    case eClear:
                        appController.deleteSelected();
                        break;
                    case eFill:
                        appController.fill();
                        break;
                    case eColorSetting:
                        appController.colorSetting();
                        break;
                    default:
                        // 기존 invoke 메서드를 백업으로 사용
                        invokeMethod(eMenuItem.getMethodName());
                        break;
                }

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