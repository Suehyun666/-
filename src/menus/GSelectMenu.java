package menus;

import controller.AppController;
import language.LanguageManager;
import language.LanguageSupport;
import menus.GMenuConstants.ESelectMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GSelectMenu extends JMenu implements LanguageSupport {
	private static final long serialVersionUID = 1L;
	private AppController controller;

	public GSelectMenu(String text) {
		super(text);
		ActionHandler actionHandler = new ActionHandler();

		for (ESelectMenuItem eSelectMenuItem : ESelectMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eSelectMenuItem.getText());
			menuItem.setActionCommand(eSelectMenuItem.name());
			menuItem.addActionListener(actionHandler);

			setAccelerator(menuItem, eSelectMenuItem);
			add(menuItem);
		}

		// 언어 변경 리스너 등록
		LanguageManager.getInstance().addLanguageChangeListener(() -> updateLanguage());
	}

	@Override
	public void updateLanguage() {
		// 메뉴 제목 업데이트
		setText(LanguageManager.getInstance().getText("select.menu"));

		// 각 메뉴 아이템 텍스트 업데이트
		for (int i = 0; i < getItemCount(); i++) {
			JMenuItem item = getItem(i);
			if (item != null) {
				String command = item.getActionCommand();
				if (command != null) {
					try {
						ESelectMenuItem menuItem = ESelectMenuItem.valueOf(command);
						item.setText(menuItem.getText());
					} catch (IllegalArgumentException e) {
						// 무시 - 유효하지 않은 명령어
					}
				}
			}
		}
	}

	private void setAccelerator(JMenuItem menuItem, ESelectMenuItem eSelectMenuItem) {
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
						KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
				break;
			case eAllLayer:
				menuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
				break;
			default:
				break;
		}
	}

	public void initialize() {
		updateMenuState();
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}

	public void updateMenuState() {
		if (controller == null) return;

		boolean hasActiveTab = controller.hasActiveTab();
		boolean hasSelection = controller.hasSelection();

		for (int i = 0; i < getItemCount(); i++) {
			JMenuItem item = getItem(i);
			if (item != null) {
				String command = item.getActionCommand();
				if (command != null) {
					try {
						ESelectMenuItem menuItem = ESelectMenuItem.valueOf(command);
						switch (menuItem) {
							case eAll:
							case eAllLayer:
								item.setEnabled(hasActiveTab);
								break;
							case eDeselect:
							case eReselect:
								item.setEnabled(hasActiveTab && hasSelection);
								break;
							default:
								item.setEnabled(hasActiveTab);
								break;
						}
					} catch (IllegalArgumentException e) {
						item.setEnabled(hasActiveTab);
					}
				}
			}
		}
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (controller == null) {
				System.err.println("Controller not set!");
				return;
			}

			try {
				ESelectMenuItem eMenuItem = ESelectMenuItem.valueOf(e.getActionCommand());

				switch (eMenuItem) {
					case eAll:
						controller.selectAll();
						break;
					case eDeselect:
						controller.deselect();
						break;
					case eReselect:
						controller.reselect();
						break;
					case eAllLayer:
						controller.selectAllLayer();
						break;
					default:
						invokeMethod(eMenuItem.getMethodName());
						break;
				}

				updateMenuState();

			} catch (IllegalArgumentException ex) {
				System.err.println("Unknown menu command: " + e.getActionCommand());
			}
		}
	}

	private void invokeMethod(String methodName) {
		try {
			if (controller != null) {
				controller.getClass().getMethod(methodName).invoke(controller);
			}
		} catch (Exception e) {
			System.err.println("Failed to invoke method: " + methodName);
			e.printStackTrace();
		}
	}
}