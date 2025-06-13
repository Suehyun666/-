package shapes;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GRectangle extends GShape{
	private Rectangle2D rectangle;

	public GRectangle() {
		super(new Rectangle2D.Float(0,0,0,0));
		this.rectangle = (Rectangle2D) this.getShape();
	}
	
	@Override
	public void setPoint(int x, int y) {
		this.startX = x;
		this.startY = y;
		this.rectangle.setFrame(x, y, 0, 0);
		this.transform = new AffineTransform();
	}
	
	@Override
	public void dragPoint(int x, int y) {
		int drawX = Math.min(startX, x);
		int drawY = Math.min(startY, y);
		int width = Math.abs(x - startX);
		int height = Math.abs(y - startY);
		this.shape = new Rectangle2D.Float(drawX, drawY, width, height);
	}

	@Override
	public void addPoint(int x, int y) {}
}