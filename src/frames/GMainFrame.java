package frames;

import static java.awt.Color.DARK_GRAY;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import controller.AppController;
import menus.GMenubar;
import global.GConstants;
import dialog.DialogManager;

public class GMainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    // Components
    private GMenubar menuBar;
    private GShapeToolBar shapeToolBar;
    private GColorPanel colorPanel;
    private GPictureToolBar pictureToolBar;
    private GTabManager tabManager;

    // Controllers
    private AppController appController;

    // Constructor
    public GMainFrame() {
        initializeFrame();
        createComponents();
        layoutComponents();
        setupControllers();
        setupEventHandlers();

        setVisible(true);
    }

    private void initializeFrame() {
        setTitle(GConstants.GMainFrame.TITLE);
        setSize(GConstants.GMainFrame.WIDTH, GConstants.GMainFrame.HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(DARK_GRAY);
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage("src/resources/icon.png");
            this.setIconImage(icon);
        } catch (Exception e) {
            System.err.println("Icon Load Failed " + e.getMessage());
        }
    }

    private void createComponents() {
        this.menuBar = new GMenubar();
        setJMenuBar(menuBar);

        this.tabManager = new GTabManager(this);
        this.shapeToolBar = new GShapeToolBar();
        this.colorPanel = new GColorPanel();
        this.pictureToolBar = new GPictureToolBar(this);

        DialogManager.setDefaultParent(this);
    }

    private void layoutComponents() {
        add(shapeToolBar, BorderLayout.WEST);
        add(tabManager, BorderLayout.CENTER);  // 탭 매니저가 CENTER
        add(colorPanel, BorderLayout.EAST);
        add(pictureToolBar, BorderLayout.SOUTH);
    }

    private void setupControllers() {
        this.appController = new AppController(tabManager);

        menuBar.getFileMenu().setController(appController);
        menuBar.getEditMenu().setController(appController);
        menuBar.getFilterMenu().setController(appController);
        menuBar.getImageMenu().setController(appController);
        menuBar.getSelectMenu().setController(appController);
        menuBar.getGraphicMenu().setController(appController);
        menuBar.getLayerMenu().setController(appController);

        shapeToolBar.setController(appController);
        colorPanel.setController(appController);
    }

    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    private void handleWindowClosing() {
        if (tabManager.checkSaveAllTabs()) {
            System.exit(0);
        }
    }



    public GMainPanel getCurrentPanel() {
        return tabManager.getCurrentPanel();
    }
    public void updateTabTitle(GMainPanel panel, boolean modified) {
        tabManager.updateTabTitle(panel, modified);
    }
    public void setModified(boolean modified) {
        tabManager.setModified(modified);
    }
    public boolean setTabTitle(GMainPanel panel, String newTitle) {
        return tabManager.setTabTitle(panel, newTitle);
    }
    public boolean closeTab(int index) {
        return tabManager.closeTab(index);
    }
    public boolean isTabOpenWithFile(java.io.File file) {
        return tabManager.isTabOpenWithFile(file);
    }
    public boolean isTitleExists(String title) {
        return tabManager.isTitleExists(title);
    }
    public String makeUniqueTitle(String baseTitle) {
        String uniqueTitle = baseTitle;
        int count = 1;

        while (isTitleExists(uniqueTitle)) {
            count++;
            uniqueTitle = baseTitle + " (" + count + ")";
        }
        return uniqueTitle;
    }


    public void createNewTab(String title) {
        // AppController를 통해 새 파일 생성
        appController.newFile();
    }

    public void openTab(String fileName) {
        createNewTab(fileName);
    }

    public void newTab(String title) {
        createNewTab(title);
    }

    //getters

    public AppController getAppController() {
        return appController;
    }

    public GMenubar getMenubar() {
        return menuBar;
    }

    public GShapeToolBar getShapeToolBar() {
        return shapeToolBar;
    }

    public GColorPanel getColorPanel() {
        return colorPanel;
    }

    public GPictureToolBar getPictureToolBar() {
        return pictureToolBar;
    }

    public GTabManager getTabManager() {
        return tabManager;
    }

    public GColorPanel getColorToolBar() {
        return colorPanel;
    }

    public GMenubar getmenuBar() {
        return menuBar;
    }

    public javax.swing.JTabbedPane getTabbedPane() {
        return tabManager.getTabbedPane();
    }

    public java.util.Vector<GMainPanel> getCanvasPanels() {
        return tabManager.getCanvasPanels();
    }

    // initialize
    public void initialize() {
        if (menuBar != null) menuBar.initialize();
        if (shapeToolBar != null) shapeToolBar.initialize();
        if (colorPanel != null) colorPanel.initialize();
        if (pictureToolBar != null) pictureToolBar.initialize();
    }
}