package shapes;

import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;

import global.GConstants.EAnchor;

public abstract class GShape implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;
	private final static int ANCHOR_W=10;
	private final static int ANCHOR_H=10;

	private Ellipse2D[] anchors;

	protected Shape shape;
	protected Shape originalShape;
	protected int startX, startY;//위치
	protected int px, py; // 이전 위치
	protected AffineTransform transform;
	protected EAnchor eSelectedAnchor;
	protected Shape transformedShape;
	protected boolean isSelected;
	protected boolean isFillEnabled;
	protected boolean isStrokeEnabled;
	protected boolean isSelectMode;
	protected boolean visible;
	protected Color currentFillColor, currentStrokeColor;
	protected int currentStrokeWidth;

	public Shape getOriginalShape() {
		 return originalShape;
	}

	public enum EPoints{
		e2P,
		enP
	}
	public GShape(Shape shape) {
		this.shape=shape;
		this.transform =new AffineTransform();
		this.anchors=new Ellipse2D.Double[EAnchor.values().length-1];
		for (int i=0; i<this.anchors.length; i++) {
			this.anchors[i]=new Ellipse2D.Double();
		}
		this.isSelected=false;
		this.isSelectMode=false;
		this.visible=true;
		this.eSelectedAnchor=null;
		this.transformedShape=null;

		// default color
		this.currentFillColor = Color.BLACK;
		this.currentStrokeColor = Color.BLACK;
		this.currentStrokeWidth = 1;
		this.isFillEnabled = true;
		this.isStrokeEnabled = true;
	}
	//method

	//getters & setters
	public Shape getShape() {return this.shape;}
	public EAnchor getSelectedAnchor() {
		return this.eSelectedAnchor;
	}
	public Rectangle getSize(){return this.getBounds();}
	public AffineTransform getAffineTransform() {
		return this.transform;
	}
	public Shape getTransformedShape() {
		return this.transform.createTransformedShape(this.shape);
	}
	public boolean isVisible() {return visible;}
	public Color getFillColor() {return currentFillColor;}
	public Color getStrokeColor() { return currentStrokeColor;}
	public int getStrokeWidth(){return currentStrokeWidth;}
	public boolean isFillEnabled(){return isFillEnabled;}
	public boolean isStrokeEnabled(){return isStrokeEnabled;}

	public void setVisible(boolean visible) {this.visible = visible;}
	public void setSelected(boolean input) {
		this.isSelected=input;
	}
	public void setSelectedAnchor(EAnchor input) {
		this.eSelectedAnchor=input;
	}
	public void setSelectMode(boolean bool){this.isSelectMode = bool;}
	public void setFillColor(Color fadedColor) {this.currentFillColor = fadedColor;}
	public void setCurrentStrokeColor(Color savedColor) {this.currentStrokeColor=savedColor;}
	public void setColorProperties(Color fillColor, Color strokeColor, int strokeWidth, boolean fillEnabled, boolean strokeEnabled) {
		this.currentFillColor = fillColor;
		this.currentStrokeColor = strokeColor;
		this.currentStrokeWidth = strokeWidth;
		this.isFillEnabled = fillEnabled;
		this.isStrokeEnabled = strokeEnabled;
	}

	public void draw(Graphics2D g2d) {
		//select
		if (isSelectMode) {
			drawSelectMode(g2d);
			return;
		}
		this.transformedShape = this.transform.createTransformedShape(this.shape);
		Color originalColor = g2d.getColor();
		Stroke originalStroke = g2d.getStroke();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// fill color
		if (isFillEnabled && currentFillColor != null) {
			g2d.setColor(currentFillColor);
			g2d.fill(transformedShape);
		}
		// stroke
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
		// default setting
		g2d.setColor(originalColor);
		g2d.setStroke(originalStroke);
	}
	public void drawSelectMode(Graphics2D g2d) {}
	protected void drawAnchors(Graphics2D g2d) {
		for (EAnchor anchor : EAnchor.values()) {
			if (anchor == EAnchor.MM) continue;
			Ellipse2D shape = getAnchorShape(anchor);
			Color prevColor = g2d.getColor();
			g2d.setColor(g2d.getBackground());
			g2d.fill(shape);
			g2d.setColor(prevColor);
			g2d.draw(shape);
		}
	}
	public boolean contains(int x, int y) {
		this.transformedShape = this.transform.createTransformedShape(this.shape);
		if (isSelected) {
			for (int i = 0; i < EAnchor.values().length - 1; i++) {
				Ellipse2D anchor = getAnchorShape(EAnchor.values()[i]);
				if (anchor.contains(x, y)) {
					this.eSelectedAnchor = EAnchor.values()[i];
					return true;
				}
			}
		}
		if (this.transformedShape.contains(x, y)) {
			this.eSelectedAnchor = EAnchor.MM;
			return true;
		}
		return false;
	}
	public boolean contains(GShape shape) {return getTransformedShape().contains(shape.getTransformedShape().getBounds2D());}

	private Ellipse2D getAnchorShape(EAnchor anchor) {
		Rectangle2D bounds = this.shape.getBounds2D();
		double cx = 0, cy = 0;
		switch (anchor) {
			case SS -> { cx = bounds.getCenterX(); cy = bounds.getMaxY(); }
			case SE -> { cx = bounds.getMaxX();    cy = bounds.getMaxY(); }
			case SW -> { cx = bounds.getMinX();    cy = bounds.getMaxY(); }
			case NN -> { cx = bounds.getCenterX(); cy = bounds.getMinY(); }
			case NE -> { cx = bounds.getMaxX();    cy = bounds.getMinY(); }
			case NW -> { cx = bounds.getMinX();    cy = bounds.getMinY(); }
			case EE -> { cx = bounds.getMaxX();    cy = bounds.getCenterY(); }
			case WW -> { cx = bounds.getMinX();    cy = bounds.getCenterY(); }
			case RR -> { cx = bounds.getCenterX(); cy = bounds.getMinY() - 30; }
		}
		Point2D transformed = transform.transform(new Point2D.Double(cx, cy), null);
		return new Ellipse2D.Double(transformed.getX() - ANCHOR_W / 2, transformed.getY() - ANCHOR_H / 2, ANCHOR_W, ANCHOR_H);
	}
	public void setMovePoint(int x, int y) {
		this.px = x;
		this.py = y;
	}
	public void movePoint(int x, int y) {
		int dx = x - px;
		int dy = y - py;
		transform.translate(dx, dy);
		px = x;
		py = y;
	}
	public Rectangle getBounds() {
		return getTransformedShape().getBounds();
	}
	public abstract void setPoint(int x, int y);
	public abstract void addPoint(int x, int y);
	public abstract void dragPoint(int x, int y);
	public GShape clone(){
		try {
			GShape cloned = (GShape) super.clone();
			cloned.transform = new AffineTransform(this.transform);
			cloned.anchors = new Ellipse2D.Double[this.anchors.length];
			for (int i = 0; i < this.anchors.length; i++) {
				cloned.anchors[i] = new Ellipse2D.Double();
			}
			cloned.isSelected = false;
			cloned.eSelectedAnchor = null;
			return cloned;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Clone not supported", e);
		}
	}

	//affign
	public void applyTransform(AffineTransform at) {
		this.transform = new AffineTransform();        // 초기화
		this.transform.concatenate(at);                // 새로 적용
		updateTransformedShape();
	}
	public void appendTransform(AffineTransform at) {
		this.transform.concatenate(at);                // 누적
		updateTransformedShape();
	}
	public void resetTransform() {
		this.transform = new AffineTransform();
		updateTransformedShape();
	}
	public void updateTransformedShape() {
		this.transformedShape = transform.createTransformedShape(this.shape);
	}
	public Point2D getInverseTransformedPoint(double x, double y) {
		try {
			return transform.createInverse().transform(new Point2D.Double(x, y), null);
		} catch (NoninvertibleTransformException e) {
			System.err.println("Inverse transform failed: " + e.getMessage());
			return new Point2D.Double(x, y); // fallback 처리
		}
	}
}