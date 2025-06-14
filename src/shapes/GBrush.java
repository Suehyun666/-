
package shapes;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class GBrush extends GShape {
    private static final long serialVersionUID = 1L;
    private Area brushArea;
    private ArrayList<Point> points;
    private int brushSize = 8; // 브러시 크기

    public GBrush() {
        super(new Area());
        this.brushArea = (Area) super.shape;
        this.points = new ArrayList<>();

        // 브러시는 기본적으로 fill만 사용, stroke 없음
        this.isFillEnabled = true;
        this.isStrokeEnabled = false;
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.points.clear();
        this.points.add(new Point(x, y));

        this.brushArea.reset();
        // 첫 점에 원형 브러시 추가
        addBrushStroke(x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        this.points.add(new Point(x, y));
        addBrushStroke(x, y);
    }

    @Override
    public void addPoint(int x, int y) {
        this.points.add(new Point(x, y));
        addBrushStroke(x, y);
    }

    private void addBrushStroke(int x, int y) {
        Ellipse2D brushStroke = new Ellipse2D.Double(
                x - brushSize/2, y - brushSize/2,
                brushSize, brushSize
        );
        this.brushArea.add(new Area(brushStroke));
    }

    private void reconstructBrushArea() {
        this.brushArea.reset();
        for (Point point : points) {
            addBrushStroke(point.x, point.y);
        }
    }

    @Override
    public GBrush clone() {
        GBrush cloned = (GBrush) super.clone();
        cloned.brushArea = new Area(this.brushArea);
        cloned.points = new ArrayList<>(this.points);
        cloned.brushSize = this.brushSize;
        cloned.shape = cloned.brushArea;
        return cloned;
    }

    // Brush select mode
    @Override
    public void drawSelectMode(Graphics2D g2d) {
        if (brushArea.isEmpty()) return;

        Color originalColor = g2d.getColor();
        Stroke originalStroke = g2d.getStroke();

        // 점선으로 브러시 영역 외곽선 표시
        g2d.setColor(new Color(0, 100, 255, 150));
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{3, 3}, 0));

        //
        Shape transformedArea = this.transform.createTransformedShape(this.brushArea);
        g2d.draw(transformedArea);

        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);
    }

    // brush size
    public void setBrushSize(int size) {
        this.brushSize = Math.max(1, size);
    }

    public int getBrushSize() {
        return this.brushSize;
    }

    public void moveAllPoints(int dx, int dy) {

    }
}