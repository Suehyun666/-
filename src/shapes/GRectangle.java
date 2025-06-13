package shapes;
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
	public void moveBy(int dx, int dy) {
		this.rectangle.setFrame(
				rectangle.getX() + dx,
				rectangle.getY() + dy,
				rectangle.getWidth(),
				rectangle.getHeight()
		);
	}
	@Override
	public void resize(double sx, double sy, int anchorX, int anchorY) {
		Rectangle2D bounds = this.rectangle.getBounds2D();

		double newWidth = bounds.getWidth() * sx;
		double newHeight = bounds.getHeight() * sy;
		double newX = anchorX - (anchorX - bounds.getX()) * sx;
		double newY = anchorY - (anchorY - bounds.getY()) * sy;

		this.rectangle.setFrame(newX, newY, newWidth, newHeight);
	}
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
}