package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class GPen extends GShape {
    private static final long serialVersionUID = 1L;
    private GeneralPath path;
    private ArrayList<Point> points;
    private boolean isDrawing = false;

    public GPen() {
        super(new GeneralPath());
        this.path = (GeneralPath) super.originalShape;
        this.points = new ArrayList<>();

        this.isFillEnabled = false;
        this.isStrokeEnabled = true;
        this.currentStrokeWidth = 2;
        this.updateTransformedShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.points.clear();
        this.points.add(new Point(x, y));

        this.path = new GeneralPath();
        this.path.moveTo(x, y);

        this.shape = this.path;
        this.originalShape = this.path;
        this.isDrawing = true;

        this.updateTransformedShape();
    }

    @Override
    public void dragPoint(int x, int y) {
        if (!isDrawing) {
            return;
        }
        this.points.add(new Point(x, y));
        this.path.lineTo(x, y);
        this.shape = this.path;
        this.updateTransformedShape();
    }

    @Override
    public void addPoint(int x, int y) {}

    @Override
    public void draw(Graphics2D g2d) {
        if (points.isEmpty()) {
            return;
        }
        Color originalColor = g2d.getColor();
        Stroke originalStroke = g2d.getStroke();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (isStrokeEnabled && currentStrokeColor != null) {
            g2d.setColor(currentStrokeColor);
            g2d.setStroke(new BasicStroke(currentStrokeWidth,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.draw(transformedShape);
        }
        if (this.isSelected) {
            g2d.setColor(originalColor);
            g2d.setStroke(originalStroke);
            drawAnchors(g2d);
        }
        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);
    }

    @Override
    public void drawSelectMode(Graphics2D g2d) {
        if (points.isEmpty()) return;

        Color originalColor = g2d.getColor();
        Stroke originalStroke = g2d.getStroke();

        g2d.setColor(new Color(0, 100, 255, 150));
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{3, 3}, 0));

        g2d.draw(this.transformedShape);

        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);
    }
}