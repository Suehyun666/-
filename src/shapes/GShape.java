package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import global.GConstants.EAnchor;

public abstract class GShape implements Serializable{
	private static final long serialVersionUID = 1L;
	private final static int ANCHOR_W=10;
	private final static int ANCHOR_H=10;
	public enum EPoints{
    	e2P,
    	enP
    }
	private Shape shape;
	private AffineTransform affimetransform;
	
	private Ellipse2D[] anchors;
	private boolean isSelected;
	private EAnchor eSelectedAnchor;
	private int px,py;
	private Shape transformedShape;
	
	public GShape(Shape shape) {
		this.shape=shape;
		this.affimetransform=new AffineTransform();
		this.anchors=new Ellipse2D.Double[EAnchor.values().length-1];
		for (int i=0; i<this.anchors.length; i++) {
			this.anchors[i]=new Ellipse2D.Double();
		}
		this.isSelected=false;
		this.eSelectedAnchor=null;
	}
	//getters & setters
	public Shape getShape() {
		return this.shape;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean input) {
		this.isSelected=input;
	}
	public EAnchor getSelectedAnchor() {
		return this.eSelectedAnchor;
	}
	public void setSelectedAnchor(EAnchor input) {
		this.eSelectedAnchor=input;
	}
	public AffineTransform getAffineTransform() {
		return this.affimetransform;
	}
	public Shape getTransformedShape() {
		return this.affimetransform.createTransformedShape(this.shape);
	}
	//method
	public void draw(Graphics2D g2d) {
		transformedShape= this.affimetransform.createTransformedShape(shape);
	    g2d.draw(transformedShape);
	    if(isSelected) {
	    	//여기 또 추가해야함.
	    	this.setAnchors();
	    	for (int i=0; i<this.anchors.length; i++) {
	    		Shape transformedAnchor = this.affimetransform.createTransformedShape(anchors[i]);
	    		Color penColor =g2d.getColor();
	    		g2d.setColor(g2d.getBackground());
	    		g2d.fill(transformedAnchor);
	    		g2d.setColor(penColor);
	    		g2d.draw(transformedAnchor);
	    	}
	    }
	}
	public boolean contains(int x, int y) {
		if(isSelected) {
			for (int i=0; i<this.anchors.length; i++) {
				Shape transformedAnchor=this.affimetransform.createTransformedShape(anchors[i]);
				if(transformedAnchor.contains(x,y)) {
					this.eSelectedAnchor= EAnchor.values()[i];
					return true;
				}
	    	}
		}
		Shape transformedShape=this.affimetransform.createTransformedShape(shape);
		if(transformedShape.contains(x,y)) {
			this.eSelectedAnchor=EAnchor.MM;
			return true;
		}
		return false;
	}
	public boolean contains(GShape shape) {
		return this.shape.contains(shape.getShape().getBounds());
	}
	protected void setAnchors() {
		Rectangle bounds = this.shape.getBounds();
		int bx=bounds.x;
		int by=bounds.y;
		int bw=bounds.width;
		int bh=bounds.height;
		int cx=0,cy=0;
		for (int i=0; i<this.anchors.length; i++) {
			switch(EAnchor.values()[i]) {
			case SS: cx=bx+bw/2;	cy=by+bh; break;
			case SE: cx=bx+bw;		cy=by+bh; break;
			case SW: cx=bx;			cy=by+bh; break;
			case NN: cx=bx+bw/2;	cy=by; break;
			case NE: cx=bx+bw;		cy=by; break;
			case NW: cx=bx;			cy=by; break;
			case EE: cx=bx+bw;		cy=by+bh/2; break;
			case WW: cx=bx;			cy=by+bh/2; break;
			case RR: cx=bx+bw/2;	cy=by-30; break;
			default: break;
			}
			anchors[i].setFrame(cx-ANCHOR_W/2,cy-ANCHOR_W/2,ANCHOR_W,ANCHOR_H);
		}
		
	}
	public Rectangle getBounds() {
	    return this.shape.getBounds();
	}
	public abstract void setPoint(int x, int y);
	public abstract void addPoint(int x, int y);
	public abstract void dragPoint(int x, int y);
}