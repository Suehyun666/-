package frames;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Stack;
import java.util.Vector;

import javax.swing.*;

import global.GClipboard;
import global.GConstants.EAnchor;
import global.GConstants.EShapeTool;
import shapes.GShape;
import shapes.GShape.EPoints;
import transformers.GDrawer;
import transformers.GMover;
import transformers.GResizer;
import transformers.GRotater;
import transformers.GTransFormer;

public class GMainPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public enum EDrawingState{
    	eidle,
    	e2P,
    	enP
    }
    private GMainFrame mainFrame;
    private Graphics2D graphics2d;
    private GTransFormer transformer;
    private Vector<GShape> shapes;
    private Vector<GShape> selectedShapes;
    private GShape selectedShape;
    private GShape toolshape;

    private Stack<Vector<GShape>> undoStack;
    private Stack<Vector<GShape>> redoStack;

    private double zoomLevel = 1.0;
    private EShapeTool eShapeTool;
    private EDrawingState eDrawingState;
    private boolean bUpdated;
    
    public GMainPanel(GMainFrame mainFrame) {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        MouseEventHandler mouseHandler = new MouseEventHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        this.mainFrame = mainFrame;
        this.toolshape=null;
        this.selectedShape=null;
        this.shapes = new Vector<GShape>();
        this.selectedShapes = new Vector<GShape>();
        this.eShapeTool=EShapeTool.eSelect;
        this.eDrawingState = EDrawingState.eidle;
        this.bUpdated=false;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }
    public void initialize() {
    	shapes.clear();
        repaint();
    }
    //getters & setters
    public GMainFrame getFrame() {return this.mainFrame;}
    public Vector<GShape> getshapes(){
    	return this.shapes;
    }
    public GShape getSelectedShape() {return this.selectedShape;}
    public boolean getUpdated() {
    	return this.bUpdated;
    }
    public void setShapes(Vector<GShape> shapes) {
    	this.shapes=shapes;
    }
    public void setEShapeTool(EShapeTool shapetool) {
    	this.eShapeTool=shapetool;
        if(shapetool != EShapeTool.eSelect) {
            clearSelection();
        }
    }
    public void setUpdate(boolean b) {
		this.bUpdated=b;
	}
    private void selectShape(GShape shape) {
        for(GShape eachshape : this.shapes) {
            eachshape.setSelected(false);
        }
        this.selectedShape=null;
        this.selectedShapes.clear();
        if(shape != null) {
            shape.setSelected(true);
            this.selectedShape = shape;
        }
        updateMenuState();
    }

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

    public void addShape(GShape shape) {
        this.shapes.add(shape);
        updateMenuState();
    }
    public void deleteShape(GShape selectedShape) {
        this.shapes.remove(selectedShape);
        this.selectedShapes.remove(selectedShape);
        if (this.selectedShape == selectedShape) {
            this.selectedShape = null;
        }
        updateMenuState();
    }
    private void clearSelection() {
        for(GShape shape : this.shapes) {
            shape.setSelected(false);
        }
        this.selectedShapes.clear();
        this.selectedShape = null;
    }
    private void changeCursor(int x, int y) {
    	if(this.eShapeTool==EShapeTool.eSelect) {
    		this.selectedShape=onShape(x,y);
        	if(this.selectedShape==null) {
        		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        	}
        	else  {
        		EAnchor anchor =this.selectedShape.getSelectedAnchor();
        		this.setCursor(anchor.getCursor());
        	}
    	}
    }
    public void clear() {
		this.shapes.clear();
	}
    public void cutShape(GShape selectedShape) {}
    public void exit() {
        System.exit(0);
    }
    public void undo() {
        if (!undoStack.isEmpty()) {
            Vector<GShape> currentState = getClonedShapes();
            redoStack.push(currentState);

            Vector<GShape> previousState = undoStack.pop();
            setShapes(previousState);
            repaint();
        }
    }
    public void redo() {
        if (!redoStack.isEmpty()) {
            Vector<GShape> currentState = getClonedShapes();
            undoStack.push(currentState);

            Vector<GShape> nextState = redoStack.pop();
            setShapes(nextState);
            repaint();
        }
    }
    private Vector<GShape> getClonedShapes() {
        Vector<GShape> clonedShapes = new Vector<>();
        for (GShape shape : shapes) {
            clonedShapes.add(shape.clone());
        }
        return clonedShapes;
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
        setUpdate(true);
        mainFrame.setModified(true);
        repaint();
    }

    public void pasteShapes() {
        GClipboard clipboard = GClipboard.getInstance();

        if (clipboard.isEmpty()) {
            return;
        }

        // 상태 저장 (undo를 위해)
        saveStateForUndo();

        Vector<GShape> pastedShapes = clipboard.paste();

        // 붙여넣은 도형들을 메인 패널에 추가
        for (GShape shape : pastedShapes) {
            shapes.add(shape);
        }

        // 붙여넣은 도형들을 선택 상태로 만들기
        clearSelection();
        for (GShape shape : pastedShapes) {
            shape.setSelected(true);
            selectedShapes.add(shape);
        }

        if (!pastedShapes.isEmpty()) {
            selectedShape = pastedShapes.get(0);
        }

        setUpdate(true);
        mainFrame.setModified(true);
        repaint();
    }


    public void duplicateSelectedShapes() {
        if (selectedShape != null || !selectedShapes.isEmpty()) {
            copySelectedShapes();
            pasteShapes();
        }
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
            setUpdate(true);
            mainFrame.setModified(true);
            repaint();
        }
    }

    private void saveStateForUndo() {
        Vector<GShape> currentState = getClonedShapes();
        undoStack.push(currentState);
        redoStack.clear(); // 새로운 작업 시 redo 스택 클리어
    }
    public boolean hasClipboardContent() {
        return !GClipboard.getInstance().isEmpty();
    }

    public boolean hasSelectedShapes() {
        boolean hasSelected = selectedShape != null || !selectedShapes.isEmpty();
        return hasSelected;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(zoomLevel, zoomLevel);
        for(GShape shape: shapes) {
        	shape.draw(g2d);
        }
    }
    private void startTransform(int x, int y) {
    	this.toolshape = this.eShapeTool.newShape();
    	this.shapes.add(this.toolshape);

        if(this.eShapeTool==EShapeTool.eSelect){
        	this.selectedShape = onShape(x,y);
        	if(this.selectedShape == null) {
        		this.transformer = new GDrawer(this.toolshape);
        	}else if(this.selectedShape.getSelectedAnchor()==EAnchor.MM) {
        		this.selectedShape.setSelected(true);
                this.selectedShapes.add(this.selectedShape);
        		this.transformer = new GMover(this.selectedShapes);
        	}else if(this.selectedShape.getSelectedAnchor()==EAnchor.RR) {
        		this.selectedShape.setSelected(true);
        		this.transformer = new GRotater(this.selectedShape);
        	}else {
        		this.selectedShape.setSelected(true);
        		this.transformer = new GResizer(this.selectedShape);
        	}
        }
        else {
        	this.transformer = new GDrawer(this.toolshape);
        }
        this.transformer.start((Graphics2D)getGraphics(), x, y);
    }
    private GShape onShape(int x, int y) {
    	for(GShape shape:this.shapes) {
    		if(shape.contains(x, y)) {
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
    	this.selectShape(this.toolshape);
    	if(this.eShapeTool==EShapeTool.eSelect) {
    		this.shapes.removeLast();
    		for(GShape shape:this.shapes) {
    			if(this.selectedShape.contains(shape)) {
    				shape.setSelected(true);
                    this.selectedShapes.add(shape);
    			}else {
    				shape.setSelected(false);
                    this.selectedShapes.remove(shape);
    			}
    		}
    	}
    	this.bUpdated=true;
    	this.repaint();
    }
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
            int x = canvasPoint.x;
            int y = canvasPoint.y;

            if(eDrawingState == EDrawingState.eidle) {
            	//set transformer
                if(eShapeTool.getEPoints() == EPoints.e2P) {
                    startTransform(x,y);
                    eDrawingState = EDrawingState.e2P;
                } else if(eShapeTool.getEPoints() == EPoints.enP) {
                    startTransform(x,y);
                    eDrawingState = EDrawingState.enP;
                }
            } else if(eDrawingState == EDrawingState.e2P) {
                finishTransform(x,y);
                eDrawingState = EDrawingState.eidle;
            } else if(eDrawingState == EDrawingState.enP) {
                addPoint(x,y);
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            Point canvasPoint = toCanvasPoint(e.getPoint());
            if (eDrawingState == EDrawingState.e2P || eDrawingState == EDrawingState.enP) {
                keepTransform(canvasPoint.x, canvasPoint.y);
            }
            if (eDrawingState==EDrawingState.eidle) {
            	changeCursor(canvasPoint.x,canvasPoint.y);
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
                int rotation;
                rotation = e.getWheelRotation();
                double scaleFactor = 1.1;
                if (rotation < 0) {
                    zoomLevel *= scaleFactor;
                } else {
                    zoomLevel /= scaleFactor;
                }
                zoomLevel = Math.max(0.1, Math.min(10.0, zoomLevel)); // 제한
                repaint();
            }
        }
    }
    private Point toCanvasPoint(Point screenPoint) {
        int x = (int)(screenPoint.x / zoomLevel);
        int y = (int)(screenPoint.y / zoomLevel);
        return new Point(x, y);
    }
}