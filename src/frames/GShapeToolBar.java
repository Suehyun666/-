package frames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import controller.AppController;
import global.GConstants.EShapeTool;

public class GShapeToolBar extends JToolBar {
    // attributes
    private static final long serialVersionUID = 1L;

    //components
    private EShapeTool selectedShape;

    private AppController controller;

    // constructor
    public GShapeToolBar() {
        this.selectedShape = null;
        setOrientation(JToolBar.VERTICAL);
        setFloatable(false);
        setBorderPainted(false);
        setBackground(Color.GRAY);
        ButtonGroup buttongroup = new ButtonGroup();
        ActionHandler actionhandler = new ActionHandler();

        for (EShapeTool eshapeType : EShapeTool.values()) {
            JRadioButton btn = createToolButton(eshapeType.getName(), eshapeType.getName());
            btn.addActionListener(actionhandler);
            btn.setActionCommand(eshapeType.toString());
            buttongroup.add(btn);
            this.add(btn);
        }
    }

    private JRadioButton createToolButton(String text, String tooltip) {
        JRadioButton button = new JRadioButton(text);
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        add(button);
        return button;
    }

    // method

    // initialize
    public void initialize() {
        JRadioButton button =(JRadioButton) this.getComponent(EShapeTool.eSelect.ordinal());
        button.doClick();
    }
    public void setController(AppController controller) {
        this.controller = controller;
    }

    private void invokeMethod(String methodName, String arg) {
        try {
            controller.getClass().getMethod(methodName, String.class).invoke(controller, arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ActionHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String sShapeType = e.getActionCommand();
            selectedShape=EShapeTool.valueOf(sShapeType);
            controller.setEShapeTool(selectedShape);
        }
    }

}
