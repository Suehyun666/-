package transformers;

import java.awt.Graphics2D;
import java.util.Vector;

import shapes.GShape;

public class GMover extends GTransFormer {
	private Vector<GShape> selectedShapes;
	private int px, py; 
	
	public GMover(Vector<GShape> selectedShapes) {
		super(selectedShapes.getFirst());
		this.selectedShapes=selectedShapes;
	}
	
	@Override
	public void start(Graphics2D graphics, int x, int y) {
		this.px=x;
		this.py=y;
	}
	
	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		int tx=x-px;
		int ty=y-py;
		this.shape.getAffineTransform().translate(tx, ty);		
		this.px=x;
		this.py=y;
	}
	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {
	}
	@Override
	public void finish(Graphics2D graphics, int x, int y) {}
}