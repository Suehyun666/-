package shapes;

import global.GConstants;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class GLine extends GShape {
    private Line2D.Float line;

    public GLine() {
        super(new Line2D.Float(0, 0, 0, 0));
        this.line = (Line2D.Float) this.getShape();
        this.shape=line;
    }

    @Override
    public void setPoint(int x, int y) {
        this.line.setLine(x, y, x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        Line2D.Float line = (Line2D.Float) this.shape;
        line.setLine(line.x1, line.y1, x, y);
    }
    @Override
    public void addPoint(int x, int y) {}

    @Override
    public boolean contains(int x, int y) {
        if (this.shape instanceof Line2D.Float line) {
            try {
                Point2D mouse = new Point2D.Float(x, y);
                Point2D inv = transform.inverseTransform(mouse, null);
                final double tolerance = 5.0;
                if (line.ptSegDist(inv) <= tolerance) {
                    this.eSelectedAnchor = GConstants.EAnchor.MM;
                    return true;
                }
            } catch (NoninvertibleTransformException e) {
                e.printStackTrace();
            }
            return false;
        }
        return super.contains(x, y);
    }

    @Override
    public Rectangle getBounds() {
        return getTransformedShape().getBounds();
    }
    @Override
    public GLine clone() {
        GLine cloned = (GLine) super.clone();
        cloned.line = new Line2D.Float(
                this.line.x1, this.line.y1,
                this.line.x2, this.line.y2
        );
        cloned.shape = cloned.line;
        return cloned;
    }
    @Override
    public void drawSelectMode(Graphics2D g2d) {
        Color originalColor = g2d.getColor();
        Stroke originalStroke = g2d.getStroke();

        g2d.setColor(new Color(0, 100, 255, 150));
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{3, 3}, 0));

        Shape transformedShape = this.transform.createTransformedShape(this.shape);
        g2d.draw(transformedShape);

        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);
    }
}