package controller;

import frames.GTabManager;
import global.GConstants.EShapeTool;
import frames.GMainPanel;



public class ShapeController {
    private final GTabManager tabManager;

    public ShapeController(GTabManager tabManager) {
        this.tabManager = tabManager;
    }

    // Shape Tool
    public void setCurrentShapeTool(EShapeTool shapeTool) {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.setEShapeTool(shapeTool);
        }
        updateAllPanelsShapeTool(shapeTool);
    }

    private void updateAllPanelsShapeTool(EShapeTool shapeTool) {
        for (int i = 0; i < tabManager.getTabCount(); i++) {
            GMainPanel panel = tabManager.getPanelAt(i);
            if (panel != null) {
                panel.setEShapeTool(shapeTool);
            }
        }
    }
}