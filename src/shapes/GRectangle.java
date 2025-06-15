package shapes;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape{
	private static final long serialVersionUID = 1L;
	private Rectangle2D.Float rectangle;

	public GRectangle() {
		super(new Rectangle2D.Float(0, 0, 0, 0));
		this.rectangle = (Rectangle2D.Float) super.originalShape;
		this.updateTransformedShape();
	}
	
	@Override
	public void setPoint(int x, int y) {
		this.startX = x;
		this.startY = y;
		if (this.rectangle == null) {
			this.rectangle = new Rectangle2D.Float(x, y, 0, 0);
		}else{
			this.rectangle.setFrame(x, y, 0, 0);
		}
		this.originalShape = this.rectangle;
		this.updateTransformedShape();
	}

	@Override
	public void dragPoint(int x, int y) {
		int drawX = Math.min(startX, x);
		int drawY = Math.min(startY, y);
		int width = Math.abs(x - startX);
		int height = Math.abs(y - startY);
		this.rectangle.setFrame(drawX, drawY, width, height);
		this.shape = this.rectangle;
		this.updateTransformedShape();
	}

	@Override
	public void addPoint(int x, int y) {}

	@Override
	public void drawSelectMode(Graphics2D g2d){
		g2d.setColor(new Color(0, 100, 255, 150));
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10, new float[]{5, 5}, 0));
		g2d.draw(this.transformedShape);
	}

}