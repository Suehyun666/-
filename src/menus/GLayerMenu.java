package menus;

import global.GMenuConstants.ELayerMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GLayerMenu extends JMenu{
	private static final long serialVersionUID = 1L;
	
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
	}

	public void initialize() {}
	public void hide(){
		System.out.println("Hide");
	}
	public void renamelayer(){
		System.out.println("Rename");
	}
	public void deletelayer(){}
	public void group(){}
	public void duplicate(){}
	public void lock(){}
	public void ungroup(){}
	public void newlayer(){}
	public void merge(){}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ELayerMenuItem eMenuItem= ELayerMenuItem.valueOf(e.getActionCommand());
			switch (eMenuItem) {
				case eHide:
					hide();
					break;
				case eRenameLayer:
					renamelayer();
					break;
				case eDeleteLayer:
					deletelayer();
					break;
				case eGroupLayer:
					group();
					break;
				case eDuplicate:
					duplicate();
					break;
				case eLock:
					lock();
					break;
				case eNewLayer:
					newlayer();
					break;
				case eUnGroupLayer:
					ungroup();
					break;
				case eMerge:
					merge();
					break;
				default:
					break;
			}
		}
	}
}