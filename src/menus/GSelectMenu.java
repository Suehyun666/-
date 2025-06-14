package menus;

import frames.GMainPanel;
import global.GMenuConstants.ESelectMenuItem;
import shapes.GShape;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GSelectMenu extends JMenu {
	//
	private static final long serialVersionUID = 1L;
	private GMainPanel mainPanel;
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
	public void selectall(){
		this.mainPanel.selectAll();
		this.mainPanel.repaint();
	}
	public void deselect(){
		this.mainPanel.deselectAll();
		this.mainPanel.repaint();
	}
	public void alllayer(){}
	public void reselect(){}
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ESelectMenuItem eMenuItem= ESelectMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
				case eAll:
					selectall();
					break;
				case eAllLayer:
					alllayer();
					break;
				case eDeselect:
					deselect();
					break;
				case eReselect:
					reselect();
					break;
				default:
					break;
			}
		}
	}
	public void associate(GMainPanel mainPanel){
		this.mainPanel = mainPanel;
	}
}
