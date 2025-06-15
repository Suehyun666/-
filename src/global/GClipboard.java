package global;

import shapes.GShape;
import java.util.Vector;

public class GClipboard {
    private static GClipboard instance;
    private Vector<GShape> copiedShapes;
    private boolean isCutOperation;

    private double cumulativeOffsetX = 0.0;
    private double cumulativeOffsetY = 0.0;
    private static final double PASTE_OFFSET = 20.0;

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

        resetPasteOffset();
    }

    public void copy(GShape shape) {
        this.copiedShapes.clear();
        if (shape != null) {
            this.copiedShapes.add(shape.clone());
        }
        this.isCutOperation = false;

        resetPasteOffset();
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

        cumulativeOffsetX += PASTE_OFFSET;
        cumulativeOffsetY += PASTE_OFFSET;

        for (GShape shape : copiedShapes) {
            GShape clonedShape = shape.clone();

            clonedShape.translateX += cumulativeOffsetX;
            clonedShape.translateY += cumulativeOffsetY;

            clonedShape.updateTransformedShape();

            pastedShapes.add(clonedShape);
        }
        return pastedShapes;
    }

    private void resetPasteOffset() {
        cumulativeOffsetX = 0.0;
        cumulativeOffsetY = 0.0;
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
        resetPasteOffset();
    }

    public int getShapeCount() {
        return copiedShapes.size();
    }

    public int getPasteCount() {
        return (int)(cumulativeOffsetX / PASTE_OFFSET);
    }
}