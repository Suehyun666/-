package menus;

import controller.AppController;
import frames.GMainPanel;
import menus.GMenuConstants.ESelectMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GSelectMenu extends JMenu {
	//
	private static final long serialVersionUID = 1L;
	private AppController controller;
	//constructor
	public GSelectMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();

		for (ESelectMenuItem eSelectMenuItem : ESelectMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eSelectMenuItem.getText());
			menuItem.setActionCommand(eSelectMenuItem.name());
			menuItem.addActionListener(actionHandler);

			switch (eSelectMenuItem) {
				case eAll:
					menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
					break;
				case eDeselect:
					menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
					break;
				case eReselect:
					menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK ));
					break;
				case eAllLayer:
					menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
					break;
				default:
					break;
			}
			add(menuItem);
		}
	}

	public void initialize() {}
	public void setController(AppController controller) {
		this.controller = controller;
	}
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ESelectMenuItem eMenuItem = ESelectMenuItem.valueOf(e.getActionCommand());
			invokeMethod(eMenuItem.getMethodName());
		}
	}
	private void invokeMethod(String methodName) {
		try {
			controller.getClass().getMethod(methodName).invoke(controller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
