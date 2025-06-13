package global;

import shapes.GShape;
import java.util.Vector;

public class GClipboard {
    private static GClipboard instance;
    private Vector<GShape> copiedShapes;
    private boolean isCutOperation;

    private GClipboard() {
        this.copiedShapes = new Vector<>();
        this.isCutOperation = false;
    }

    public static GClipboard getInstance() {
        if (instance == null) {
            instance = new GClipboard();
        }
        return instance;
    }

    public void copy(Vector<GShape> shapes) {
        this.copiedShapes.clear();
        for (GShape shape : shapes) {
            this.copiedShapes.add(shape.clone());
        }
        this.isCutOperation = false;
    }

    public void copy(GShape shape) {
        this.copiedShapes.clear();
        if (shape != null) {
            this.copiedShapes.add(shape.clone());
        }
        this.isCutOperation = false;
    }

    public void cut(Vector<GShape> shapes) {
        copy(shapes);
        this.isCutOperation = true;
    }

    public void cut(GShape shape) {
        copy(shape);
        this.isCutOperation = true;
    }

    public Vector<GShape> paste() {
        Vector<GShape> pastedShapes = new Vector<>();
        for (GShape shape : copiedShapes) {
            GShape clonedShape = shape.clone();
            clonedShape.moveBy(20, 20);
            pastedShapes.add(clonedShape);
        }
        return pastedShapes;
    }

    public boolean isEmpty() {
        return copiedShapes.isEmpty();
    }

    public boolean isCutOperation() {
        return isCutOperation;
    }

    public void clear() {
        copiedShapes.clear();
        isCutOperation = false;
    }

    public int getShapeCount() {
        return copiedShapes.size();
    }
}