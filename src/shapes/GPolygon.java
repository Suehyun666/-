package shapes;

import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class GPolygon extends GShape{
	private static final long serialVersionUID = 1L;
	private Polygon polygon;
	
	public GPolygon() {
		super(new Polygon());
		this.polygon = (Polygon) this.getShape();
		this.shape=this.polygon;
	}
	
	@Override
	public void setPoint(int x, int y) {
		this.polygon.addPoint(x, y);
		this.polygon.addPoint(x, y);
	}
	
	@Override
	public void dragPoint(int x, int y) {
		polygon.xpoints[polygon.npoints - 1] = x;
		polygon.ypoints[polygon.npoints - 1] = y;
		polygon.invalidate();
	}

	@Override
	public void addPoint(int x, int y) {
		this.shape = new Line2D.Float(x, y, x, y);
		this.transform = new AffineTransform();
	}
	@Override
	public void resize(double sx, double sy, int anchorX, int anchorY) {

	}

	@Override
	public void moveBy(int dx, int dy) {

	}
}