package shapes;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape{
	private static final long serialVersionUID = 1L;
	private Rectangle2D.Float rectangle;

	public GRectangle() {
		super(new Rectangle2D.Float(0,0,0,0));
		this.rectangle = (Rectangle2D.Float) super.shape;
	}
	
	@Override
	public void setPoint(int x, int y) {
		this.startX = x;
		this.startY = y;
		this.rectangle.setFrame(x, y, 0, 0);
	}
	
	@Override
	public void dragPoint(int x, int y) {
		int drawX = Math.min(startX, x);
		int drawY = Math.min(startY, y);
		int width = Math.abs(x - startX);
		int height = Math.abs(y - startY);
		this.rectangle.setFrame(drawX, drawY, width, height);
	}

	@Override
	public void addPoint(int x, int y) {}

	@Override
	public GRectangle clone() {
		GRectangle cloned = (GRectangle) super.clone();
		cloned.rectangle = new Rectangle2D.Float(
				(float) this.rectangle.getX(),
				(float) this.rectangle.getY(),
				(float) this.rectangle.getWidth(),
				(float) this.rectangle.getHeight()
		);
		cloned.shape = cloned.rectangle;
		return cloned;
	}
	@Override
	public void drawSelectMode(Graphics2D g2d){
		Color originalColor = g2d.getColor();
		Stroke originalStroke = g2d.getStroke();
		g2d.setColor(new Color(0, 100, 255, 150));
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10, new float[]{5, 5}, 0));
		g2d.draw(this.rectangle);
		g2d.setColor(originalColor);
		g2d.setStroke(originalStroke);
	}
}