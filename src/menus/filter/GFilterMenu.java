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

		for (GMenuConstants.EEditMenuItem eEditMenuItem : GMenuConstants.EEditMenuItem.values()) {
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
				default:
					break;
			}
			add(menuItem);
		}
	}

	public void initialize() {}
	public void blur(){}
	public void unBlur(){}
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
