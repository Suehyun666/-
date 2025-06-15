package menus;

import controller.AppController;
import menus.GMenuConstants.EFilterMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GFilterMenu extends JMenu{
	private static final long serialVersionUID = 1L;
	private AppController controller;

	public GFilterMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();

		for (GMenuConstants.EFilterMenuItem eFilterMenuItem : GMenuConstants.EFilterMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eFilterMenuItem.getText());
			menuItem.setActionCommand(eFilterMenuItem.name());
			menuItem.addActionListener(actionHandler);
			add(menuItem);
		}
	}

	// initialize
	public void initialize() {}
	public void setController(AppController controller) {
		this.controller = controller;
	}
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			GMenuConstants.EFilterMenuItem eMenuItem = GMenuConstants.EFilterMenuItem.valueOf(e.getActionCommand());
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
