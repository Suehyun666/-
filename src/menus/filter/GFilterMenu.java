package menus.filter;

import global.GMenuConstants;
import global.GMenuConstants.EFilterMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GFilterMenu extends JMenu{
	private static final long serialVersionUID = 1L;

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

	public void initialize() {}
	public void blur(){}
	public void noise(){}
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EFilterMenuItem eMenuItem= EFilterMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
				case eBlur:
					blur();
					break;
				case eNoise:
					noise();
					break;
				default:
					break;
			}
		}
	}
}
