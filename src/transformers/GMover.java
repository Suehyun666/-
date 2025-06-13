package transformers;

import java.awt.Graphics2D;
import java.util.Vector;

import shapes.GShape;

public class GMover extends GTransFormer {
	private Vector<GShape> selectedShapes;
	
	public GMover(Vector<GShape> selectedShapes) {
		super(selectedShapes.getFirst());
		this.selectedShapes=selectedShapes;
	}
	
	@Override
	public void start(Graphics2D graphics, int x, int y) {
		for (GShape shape : selectedShapes) {
			shape.setMovePoint(x, y);
		}
	}
	
	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		for(GShape shape : selectedShapes) {
			shape.movePoint(x, y);
		}
	}
	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {
	}
	@Override
	public void finish(Graphics2D graphics, int x, int y) {}
}