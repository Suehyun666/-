package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import controller.AppController;
import menus.GMenuConstants.EFileMenuItem;

public class GFileMenu extends JMenu {
    //attribute
    private static final long serialVersionUID = 1L;
    private AppController appController;

    //constructor
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

    // AppController 설정
    public void setController(AppController controller) {
        this.appController = controller;
    }

    //initialize
    public void initialize() {
        updateMenuState();
    }

    private void updateMenuState() {
        // AppController를 통해 메뉴 상태 업데이트
        if (appController != null) {
            boolean hasActiveTab = appController.hasActiveTab();

            for (int i = 0; i < getItemCount(); i++) {
                JMenuItem item = getItem(i);
                if (item != null) {
                    String command = item.getActionCommand();
                    if (command != null) {
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
                    }
                }
            }
        }
    }

    //action handler
    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {

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
                    break;
            }
            updateMenuState();
        }
    }
    private void invokeMethod(String eMenuItem) {
        try {
            this.getClass().getMethod(eMenuItem).invoke(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}