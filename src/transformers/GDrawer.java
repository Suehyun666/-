package transformers;

import java.awt.Graphics2D;
import shapes.GShape;

public class GDrawer extends GTransFormer {
	private GShape shape;
	
	public GDrawer(GShape gshape) {
		super(gshape);
		this.shape=gshape;
	}
	
	@Override
	public void start(Graphics2D graphics, int x, int y) {
		this.shape.setPoint(x, y);
	}
	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		this.shape.dragPoint(x, y);
		shape.updateTransformedShape();
	}
	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {
		shape.setPoint(x, y);
	}
	@Override
	public void finish(Graphics2D graphics, int x, int y) {
		System.out.println("finish");
		shape.updateTransformedShape();}
}