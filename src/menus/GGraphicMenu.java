package menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GGraphicMenu extends JMenu {
    // attributes
    private static final long serialVersionUID = 1L;
    
    private JMenuItem lineThicknessItem;
    private JMenuItem lineStyleItem;
    private JMenuItem fontStyleItem;
    private JMenuItem fontSizeItem;
    
    // constructor
    public GGraphicMenu(String label) {
        super(label);
        
        this.lineThicknessItem = new JMenuItem("Line Thickness");
        this.add(lineThicknessItem);
        
        this.lineStyleItem = new JMenuItem("Line Style");
        this.add(lineStyleItem);
        
        this.addSeparator();
        
        this.fontStyleItem = new JMenuItem("Font Style");
        this.add(fontStyleItem);
        
        this.fontSizeItem = new JMenuItem("Font Size");
        this.add(fontSizeItem);
    }
    //initialize
	public void initialize() {
		
	}
}