package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import global.GConstants.EAnchor;

public abstract class GShape implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;
	private final static int ANCHOR_W=10;
	private final static int ANCHOR_H=10;

	protected Shape shape;
	protected int startX, startY;//위치
	protected int px, py; // 이전 위치
	protected AffineTransform transform;
	private Ellipse2D[] anchors;
	protected EAnchor eSelectedAnchor;
	protected Shape transformedShape;
	protected boolean isSelected;
	private boolean visible = true;
	private Color color = null;

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
		this.eSelectedAnchor=null;
	}
	//getters & setters
	public Shape getShape() {return this.shape;}
	public Rectangle getSize(){return this.getBounds();}
	public AffineTransform getAffineTransform() {
		return this.transform;
	}
	public Shape getTransformedShape() {
		return this.transform.createTransformedShape(this.shape);
	}
	public boolean isVisible() {return visible;}
	public Color getFillColor() {return color;}

	public void setVisible(boolean visible) {}
	public void setSelected(boolean input) {
		this.isSelected=input;
	}
	public EAnchor getSelectedAnchor() {
		return this.eSelectedAnchor;
	}
	public void setSelectedAnchor(EAnchor input) {
		this.eSelectedAnchor=input;
	}
	public void setFillColor(Color fadedColor) {this.color = fadedColor;}

	//method
	public void draw(Graphics2D g2d) {
		this.transformedShape = this.transform.createTransformedShape(this.shape);
		g2d.draw(this.transformedShape);
		if (this.isSelected) {
			drawAnchors(g2d);
		}
	}

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

	private Ellipse2D getAnchorShape(EAnchor anchor) {
		Rectangle bounds = this.transformedShape.getBounds();
		int cx = 0, cy = 0;
		switch (anchor) {
			case SS: cx = bounds.x + bounds.width / 2; cy = bounds.y + bounds.height; break;
			case SE: cx = bounds.x + bounds.width;     cy = bounds.y + bounds.height; break;
			case SW: cx = bounds.x;                    cy = bounds.y + bounds.height; break;
			case NN: cx = bounds.x + bounds.width / 2; cy = bounds.y; break;
			case NE: cx = bounds.x + bounds.width;     cy = bounds.y; break;
			case NW: cx = bounds.x;                    cy = bounds.y; break;
			case EE: cx = bounds.x + bounds.width;     cy = bounds.y + bounds.height / 2; break;
			case WW: cx = bounds.x;                    cy = bounds.y + bounds.height / 2; break;
			case RR: cx = bounds.x + bounds.width / 2; cy = bounds.y - 30; break;
			default: break;
		}
		return new Ellipse2D.Double(cx - ANCHOR_W/2, cy - ANCHOR_H/2, ANCHOR_W, ANCHOR_H);
	}

	public boolean contains(GShape shape) {return getTransformedShape().contains(shape.getTransformedShape().getBounds2D());}
	public Rectangle getBounds() {
		return getTransformedShape().getBounds();
	}
	public abstract void setPoint(int x, int y);
	public abstract void addPoint(int x, int y);
	public abstract void dragPoint(int x, int y);
	public abstract void resize(double sx, double sy, int anchorX, int anchorY);
	public abstract void moveBy(int dx, int dy);
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

}