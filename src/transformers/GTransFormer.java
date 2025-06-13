package transformers;

import java.awt.Graphics2D;

import shapes.GRectangle;
import shapes.GShape;

public abstract class GTransFormer {
	protected GShape shape;
	protected int startX,startY;
	public GTransFormer(GShape gshape) {
		this.shape=gshape;
	}
	public abstract void start(Graphics2D graphics, int x, int y);
	public abstract void drag(Graphics2D graphics, int x, int y);
	public abstract void finish(Graphics2D graphics, int x, int y);
	public abstract void addpoint(Graphics2D graphics, int x, int y);
}
