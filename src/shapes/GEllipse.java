package shapes;

import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class GEllipse extends GShape {
    private static final long serialVersionUID = 1L;
    private Ellipse2D ellipse;
    public GEllipse() {
        super(new Ellipse2D.Float(0, 0, 0, 0));
        this.ellipse = (Ellipse2D) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.shape = new Ellipse2D.Float(x, y, 0, 0);
        this.transform = new AffineTransform();
    }

    @Override
    public void dragPoint(int x, int y) {
        int drawX = Math.min(startX, x);
        int drawY = Math.min(startY, y);
        int width = Math.abs(x - startX);
        int height = Math.abs(y - startY);
        if (this.shape instanceof Ellipse2D.Float ellipse) {
            ellipse.setFrame(drawX, drawY, width, height);
        } else {
            // fallback: 새로운 객체 생성
            this.shape = new Ellipse2D.Float(drawX, drawY, width, height);
        }
    }

    @Override
    public void addPoint(int x, int y) {}
    @Override
    public void resize(double sx, double sy, int anchorX, int anchorY) {

    }
    @Override
    public void moveBy(int dx, int dy) {

    }
}