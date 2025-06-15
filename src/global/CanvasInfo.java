package global;

import frames.GMainPanel;
import shapes.GShape;
import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

public class CanvasInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Dimension canvasSize;
    private Color backgroundColor;
    private String fileName;
    private boolean limitCanvasSize;
    private Vector<GShape> shapes;
    private double zoomLevel = 1.0; // 기본 줌 레벨 1.0 (100%)

    // constructors
    public CanvasInfo(FileData fileData) {
        this.canvasSize = new Dimension(fileData.getWidth(), fileData.getHeight());
        this.backgroundColor = getBackgroundColor(fileData.getBackground());
        this.fileName = fileData.getFileName();
        this.limitCanvasSize = false;
        this.shapes = new Vector<>();
        this.zoomLevel = 1.0;
    }

    private CanvasInfo(Dimension canvasSize, Color backgroundColor, String fileName,
                       boolean limitCanvasSize, Vector<GShape> shapes, double zoomLevel) {
        this.canvasSize = canvasSize;
        this.backgroundColor = backgroundColor;
        this.fileName = fileName;
        this.limitCanvasSize = limitCanvasSize;
        this.shapes = new Vector<>(shapes);
        this.zoomLevel = zoomLevel;
    }

    public CanvasInfo() {
        this(new Dimension(800, 600), Color.WHITE, "Untitled", false, new Vector<>(), 1.0);
    }

    public static class Builder {
        private Dimension canvasSize = new Dimension(800, 600);
        private Color backgroundColor = Color.WHITE;
        private String fileName = "Untitled";
        private boolean limitCanvasSize = false;
        private Vector<GShape> shapes = new Vector<>();
        private double zoomLevel = 1.0;

        public Builder canvasSize(Dimension size) {
            this.canvasSize = size;
            return this;
        }

        public Builder canvasSize(int width, int height) {
            this.canvasSize = new Dimension(width, height);
            return this;
        }

        public Builder backgroundColor(Color color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder fileName(String name) {
            this.fileName = name;
            return this;
        }

        public Builder limitCanvasSize(boolean limit) {
            this.limitCanvasSize = limit;
            return this;
        }

        public Builder shapes(Vector<GShape> shapes) {
            this.shapes = shapes != null ? new Vector<>(shapes) : new Vector<>();
            return this;
        }

        public Builder zoomLevel(double zoom) {
            this.zoomLevel = Math.max(0.1, Math.min(10.0, zoom)); // 10%~1000% 제한
            return this;
        }

        public CanvasInfo build() {
            return new CanvasInfo(canvasSize, backgroundColor, fileName,
                    limitCanvasSize, shapes, zoomLevel);
        }
    }

    //  panel method
    public void applyToPanel(GMainPanel panel) {
        if (panel == null) return;

        if (shapes != null) {
            panel.setShapes(shapes);
        }

        if (fileName != null) {
            panel.setFileName(fileName);
        }

        if (canvasSize != null) {
            panel.setCanvasSize(canvasSize.width, canvasSize.height);
        }

        if (backgroundColor != null) {
            panel.setBackground(backgroundColor);
        }

        panel.setZoomLevel(zoomLevel);

        panel.setCanvasSizeLimited(limitCanvasSize);

        panel.revalidate();
        panel.repaint();
    }

    public static CanvasInfo fromPanel(GMainPanel panel) {
        if (panel == null) return new CanvasInfo();

        return new Builder()
                .canvasSize(panel.getCanvasSize())
                .backgroundColor(panel.getBackground())
                .fileName(panel.getFileName())
                .limitCanvasSize(panel.isCanvasSizeLimited())
                .shapes(panel.getshapes())
                .zoomLevel(panel.getZoomLevel())
                .build();
    }

    // zoom
    public void setZoom(double zoom) {
        this.zoomLevel = Math.max(0.1, Math.min(10.0, zoom));
    }
    public void zoomIn() {
        setZoom(zoomLevel * 1.2);
    }
    public void zoomOut() {
        setZoom(zoomLevel / 1.2);
    }
    public void resetZoom() {
        setZoom(1.0);
    }

    public void fitToWindow(Dimension windowSize) {
        if (canvasSize != null && windowSize != null) {
            double scaleX = (double) windowSize.width / canvasSize.width;
            double scaleY = (double) windowSize.height / canvasSize.height;
            setZoom(Math.min(scaleX, scaleY));
        }
    }
    //Getters and Setters
    public Dimension getCanvasSize() {return canvasSize;}
    public void setCanvasSize(Dimension canvasSize) {this.canvasSize = canvasSize;}
    public void setCanvasSize(int width, int height) {this.canvasSize = new Dimension(width, height);}

    public Color getBackgroundColor() {return backgroundColor;}
    public void setBackgroundColor(Color backgroundColor) {this.backgroundColor = backgroundColor;}

    public String getFileName() {return fileName;}
    public void setFileName(String fileName) {this.fileName = fileName;}

    public void setLimitCanvasSize(boolean limitCanvasSize) {this.limitCanvasSize = limitCanvasSize;}

    public Vector<GShape> getShapes() {return shapes;}
    public void setShapes(Vector<GShape> shapes) {this.shapes = shapes != null ? shapes : new Vector<>();}

    public double getZoomLevel() {return zoomLevel;}
    public void setZoomLevel(double zoomLevel) {this.zoomLevel = Math.max(0.1, Math.min(10.0, zoomLevel));}

    public boolean isCanvasSizeLimited() {return limitCanvasSize;}
    public Color getBackgroundColor(String background) {
        if (background == null) return Color.WHITE;

        switch (background.toLowerCase()) {
            case "white": return Color.WHITE;
            case "black": return Color.BLACK;
            case "background color":
            case "gray": return Color.LIGHT_GRAY;
            case "transparent": return new Color(0, 0, 0, 0);
            default: return Color.WHITE;
        }
    }
    public CanvasInfo copy() {
        return new Builder()
                .canvasSize(new Dimension(canvasSize))
                .backgroundColor(backgroundColor)
                .fileName(fileName)
                .limitCanvasSize(limitCanvasSize)
                .shapes(new Vector<>(shapes))
                .zoomLevel(zoomLevel)
                .build();
    }
    public boolean isEmpty() {
        return shapes == null || shapes.isEmpty();
    }
}