package transformers;

import java.awt.*;
import java.awt.geom.*;

import global.GConstants.EAnchor;
import shapes.GBrush;
import shapes.GPen;
import shapes.GShape;

public class GResizer extends GTransFormer {
	private GShape shape;
	private Rectangle originalBounds;
	private int fixedX, fixedY;
	private EAnchor anchor;

	public GResizer(GShape gshape) {
		super(gshape);
		this.shape = gshape;
	}

	@Override
	public void start(Graphics2D graphics, int x, int y) {
		this.startX = x;
		this.startY = y;
		Rectangle currentBounds = shape.getTransformedShape().getBounds();
		if (shape.getShape() instanceof Rectangle2D) {
			Rectangle2D rect = (Rectangle2D) shape.getShape();
			rect.setFrame(currentBounds);
		}else if (shape.getShape() instanceof GeneralPath) {
			// GPen 처리
		} else if (shape.getShape() instanceof Area) {
			// GBrush 처리
		} else if (shape.getShape() instanceof Path2D) {
			// GTriangle 처리
		}

		shape.getAffineTransform().setToIdentity();
		this.originalBounds = currentBounds;
		this.anchor = shape.getSelectedAnchor();

		switch (anchor) {
			case SE: // 우하단 - 좌상단 고정
				fixedX = originalBounds.x;
				fixedY = originalBounds.y;
				break;
			case NE: // 우상단 - 좌하단 고정
				fixedX = originalBounds.x;
				fixedY = originalBounds.y + originalBounds.height;
				break;
			case SW: // 좌하단 - 우상단 고정
				fixedX = originalBounds.x + originalBounds.width;
				fixedY = originalBounds.y;
				break;
			case NW: // 좌상단 - 우하단 고정
				fixedX = originalBounds.x + originalBounds.width;
				fixedY = originalBounds.y + originalBounds.height;
				break;
		}
	}

	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		int newX = Math.min(fixedX, x);
		int newY = Math.min(fixedY, y);
		int newWidth = Math.abs(x - fixedX);
		int newHeight = Math.abs(y - fixedY);

		if (newWidth < 5) newWidth = 5;
		if (newHeight < 5) newHeight = 5;

		if (shape.getShape() instanceof Rectangle2D) {
			Rectangle2D rect = (Rectangle2D) shape.getShape();
			rect.setFrame(newX, newY, newWidth, newHeight);
		}else if (shape.getShape() instanceof Point2D){
			Point2D point = (Point2D) shape.getShape();
			point.setLocation(newX, newY);
		}else if (shape instanceof GPen) {
			((GPen) shape).moveAllPoints(x, y);
		} else if (shape instanceof GBrush) {
			((GBrush) shape).moveAllPoints(x, y);
		}
	}

	@Override
	public void finish(Graphics2D graphics, int x, int y) {}

	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {}
}
