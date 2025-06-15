package controller;

import frames.GTabManager;
import global.GConstants.EShapeTool;

public class AppController {
    private final TabController tabController;
    private final FileController fileController;
    private final ShapeController shapeController;

    public AppController(GTabManager tabManager) {
        this.tabController = new TabController(tabManager);
        this.fileController = new FileController(tabController, tabManager);
        this.shapeController = new ShapeController(tabManager);
    }

    // File
    public void newFile() {fileController.handleNewFile();}
    public void openFile() {fileController.handleOpenFile();}
    public void saveFile() { fileController.handleSaveFile(); }
    public void saveAsFile() { fileController.handleSaveAsFile(); }
    public void closeFile() { fileController.handleCloseFile(); }
    public void printFile() { fileController.handlePrintFile(); }
    public void importFile() { fileController.handleImportFile(); }
    public void exportFile() { fileController.handleExportFile(); }
    public void exit() { fileController.handleExit(); }

    // Tab
    public void newTab() {tabController.handleNewTab();}
    public void closeTab() {tabController.handleCloseCurrentTab();}
    public void closeAllTabs() { tabController.handleCloseAllTabs(); }
    public void selectNextTab() {tabController.handleSelectNextTab();}
    public void selectPreviousTab() {tabController.handleSelectPreviousTab();}

    // Edit
    public void undo() { tabController.handleUndo(); }
    public void redo() { tabController.handleRedo(); }
    public void cut() { tabController.handleCut(); }
    public void copy() { tabController.handleCopy(); }
    public void paste() { tabController.handlePaste(); }
    public void selectAll() { tabController.handleSelectAll(); }
    public void deleteSelected() { tabController.handleDeleteSelected(); }

    // Shape Tool
    public void setEShapeTool(EShapeTool tool) {shapeController.setCurrentShapeTool(tool);}

    // Canvas
    public void zoomIn() { tabController.handleZoomIn(); }
    public void zoomOut() { tabController.handleZoomOut(); }
    public void resetZoom() { tabController.handleResetZoom(); }
    public void fitToWindow() { tabController.handleFitToWindow(); }

    // Status
    public boolean hasActiveTab() { return tabController.hasActiveTab(); }
    public boolean canUndo() { return tabController.canUndo(); }
    public boolean canRedo() { return tabController.canRedo(); }
    public boolean hasSelection() { return tabController.hasSelection(); }
}