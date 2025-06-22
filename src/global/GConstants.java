package global;

import java.awt.Cursor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import language.LanguageManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shapes.*;
import shapes.GShape.EPoints;

public class GConstants {
	public final class GMainFrame{
		public final int x= 0;
		public final static String TITLE=LanguageManager.getInstance().getText("app.title");;
		public final static int WIDTH=1200;
		public final static int HEIGHT=1200;
	}

	public enum EMenuType {
		eFile("file.menu"),
		eEdit("edit.menu"),
		eSelect("select.menu"),
		eGraphic("graphic.menu"),
		eImage("image.menu"),
		eLayer("layer.menu"),
		eFilter("filter.menu"),
		eWindow("window.menu"),
		eHelp("help.menu");

		private String messageKey;

		EMenuType(String messageKey) {
			this.messageKey = messageKey;
		}
		public String getText() {return LanguageManager.getInstance().getText(this.messageKey);}
		public String getMessageKey() {
			return this.messageKey;
		}
	}

	public enum EShapeTool {
		eSelect("select", EPoints.e2P,GRectangle.class),
		eRectangle("rectangle", EPoints.e2P,GRectangle.class),
		eTriangle("triangle", EPoints.e2P, GTriangle.class),
		eEllipse("ellipse", EPoints.e2P, GEllipse.class),
		eLine("line", EPoints.e2P,GLine.class),
		ePolygon("polygon",EPoints.enP,GPolygon.class),
		ePen("Pen", EPoints.e2P,GPen.class),
		eBrush("Brush", EPoints.e2P,GBrush.class),
		eErase("Erase",EPoints.e2P,GEraser.class);

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
			} catch (Exception e) {e.printStackTrace();}return null;
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
}
