package constants;

import org.w3c.dom.Node;

public class GMenuConstants {
	public enum EFileMenuItem {
		eNew("New","create"),
		eOpen("Open","open"),
		eSave("Save","save"),
		eSaveAs("Save as","saveas"),
		eClose("Close","close"),
		ePrint("Print","print"),
		eImport("Import","import"),
		eExport("Export","export"),
		eExit("Exit","exit");
		
		private String menu;
		private String toolTipText;
		private String methodname;
		EFileMenuItem(String menu, String methodname) {
			this.menu = menu;
			this.methodname=methodname;
		}
		public String getText() {
			return this.menu;
		}
		public String getMethodName() {
			return this.methodname;
		}
		void setValues(Node node) {
			//this.node=node;
		}
	}

	public enum EEditMenuItem {
		eProperty("Property"),
		eUndo("Undo"),
		eForward("Forward"),
		eBackward("Backward"),
		eFade("Fade"),
		eCut("Cut"),
		eCopy("Copy"),
		ePaste("Paste"),
		eClear("Clear"),
		eFill("Fill"),
		eColorSetting("Color Setting");

		private String menu;
		EEditMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum EGraphicMenuItem {
		eLineThickness("Line Thickness"),
		eLineStyle("Line Style"),
		eFontStyle("Font Style"),
		eFontSize("Font Size");

		private String menu;
		EGraphicMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum EHelpMenuItem {
		eSystemInfo("System Info"),
		eAbout("about"),
		eOnline("online");

		private String menu;
		EHelpMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum EImageMenuItem {
		eMode("Mode"),
		eImageSize("Image Size"),
		eCanvasSize("Canvas Size"),
		eRotate("Rotate Image"),
		eCrop("Crop"),
		eTrim("Trim"),
		eDuplicate("Duplicate");
		private String menu;
		EImageMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum ELayerMenuItem {
		eNewLayer("New Layer"),
		eDuplicate("Duplicate"),
		eDeleteLayer("Delete Layer"),
		eRenameLayer("Rename Layer"),
		eLock("Lock"),
		eMerge("Merge"),
		eHide("Hide");
		private String menu;
		ELayerMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum ESelectMenuItem {
		eAll("All"),
		eDeselect("Deselect"),
		eReselect("ReSelect"),
		eAllLayer("All Layer");

		private String menu;
		ESelectMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum EFilterMenuItem {
		eBlur("Blur"),
		eNoise("Noise");

		private String menu;
		EFilterMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum ETypeMenuItem {
		ePanel("Panel"),
		eLanguage("Language"),
		eForward("Forward");

		private String menu;
		ETypeMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

	public enum EWindowMenuItem {
		eProperty("Workspace"),
		eExtension("Extension"),
		eOption("Option"),
		eTool("Tool"),
		eHistory("History"),
		ePreFerence("Preference"),;
		private String menu;
		EWindowMenuItem(String menu) {
			this.menu = menu;
		}
		public String getText() {
			return this.menu;
		}
	}

}
