package global;

import java.awt.Cursor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import constants.GMenuConstants.EEditMenuItem;
import constants.GMenuConstants.EFileMenuItem;
import constants.GMenuConstants.EGraphicMenuItem;
import constants.GMenuConstants.EHelpMenuItem;
import constants.GMenuConstants.EImageMenuItem;
import constants.GMenuConstants.ELayerMenuItem;
import constants.GMenuConstants.ESelectMenuItem;
import shapes.GPolygon;
import shapes.GRectangle;
import shapes.GShape;
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
	public final class GMainPanel{
		
	}
	
	public enum EShapeTool {
    	eSelect("select", EPoints.e2P,GRectangle.class),
    	eRectangle("rectangle", EPoints.e2P,GRectangle.class),
    	eEllipse("ellipse", EPoints.e2P,GRectangle.class),
    	eLine("line", EPoints.e2P,GRectangle.class),
    	ePolygon("polygon",EPoints.enP,GPolygon.class);
    	//여기다 상수 쓰는 건 안 좋다. 나눠서 리소스 파일로 다 빼야한다. xml 이나 locale 별로 
    	//json이나 xml로 뜯어낼 것이다.
    	//실제로 어느 프로그램이나 그렇게 한다. -reddundency
    	
    	private String name;
    	private EPoints ePoints;
    	private Class<?> classShape;
    	
		private EShapeTool(String name, EPoints eDrawingType, Class<?> classShape) {
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
			try {
				GShape shape = (GShape) classShape.getConstructor().newInstance();
				return shape;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
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
