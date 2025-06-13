package menus.edit;

import global.GMenuConstants.EEditMenuItem;
import frames.GMainFrame;
import frames.GMainPanel;
import shapes.GShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;

public class GEditMenu extends JMenu {
    // attributes
    private static final long serialVersionUID = 1L;
    private GMainFrame mainFrame;
    private GMainPanel mainPanel;

    // constructor
    public GEditMenu(String label) {
        super(label);

        ActionHandler actionHandler = new ActionHandler();

        for (EEditMenuItem eEditMenuItem : EEditMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eEditMenuItem.getText());
            menuItem.setActionCommand(eEditMenuItem.name());
            menuItem.addActionListener(actionHandler);

            switch (eEditMenuItem) {
                case eUndo:
                    menuItem.setAccelerator(KeyStroke.getKeyStroke(
                            KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
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

            this.add(menuItem);

            if (eEditMenuItem == EEditMenuItem.eProperty) {
                this.addSeparator();
            }
        }
    }

    // associate
    public void associate(GMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.mainPanel = mainFrame.getMainPanel();
    }

    // method
    public void initialize() {
        // Add initialization logic if needed
    }

    // Edit menu functions
    private void property() {
        if (mainPanel != null) {
            GShape selectedShape = mainPanel.getSelectedShape();
            if (selectedShape != null) {
                GPropertyDialog dialog = new GPropertyDialog(mainFrame, selectedShape);
                if (dialog.showDialog()) {
                    // Apply changes to the shape
                    mainPanel.repaint();
                    mainFrame.setModified(true);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please select a shape first.",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void undo() {
        if (mainPanel != null) {
            mainPanel.undo();
            mainFrame.setModified(true);
        }
    }

    private void forward() {
        if (mainPanel != null) {
            GShape selectedShape = mainPanel.getSelectedShape();
            if (selectedShape != null) {
                //mainPanel.moveShapeForward(selectedShape);
                mainFrame.setModified(true);
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please select a shape first.",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void backward() {
        if (mainPanel != null) {
            GShape selectedShape = mainPanel.getSelectedShape();
            if (selectedShape != null) {
                //mainPanel.moveShapeBackward(selectedShape);
                mainFrame.setModified(true);
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please select a shape first.",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void fade() {
        if (mainPanel != null) {
            GShape selectedShape = mainPanel.getSelectedShape();
            if (selectedShape != null) {
                // Apply fade effect (reduce opacity)
                Color currentColor = selectedShape.getFillColor();
                if (currentColor != null) {
                    int alpha = Math.max(0, currentColor.getAlpha() - 25);
                    Color fadedColor = new Color(
                            currentColor.getRed(),
                            currentColor.getGreen(),
                            currentColor.getBlue(),
                            alpha
                    );
                    selectedShape.setFillColor(fadedColor);
                    mainPanel.repaint();
                    mainFrame.setModified(true);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please select a shape first.",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void cut() {
        if (mainPanel != null) {
            GShape selectedShape = mainPanel.getSelectedShape();
            if (selectedShape != null) {
                mainPanel.cutShape(selectedShape);
                mainFrame.setModified(true);
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please select a shape first.",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void copy() {
        if (mainPanel != null) {
            GShape selectedShape = mainPanel.getSelectedShape();
            if (selectedShape != null) {
                //mainPanel.copyShape(selectedShape);
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please select a shape first.",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void paste() {
        if (mainPanel != null) {
//            if (mainPanel.hasCopiedShape()) {
//                mainPanel.pasteShape();
//                mainFrame.setModified(true);
//            } else {
//                JOptionPane.showMessageDialog(mainFrame,
//                        "Nothing to paste. Please copy or cut a shape first.",
//                        "No Content",
//                        JOptionPane.INFORMATION_MESSAGE);
//            }
        }
    }

    private void clear() {
        if (mainPanel != null) {
            GShape selectedShape = mainPanel.getSelectedShape();
            if (selectedShape != null) {
                int response = JOptionPane.showConfirmDialog(mainFrame,
                        "Are you sure you want to delete the selected shape?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    mainPanel.deleteShape(selectedShape);
                    mainFrame.setModified(true);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please select a shape first.",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void fill() {
        if (mainPanel != null) {
            GShape selectedShape = mainPanel.getSelectedShape();
            if (selectedShape != null) {
                Color newColor = JColorChooser.showDialog(
                        mainFrame,
                        "Choose Fill Color",
                        selectedShape.getFillColor()
                );

                if (newColor != null) {
                    selectedShape.setFillColor(newColor);
                    mainPanel.repaint();
                    mainFrame.setModified(true);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please select a shape first.",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void colorSetting() {

            GColorSettingsDialog dialog = new GColorSettingsDialog(mainFrame);
            if (dialog.showDialog()) {
                // Apply color settings
                Map<String, String> settings = dialog.getSettings();
                System.out.println("Applied color settings: " + settings);
                mainFrame.setModified(true);
            }

    }

    // Inner class for handling actions
    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EEditMenuItem eMenuItem = EEditMenuItem.valueOf(e.getActionCommand());
            switch (eMenuItem) {
                case eProperty:
                    property();
                    break;
                case eUndo:
                    undo();
                    break;
                case eForward:
                    forward();
                    break;
                case eBackward:
                    backward();
                    break;
                case eFade:
                    fade();
                    break;
                case eCut:
                    cut();
                    break;
                case eCopy:
                    copy();
                    break;
                case ePaste:
                    paste();
                    break;
                case eClear:
                    clear();
                    break;
                case eFill:
                    fill();
                    break;
                case eColorSetting:
                    colorSetting();
                    break;
                default:
                    break;
            }
        }
    }
}