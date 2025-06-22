package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import controller.AppController;
import language.LanguageManager;
import language.LanguageSupport;
import menus.GMenuConstants.EFileMenuItem;

public class GFileMenu extends JMenu implements LanguageSupport {
    private static final long serialVersionUID = 1L;
    private AppController appController;

    public GFileMenu(String text) {
        super(text);
        ActionHandler actionHandler = new ActionHandler();

        for (EFileMenuItem eFileMenuItem : EFileMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eFileMenuItem.getText());
            menuItem.setActionCommand(eFileMenuItem.name());
            menuItem.addActionListener(actionHandler);

            // 키보드 단축키 설정
            setAccelerator(menuItem, eFileMenuItem);
            this.add(menuItem);
        }

        // 언어 변경 리스너 등록
        LanguageManager.getInstance().addLanguageChangeListener(() -> updateLanguage());
    }

    @Override
    public void updateLanguage() {
        // 메뉴 제목 업데이트
        setText(LanguageManager.getInstance().getText("file.menu"));

        // 각 메뉴 아이템 텍스트 업데이트
        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (command != null) {
                    try {
                        EFileMenuItem menuItem = EFileMenuItem.valueOf(command);
                        item.setText(menuItem.getText());
                    } catch (IllegalArgumentException e) {
                        // 무시 - 유효하지 않은 명령어
                    }
                }
            }
        }
    }

    private void setAccelerator(JMenuItem menuItem, EFileMenuItem eFileMenuItem) {
        switch (eFileMenuItem) {
            case eNew:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eOpen:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eSave:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eSaveAs:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
                break;
            case eClose:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
                break;
            case ePrint:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eExit:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
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

    private void updateMenuState() {
        if (appController != null) {
            boolean hasActiveTab = appController.hasActiveTab();
            for (int i = 0; i < getItemCount(); i++) {
                JMenuItem item = getItem(i);
                if (item != null) {
                    String command = item.getActionCommand();
                    if (command != null) {
                        try {
                            EFileMenuItem menuItem = EFileMenuItem.valueOf(command);
                            switch (menuItem) {
                                case eSave:
                                case eSaveAs:
                                case eClose:
                                case closeCurrentTab:
                                case ePrint:
                                case eExport:
                                    item.setEnabled(hasActiveTab);
                                    break;
                                default:
                                    item.setEnabled(true);
                                    break;
                            }
                        } catch (IllegalArgumentException e) {
                            // 무시 - 유효하지 않은 명령어
                        }
                    }
                }
            }
        }
    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (appController == null) {
                System.err.println("AppController not set!");
                return;
            }

            try {
                EFileMenuItem eMenuItem = EFileMenuItem.valueOf(event.getActionCommand());
                switch (eMenuItem) {
                    case eNew:
                        appController.newFile();
                        break;
                    case eOpen:
                        appController.openFile();
                        break;
                    case eSave:
                        appController.saveFile();
                        break;
                    case eSaveAs:
                        appController.saveAsFile();
                        break;
                    case eClose:
                        appController.closeFile();
                        break;
                    case closeCurrentTab:
                        appController.closeTab();
                        break;
                    case ePrint:
                        appController.printFile();
                        break;
                    case eImport:
                        appController.importFile();
                        break;
                    case eExport:
                        appController.exportFile();
                        break;
                    case eExit:
                        appController.exit();
                        break;
                    default:
                        // 기존 invoke 메서드를 백업으로 사용
                        invokeMethod(eMenuItem.getMethodName());
                        break;
                }
                updateMenuState();
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown menu command: " + event.getActionCommand());
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