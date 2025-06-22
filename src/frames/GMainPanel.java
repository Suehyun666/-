package frames;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Stack;
import java.util.Vector;

import javax.swing.*;

import menus.GMenubar;
import global.CanvasInfo;
import global.FileData;
import global.GClipboard;
import global.GConstants.EAnchor;
import global.GConstants.EShapeTool;
import dialog.edit.GColorDialog;
import shapes.GShape;
import shapes.GShape.EPoints;
import transformers.GDrawer;
import transformers.GMover;
import transformers.GResizer;
import transformers.GRotater;
import transformers.GTransFormer;

public class GMainPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public enum EDrawingState {
        eidle,
        e2P,
        enP
    }

    // Core references
    private GMainFrame mainFrame;
    private CanvasInfo canvasState;
    private FileData fileState;

    // Drawing state
    private Graphics2D graphics2d;
    private GTransFormer transformer;
    private EShapeTool eShapeTool;
    private EDrawingState eDrawingState;

    // Shape management
    private Vector<GShape> shapes;
    private Vector<GShape> selectedShapes;
    private GShape selectedShape;
    private GShape toolshape;

    // Undo/Redo
    private Stack<Vector<GShape>> undoStack;
    private Stack<Vector<GShape>> redoStack;

    // State flags
    private boolean isUpdated;

    // Constructor
    public GMainPanel(GMainFrame mainFrame, CanvasInfo canvasInfo, FileData fileData) {
        this.mainFrame = mainFrame;
        this.canvasState = canvasInfo;
        this.fileState = fileData;

        initializePanel();
        initializeState();
        setupEventHandlers();

        applyCanvasSettings();
    }

    private void initializePanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
    }

    private void initializeState() {
        this.shapes = new Vector<GShape>();
        this.selectedShapes = new Vector<GShape>();
        this.selectedShape = null;
        this.toolshape = null;

        this.eShapeTool = EShapeTool.eSelect;
        this.eDrawingState = EDrawingState.eidle;

        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();

        this.isUpdated = false;
    }

    private void setupEventHandlers() {
        MouseEventHandler mouseHandler = new MouseEventHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
    }
    private void applyCanvasSettings() {
        Dimension canvasSize = new Dimension(fileState.getWidth(), fileState.getHeight());
        canvasState.setCanvasSize(canvasSize);
        canvasState.setLimitCanvasSize(true);

        Color bgColor = parseBackgroundColor(fileState.getBackground());
        setBackground(bgColor);

        updatePanelSizeForZoom();
        System.out.println("Canvas applied: " + canvasSize + ", Background: " + bgColor);
    }
    private Color parseBackgroundColor(String backgroundType) {
        if (backgroundType == null) return Color.WHITE;
        switch (backgroundType.toLowerCase()) {
            case "white": return Color.WHITE;
            case "black": return Color.BLACK;
            case "transparent": return Color.WHITE;
            default: return Color.WHITE;
        }
    }

    // initialize
    public void initialize() {
        shapes.clear();
        clearSelection();
        repaint();
    }

    // getters setters
    public GMainFrame getFrame() {return this.mainFrame;}
    public Vector<GShape> getshapes() {return this.shapes;}
    public GShape getSelectedShape() {return this.selectedShape;}
    public CanvasInfo getCanvasState() {return this.canvasState;}
    public FileData getFileData() {return fileState;}
    public String getFileName() {return fileState.getFileName();}

    public void setFileName(String name) {
        fileState.setFileName(name);
    }

    public File getCurrentFile() {
        return fileState.getCurrentFile();
    }

    public void setCurrentFile(File file) {
        fileState.setCurrentFile(file);
    }

    public boolean isUpdated() {
        return fileState.hasUnsavedChanges();
    }

    public void setUpdated(boolean changed) {
        fileState.setUnsavedChanges(changed);
        if (mainFrame != null) {
            mainFrame.updateTabTitle(this, changed);
        }
    }

    // Canvas
    public void setCanvasSize(int width, int height) {
        canvasState.setCanvasSize(new Dimension(width, height));
        updatePanelSizeForZoom();
    }
    public Dimension getCanvasSize() {
        return canvasState.getCanvasSize();
    }

    public boolean isCanvasSizeLimited() {
        return canvasState.isCanvasSizeLimited();
    }
    public void setCanvasSizeLimited(boolean limitCanvasSize) {
        canvasState.setLimitCanvasSize(limitCanvasSize);
        updatePanelSizeForZoom();
    }

    // Zoom
    public double getZoomLevel() {
        return canvasState.getZoomLevel();
    }
    public void setZoomLevel(double zoomLevel) {
        canvasState.setZoomLevel(zoomLevel);
        updatePanelSizeForZoom();
    }
    public void zoomIn() {
        canvasState.zoomIn();
        updatePanelSizeForZoom();
        repaint();
    }
    public void zoomOut() {
        canvasState.zoomOut();
        updatePanelSizeForZoom();
        repaint();
    }
    public void resetZoom() {
        canvasState.resetZoom();
        updatePanelSizeForZoom();
        repaint();
    }
    public void fitToWindow() {
        canvasState.fitToWindow(getSize());
        updatePanelSizeForZoom();
        repaint();
    }

    // Shape Tool
    public void setEShapeTool(EShapeTool shapetool) {
        this.eShapeTool = shapetool;
        if (shapetool != EShapeTool.eSelect) {
            updateMenuState();
            clearSelection();
        }
    }
    public EShapeTool getCurrentShapeTool() {
        return eShapeTool;
    }

    // Color

    public void setForegroundColor(Color color) {}

    public void setBackgroundColor(Color color) {
        // 현재 선택된 도형의 배경색 설정
        if (selectedShape != null) {
            // 구현 필요
        }
    }

    public Color getForegroundColor() {
        return selectedShape != null ? selectedShape.getStrokeColor() : Color.BLACK;
    }

    public Color getBackgroundColor() {
        return selectedShape != null ? selectedShape.getFillColor() : Color.WHITE;
    }

    // Shape
    public void setShapes(Vector<GShape> shapes) {
        this.shapes = shapes;
    }

    public void addShape(GShape shape) {
        System.out.println("addShape");
        this.shapes.add(shape);
        updateMenuState();
    }

    public void deleteShape(GShape selectedShape) {
        System.out.println("delete Shape");
        this.shapes.remove(selectedShape);
        this.selectedShapes.remove(selectedShape);
        if (this.selectedShape == selectedShape) {
            this.selectedShape = null;
        }
        updateMenuState();
    }

    // Selection

    private void selectShape(GShape shape) {
        for (GShape eachshape : this.shapes) {
            eachshape.setSelected(false);
        }
        this.selectedShape = null;
        this.selectedShapes.clear();
        if (shape != null) {
            shape.setSelected(true);
            this.selectedShape = shape;
            this.selectedShapes.add(shape);
            if (mainFrame.getColorPanel() != null) {
                mainFrame.getColorPanel().updateFromShape(shape);
            }
        }
        updateMenuState();
    }

    private void clearSelection() {
        System.out.println("clear selection");
        for (GShape shape : this.shapes) {
            shape.setSelected(false);
        }
        this.selectedShapes.clear();
        this.selectedShape = null;
        if (mainFrame.getColorPanel() != null) {
            mainFrame.getColorPanel().initialize();
        }
    }

    public void selectAll() {
        this.selectedShapes.clear();
        this.selectedShape = null;
        for (GShape shape : this.shapes) {
            shape.setSelected(true);
            this.selectedShapes.add(shape);
        }
        if (!this.shapes.isEmpty()) {
            this.selectedShape = this.shapes.get(0);
        }
        updateMenuState();
        repaint();
    }

    public void deselectAll() {
        for (GShape shape : this.shapes) {
            shape.setSelected(false);
        }
        this.selectedShapes.clear();
        this.selectedShape = null;
        updateMenuState();
        repaint();
    }

    public boolean hasSelectedShapes() {
        return selectedShape != null || !selectedShapes.isEmpty();
    }

    public boolean hasSelection() {
        return hasSelectedShapes();
    }

    // === Undo/Redo Operations ===

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undo() {
        System.out.println("undo");
        if (!undoStack.isEmpty()) {
            Vector<GShape> currentState = getClonedShapes();
            redoStack.push(currentState);

            Vector<GShape> previousState = undoStack.pop();
            setShapes(previousState);
            repaint();
        }
    }

    public void redo() {
        System.out.println("redo");
        if (!redoStack.isEmpty()) {
            Vector<GShape> currentState = getClonedShapes();
            undoStack.push(currentState);

            Vector<GShape> nextState = redoStack.pop();
            setShapes(nextState);

            clearSelectionWithoutUpdate();

            setUpdated(true);
            mainFrame.setModified(true);
            updateMenuState();
            repaint();
        }
    }

    private void saveStateForUndo() {
        Vector<GShape> currentState = getClonedShapes();
        undoStack.push(currentState);
        redoStack.clear();
    }

    private Vector<GShape> getClonedShapes() {
        Vector<GShape> clonedShapes = new Vector<>();
        for (GShape shape : shapes) {
            clonedShapes.add(shape.clone());
        }
        return clonedShapes;
    }

    // Clipboard

    public boolean canPaste() {
        return !GClipboard.getInstance().isEmpty();
    }
    public void cut() {
        cutSelectedShapes();
    }
    public void copy() {
        copySelectedShapes();
    }
    public void paste() {
        pasteShapes();
    }
    public void deleteSelected() {
        deleteSelectedShapes();
    }
    public void copySelectedShapes() {
        GClipboard clipboard = GClipboard.getInstance();

        if (selectedShape != null) {
            clipboard.copy(selectedShape);
        } else if (!selectedShapes.isEmpty()) {
            clipboard.copy(selectedShapes);
        }
    }
    public void cutSelectedShapes() {
        GClipboard clipboard = GClipboard.getInstance();
        Vector<GShape> shapesToCut = new Vector<>();
        if (selectedShape != null) {
            shapesToCut.add(selectedShape);
            clipboard.cut(selectedShape);
        } else if (!selectedShapes.isEmpty()) {
            shapesToCut.addAll(selectedShapes);
            clipboard.cut(selectedShapes);
        }
        saveStateForUndo();
        for (GShape shape : shapesToCut) {
            shapes.remove(shape);
        }
        clearSelection();
        setUpdated(true);
        mainFrame.setModified(true);
        repaint();
    }
    public void pasteShapes() {
        GClipboard clipboard = GClipboard.getInstance();

        if (clipboard.isEmpty()) {
            return;
        }
        saveStateForUndo();
        Vector<GShape> pastedShapes = clipboard.paste();
        for (GShape shape : pastedShapes) {
            shapes.add(shape);
        }
        clearSelection();
        for (GShape shape : pastedShapes) {
            shape.setSelected(true);
            selectedShapes.add(shape);
        }
        if (!pastedShapes.isEmpty()) {
            selectedShape = pastedShapes.get(0);
        }
        setUpdated(true);
        mainFrame.setModified(true);
        repaint();
    }

    public void deleteSelectedShapes() {
        Vector<GShape> shapesToDelete = new Vector<>();
        if (selectedShape != null) {
            shapesToDelete.add(selectedShape);
        } else if (!selectedShapes.isEmpty()) {
            shapesToDelete.addAll(selectedShapes);
        }
        if (!shapesToDelete.isEmpty()) {
            saveStateForUndo();
            for (GShape shape : shapesToDelete) {
                shapes.remove(shape);
            }
            clearSelection();
            setUpdated(true);
            mainFrame.setModified(true);
            repaint();
        }
    }

    //Color Properties

    public void updateSelectedShapeColors(Color fillColor, Color strokeColor, int strokeWidth, boolean fillEnabled, boolean strokeEnabled) {
        if (selectedShape != null) {
            selectedShape.setColorProperties(fillColor, strokeColor, strokeWidth, fillEnabled, strokeEnabled);
            repaint();
            setUpdated(true);
            mainFrame.setModified(true);
        }
        if (!selectedShapes.isEmpty()) {
            for (GShape shape : selectedShapes) {
                shape.setColorProperties(fillColor, strokeColor, strokeWidth, fillEnabled, strokeEnabled);
            }
            repaint();
            setUpdated(true);
            mainFrame.setModified(true);
        }
    }

    // === Shape Properties (Placeholders) ===

    public void setLineWidth(int width) {
        // 구현 필요
    }

    public void setFillMode(boolean filled) {
        // 구현 필요
    }

    public void setDashPattern(float[] dashPattern) {
        // 구현 필요
    }

    // === Helper Methods ===

    private void updateMenuState() {
        if (mainFrame != null && mainFrame.getJMenuBar() instanceof GMenubar) {
            GMenubar menubar = (GMenubar) mainFrame.getJMenuBar();
            if (menubar.getEditMenu() != null) {
                SwingUtilities.invokeLater(() -> {
                    menubar.getEditMenu().updateMenuState();
                });
            }
        }
    }

    private void clearSelectionWithoutUpdate() {
        for (GShape shape : this.shapes) {
            shape.setSelected(false);
        }
        this.selectedShapes.clear();
        this.selectedShape = null;
    }

    private void changeCursor(int x, int y) {
        if (this.eShapeTool == EShapeTool.eSelect) {
            GShape hoverShape = onShape(x, y);
            if (hoverShape  == null) {
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                EAnchor anchor = hoverShape.getSelectedAnchor();
                this.setCursor(anchor.getCursor());
            }
        }
    }

    private void updatePanelSizeForZoom() {
        boolean limitCanvasSize = canvasState.isCanvasSizeLimited();
        Dimension canvasSize = canvasState.getCanvasSize();
        double zoomLevel = canvasState.getZoomLevel();
        if (limitCanvasSize && canvasSize != null) {
            int scaledWidth = (int) (canvasSize.width * zoomLevel);
            int scaledHeight = (int) (canvasSize.height * zoomLevel);
            setPreferredSize(new Dimension(scaledWidth, scaledHeight));
            revalidate();
        }
    }

    private Point toCanvasPoint(Point screenPoint) {
        boolean limitCanvasSize = canvasState.isCanvasSizeLimited();
        Dimension canvasSize = canvasState.getCanvasSize();
        double zoomLevel = canvasState.getZoomLevel();
        int x = (int) (screenPoint.x / zoomLevel);
        int y = (int) (screenPoint.y / zoomLevel);
        if (limitCanvasSize && canvasSize != null) {
            int scaledCanvasWidth = (int) (canvasSize.width * zoomLevel);
            int scaledCanvasHeight = (int) (canvasSize.height * zoomLevel);
            if (screenPoint.x < 0 || screenPoint.y < 0 ||
                    screenPoint.x > scaledCanvasWidth || screenPoint.y > scaledCanvasHeight) {
                return null;
            }
            if (x < 0 || y < 0 || x > canvasSize.width || y > canvasSize.height) {
                return null;
            }
        }
        return new Point(x, y);
    }

    //Draw

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        boolean limitCanvasSize = canvasState.isCanvasSizeLimited();
        Dimension canvasSize = canvasState.getCanvasSize();
        double zoomLevel = canvasState.getZoomLevel();

        if (limitCanvasSize && canvasSize != null) {
            int scaledCanvasWidth = (int) (canvasSize.width * zoomLevel);
            int scaledCanvasHeight = (int) (canvasSize.height * zoomLevel);

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, scaledCanvasWidth, scaledCanvasHeight);
            g2d.setColor(Color.DARK_GRAY);

            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(0, 0, scaledCanvasWidth - 1, scaledCanvasHeight - 1);

            Shape oldClip = g2d.getClip();
            g2d.setClip(0, 0, scaledCanvasWidth, scaledCanvasHeight);
            g2d.scale(zoomLevel, zoomLevel);

            for (GShape shape : shapes) {
                shape.draw(g2d);
            }
            g2d.setClip(oldClip);
        } else {
            g2d.scale(zoomLevel, zoomLevel);
            for (GShape shape : shapes) {
                shape.draw(g2d);
            }
        }
    }

    private void startTransform(int x, int y) {
        this.toolshape = this.eShapeTool.newShape();
        this.shapes.add(this.toolshape);

        if (this.eShapeTool == EShapeTool.eSelect) {
            GShape clickedShape = onShape(x, y);
            if (clickedShape == null) {
                System.out.println("영역 모드");
                clearSelection();
                this.toolshape.setSelectMode(true);
                this.transformer = new GDrawer(this.toolshape);
            } else {
                System.out.println("도형 선택: " + clickedShape.getClass().getSimpleName());
                selectShape(clickedShape);
                this.selectedShape = clickedShape;

                if (this.selectedShape.getSelectedAnchor() == EAnchor.MM) {
                    this.selectedShape.setSelected(true);
                    if (!this.selectedShapes.contains(this.selectedShape)) {
                        this.selectedShapes.add(this.selectedShape);
                    }
                    this.transformer = new GMover(this.selectedShapes);
                } else if (this.selectedShape.getSelectedAnchor() == EAnchor.RR) {
                    this.selectedShape.setSelected(true);
                    this.transformer = new GRotater(this.selectedShape);
                } else {
                    this.selectedShape.setSelected(true);
                    this.transformer = new GResizer(this.selectedShape);
                }
            }
        } else {
            if (this.toolshape != null && mainFrame.getColorPanel() != null) {
                GColorDialog.ColorProperties colorProps = mainFrame.getColorPanel().getCurrentColorProperties();
                this.toolshape.setColorProperties(colorProps.fillColor, colorProps.strokeColor, colorProps.strokeWidth, colorProps.fillEnabled, colorProps.strokeEnabled);
            }
            this.transformer = new GDrawer(this.toolshape);
        }

        this.transformer.start((Graphics2D) getGraphics(), x, y);
    }

    private GShape onShape(int x, int y) {
        for (int i = this.shapes.size() - 1; i >= 0; i--) {
            GShape shape = this.shapes.get(i);
            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }

    private void keepTransform(int x, int y) {
        this.transformer.drag(graphics2d, x, y);
        this.repaint();
    }

    private void addPoint(int x, int y) {
        this.transformer.addpoint((Graphics2D) getGraphics(), x, y);
    }

    private void finishTransform(int x, int y) {
        this.transformer.finish((Graphics2D) getGraphics(), x, y);

        if (this.eShapeTool == EShapeTool.eSelect) {
            this.shapes.remove(this.toolshape);

            if (this.toolshape.isSelectMode()) {
                clearSelection();
                for (GShape shape : this.shapes) {
                    if (this.toolshape.contains(shape)) {
                        shape.setSelected(true);
                        if (!this.selectedShapes.contains(shape)) {
                            shape.setSelected(true);
                            this.selectedShape=shape;
                            System.out.println("선택되었습니다.");
                            this.selectedShapes.add(shape);
                        }
                    } else {
                        shape.setSelected(false);
                        this.selectedShapes.remove(shape);
                    }
                }
            }
        } else {
            this.selectShape(this.toolshape);
        }

        this.setUpdated(true);
        this.repaint();
    }

    // Mouse event
    private class MouseEventHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                this.mouse1Clicked(e);
            } else if (e.getClickCount() == 2) {
                this.mouse2Clicked(e);
            }
        }

        private void mouse1Clicked(MouseEvent e) {
            Point canvasPoint = toCanvasPoint(e.getPoint());
            if (canvasPoint == null) return;
            int x = canvasPoint.x;
            int y = canvasPoint.y;

            if (eDrawingState == EDrawingState.eidle) {
                if (eShapeTool.getEPoints() == EPoints.e2P) {
                    startTransform(x, y);
                    eDrawingState = EDrawingState.e2P;
                } else if (eShapeTool.getEPoints() == EPoints.enP) {
                    startTransform(x, y);
                    eDrawingState = EDrawingState.enP;
                }
            } else if (eDrawingState == EDrawingState.e2P) {
                finishTransform(x, y);
                eDrawingState = EDrawingState.eidle;
            } else if (eDrawingState == EDrawingState.enP) {
                addPoint(x, y);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point canvasPoint = toCanvasPoint(e.getPoint());
            if (canvasPoint == null) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return;
            }
            if (eDrawingState == EDrawingState.e2P || eDrawingState == EDrawingState.enP) {
                keepTransform(canvasPoint.x, canvasPoint.y);
            }
            if (eDrawingState == EDrawingState.eidle) {
                changeCursor(canvasPoint.x, canvasPoint.y);
            }
        }

        private void mouse2Clicked(MouseEvent e) {
            Point canvasPoint = toCanvasPoint(e.getPoint());
            if (eDrawingState == EDrawingState.enP) {
                finishTransform(canvasPoint.x, canvasPoint.y);
                eDrawingState = EDrawingState.eidle;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseDragged(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.isControlDown()) {
                int rotation = e.getWheelRotation();
                double scaleFactor = 1.1;
                double currentZoom = canvasState.getZoomLevel();

                if (rotation < 0) {
                    currentZoom *= scaleFactor;
                } else {
                    currentZoom /= scaleFactor;
                }
                currentZoom = Math.max(0.1, Math.min(10.0, currentZoom));

                canvasState.setZoomLevel(currentZoom);
                updatePanelSizeForZoom();
                repaint();
            }
        }
    }
    public void clear() {
        System.out.println("clear");
        this.shapes.clear();
    }
    public void exit() {
        System.exit(0);
    }

    public void hide() {
        System.out.println("hide");
        if(this.selectedShape==null) return;
        this.selectedShape.setVisible(false);
    }

    public int getUndoStackSize() {
        return undoStack.size();
    }

    public int getRedoStackSize() {
        return redoStack.size();
    }

    public String getCanvasInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Shapes: ").append(shapes.size());
        info.append(", Selected: ").append(hasSelection() ? "Yes" : "No");
        info.append(", Undo: ").append(canUndo() ? undoStack.size() : 0);
        info.append(", Redo: ").append(canRedo() ? redoStack.size() : 0);
        return info.toString();
    }

    public Vector<String> getShapesList() {
        Vector<String> shapeNames = new Vector<>();
        for (int i = 0; i < shapes.size(); i++) {
            GShape shape = shapes.get(i);
            String name = shape.getClass().getSimpleName() + " " + (i + 1);
            if (shape == selectedShape) {
                name += " (Selected)";
            }
            if (!shape.isVisible()) {
                name += " (Hidden)";
            }
            shapeNames.add(name);
        }
        return shapeNames;
    }
}