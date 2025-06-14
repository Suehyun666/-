package transformers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import shapes.GShape;

public class GRotater extends GTransFormer {
	private double centerX, centerY;
	private double startAngle;

	public GRotater(GShape shape) {
		super(shape);
	}

	@Override
	public void start(Graphics2D g, int x, int y) {
		Rectangle2D bounds = shape.getTransformedShape().getBounds2D();
		centerX = bounds.getCenterX();
		centerY = bounds.getCenterY();

		Point2D localMouse = shape.getInverseTransformedPoint(x, y);
		Point2D localCenter = shape.getInverseTransformedPoint(centerX, centerY);
		startAngle = Math.atan2(
				localMouse.getY() - localCenter.getY(),
				localMouse.getX() - localCenter.getX()
		);
	}

	@Override
	public void drag(Graphics2D g, int x, int y) {
		Point2D localMouse = shape.getInverseTransformedPoint(x, y);
		Point2D localCenter = shape.getInverseTransformedPoint(centerX, centerY);
		double currentAngle = Math.atan2(
				localMouse.getY() - localCenter.getY(),
				localMouse.getX() - localCenter.getX()
		);
		double angleDelta = currentAngle - startAngle;
		AffineTransform rotate = new AffineTransform();
		rotate.rotate(angleDelta, localCenter.getX(), localCenter.getY());
		shape.appendTransform(rotate);
	}

	@Override
	public void finish(Graphics2D graphics, int x, int y) {}

	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {}

}