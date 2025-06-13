package transformers;

import java.awt.Graphics2D;

import shapes.GShape;

public class GRotater extends GTransFormer {
	private GShape shape;
	private int px, py; 
	private int centerX, centerY;
	public GRotater(GShape gshape) {
		super(gshape);
		this.shape=gshape;
	}
	
	@Override
	public void start(Graphics2D graphics, int x, int y) {
		this.px=x;
		this.py=y;
	}
	
	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		double prevAngle = Math.atan2(py - centerY, px - centerX);
        double currAngle = Math.atan2(y - centerY, x - centerX);
        double deltaAngle = currAngle - prevAngle;
        this.shape.getAffineTransform().rotate(deltaAngle, centerX, centerY);
		this.px=x;
		this.py=y;
	}
	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {
	}
	@Override
	public void finish(Graphics2D graphics, int x, int y) {}
}