package menus;

import menus.GMenuConstants.EWindowMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GWindowMenu extends JMenu {
    // attributes
    private static final long serialVersionUID = 1L;
    // constructor
    public GWindowMenu(String label) {
        super(label);
        ActionHandler actionHandler = new ActionHandler();

        for (EWindowMenuItem eWindowMenuItem : EWindowMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eWindowMenuItem.getText());
            menuItem.setActionCommand(eWindowMenuItem.name());
            menuItem.addActionListener(actionHandler);
            add(menuItem);
        }
    }
    public void initialize() {}
    public void history(){}
    public void option(){}
    public void preference(){}
    public void property(){}
    public void tool(){}
    public void extension(){}

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EWindowMenuItem eMenuItem= EWindowMenuItem.valueOf(e.getActionCommand());
            switch (eMenuItem) {
                case eHistory:
                    history();
                    break;
                case eOption:
                    option();
                    break;
                case ePreFerence:
                    preference();
                    break;
                case eProperty:
                    property();
                    break;
                case eTool:
                    tool();
                    break;
                case eExtension:
                    extension();
                    break;
                default:
                    break;
            }
        }
    }
}