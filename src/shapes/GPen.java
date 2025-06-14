// GPen.java - 펜 도구 구현
package shapes;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class GPen extends GShape {
    private static final long serialVersionUID = 1L;
    private GeneralPath path;
    private ArrayList<Point> points;

    public GPen() {
        super(new GeneralPath());
        this.path = (GeneralPath) super.shape;
        this.points = new ArrayList<>();

        // 펜은 기본적으로 fill 없이, stroke만 사용
        this.isFillEnabled = false;
        this.isStrokeEnabled = true;
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.points.clear();
        this.points.add(new Point(x, y));

        this.path.reset();
        this.path.moveTo(x, y);
    }

    @Override
    public void dragPoint(int x, int y) {
        this.points.add(new Point(x, y));
        this.path.lineTo(x, y);
    }

    @Override
    public void addPoint(int x, int y) {
        this.points.add(new Point(x, y));
        this.path.lineTo(x, y);
    }

    @Override
    public GPen clone() {
        GPen cloned = (GPen) super.clone();
        cloned.path = new GeneralPath(this.path);
        cloned.points = new ArrayList<>(this.points);
        cloned.shape = cloned.path;
        return cloned;
    }

    // 펜용 선택 모드 - 점선으로 경로 표시
    @Override
    public void drawSelectMode(Graphics2D g2d) {
        if (points.isEmpty()) return;

        Color originalColor = g2d.getColor();
        Stroke originalStroke = g2d.getStroke();

        // 점선으로 경로 표시
        g2d.setColor(new Color(0, 100, 255, 150));
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{3, 3}, 0));

        // 변환된 경로 그리기
        Shape transformedPath = this.transform.createTransformedShape(this.path);
        g2d.draw(transformedPath);

        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);
    }
    public void moveAllPoints(int dx, int dy) {

    }
}