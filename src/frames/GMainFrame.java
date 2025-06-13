package frames;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.RED;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Vector;
import javax.swing.*;

import global.GConstants;
import shapes.GShapeToolBar;

public class GMainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    //components
    private JTabbedPane tabbedPane;
    private GShapeToolBar shapetoolBar;
    private GMenubar menuBar;
    private Vector<GMainPanel> canvasPanels;
    private GPictureToolBar pictureToolBar;

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
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        this.canvasPanels = new Vector<>();
        JScrollPane scrollPane = new JScrollPane(tabbedPane);
        add(scrollPane, BorderLayout.CENTER);

        this.shapetoolBar = new GShapeToolBar(this);
        add(shapetoolBar, BorderLayout.WEST);

        this.pictureToolBar=new GPictureToolBar(this);
        add(pictureToolBar, BorderLayout.SOUTH);

        setVisible(true);

        tabbedPane.addChangeListener(e -> {
            GMainPanel currentPanel = getCurrentPanel();
            if (currentPanel != null) {
                this.shapetoolBar.associate(currentPanel);
                this.menuBar.associateCurrentPanel(currentPanel);
            }
        });
        tabbedPane.addChangeListener(e -> {
            GMainPanel currentPanel = getCurrentPanel();
            if (currentPanel != null) {
                this.shapetoolBar.associate(currentPanel);
                this.menuBar.associateCurrentPanel(currentPanel);
                this.menuBar.getEditMenu().updateMenuState();
            }
        });
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    private void handleWindowClosing() {
        this.menuBar.getFileMenu().close();
    }
    // initialize
    public void initialize() {
        this.menuBar.initialize();
        this.shapetoolBar.initialize();
        this.pictureToolBar.initialize();
        this.menuBar.associate(null);
        this.shapetoolBar.associate(null);
    }

    public void createNewTab(String title) {
        GMainPanel newPanel = new GMainPanel(this);
        canvasPanels.add(newPanel);

        JScrollPane scrollPane = new JScrollPane(newPanel);
        tabbedPane.addTab(title, scrollPane);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

        this.shapetoolBar.associate(newPanel);
        this.menuBar.associate(newPanel);

        newPanel.initialize();
    }
    //getters & setters
    public GMainPanel getCurrentPanel() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < canvasPanels.size()) {
            return canvasPanels.get(selectedIndex);
        }
        return null;
    }
    public GShapeToolBar getToolBar() {
        return shapetoolBar;
    }
    public void setModified(boolean bool) {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.setUpdate(bool);
            updateTabTitle(currentPanel, bool);
        }
    }
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

    public void closeTab(int index) {
        if (index >= 0 && index < canvasPanels.size()) {
            GMainPanel panel = canvasPanels.get(index);
            if (panel.getUpdated()) {
                String tabTitle = tabbedPane.getTitleAt(index).replace("*", "");
                int reply = JOptionPane.showConfirmDialog(
                        this,
                        tabTitle + "의 변경내용을 저장할까요?",
                        "저장 확인",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );

                if (reply == JOptionPane.CANCEL_OPTION) {
                    return; // 취소
                } else if (reply == JOptionPane.YES_OPTION) {
                    // 저장 로직
                }
            }

            canvasPanels.remove(index);
            tabbedPane.removeTabAt(index);

            // 마지막 탭이면 새 탭 생성
            if (tabbedPane.getTabCount() == 0) {
                createNewTab("Untitled-1");
            }
        }
    }

    //getter
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
    public Vector<GMainPanel> getCanvasPanels() {
        return canvasPanels;
    }

}