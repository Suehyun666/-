package frames;

import static java.awt.Color.DARK_GRAY;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.*;

import global.GConstants;
import menus.file.GSaveConfirmDialog;
import shapes.GShapeToolBar;

public class GMainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static String base="Untitled";
    //components
    private JTabbedPane tabbedPane;
    private GShapeToolBar shapetoolBar;
    private GMenubar menuBar;
    private Vector<GMainPanel> canvasPanels;
    private GPictureToolBar pictureToolBar;
    private GColorPanel colorPanel;

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

        this.tabbedPane = new JTabbedPane();
        this.canvasPanels = new Vector<>();

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(e -> {
            GMainPanel currentPanel = getCurrentPanel();
            if (currentPanel != null) {
                this.shapetoolBar.associate(currentPanel);
                this.menuBar.associateCurrentPanel(currentPanel);
                this.menuBar.getEditMenu().updateMenuState();
                SwingUtilities.invokeLater(this::updateCurrentTabContext);
            } else {
                this.shapetoolBar.associate(null);
                this.menuBar.associate(null);
            }
        });

        this.canvasPanels = new Vector<>();
        JScrollPane scrollPane = new JScrollPane(tabbedPane);
        add(scrollPane, BorderLayout.CENTER);

        this.shapetoolBar = new GShapeToolBar(this);
        add(shapetoolBar, BorderLayout.WEST);

        this.colorPanel = new GColorPanel(this);
        add(colorPanel, BorderLayout.EAST);

        this.pictureToolBar=new GPictureToolBar(this);
        add(pictureToolBar, BorderLayout.SOUTH);
        setVisible(true);

        //action listner
        tabbedPane.addChangeListener(e -> {
            System.out.println("GMainFrame: getCurrentPanel() returned null");
            GMainPanel currentPanel = getCurrentPanel();
            if(currentPanel==null) {
                System.out.println("GMainFrame: getCurrentPanel() returned null");
            }
            if (currentPanel != null) {
                System.out.println("액션 리스너 작동");
                this.shapetoolBar.associate(currentPanel);
                this.menuBar.associateCurrentPanel(currentPanel);
                this.menuBar.getEditMenu().updateMenuState();
                SwingUtilities.invokeLater(this::updateCurrentTabContext);
                if (currentPanel.getSelectedShape() != null) {
                    colorPanel.updateFromShape(currentPanel.getSelectedShape());
                }
            } else {
                this.shapetoolBar.associate(null);
                this.menuBar.associate(null);
            }
        });
    }
    private void handleWindowClosing() {
        this.menuBar.getFileMenu().close();
    }
    // initialize
    public void initialize() {
        this.menuBar.initialize();
        this.shapetoolBar.initialize();
        this.pictureToolBar.initialize();
        this.colorPanel.initialize();
        this.menuBar.associate(null);
        this.shapetoolBar.associate(null);
        updateCurrentTabContext();
    }
    //update
    private void updateCurrentTabContext (){
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null) {
            this.shapetoolBar.associate(currentPanel);
            GConstants.EShapeTool currentTool = this.shapetoolBar.getSelectedShape();
            if (currentTool != null) {
                currentPanel.setEShapeTool(currentTool);
            }
            this.menuBar.associateCurrentPanel(currentPanel);
            this.menuBar.getEditMenu().updateMenuState();
        } else {
            this.shapetoolBar.associate(null);
            this.menuBar.associate(null);
        }
    }
    //new tab
    public void createNewTab(String title) {insertTabBeforePlus(makeUniqueTitle(title));}
    public String makeUniqueTitle(String title) {
        int count = 1;
        title = base + "-" + count;
        while (isTitleExists(title)) {
            count++;
            title = base + "-" + count;
        }
        return title;
    }
    private boolean isTitleExists(String title) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component tab = tabbedPane.getTabComponentAt(i);
            if (tab instanceof GTabComponent) {
                if (((GTabComponent) tab).getTitle().equals(title)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void insertTabBeforePlus(String uniqueTitle) {
        GMainPanel panel = new GMainPanel(this);
        canvasPanels.add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        int insertIndex = tabbedPane.getTabCount();
        tabbedPane.insertTab(uniqueTitle, null, scrollPane, null, insertIndex);
        tabbedPane.setTabComponentAt(insertIndex, new GTabComponent(tabbedPane, uniqueTitle, this));

        SwingUtilities.invokeLater(() -> tabbedPane.setSelectedIndex(insertIndex));

        panel.initialize();
        updateCurrentTabContext();
    }

    //open
    public boolean isTabOpenWithFile(File file) {
        String rawName = file.getName().replaceFirst("[.][^.]+$", "");
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component tab = tabbedPane.getTabComponentAt(i);
            if (tab instanceof GTabComponent) {
                if (((GTabComponent) tab).getTitle().startsWith(rawName)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ?
    private void updateTabTitle(GMainPanel currentPanel, boolean modified) {
        int index = canvasPanels.indexOf(currentPanel);
        if (index >= 0) {
            String title = tabbedPane.getTitleAt(index);
            if (modified && !title.endsWith("*")) {
                tabbedPane.setTitleAt(index, title + "*");
            } else if (!modified && title.endsWith("*")) {
                tabbedPane.setTitleAt(index, title.substring(0, title.length() - 1));
            }
        }
    }
    //close
    public boolean closeTab(int index) {
        if (index >= 0 && index < canvasPanels.size()) {
            GMainPanel panel = canvasPanels.get(index);
            if (panel.getUpdated()) {
                String tabTitle = tabbedPane.getTitleAt(index).replace("*", "");
                int reply = GSaveConfirmDialog.showSaveConfirmDialog(this, tabTitle);
                if (reply == GSaveConfirmDialog.CANCEL_OPTION) {
                    return false;
                } else if (reply == GSaveConfirmDialog.SAVE_OPTION) {
                    this.menuBar.getFileMenu().save();
                }
            }
            canvasPanels.remove(index);
            tabbedPane.removeTabAt(index);
            return true;
        }return false;
    }
    public boolean checkSaveAllTabs() {
        for (int i = 0; i < canvasPanels.size(); i++) {
            GMainPanel panel = canvasPanels.get(i);
            if (panel.getUpdated()) {
                tabbedPane.setSelectedIndex(i);
                String tabTitle = tabbedPane.getTitleAt(i).replace("*", "");

                int reply = JOptionPane.showConfirmDialog(
                        this,
                        tabTitle + "의 변경내용을 저장할까요?",
                        "저장 확인",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                if (reply == JOptionPane.YES_OPTION) {
                    menuBar.getFileMenu().save();
                } else if (reply == JOptionPane.CANCEL_OPTION) {
                    return false;
                }
            }
        }
        return true;
    }

    //getter & setters
    public GMainPanel getCurrentPanel() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < canvasPanels.size()) {
            return canvasPanels.get(selectedIndex);
        }return null;
    }
    public GColorPanel getColorPanel() {return this.colorPanel;}
    public JTabbedPane getTabbedPane() {return tabbedPane;}
    public Vector<GMainPanel> getCanvasPanels() {return canvasPanels;}
    public GShapeToolBar getToolBar() {return shapetoolBar;}
    public void setModified(boolean bool) {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.setUpdate(bool);
            updateTabTitle(currentPanel, bool);
        }
    }
}