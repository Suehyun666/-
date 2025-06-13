package menus.file;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import frames.GMainFrame;
import global.GMenuConstants.EFileMenuItem;
import frames.GMainPanel;
import shapes.GShape;

public class GFileMenu extends JMenu {
    private static final long serialVersionUID = 1L;

    //components
    private GMainPanel mainpanel;
    private JFileChooser fileChooser;
    private File currentFile;

    //constructor
    public GFileMenu(String text) {
        super(text);
        ActionHandler actionHandler = new ActionHandler();

        for (EFileMenuItem eFileMenuItem : EFileMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eFileMenuItem.getText());
            menuItem.setActionCommand(eFileMenuItem.name());
            menuItem.addActionListener(actionHandler);
            if (eFileMenuItem == EFileMenuItem.eNew) {
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));}
            if (eFileMenuItem == EFileMenuItem.eSave) {
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));}
            if (eFileMenuItem == EFileMenuItem.eSaveAs) {
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));}
            if (eFileMenuItem == EFileMenuItem.eOpen) {
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));}
            if (eFileMenuItem == EFileMenuItem.eExit) {
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));}
            if (eFileMenuItem == EFileMenuItem.ePrint) {
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));}
            if (eFileMenuItem == EFileMenuItem.eClose) {
                menuItem.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));}
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
        GMainFrame mainFrame = (GMainFrame) SwingUtilities.getWindowAncestor(this);
        if (mainFrame != null) {
            String newTabName = "Untitled-" + (mainFrame.getCanvasPanels().size() + 1);
            mainFrame.createNewTab(newTabName);
            currentFile = null;
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
                this.mainpanel = getCurrentPanel();
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
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();

            printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                this.mainpanel = getCurrentPanel();
                if (mainpanel == null) return Printable.NO_SUCH_PAGE;

                double scaleX = pageFormat.getImageableWidth() / mainpanel.getWidth();
                double scaleY = pageFormat.getImageableHeight() / mainpanel.getHeight();
                double scale = Math.min(scaleX, scaleY);
                g2d.scale(scale, scale);

                mainpanel.paint(g2d);

                return Printable.PAGE_EXISTS;
            });
            if (printerJob.printDialog()) {
                printerJob.print();
                JOptionPane.showMessageDialog(this, "인쇄가 완료되었습니다.");
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this,
                    "인쇄 오류: " + e.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public void save() {
        System.out.println("save");
        this.mainpanel = getCurrentPanel();
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
            this.mainpanel=getCurrentPanel();
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
        GMainFrame mainFrame = (GMainFrame) SwingUtilities.getWindowAncestor(this);
        if (mainFrame.checkSaveAllTabs()) {
            System.exit(0);
        }
    }
    public void closeCurrentTab() {
        GMainFrame mainFrame = (GMainFrame) SwingUtilities.getWindowAncestor(this);
        int currentIndex = mainFrame.getTabbedPane().getSelectedIndex();
        mainFrame.closeTab(currentIndex);
    }

    public void exit() {
        this.mainpanel = getCurrentPanel();
        if(mainpanel == null) {
            System.exit(0);
        }
    }

    public void importFile(){

    }
    public void exportFile(){

    }

    //getters
    private GMainPanel getCurrentPanel() {
        GMainFrame mainFrame = (GMainFrame) SwingUtilities.getWindowAncestor(this);
        return mainFrame != null ? mainFrame.getCurrentPanel() : null;
    }
    public File getCurrentFile() {return currentFile;}
    public String getCurrentFileName() {return currentFile != null ? currentFile.getName() : "제목 없음";}

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            EFileMenuItem eMenuItem = EFileMenuItem.valueOf(event.getActionCommand());
            invokeMethod(eMenuItem.getMethodName());
        }
    }
    private void invokeMethod(String eMenuItem) {
        try {
            this.getClass().getMethod(eMenuItem).invoke(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void associate(GMainPanel mainpanel) {
        this.mainpanel=getCurrentPanel();
    }
}