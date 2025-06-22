package menus;

import controller.AppController;
import frames.GMainPanel;
import language.LanguageManager;
import language.LanguageSupport;
import menus.GMenuConstants.EImageMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GImageMenu extends JMenu implements LanguageSupport {
	private static final long serialVersionUID = 1L;
	private AppController controller;

	public GImageMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();

		for (EImageMenuItem eImageMenuItem : EImageMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eImageMenuItem.getText());
			menuItem.setActionCommand(eImageMenuItem.name());
			menuItem.addActionListener(actionHandler);

			switch (eImageMenuItem) {
				case eImageSize:
					menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
					break;
				case eCanvasSize:
					menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK| KeyEvent.ALT_DOWN_MASK));
					break;
				case eGenerateImage:
					menuItem.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_PLUS, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
					break;
				default:
					break;
			}
			add(menuItem);
		}LanguageManager.getInstance().addLanguageChangeListener(() -> updateLanguage());
	}
	@Override
	public void updateLanguage() {
		// 메뉴 제목 업데이트
		setText(LanguageManager.getInstance().getText("image.menu"));

		// 각 메뉴 아이템 텍스트 업데이트
		for (int i = 0; i < getItemCount(); i++) {
			JMenuItem item = getItem(i);
			if (item != null) {
				String command = item.getActionCommand();
				if (command != null) {
					try {
						EImageMenuItem menuItem = EImageMenuItem.valueOf(command);
						item.setText(menuItem.getText());
					} catch (IllegalArgumentException e) {
						// 무시 - 유효하지 않은 명령어
					}
				}
			}
		}
	}
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
			e.printStackTrace();
		}
	}
}