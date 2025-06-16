package menus;

import controller.AppController;
import language.LanguageManager;
import language.LanguageSupport;
import menus.GMenuConstants.ELayerMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GLayerMenu extends JMenu implements LanguageSupport {
	private static final long serialVersionUID = 1L;
	private AppController controller;

	public GLayerMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();

		for (ELayerMenuItem eLayerMenuItem : ELayerMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eLayerMenuItem.getText());
			menuItem.setActionCommand(eLayerMenuItem.name());
			menuItem.addActionListener(actionHandler);
			switch (eLayerMenuItem) {
				case eUnGroupLayer:
					menuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK| KeyEvent.SHIFT_DOWN_MASK));
					break;
				case eMerge:
					menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
					break;
				default:
					break;
			}
			add(menuItem);
		}
		LanguageManager.getInstance().addLanguageChangeListener(() -> updateLanguage());
	}
	@Override
	public void updateLanguage() {
		setText(LanguageManager.getInstance().getText("layer.menu"));

		for (int i = 0; i < getItemCount(); i++) {
			JMenuItem item = getItem(i);
			if (item != null) {
				String command = item.getActionCommand();
				if (command != null) {
					try {
						ELayerMenuItem menuItem = ELayerMenuItem.valueOf(command);
						item.setText(menuItem.getText());
					} catch (IllegalArgumentException e) {
					}
				}
			}
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
			GMenuConstants.ELayerMenuItem eMenuItem = GMenuConstants.ELayerMenuItem.valueOf(e.getActionCommand());
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