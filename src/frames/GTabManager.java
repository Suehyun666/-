package frames;

import global.CanvasInfo;
import global.FileData;
import dialog.DialogManager;
import dialog.DialogResult;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Vector;

import static java.awt.Color.DARK_GRAY;

public class GTabManager extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;
    private Vector<GMainPanel> canvasPanels;
    private GMainFrame mainFrame;

    public GTabManager(GMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.canvasPanels = new Vector<>();
        this.tabbedPane = new JTabbedPane();

        setupTabPane();
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(tabbedPane);
        scrollPane.getViewport().setBackground(DARK_GRAY);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupTabPane() {
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(e -> {
            GMainPanel currentPanel = getCurrentPanel();
            if (currentPanel != null) {
                associateComponents(currentPanel);
                updateCurrentTabContext();
            } else {
                associateComponents(null);
            }
        });
    }

    public GMainPanel getCurrentPanel() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < canvasPanels.size()) {
            return canvasPanels.get(selectedIndex);
        }
        return null;
    }

    public int getCurrentTabIndex() {
        return tabbedPane.getSelectedIndex();
    }

    public int getTabCount() {
        return tabbedPane.getTabCount();
    }

    public GMainPanel getPanelAt(int index) {
        if (index >= 0 && index < canvasPanels.size()) {
            return canvasPanels.get(index);
        }
        return null;
    }

    public void selectTab(int index) {
        if (index >= 0 && index < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(index);
        }
    }

    public int findTabWithFile(File file) {
        String fileName = file.getName().replaceFirst("[.][^.]+$", "");
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component tab = tabbedPane.getTabComponentAt(i);
            if (tab instanceof GTabComponent) {
                if (((GTabComponent) tab).getTitle().startsWith(fileName)) {
                    return i;
                }
            }
        }
        return -1;
    }

    // Tab
    public void createTabWithFileData(FileData fileData) {
        CanvasInfo canvasInfo = new CanvasInfo();
        GMainPanel newPanel = new GMainPanel(mainFrame, canvasInfo, fileData);

        JScrollPane scrollPane = new JScrollPane(newPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.LIGHT_GRAY);

        String tabTitle = fileData.getFileName();

        tabbedPane.addTab(tabTitle, scrollPane);
        canvasPanels.add(newPanel);

        int newIndex = tabbedPane.getTabCount() - 1;
        tabbedPane.setSelectedIndex(newIndex);

        GTabComponent tabComponent = new GTabComponent(tabbedPane, tabTitle, this);
        tabbedPane.setTabComponentAt(newIndex, tabComponent);

        associateComponents(newPanel);

        System.out.println("Created new tab: " + tabTitle);
    }

    // (파일 열기용 - CanvasInfo
    public void createTabWithFileData(FileData fileData, CanvasInfo canvasInfo) {
        GMainPanel newPanel = new GMainPanel(mainFrame, canvasInfo, fileData);

        JScrollPane scrollPane = new JScrollPane(newPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.LIGHT_GRAY);

        String tabTitle = fileData.getFileName();

        tabbedPane.addTab(tabTitle, scrollPane);
        canvasPanels.add(newPanel);

        int newIndex = tabbedPane.getTabCount() - 1;
        tabbedPane.setSelectedIndex(newIndex);

        GTabComponent tabComponent = new GTabComponent(tabbedPane, tabTitle, this);
        tabbedPane.setTabComponentAt(newIndex, tabComponent);

        associateComponents(newPanel);

        System.out.println("Created tab from file: " + tabTitle + " with canvas: " + canvasInfo);
    }

    private void addTab(GMainPanel panel, String title) {
        canvasPanels.add(panel);
        JScrollPane scrollPane = new JScrollPane(panel);
        int insertIndex = tabbedPane.getTabCount();

        tabbedPane.insertTab(title, null, scrollPane, null, insertIndex);
        tabbedPane.setTabComponentAt(insertIndex, new GTabComponent(tabbedPane, title, this));

        SwingUtilities.invokeLater(() -> tabbedPane.setSelectedIndex(insertIndex));
        panel.initialize();
    }

    public boolean closeTab(int index) {
        if (!isValidTabIndex(index)) return false;

        GMainPanel panel = canvasPanels.get(index);
        if (panel.isUpdated()) {
            DialogManager.SaveAction action = DialogManager.confirmSave(getTabTitle(index));
            if (!handleSaveAction(action)) return false;
        }

        canvasPanels.remove(index);
        tabbedPane.removeTabAt(index);
        return true;
    }

    public boolean checkSaveAllTabs() {
        for (int i = 0; i < canvasPanels.size(); i++) {
            GMainPanel panel = canvasPanels.get(i);
            if (panel.isUpdated()) {
                tabbedPane.setSelectedIndex(i);
                String tabTitle = getTabTitle(i);

                DialogManager.SaveAction action = DialogManager.confirmSave(tabTitle);
                if (!handleSaveAction(action)) return false;
            }
        }
        return true;
    }

    private boolean handleSaveAction(DialogManager.SaveAction action) {
        switch (action) {
            case SAVE:
                // AppController를 통해 저장
                if (mainFrame.getAppController() != null) {
                    mainFrame.getAppController().saveFile();
                }
                return true;
            case DONT_SAVE:
                return true;
            case CANCEL:
            default:
                return false;
        }
    }

    // Title
    public boolean setTabTitle(GMainPanel panel, String newTitle) {
        if (isTitleExists(newTitle)) {
            DialogManager.warning(
                    "A tab with this name already exists.\nPlease choose a different name or close the existing tab.",
                    "Duplicate Name"
            );
            return false;
        }

        int index = canvasPanels.indexOf(panel);
        if (index >= 0) {
            Component tab = tabbedPane.getTabComponentAt(index);
            if (tab instanceof GTabComponent) {
                ((GTabComponent) tab).setTitle(newTitle);
                panel.setFileName(newTitle);
            }
        }
        return true;
    }

    public void updateTabTitle(GMainPanel currentPanel, boolean modified) {
        int index = canvasPanels.indexOf(currentPanel);
        if (index >= 0) {
            Component tab = tabbedPane.getTabComponentAt(index);
            if (tab instanceof GTabComponent) {
                String title = currentPanel.getFileName();
                if (modified && !title.endsWith("*")) {
                    title += "*";
                }
                ((GTabComponent) tab).setTitle(title);
            }
        }
    }

    public void setModified(boolean modified) {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.setUpdated(modified);
            updateTabTitle(currentPanel, modified);
        }
    }

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


    private String makeUniqueTitle(String title) {
        String baseTitle = title;
        String uniqueTitle = baseTitle;
        int count = 1;

        while (isTitleExists(uniqueTitle)) {
            count++;
            uniqueTitle = baseTitle + "-" + count;
        }
        return uniqueTitle;
    }

    public boolean isTitleExists(String title) {
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

    private boolean isValidTabIndex(int index) {
        return index >= 0 && index < canvasPanels.size();
    }

    private String getTabTitle(int index) {
        Component tab = tabbedPane.getTabComponentAt(index);
        if (tab instanceof GTabComponent) {
            return ((GTabComponent) tab).getTitle().replace("*", "");
        }
        return "Untitled";
    }

    private void updateCurrentTabContext() {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null && mainFrame.getJMenuBar() instanceof menus.GMenubar) {
            menus.GMenubar menubar = (menus.GMenubar) mainFrame.getJMenuBar();
            if (menubar.getEditMenu() != null) {
                SwingUtilities.invokeLater(() -> {
                    menubar.getEditMenu().updateMenuState();
                });
            }
        }
    }

    private void associateComponents(GMainPanel panel) {
        if (mainFrame.getColorPanel() != null) {
            mainFrame.getColorPanel().associate(panel);
        }
    }

    // Tab Menu Methods
    public void createNewTab() {
        createNewTabFromDialog();
    }
    public void createNewTabFromDialog() {
        DialogResult<FileData> result = DialogManager.newFile();
        if (result.isConfirmed()) {
            createTabWithFileData(result.getResult());
        }
    }

    public void addNewTab(GMainPanel panel, String title) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.LIGHT_GRAY);

        tabbedPane.addTab(title, scrollPane);
        canvasPanels.add(panel);

        int newIndex = tabbedPane.getTabCount() - 1;
        tabbedPane.setSelectedIndex(newIndex);

        GTabComponent tabComponent = new GTabComponent(tabbedPane, title, this);
        tabbedPane.setTabComponentAt(newIndex, tabComponent);

        associateComponents(panel);

        System.out.println("Added tab: " + title);
    }


    public void closeOtherTabs(int keepIndex) {
        for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
            if (i != keepIndex) {
                if (!closeTab(i)) break;
            }
        }
    }

    public void closeAllTabs() {
        for (int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
            if (!closeTab(i)) break;
        }
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public Vector<GMainPanel> getCanvasPanels() {
        return canvasPanels;
    }

    public Frame getMainFrame() {
        return mainFrame;
    }
}