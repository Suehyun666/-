package shapes;
import java.awt.geom.Path2D;

public class GTriangle extends GShape{
	private static final long serialVersionUID = 1L;
	private Path2D.Float triangle;
	private int[] xPoints;
	private int[] yPoints;

	public GTriangle() {
		super(new Path2D.Float());
		this.triangle = (Path2D.Float) this.getShape();
		this.xPoints = new int[3];
		this.yPoints = new int[3];
	}

	@Override
	public void setPoint(int x, int y) {
		this.startX = x;
		this.startY = y;

		this.xPoints[0] = x;
		this.yPoints[0] = y;
		this.xPoints[1] = x;
		this.yPoints[1] = y;
		this.xPoints[2] = x;
		this.yPoints[2] = y;

		updateTrianglePath();
		this.transform = new java.awt.geom.AffineTransform();
	}
	@Override
	public void dragPoint(int x, int y) {
		xPoints[0] = startX;
		yPoints[0] = startY;

		xPoints[1] = x;
		yPoints[1] = startY;

		xPoints[2] = startX + (x - startX) / 2;
		yPoints[2] = y;
		updateTrianglePath();
	}
	private void updateTrianglePath() {
		Path2D.Float triangle = new Path2D.Float();
		triangle.moveTo(xPoints[0], yPoints[0]);
		triangle.lineTo(xPoints[1], yPoints[1]);
		triangle.lineTo(xPoints[2], yPoints[2]);
		triangle.closePath();
		this.shape = triangle;
	}

	@Override
	public void addPoint(int x, int y) {}

	@Override
	public GTriangle clone() {
		GTriangle cloned = (GTriangle) super.clone();
		cloned.triangle = new Path2D.Float();
		cloned.xPoints = this.xPoints.clone();
		cloned.yPoints = this.yPoints.clone();
		cloned.startX = this.startX;
		cloned.startY = this.startY;
		cloned.updateTrianglePath();
		cloned.shape = cloned.triangle;
		return cloned;
	}
}