package menus;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import constants.GMenuConstants.EFileMenuItem;
import frames.GMainPanel;
import shapes.GShape;

public class GFileMenu extends JMenu {
    private static final long serialVersionUID = 1L;
    
  //components
    private GMainPanel mainpanel;
    private JFileChooser fileChooser;
    private File currentFile; 
    private File dir;
    
    //constructor
    public GFileMenu(String text) {
        super(text);
        initializeFileChooser();
        ActionHandler actionHandler = new ActionHandler();
        for (EFileMenuItem eFileMenuItem : EFileMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eFileMenuItem.getText());
            menuItem.setActionCommand(eFileMenuItem.name());
            menuItem.addActionListener(actionHandler);
            this.add(menuItem);
        }
    }
    private void initializeFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Shape Files (*.dat)", "dat");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
    }
    //methods
    
    //initialize
    public void initialize() {
    	initializeFileChooser();
        currentFile = null;
    }
    public void create() {
        System.out.println("create");
        currentFile = null;
        checkSave();
        if (mainpanel != null) {
            mainpanel.clearShapes(); 
            mainpanel.repaint();
            mainpanel.revalidate();
        }
    }
    
    public void open() {
        System.out.println("open");
        
        int result = fileChooser.showOpenDialog(this.getParent());
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                FileInputStream file = new FileInputStream(selectedFile);
                ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(file));
                Vector<GShape> shapes = (Vector<GShape>) stream.readObject();
                
                if (this.mainpanel != null) {
                    this.mainpanel.setShapes(shapes);
                    this.mainpanel.repaint();
                    this.mainpanel.revalidate();
                    currentFile = selectedFile; 
                    System.out.println("파일 열기 성공: " + selectedFile.getName());
                }
                stream.close();
            } catch(Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this.getParent(), 
                    "파일을 열 수 없습니다: " + e.getMessage(), 
                    "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void print() {
        System.out.println("print");
    }
    public void save() {
        System.out.println("save");
        if (currentFile != null) {
            saveToFile(currentFile);
        } else {
            saveas(); 
        }
    }
    public void saveas() {
        System.out.println("saveas");
        int result = fileChooser.showSaveDialog(this.getParent());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".dat")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".dat");
            }
            if (selectedFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(this.getParent(),
                    "파일이 이미 존재합니다. 덮어쓰시겠습니까?",
                    "파일 덮어쓰기", JOptionPane.YES_NO_OPTION);
                
                if (overwrite != JOptionPane.YES_OPTION) {
                    return; 
                }
            }
            saveToFile(selectedFile);
        }
    }
    
    private void saveToFile(File file) {
        try {
            if (this.mainpanel != null) {
            	BufferedImage image = new BufferedImage(this.mainpanel.getWidth(),this.mainpanel.getWidth(),BufferedImage.TYPE_INT_ARGB);
            	Graphics2D g2d = image.createGraphics();
            	paintComponent(g2d);
            	ImageIO.write(image, "png", new File("drawing.png"));
            	
                Vector<GShape> shapes = this.mainpanel.getshapes();
                
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(fileOut));
                stream.writeObject(shapes);
                stream.close();
                
                currentFile = file; 
                System.out.println("파일 저장 성공: " + file.getName());
                this.mainpanel.setUpdate(false);
                JOptionPane.showMessageDialog(this.getParent(), 
                    "파일이 저장되었습니다: " + file.getName(), 
                    "저장 완료", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.getParent(), 
                "파일을 저장할 수 없습니다: " + e.getMessage(), 
                "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void close() {
    	checkSave();
    	exit();
    	//닫는 메서드
    }
    public void exit() {
    	this.mainpanel.exit();
    }
    private boolean checkSave() {
    	boolean bCancel =false;
    	int reply =JOptionPane.NO_OPTION;
    	if(this.mainpanel.getUpdated()) {
    		reply= JOptionPane.showConfirmDialog(this.mainpanel,"변경내용을 저장할까요?");
    		if(reply ==JOptionPane.CANCEL_OPTION) {
    			bCancel=true;
    		}
    	}
    	if(!bCancel) {
    		if(reply==JOptionPane.OK_OPTION) {
    			this.save();
    		}
    	}	
    	return bCancel;
    }
    
    public File getCurrentFile() {
        return currentFile;
    }
    
    public String getCurrentFileName() {
        return currentFile != null ? currentFile.getName() : "제목 없음";
    }
    
    private void invokeMethod(String eMenuItem) {
        try {
            this.getClass().getMethod(eMenuItem).invoke(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            EFileMenuItem eMenuItem = EFileMenuItem.valueOf(event.getActionCommand());
            invokeMethod(eMenuItem.getMethodName());
        }
    }
    
    public void associate(GMainPanel mainpanel) {
        this.mainpanel = mainpanel;
    }
}