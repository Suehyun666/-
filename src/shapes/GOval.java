package shapes;


import java.awt.geom.Ellipse2D;

public class GOval extends GShape {
    private Ellipse2D.Float ellipse;

    public GOval() {
        super(new Ellipse2D.Float(0, 0, 0, 0));
        this.ellipse = (Ellipse2D.Float) this.getShape();
    }

    @Override
    public void setPoint(int x, int y) {
        this.ellipse.setFrame(x, y, 0, 0);
    }

    @Override
    public void dragPoint(int x, int y) {
        double ox = ellipse.getX();
        double oy = ellipse.getY();
        double ow = x - ox;
        double oh = y - oy;
        this.ellipse.setFrame(ox, oy, ow, oh);
    }

    @Override
    public void addPoint(int x, int y) {}
}
