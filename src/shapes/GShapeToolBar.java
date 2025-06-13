package shapes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import frames.GMainFrame;
import frames.GMainPanel;
import global.GConstants.EShapeTool;

public class GShapeToolBar extends JToolBar {
    // attributes
    private static final long serialVersionUID = 1L;

    //components
    private EShapeTool selectedShape;
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
        ButtonGroup buttongroup = new ButtonGroup();
        ActionHandler actionhandler = new ActionHandler();

        for (EShapeTool eshapeType : EShapeTool.values()) {
            JRadioButton radiobutton = createToolButton(eshapeType.getName(), eshapeType.getName());
            radiobutton.addActionListener(actionhandler);
            radiobutton.setActionCommand(eshapeType.toString());

            buttongroup.add(radiobutton);
            this.add(radiobutton);
            //여기다 붙인 이유 자기가 눌려있는지 알 수 없다. 부모가 안다.
            //눌린 애가 보이는 위치에서 감지한다.
            //서로 상호작용하려면 그 들의 부모에다 갖다놓아야한다. (전제 : 상호작용은 같은 레벨에서 )
            //jpanel에 영향을 준다면 handler는 frame에 있어야한다. jpanel에 명령을 내려야함
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
    public EShapeTool getSelectedShape() {
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
        }//이 소스코드를 mainframe에 두면 panel이랑 associate안해도된다.

    }
}