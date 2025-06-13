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
}