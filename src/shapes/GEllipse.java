package shapes;

import java.awt.*;
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
    public GEllipse clone() {
        GEllipse cloned = (GEllipse) super.clone();
        if (this.shape instanceof Ellipse2D.Float ellipse) {
            cloned.shape = new Ellipse2D.Float(
                    (float) ellipse.getX(),
                    (float) ellipse.getY(),
                    (float) ellipse.getWidth(),
                    (float) ellipse.getHeight()
            );
        }
        cloned.ellipse = (Ellipse2D) cloned.shape;
        return cloned;
    }
    @Override
    public void drawSelectMode(Graphics2D g2d) {
        Color originalColor = g2d.getColor();
        Stroke originalStroke = g2d.getStroke();

        g2d.setColor(new Color(0, 100, 255, 150));
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{5, 5}, 0));

        Shape transformedShape = this.transform.createTransformedShape(this.shape);
        g2d.draw(transformedShape);

        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);
    }
}