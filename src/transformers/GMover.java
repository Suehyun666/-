package transformers;

import java.awt.Graphics2D;
import java.util.Vector;

import shapes.GShape;

public class GMover extends GTransFormer {
	private Vector<GShape> selectedShapes;
	private Vector<Double> initialTranslateX;
	private Vector<Double> initialTranslateY;
	private int startX, startY;

	public GMover(Vector<GShape> selectedShapes) {
		super(selectedShapes.getFirst());
		this.selectedShapes = selectedShapes;
		this.initialTranslateX = new Vector<>();
		this.initialTranslateY = new Vector<>();
	}

	@Override
	public void start(Graphics2D graphics, int x, int y) {
		this.startX = x;
		this.startY = y;

		// 각 도형의 초기 이동값 저장
		initialTranslateX.clear();
		initialTranslateY.clear();
		for (GShape shape : selectedShapes) {
			initialTranslateX.add(shape.translateX);
			initialTranslateY.add(shape.translateY);
		}
	}

	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		int dx = x - startX;
		int dy = y - startY;

		for (int i = 0; i < selectedShapes.size(); i++) {
			GShape shape = selectedShapes.get(i);

			// 파라미터 직접 업데이트 (누적 없이)
			shape.translateX = initialTranslateX.get(i) + dx;
			shape.translateY = initialTranslateY.get(i) + dy;

			shape.updateTransformedShape();
		}
	}

	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {}

	@Override
	public void finish(Graphics2D graphics, int x, int y) {
		initialTranslateX.clear();
		initialTranslateY.clear();
	}
}