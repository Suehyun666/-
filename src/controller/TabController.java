package controller;

import frames.GTabManager;
import global.CanvasInfo;
import global.FileData;
import frames.GMainPanel;

import java.io.File;
import java.awt.Color;

public class TabController {
    private final GTabManager tabManager;

    public TabController(GTabManager tabManager) {
        this.tabManager = tabManager;
    }

    // Tab
    public void createNewTab(FileData fileData) {
        tabManager.createTabWithFileData(fileData);
    }
    public void createNewTab(FileData fileData, CanvasInfo canvasInfo) {
        tabManager.createTabWithFileData(fileData, canvasInfo);
    }

    public void handleNewTab() {
        tabManager.createNewTabFromDialog();
    }

    public void handleCloseCurrentTab() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null) return;

        int currentIndex = tabManager.getCurrentTabIndex();
        tabManager.closeTab(currentIndex);
    }

    public void handleCloseAllTabs() {
        tabManager.checkSaveAllTabs();
    }

    public void handleSelectNextTab() {
        int currentIndex = tabManager.getCurrentTabIndex();
        int tabCount = tabManager.getTabCount();

        if (tabCount > 1) {
            int nextIndex = (currentIndex + 1) % tabCount;
            tabManager.selectTab(nextIndex);
        }
    }

    public void handleSelectPreviousTab() {
        int currentIndex = tabManager.getCurrentTabIndex();
        int tabCount = tabManager.getTabCount();

        if (tabCount > 1) {
            int prevIndex = (currentIndex - 1 + tabCount) % tabCount;
            tabManager.selectTab(prevIndex);
        }
    }

    public void focusTabWithFile(File file) {
        int tabIndex = tabManager.findTabWithFile(file);
        if (tabIndex >= 0) {
            tabManager.selectTab(tabIndex);
        }
    }

    // Edit
    public void handleUndo() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null && currentPanel.canUndo()) {
            currentPanel.undo();
            tabManager.setModified(true);
        }
    }

    public void handleRedo() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null && currentPanel.canRedo()) {
            currentPanel.redo();
            tabManager.setModified(true);
        }
    }

    public void handleCut() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            currentPanel.cut();
            tabManager.setModified(true);
        }
    }

    public void handleCopy() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            currentPanel.copy();
        }
    }

    public void handlePaste() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null && currentPanel.canPaste()) {
            currentPanel.paste();
            tabManager.setModified(true);
        }
    }

    public void handleSelectAll() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.selectAll();
        }
    }

    public void handleDeleteSelected() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            currentPanel.deleteSelected();
            tabManager.setModified(true);
        }
    }

    // Canvas
    public void handleZoomIn() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.zoomIn();
        }
    }

    public void handleZoomOut() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.zoomOut();
        }
    }

    public void handleResetZoom() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.resetZoom();
        }
    }

    public void handleFitToWindow() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.fitToWindow();
        }
    }

    // Status
    public boolean hasActiveTab() {
        return tabManager.getCurrentPanel() != null;
    }

    public boolean canUndo() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        return currentPanel != null && currentPanel.canUndo();
    }

    public boolean canRedo() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        return currentPanel != null && currentPanel.canRedo();
    }

    public boolean hasSelection() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        return currentPanel != null && currentPanel.hasSelection();
    }

    // Canvas
    public void setCanvasSize(int width, int height) {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.setCanvasSize(width, height);
            tabManager.setModified(true);
        }
    }

    public void setCanvasBackground(Color backgroundColor) {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.setBackground(backgroundColor);
            tabManager.setModified(true);
        }
    }
}