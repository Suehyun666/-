package menus;

import controller.AppController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GGraphicMenu extends JMenu {
    // attributes
    private static final long serialVersionUID = 1L;
    private AppController controller;
    private JMenuItem lineThicknessItem;
    private JMenuItem lineStyleItem;
    private JMenuItem fontStyleItem;
    private JMenuItem fontSizeItem;
    
    // constructor
    public GGraphicMenu(String label) {
        super(label);
        
        this.lineThicknessItem = new JMenuItem("Line Thickness");
        this.add(lineThicknessItem);
        
        this.lineStyleItem = new JMenuItem("Line Style");
        this.add(lineStyleItem);
        
        this.addSeparator();
        
        this.fontStyleItem = new JMenuItem("Font Style");
        this.add(fontStyleItem);
        
        this.fontSizeItem = new JMenuItem("Font Size");
        this.add(fontSizeItem);
    }

    //initialize
    public void initialize() {}
    public void setController(AppController controller) {
        this.controller = controller;
    }
    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GMenuConstants.EImageMenuItem eMenuItem = GMenuConstants.EImageMenuItem.valueOf(e.getActionCommand());
            invokeMethod(eMenuItem.getMethodName());
        }
    }
    private void invokeMethod(String methodName) {
        try {
            controller.getClass().getMethod(methodName).invoke(controller);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog("아직 메뉴기능 구현 전입니다.");
            e.printStackTrace();
        }
    }
}