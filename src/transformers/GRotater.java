package transformers;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import shapes.GShape;

public class GRotater extends GTransFormer {
	private double centerX, centerY;
	private double startAngle;
	private double initialScaleX, initialScaleY;
	private double initialRotation;
	private double initialTranslateX, initialTranslateY;

	public GRotater(GShape shape) {
		super(shape);
	}

	@Override
	public void start(Graphics2D g, int x, int y) {
		// 현재 파라미터 저장
		this.initialScaleX = shape.scaleX;
		this.initialScaleY = shape.scaleY;
		this.initialRotation = shape.rotationAngle;
		this.initialTranslateX = shape.translateX;
		this.initialTranslateY = shape.translateY;

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

		// 파라미터 직접 업데이트 (누적 없이)
		shape.scaleX = initialScaleX;
		shape.scaleY = initialScaleY;
		shape.rotationAngle = initialRotation + angleDelta;
		shape.translateX = initialTranslateX;
		shape.translateY = initialTranslateY;

		shape.updateTransformedShape();
	}

	@Override
	public void finish(Graphics2D graphics, int x, int y) {}

	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {}
}