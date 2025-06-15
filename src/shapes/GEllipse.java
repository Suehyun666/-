package shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GEllipse extends GShape {
    private static final long serialVersionUID = 1L;
    private Ellipse2D.Float ellipse;

    public GEllipse() {
        super(new Ellipse2D.Float(0, 0, 0, 0));
        this.ellipse = (Ellipse2D.Float) super.originalShape;
        this.updateTransformedShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        if (this.ellipse == null) {
            this.ellipse = new Ellipse2D.Float(x, y, 0, 0);
        } else {
            this.ellipse.setFrame(x, y, 0, 0);
        }
        this.originalShape = this.ellipse;
        this.updateTransformedShape();
    }

    @Override
    public void dragPoint(int x, int y) {
        int drawX = Math.min(startX, x);
        int drawY = Math.min(startY, y);
        int width = Math.abs(x - startX);
        int height = Math.abs(y - startY);
        this.ellipse.setFrame(drawX, drawY, width, height);
        this.shape = this.ellipse;
        this.updateTransformedShape();
    }

    public void updateSize(double x, double y, double w, double h) {
        this.ellipse.setFrame(x, y, w, h);
        this.shape = this.ellipse;
        this.updateTransformedShape();
    }

    @Override
    public void addPoint(int x, int y) {}

    @Override
    public GEllipse clone() {
        GEllipse cloned = (GEllipse) super.clone();
        cloned.ellipse = new Ellipse2D.Float(
                (float) ellipse.getX(),
                (float) ellipse.getY(),
                (float) ellipse.getWidth(),
                (float) ellipse.getHeight());
        cloned.originalShape = cloned.ellipse;
        cloned.updateTransformedShape();
        return cloned;
    }

    @Override
    public void drawSelectMode(Graphics2D g2d) {
        g2d.setColor(new Color(0, 100, 255, 150));
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{5, 5}, 0));
        g2d.draw(this.transformedShape);
    }
}