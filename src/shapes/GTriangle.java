package shapes;
import java.awt.geom.Path2D;

public class GTriangle extends GShape{
	private Path2D.Float triangle;
	private int startX, startY;
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

		this.triangle.reset();
		this.triangle.moveTo(x, y);
		this.triangle.lineTo(x, y);
		this.triangle.lineTo(x, y);
		this.triangle.closePath();

		this.xPoints[0] = x;
		this.yPoints[0] = y;
		this.xPoints[1] = x;
		this.yPoints[1] = y;
		this.xPoints[2] = x;
		this.yPoints[2] = y;
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
		triangle.reset();
		triangle.moveTo(xPoints[0], yPoints[0]);
		triangle.lineTo(xPoints[1], yPoints[1]);
		triangle.lineTo(xPoints[2], yPoints[2]);
		triangle.closePath();
	}

	@Override
	public void addPoint(int x, int y) {}
}