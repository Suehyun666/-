package menus.image;

import frames.GMainPanel;
import global.GMenuConstants.EImageMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GImageMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private GMainPanel mainPanel;

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
		}
	}

	public void initialize() {}
	public void getImageSize(){}
	public void generateImage(){

	}
	public void getCanvasSize(){}
	public void mode(){}
	public void rotate(){}
	public void duplicate(){}
	public void crop(){}
	public void trim(){}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EImageMenuItem eMenuItem= EImageMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
				case eImageSize:
					getImageSize();
					break;
				case eGenerateImage:
					generateImage();
					break;
				case eCanvasSize:
					getCanvasSize();
					break;
				case eCrop:
					crop();
					break;
				case eDuplicate:
					duplicate();
					break;
				case eMode:
					mode();
					break;
				case eRotate:
					rotate();
					break;
				case eTrim:
					trim();
					break;
				default:
					break;
			}
		}
	}
	public void associate(GMainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
}