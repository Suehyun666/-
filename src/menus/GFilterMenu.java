package menus;

import controller.AppController;
import language.LanguageManager;
import language.LanguageSupport;
import menus.GMenuConstants.EFilterMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GFilterMenu extends JMenu implements LanguageSupport {
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

		// 언어 변경 리스너 등록
		LanguageManager.getInstance().addLanguageChangeListener(() -> updateLanguage());
	}

	@Override
	public void updateLanguage() {
		// 메뉴 제목 업데이트
		setText(LanguageManager.getInstance().getText("filter.menu"));

		// 각 메뉴 아이템 텍스트 업데이트
		for (int i = 0; i < getItemCount(); i++) {
			JMenuItem item = getItem(i);
			if (item != null) {
				String command = item.getActionCommand();
				if (command != null) {
					try {
						EFilterMenuItem menuItem = EFilterMenuItem.valueOf(command);
						item.setText(menuItem.getText());
					} catch (IllegalArgumentException e) {
						// 무시 - 유효하지 않은 명령어
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
