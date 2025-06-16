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
    public void colorSetting(){fileController.handleColorSetting();}
    public void fill() {fileController.handlefill();}
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

    public void sendBackward() {fileController.sendBackward();}
    public void bringForward() {
        fileController.bringForward();
    }
    public void bringToFront(){fileController.bringToFront();}
    public void sendToBack(){fileController.sendToBack();}


    public void showProperties() { tabController.showProperties(); }
    public void selectall(){tabController.handleSelectAll();}
    public void deselect(){tabController.handledeSelect();}

    public void hide(){tabController.hide();}
    public void reselect() { tabController.handleReselect(); }
    public void selectAllLayer() { tabController.handleSelectAllLayer(); }
    public void mode() { fileController.mode(); }
    public void imageSize() { fileController.imageSize(); }
    public void canvasSize() { fileController.canvasSize(); }
    public void rotateImage() { fileController.rotateImage(); }
    public void crop() { fileController.crop(); }
    public void trim() { fileController.trim(); }
    public void duplicate() { fileController.duplicate(); }
    public void generateImage() { fileController.generateImage(); }

    public void newLayer() { System.out.println("New Layer - To be implemented"); }
    public void duplicateLayer() { System.out.println("Duplicate Layer - To be implemented"); }
    public void deleteLayer() { System.out.println("Delete Layer - To be implemented"); }
    public void renameLayer() { System.out.println("Rename Layer - To be implemented"); }
    public void lockLayer() { System.out.println("Lock Layer - To be implemented"); }
    public void groupLayer() { System.out.println("Group Layer - To be implemented"); }
    public void ungroupLayer() { System.out.println("Ungroup Layer - To be implemented"); }
    public void mergeLayer() { System.out.println("Merge Layer - To be implemented"); }
    public void hideLayer() { tabController.hide(); }


    // Graphic Menu 관련 메서드들
    public void lineThickness() { shapeController.showLineThicknessDialog(); }
    public void lineStyle() { shapeController.showLineStyleDialog(); }
    public void fontStyle() { System.out.println("Font Style - To be implemented"); }
    public void fontSize() { System.out.println("Font Size - To be implemented"); }

    // Filter Menu 관련 메서드들
    public void blur() { shapeController.applyBlurFilter(); }
    public void noise() { shapeController.applyNoiseFilter(); }
    public void sharpen() { shapeController.applySharpenFilter(); }
    public void emboss() { shapeController.applyEmbossFilter(); }

    // Window Menu 관련 메서드들
    public void workspace() { System.out.println("Workspace - To be implemented"); }
    public void extension() { System.out.println("Extension - To be implemented"); }
    public void option() { System.out.println("Option - To be implemented"); }
    public void tool() { System.out.println("Tool - To be implemented"); }
    public void history() { tabController.showHistory(); }
    public void preference() { System.out.println("Preference - To be implemented"); }
    public void language(){}

    // Help Menu
    public void systemInfo() { showSystemInfo(); }
    public void about() { showAbout(); }
    public void online() { System.out.println("Online Help - To be implemented"); }
    public void manual() { System.out.println("User Manual - To be implemented"); }

    private void showSystemInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("OS: ").append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version")).append("\n");
        info.append("Memory: ").append(Runtime.getRuntime().totalMemory() / 1024 / 1024).append(" MB\n");
        info.append("Free Memory: ").append(Runtime.getRuntime().freeMemory() / 1024 / 1024).append(" MB");

        javax.swing.JOptionPane.showMessageDialog(null, info.toString(), "System Information", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAbout() {
        String about = "Drawing Application\nVersion 1.0\nCopyright (C) 2025 All rights reserved.";
        javax.swing.JOptionPane.showMessageDialog(null, about, "About", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }


}