package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JPanel;

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
    private Graphics2D graphics2d;
    private GTransFormer transformer;
    private Vector<GShape> shapes;
    private Vector<GShape> selectedShapes;
    private GShape selectedShape;
    private GShape toolshape;

    private Stack<Vector<GShape>> undoStack;
    private Stack<Vector<GShape>> redoStack;
    
    private EShapeTool eShapeTool;
    private EDrawingState eDrawingState;
    private boolean bUpdated;
    
    public GMainPanel() { 
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        MouseEventHandler mouseHandler = new MouseEventHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
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
    }
    public void deleteShape(GShape selectedShape) {
        this.selectedShapes.remove(selectedShape);
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
    public void clearShapes() {
		this.shapes.clear();
	}
    public void cutShape(GShape selectedShape) {}
    public void exit() {
        System.exit(0);
    }
    public void undo() {}
    public void redo() {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(GShape shape: shapes) {
        	shape.draw((Graphics2D) g);
        }
    }
    private void startTransform(int x, int y) {
    	this.toolshape = this.eShapeTool.newShape();
    	this.shapes.add(this.toolshape);
        System.out.println(this.toolshape);
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
    		}System.out.println("선택된 도형: "+this.selectedShapes.size());
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
            if(eDrawingState == EDrawingState.eidle) {
            	//set transformer
                if(eShapeTool.getEPoints() == EPoints.e2P) {
                    startTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.e2P;
                } else if(eShapeTool.getEPoints() == EPoints.enP) {
                    startTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.enP;
                }
            } else if(eDrawingState == EDrawingState.e2P) {
                finishTransform(e.getX(), e.getY());
                eDrawingState = EDrawingState.eidle;
            } else if(eDrawingState == EDrawingState.enP) {
                addPoint(e.getX(), e.getY());
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            if (eDrawingState == EDrawingState.e2P || eDrawingState == EDrawingState.enP) {
                keepTransform(e.getX(), e.getY());
            }
            if (eDrawingState==EDrawingState.eidle) {
            	changeCursor(e.getX(),e.getY());
            }
        }
        private void mouse2Clicked(MouseEvent e) {
            if (eDrawingState == EDrawingState.enP) {
                finishTransform(e.getX(), e.getY());
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
        public void mouseWheelMoved(MouseWheelEvent e) {}
    }
	
}