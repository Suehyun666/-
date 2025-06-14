package transformers;

import java.awt.*;

import shapes.GShape;

public class GRotater extends GTransFormer {
	private double startAngle;
	private int centerX, centerY;
	private double totalRotation = 0; // 총 회전량 추적

	public GRotater(GShape gshape) {
		super(gshape);
		this.shape = gshape;
	}

	@Override
	public void start(Graphics2D graphics, int x, int y) {
		Rectangle bounds = shape.getBounds();
		this.centerX = bounds.x + bounds.width / 2;
		this.centerY = bounds.y + bounds.height / 2;
		this.startAngle = Math.atan2(y - centerY, x - centerX);
		this.totalRotation = 0;
	}

	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		double currentAngle = Math.atan2(y - centerY, x - centerX);
		double deltaAngle = currentAngle - startAngle;
		double netRotation = deltaAngle - totalRotation;
		//shape.rotate(netRotation, centerX, centerY);
		this.totalRotation = deltaAngle;
	}

	@Override
	public void finish(Graphics2D graphics, int x, int y) {}

	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {}
}