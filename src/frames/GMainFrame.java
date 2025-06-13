package frames;

import static java.awt.Color.DARK_GRAY;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.w3c.dom.Node;

import constants.ShapeType;
import global.GConstants;
import shapes.GShapeToolBar;

public class GMainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    //components
    private GShapeToolBar shapetoolBar;
    private GMenubar menuBar;
    private GMainPanel mainPanel;

    // constructor
    public GMainFrame() {
        // attributes
        setTitle(GConstants.GMainFrame.TITLE);
        setSize(GConstants.GMainFrame.WIDTH, GConstants.GMainFrame.HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        this.getContentPane().setBackground(DARK_GRAY);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
        
        
        //components
        this.menuBar = new GMenubar();
        this.setJMenuBar(menuBar);

        this.mainPanel = new GMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        this.shapetoolBar = new GShapeToolBar(this);
        add(shapetoolBar, BorderLayout.WEST);

        setVisible(true);
    }

    // initialize
    public void initialize() {
    	this.menuBar.associate(this.mainPanel);
    	this.shapetoolBar.associate(this.mainPanel);
    	
        menuBar.initialize();
        shapetoolBar.initialize();
        mainPanel.initialize();
    }
    
    private void handleWindowClosing() {
    	this.menuBar.getFileMenu().close();
	}
    //getters & setters
    public GMainPanel getMainPanel() {
        return mainPanel;
    }

    public GShapeToolBar getToolBar() {
        return shapetoolBar;
    }

    public void updateDrawingState() {
        ShapeType selectedShape = shapetoolBar.getSelectedShape();
    }
    public void setValue(Node node) {
    	//this.node=node;
    }
}