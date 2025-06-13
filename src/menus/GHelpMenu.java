package menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GHelpMenu extends JMenu{
	// attributes
    private static final long serialVersionUID = 1L;
    
    private JMenuItem systeminfo;
    private JMenuItem about;
    private JMenuItem online;
    
    // constructor
    public GHelpMenu(String label) {
        super(label);
        
    }
    
    // method
    public void initialize() {
        // Add action listeners here
    }
}
