package transformers;

import global.GConstants.EAnchor;
import shapes.GShape;

import java.awt.*;
import java.awt.geom.*;

public class GResizer extends GTransFormer {
	private Point2D fixedPoint;
	private EAnchor anchor;

	public GResizer(GShape shape) {
		super(shape);
	}

	@Override
	public void start(Graphics2D g, int x, int y) {
		this.anchor = shape.getSelectedAnchor();
		Rectangle2D bounds = shape.getTransformedShape().getBounds2D();
		// 회전 포함된 bounds에서 고정점 계산 → 반드시 inverse transform
		switch (anchor) {
			case NW -> fixedPoint = shape.getInverseTransformedPoint(bounds.getMaxX(), bounds.getMaxY());
			case NE -> fixedPoint = shape.getInverseTransformedPoint(bounds.getMinX(), bounds.getMaxY());
			case SW -> fixedPoint = shape.getInverseTransformedPoint(bounds.getMaxX(), bounds.getMinY());
			case SE -> fixedPoint = shape.getInverseTransformedPoint(bounds.getMinX(), bounds.getMinY());
			case EE -> fixedPoint = shape.getInverseTransformedPoint(bounds.getMinX(), bounds.getCenterY());
			case WW -> fixedPoint = shape.getInverseTransformedPoint(bounds.getMaxX(), bounds.getCenterY());
			case NN -> fixedPoint = shape.getInverseTransformedPoint(bounds.getCenterX(), bounds.getMaxY());
			case SS -> fixedPoint = shape.getInverseTransformedPoint(bounds.getCenterX(), bounds.getMinY());
		}
	}

	@Override
	public void drag(Graphics2D g, int x, int y) {
		if (fixedPoint == null) return;

		Point2D current = shape.getInverseTransformedPoint(x, y);
		Rectangle2D sourceBounds = shape.getOriginalShape().getBounds2D();

		double origW = sourceBounds.getWidth();
		double origH = sourceBounds.getHeight();
		if (origW == 0 || origH == 0) return;

		double scaleX = 1.0, scaleY = 1.0;

		switch (anchor) {
			case NW, NE, SW, SE -> {
				double dx = current.getX() - fixedPoint.getX();
				double dy = current.getY() - fixedPoint.getY();
				scaleX = dx / (origW * (anchor == EAnchor.NW || anchor == EAnchor.SW ? -1 : 1));
				scaleY = dy / (origH * (anchor == EAnchor.NW || anchor == EAnchor.NE ? -1 : 1));
			}
			case EE, WW -> {
				double dx = current.getX() - fixedPoint.getX();
				scaleX = dx / (origW * (anchor == EAnchor.WW ? -1 : 1));
			}
			case NN, SS -> {
				double dy = current.getY() - fixedPoint.getY();
				scaleY = dy / (origH * (anchor == EAnchor.NN ? -1 : 1));
			}
		}

		if (scaleX < 0.01) scaleX = 0.01;
		if (scaleY < 0.01) scaleY = 0.01;

		AffineTransform at = new AffineTransform();
		at.translate(fixedPoint.getX(), fixedPoint.getY());
		at.scale(scaleX, scaleY);
		at.translate(-fixedPoint.getX(), -fixedPoint.getY());

		shape.appendTransform(at);
	}

	@Override
	public void finish(Graphics2D graphics, int x, int y) {
		fixedPoint = null;
		//여기서 뭔가 해야할것같지만 다시 원래도형으로 돌아갈 것 같음.
	}

	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {}
}
