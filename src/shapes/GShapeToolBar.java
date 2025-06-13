package shapes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import constants.ShapeType;
import frames.GMainFrame;
import frames.GMainPanel;
import global.GConstants.EShapeTool;

public class GShapeToolBar extends JToolBar {
    // attributes
    private static final long serialVersionUID = 1L;
    
    //components
    private JRadioButton rectangleButton;
    private JRadioButton triangleButton;
    private JRadioButton ovalButton;
    private JRadioButton polygonButton;
    private JRadioButton textBoxButton;
    private ButtonGroup toolButtonGroup;
    private ShapeType selectedShape;
    private GMainFrame mainFrame;
    
    //associate
    private GMainPanel mainPanel;
    
    // constructor
    public GShapeToolBar(GMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.selectedShape = null;
        
        setOrientation(JToolBar.VERTICAL);
        setFloatable(false);
        setBorderPainted(false);
        setBackground(Color.GRAY);
        ButtonGroup buttongroup =new ButtonGroup();
        for (EShapeTool eshapeType : EShapeTool.values()) {
        	JRadioButton radiobutton = new JRadioButton(eshapeType.getName());
        	ActionHandler actionhandler = new ActionHandler();
        	radiobutton.addActionListener(actionhandler);
        	radiobutton.setActionCommand(eshapeType.toString());
        	
        	buttongroup.add(radiobutton); 
        	this.add(radiobutton);
        	//����� ���� ���� �ڱⰡ �����ִ��� �� �� ����. �θ� �ȴ�.
        	//���� �ְ� ���̴� ��ġ���� �����Ѵ�.
        	//���� ��ȣ�ۿ��Ϸ��� �� ���� �θ𿡴� ���ٳ��ƾ��Ѵ�. (���� : ��ȣ�ۿ��� ���� �������� )
        	//jpanel�� ������ �شٸ� handler�� frame�� �־���Ѵ�. jpanel�� ����� ��������
        }
    }

    private JRadioButton createToolButton(String text, String tooltip) {
    	JRadioButton button = new JRadioButton(text);
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        add(button);
        return button;
    }

    // methods
    public ShapeType getSelectedShape() {
        return selectedShape;
    }

    // initialize
    public void initialize() {
    	JRadioButton button =(JRadioButton) this.getComponent(EShapeTool.eSelect.ordinal());
    	button.doClick();
    }

	public void associate(GMainPanel mainPanel) {
		this.mainPanel=mainPanel;
	}
	
	private class ActionHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String sShapeType = e.getActionCommand();
			EShapeTool eshapeType=EShapeTool.valueOf(sShapeType);
			mainPanel.setEShapeTool(eshapeType);
		}//�� �ҽ��ڵ带 mainframe�� �θ� panel�̶� associate���ص��ȴ�. 
		
	}
}