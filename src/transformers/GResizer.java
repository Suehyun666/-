package transformers;

import global.GConstants.EAnchor;
import shapes.GShape;
import java.awt.*;
import java.awt.geom.*;

public class GResizer extends GTransFormer {
	private Point2D startMouse;
	private Point2D fixedPoint;
	private EAnchor anchor;
	private double startScaleX, startScaleY;
	private double startTransX, startTransY;
	private static final double MIN_SIZE = 20.0;

	public GResizer(GShape shape) {
		super(shape);
	}

	@Override
	public void start(Graphics2D g, int x, int y) {
		this.anchor = shape.getSelectedAnchor();
		this.startMouse = new Point2D.Double(x, y);
		this.startScaleX = shape.scaleX;
		this.startScaleY = shape.scaleY;
		this.startTransX = shape.translateX;
		this.startTransY = shape.translateY;
		setFixedPoint();
	}
	private void setFixedPoint() {
		Rectangle2D bounds = shape.getOriginalShape().getBounds2D();
		switch (anchor) {
			case NW -> fixedPoint = new Point2D.Double(bounds.getMaxX(), bounds.getMaxY()); // SE 고정
			case NE -> fixedPoint = new Point2D.Double(bounds.getMinX(), bounds.getMaxY()); // SW 고정
			case SW -> fixedPoint = new Point2D.Double(bounds.getMaxX(), bounds.getMinY()); // NE 고정
			case SE -> fixedPoint = new Point2D.Double(bounds.getMinX(), bounds.getMinY()); // NW 고정
			case NN -> fixedPoint = new Point2D.Double(bounds.getCenterX(), bounds.getMaxY()); // S 고정
			case SS -> fixedPoint = new Point2D.Double(bounds.getCenterX(), bounds.getMinY()); // N 고정
			case EE -> fixedPoint = new Point2D.Double(bounds.getMinX(), bounds.getCenterY()); // W 고정
			case WW -> fixedPoint = new Point2D.Double(bounds.getMaxX(), bounds.getCenterY()); // E 고정
		}
		fixedPoint = shape.getAffineTransform().transform(fixedPoint, null);
	}

	@Override
	public void drag(Graphics2D g, int x, int y) {
		Point2D currentMouse = new Point2D.Double(x, y);
		double newScaleX = startScaleX;
		double newScaleY = startScaleY;
		switch (anchor) {
			case NW, NE, SW, SE -> {
				newScaleX = calculateScaleForDirection(currentMouse, true);
				newScaleY = calculateScaleForDirection(currentMouse, false);
			}
			case EE, WW -> {
				newScaleX = calculateScaleForDirection(currentMouse, true);
			}
			case NN, SS -> {
				newScaleY = calculateScaleForDirection(currentMouse, false);
			}
		}
		Rectangle2D originalBounds = shape.getOriginalShape().getBounds2D();
		double minScaleX = MIN_SIZE / originalBounds.getWidth();
		double minScaleY = MIN_SIZE / originalBounds.getHeight();

		newScaleX = Math.max(minScaleX, newScaleX);
		newScaleY = Math.max(minScaleY, newScaleY);

		double[] offsets = calculateOffsets(newScaleX, newScaleY);

		shape.scaleX = newScaleX;
		shape.scaleY = newScaleY;
		shape.translateX = startTransX + offsets[0];
		shape.translateY = startTransY + offsets[1];

		shape.updateTransformedShape();
	}
	private double[] calculateOffsets(double newScaleX, double newScaleY) {
		Rectangle2D originalBounds = shape.getOriginalShape().getBounds2D();
		Point2D originalCenter = new Point2D.Double(originalBounds.getCenterX(), originalBounds.getCenterY());
		Point2D fixedInOriginal = getFixedPointInOriginal();
		double offsetX = (originalCenter.getX() - fixedInOriginal.getX()) * (newScaleX - startScaleX);
		double offsetY = (originalCenter.getY() - fixedInOriginal.getY()) * (newScaleY - startScaleY);
		if (shape.rotationAngle != 0) {
			double cos = Math.cos(shape.rotationAngle);
			double sin = Math.sin(shape.rotationAngle);
			double rotatedOffsetX = offsetX * cos - offsetY * sin;
			double rotatedOffsetY = offsetX * sin + offsetY * cos;

			return new double[]{rotatedOffsetX, rotatedOffsetY};
		}
		return new double[]{offsetX, offsetY};
	}
	private Point2D getFixedPointInOriginal() {
		Rectangle2D bounds = shape.getOriginalShape().getBounds2D();
		return switch (anchor) {
			case NW -> new Point2D.Double(bounds.getMaxX(), bounds.getMaxY());
			case NE -> new Point2D.Double(bounds.getMinX(), bounds.getMaxY());
			case SW -> new Point2D.Double(bounds.getMaxX(), bounds.getMinY());
			case SE -> new Point2D.Double(bounds.getMinX(), bounds.getMinY());
			case NN -> new Point2D.Double(bounds.getCenterX(), bounds.getMaxY());
			case SS -> new Point2D.Double(bounds.getCenterX(), bounds.getMinY());
			case EE -> new Point2D.Double(bounds.getMinX(), bounds.getCenterY());
			case WW -> new Point2D.Double(bounds.getMaxX(), bounds.getCenterY());
			default -> new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
		};
	}
	private double calculateScaleForDirection(Point2D currentMouse, boolean isX) {
		double startDist, currentDist;
		if (isX) {
			startDist = Math.abs(startMouse.getX() - fixedPoint.getX());
			currentDist = Math.abs(currentMouse.getX() - fixedPoint.getX());
		} else {
			startDist = Math.abs(startMouse.getY() - fixedPoint.getY());
			currentDist = Math.abs(currentMouse.getY() - fixedPoint.getY());
		}
		if (startDist < 1.0) return isX ? startScaleX : startScaleY;
		boolean shouldScale = false;
		if (isX) {
			shouldScale = (anchor == EAnchor.EE || anchor == EAnchor.WW ||
					anchor == EAnchor.NE || anchor == EAnchor.NW ||
					anchor == EAnchor.SE || anchor == EAnchor.SW);
		} else {
			shouldScale = (anchor == EAnchor.NN || anchor == EAnchor.SS ||
					anchor == EAnchor.NE || anchor == EAnchor.NW ||
					anchor == EAnchor.SE || anchor == EAnchor.SW);
		}
		if (!shouldScale) {
			return isX ? startScaleX : startScaleY;
		}
		double scaleRatio = currentDist / startDist;
		return (isX ? startScaleX : startScaleY) * scaleRatio;
	}

	@Override
	public void finish(Graphics2D graphics, int x, int y) {
		startMouse = null;
		fixedPoint = null;
	}
	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {}
}