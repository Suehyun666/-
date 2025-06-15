package shapes;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class GEraser extends GShape {
    private static final long serialVersionUID = 1L;
    private Area eraserArea;
    private ArrayList<Point> points;
    private int eraserSize = 20; // 지우개 크기

    public GEraser() {
        super(new Area());
        this.eraserArea = (Area) super.shape;
        this.points = new ArrayList<>();

        this.isFillEnabled = false;
        this.isStrokeEnabled = false;
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.points.clear();
        this.points.add(new Point(x, y));
        this.eraserArea.reset();
        addEraserStroke(x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        this.points.add(new Point(x, y));
        addEraserStroke(x, y);
    }
    private void addEraserStroke(int x, int y) {
        Ellipse2D eraserStroke = new Ellipse2D.Double(
                x - eraserSize/2, y - eraserSize/2,
                eraserSize, eraserSize
        );
        this.eraserArea.add(new Area(eraserStroke));

        if (points.size() > 1) {
            Point prevPoint = points.get(points.size() - 2);
            connectPoints(prevPoint.x, prevPoint.y, x, y);
        }
    }
    private void connectPoints(int x1, int y1, int x2, int y2) {
        double distance = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        if (distance <= eraserSize/2) return;
        int steps = (int)(distance / (eraserSize/3)); // 겹치도록
        for (int i = 1; i < steps; i++) {
            double t = (double)i / steps;
            int mx = (int)(x1 + t * (x2 - x1));
            int my = (int)(y1 + t * (y2 - y1));

            Ellipse2D midStroke = new Ellipse2D.Double(
                    mx - eraserSize/2, my - eraserSize/2,
                    eraserSize, eraserSize
            );
            this.eraserArea.add(new Area(midStroke));
        }
    }

    @Override
    public void addPoint(int x, int y) {}

    @Override
    public void draw(Graphics2D g2d) {
        if (points.isEmpty()) {
            return;
        }
        Color originalColor = g2d.getColor();
        g2d.setColor(g2d.getBackground());
        g2d.fill(transformedShape);
        if (this.isSelected) {
            g2d.setColor(originalColor);
            drawAnchors(g2d);
        }
        g2d.setColor(originalColor);
    }
    @Override
    public GEraser clone() {return null;}
    @Override
    public void setSelected(boolean input) {this.isSelected = false;}
    @Override
    public boolean contains(int x, int y) {return false;}
    @Override
    protected void drawAnchors(Graphics2D g2d) {}
    @Override
    public void drawSelectMode(Graphics2D g2d) {}
    @Override
    public void updateTransformedShape() {this.transformedShape = this.originalShape;}
}