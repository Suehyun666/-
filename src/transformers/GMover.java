package transformers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
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
		this.prevX = x;
		this.prevY = y;
	}

	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		int dx = x - prevX;
		int dy = y - prevY;

		for (GShape shape : selectedShapes) {
			try {
				AffineTransform inverse = shape.getAffineTransform().createInverse();
				Point2D deltaLocal = inverse.deltaTransform(new Point2D.Double(dx, dy), null);
				AffineTransform move = AffineTransform.getTranslateInstance(deltaLocal.getX(), deltaLocal.getY());
				shape.appendTransform(move);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		prevX = x;
		prevY = y;
	}
	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {}
	@Override
	public void finish(Graphics2D graphics, int x, int y) {}
}