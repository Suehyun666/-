package menus;

import controller.AppController;
import menus.GMenuConstants.EEditMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GEditMenu extends JMenu {
    // attributes
    private static final long serialVersionUID = 1L;
    private AppController appController;

    // constructor
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
    }

    private void setAccelerator(JMenuItem menuItem, EEditMenuItem eEditMenuItem) {
        switch (eEditMenuItem) {
            case eUndo:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eRedo:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eCut:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eCopy:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
                break;
            case ePaste:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
                break;
            case eClear:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_DELETE, 0));
                break;
            case eFill:
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
                break;
            default:
                break;
        }
    }

    // Controller
    public void setController(AppController controller) {
        this.appController = controller;
    }

    // method
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
                                // 클립보드에 내용이 있는지 확인
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
                        // TODO: 속성 대화상자는 별도 처리 필요
                        showPropertyDialog();
                        break;
                    case eUndo:
                        appController.undo();
                        break;
                    case eRedo:
                        appController.redo();
                        break;
                    case eForward:
                        // TODO: 도형 순서 변경 - 향후 구현
                        break;
                    case eBackward:
                        // TODO: 도형 순서 변경 - 향후 구현
                        break;
                    case eFade:
                        // TODO: 투명도 조정 - 향후 구현
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
                        // TODO: 색상 선택 대화상자 - 향후 구현
                        showFillColorDialog();
                        break;
                    case eColorSetting:
                        // TODO: 색상 설정 대화상자 - 향후 구현
                        showColorSettingDialog();
                        break;
                    default:
                        System.err.println("Unhandled edit menu item: " + eMenuItem);
                        break;
                }

                updateMenuState();

            } catch (IllegalArgumentException ex) {
                System.err.println("Unknown menu command: " + e.getActionCommand());
            }
        }

        private void showPropertyDialog() {
            // TODO: 속성 대화상자 구현
            JOptionPane.showMessageDialog(GEditMenu.this,
                    "Property dialog - To be implemented",
                    "Property",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        private void showFillColorDialog() {
            // TODO: 색상 선택 대화상자 구현
            JOptionPane.showMessageDialog(GEditMenu.this,
                    "Fill color dialog - To be implemented",
                    "Fill Color",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        private void showColorSettingDialog() {
            // TODO: 색상 설정 대화상자 구현
            JOptionPane.showMessageDialog(GEditMenu.this,
                    "Color setting dialog - To be implemented",
                    "Color Settings",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}