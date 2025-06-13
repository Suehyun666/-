package transformers;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import global.GConstants.EAnchor;
import shapes.GShape;

public class GResizer extends GTransFormer {
	private GShape shape;
	private int px, py; 
	private int cx,cy;
	private EAnchor eRisizeAnchor;
	private Rectangle originalBounds;
	public GResizer(GShape gshape) {
		super(gshape);
		this.shape=gshape;
	}
	
	@Override
	public void start(Graphics2D graphics, int x, int y) {
		this.px=x;
		this.py=y;
		this.originalBounds = this.shape.getBounds();
		Rectangle r = new Rectangle(originalBounds);;
		
		EAnchor eSelectedAnchor =this.shape.getSelectedAnchor();
		eRisizeAnchor = null;
		switch(eSelectedAnchor) {
		case NW: eRisizeAnchor=EAnchor.SE; cx = r.x+r.width; 	cy = r.y+r.height; break;
		case WW: eRisizeAnchor=EAnchor.EE; cx = r.x+r.width; 	cy = r.y+r.height/2; break;
		case SW: eRisizeAnchor=EAnchor.NE; cx = r.x+r.width; 	cy = r.y; break;
		case SS: eRisizeAnchor=EAnchor.NN; cx = r.x+r.width/2; 	cy = r.y; break;
		case SE: eRisizeAnchor=EAnchor.NW; cx = r.x; 			cy = r.y; break;
		case EE: eRisizeAnchor=EAnchor.WW; cx = r.x; 			cy = r.y+r.height/2; break;
		case NE: eRisizeAnchor=EAnchor.SW; cx = r.x; 			cy = r.y+r.height; break;
		case NN: eRisizeAnchor=EAnchor.SS; cx = r.x+r.width/2; 	cy = r.y+r.height; break;
		default: break;}
		
		this.shape.getAffineTransform().scale(1, 1);
		this.px=x;
		this.py=y;
		
	}
	
	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		double dx = 0, dy=0;
		//old width, height / new width, height 
		int newX = Math.min(x, cx);
        int newY = Math.min(y, cy);
        int newWidth = Math.abs(x - cx);
        int newHeight = Math.abs(y - cy);
        //0pixel제한 
        if (newWidth < 5) newWidth = 5;
        if (newHeight < 5) newHeight = 5;
        
		//어떤 앵커인지 알아야함.
		switch(eRisizeAnchor) {
		case NW: dx=(x-px); 	dy=(y-py); 		break;
		case WW: dx=(x-px); 	dy=0; 			break;
		case SW: dx=(x-px); 	dy=-(y-py); 	break;
		case SS: dx=0; 			dy=-(y-py);		break;
		case SE: dx=-(x-px);	dy=-(y-py); 	break;
		case EE: dx=-(x-px); 	dy=0; 			break;
		case NE: dx=-(x-px); 	dy=(y-py);		break;
		case NN: dx=0; 			dy=(y-py);		break;
		default: break;
		}
		Shape transformedShape= this.shape.getTransformedShape();
		double w1=transformedShape.getBounds().width;
		double w2=dx+w1;
		double h1=transformedShape.getBounds().height;
		double h2=dy+h1;
		double xScale = w2/w1;
		double yScale = h2/h1;
		
		this.shape.getAffineTransform().translate(cx, cy);// 왜 이렇게 했는지 생각 -찌그러짐
		this.shape.getAffineTransform().scale(xScale, yScale); 
		this.shape.getAffineTransform().translate(-cx, -cy);//문제있는 코드다.
		
		this.px=x;
		this.py=y;
	}
	@Override
	public void addpoint(Graphics2D graphics, int x, int y) {}
	@Override
	public void finish(Graphics2D graphics, int x, int y) {}
}