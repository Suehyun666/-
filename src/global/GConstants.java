package global;

import java.awt.Cursor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shapes.*;
import shapes.GShape.EPoints;

public class GConstants {
	public final class GMainFrame{
		public final int x= 0;
		public final static String TITLE="Drawing Application";
		public final static int WIDTH=1200;
		public final static int HEIGHT=1200;
	}


	public enum EMainFrame {
		eX(0),
		eY(0),
		eW(0),
		eH(0);
		private int value;
		private EMainFrame(int value) {
			this.value=value;
		}
		private int getvalue() {
			return this.value;
		}
		public static void setValue(Node node) {
			for(EMainFrame eMainFrame :EMainFrame.values()) {
				Node attribute = (Node) node.getChildNodes();
				eMainFrame.value =Integer.parseInt(attribute.getNodeValue());
			}

		}
	}
	public final class GMainPanel{}

	public enum EShapeTool {
		eSelect("select", EPoints.e2P,GRectangle.class),
		eRectangle("rectangle", EPoints.e2P,GRectangle.class),
		eTriangle("triangle", EPoints.e2P, GTriangle.class),
		eEllipse("ellipse", EPoints.e2P, GEllipse.class),
		eLine("line", EPoints.e2P,GLine.class),
		ePolygon("polygon",EPoints.enP,GPolygon.class),
		ePen("Pen", EPoints.enP,GPen.class),
		eBrush("Brush", EPoints.enP,GBrush.class),
		eErase("Erase",EPoints.e2P,GEllipse.class);

		private String name;
		private EPoints ePoints;
		private Class<?> classShape;

		EShapeTool(String name, EPoints eDrawingType, Class<?> classShape) {
			this.name=name;
			this.ePoints=eDrawingType;
			this.classShape=classShape;
		}

		public String getName() {
			return this.name;
		}
		public EPoints getEPoints() {
			return this.ePoints;
		}
		public GShape newShape(){
			try {return (GShape) classShape.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					 | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}return null;
		}
	}

	public enum EAnchor{
		NN(new Cursor(Cursor.N_RESIZE_CURSOR)),
		NE(new Cursor(Cursor.NE_RESIZE_CURSOR)),
		NW(new Cursor(Cursor.NW_RESIZE_CURSOR)),
		SS(new Cursor(Cursor.S_RESIZE_CURSOR)),
		SE(new Cursor(Cursor.SE_RESIZE_CURSOR)),
		SW(new Cursor(Cursor.SW_RESIZE_CURSOR)),
		EE(new Cursor(Cursor.E_RESIZE_CURSOR)),
		WW(new Cursor(Cursor.W_RESIZE_CURSOR)),
		RR(new Cursor(Cursor.HAND_CURSOR)),
		MM(new Cursor(Cursor.MOVE_CURSOR));

		private Cursor cursor;
		private EAnchor(Cursor cursor) {
			this.cursor=cursor;
		}
		public Cursor getCursor() {
			return this.cursor;
		}
	}



	public void readFromFile(String fileName) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(fileName);
			Document document =builder.parse(file);
			NodeList nodeList=document.getDocumentElement().getChildNodes();

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
