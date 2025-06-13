package menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GEditMenu extends JMenu {
    // attributes
    private static final long serialVersionUID = 1L;
    
    private JMenuItem propertyItem;
    private JMenuItem undoItem;
    private JMenuItem backward;
    private JMenuItem forward;
    private JMenuItem fade;
    private JMenuItem cut;
    private JMenuItem copy;
    private JMenuItem paste;
    private JMenuItem clear;
    private JMenuItem fill;
    private JMenuItem colorsetting;
    
    // constructor
    public GEditMenu(String label) {
        super(label);
        
        this.propertyItem = new JMenuItem("Property");
        this.add(propertyItem);
        
        this.addSeparator();
        
        this.undoItem = new JMenuItem("Undo");
        this.add(undoItem);
        
        this.forward = new JMenuItem("Forward");
        this.add(forward);
        
        this.backward = new JMenuItem("Backward");
        this.add(backward);
        
        this.fade = new JMenuItem("Fade");
        this.add(fade);
        
        this.cut = new JMenuItem("Cut");
        this.add(cut);
        
        this.copy = new JMenuItem("Copy");
        this.add(copy);
        
        this.paste = new JMenuItem("Paste");
        this.add(paste);
        
        this.clear = new JMenuItem("Clear");
        this.add(clear);
        
        this.fill = new JMenuItem("Fill");
        this.add(fill);

        this.colorsetting = new JMenuItem("Color Setting");
        this.add(colorsetting);
        
    }
    
    // method
    public void initialize() {
        // Add action listeners here
    }
}