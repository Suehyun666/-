package global;

public class GMenuConstants {
	public enum EFileMenuItem {
		eNew("New","create"),
		eOpen("Open","open"),
		eSave("Save","save"),
		eSaveAs("Save as","saveas"),
		eClose("Close","close"),
		closeCurrentTab("Close Tab", "closeCurrentTab"),
		ePrint("Print","print"),
		eImport("Import","importFile"),
		eExport("Export","exportFile"),
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
	}

	public enum EEditMenuItem {
		eProperty("Property", "property"),
		eUndo("Undo", "undo"),
		eRedo("Redo", "redo"),
		eForward("Forward", "forward"),
		eBackward("Backward", "backward"),
		eFade("Fade", "fade"),
		eCut("Cut", "cut"),
		eCopy("Copy", "copy"),
		ePaste("Paste", "paste"),
		eClear("Clear", "clear"),
		eFill("Fill", "fill"),
		eColorSetting("ColorSetting", "colorSetting");

		private String menu;
		private String methodname;
		EEditMenuItem(String menu, String methodname) {
			this.menu = menu;
			this.methodname = methodname;
		}
		public String getText() {
			return this.menu;
		}
		public String getMethodName() {
			return this.methodname;
		}
	}

	public enum EGraphicMenuItem {
		eLineThickness("Line Thickness", "lineThickness"),
		eLineStyle("Line Style", "lineStyle"),
		eFontStyle("Font Style", "fontStyle"),
		eFontSize("Font Size", "fontSize");

		private String menu;
		private String methodname;
		EGraphicMenuItem(String menu, String methodname) {
			this.menu = menu;
			this.methodname = methodname;
		}
		public String getText() {
			return this.menu;
		}
		public String getMethodName() {
			return this.methodname;
		}
	}

	public enum EHelpMenuItem {
		eSystemInfo("System Info", "systemInfo"),
		eAbout("about", "about"),
		eOnline("online", "online");

		private String menu;
		private String methodname;
		EHelpMenuItem(String menu, String methodname) {
			this.menu = menu;
			this.methodname = methodname;
		}
		public String getText() {
			return this.menu;
		}
		public String getMethodName() {
			return this.methodname;
		}
	}

	public enum EImageMenuItem {
		eMode("Mode", "mode"),
		eImageSize("Image Size", "imageSize"),
		eCanvasSize("Canvas Size", "canvasSize"),
		eRotate("Rotate Image", "rotateImage"),
		eCrop("Crop", "crop"),
		eTrim("Trim", "trim"),
		eDuplicate("Duplicate", "duplicate"),
		eGenerateImage("GenerateImage", "generateImage");

		private String menu;
		private String methodname;
		EImageMenuItem(String menu, String methodname) {
			this.menu = menu;
			this.methodname = methodname;
		}
		public String getText() {
			return this.menu;
		}
		public String getMethodName() {
			return this.methodname;
		}
	}

	public enum ELayerMenuItem {
		eNewLayer("New Layer", "newLayer"),
		eDuplicate("Duplicate", "duplicate"),
		eDeleteLayer("Delete Layer", "deleteLayer"),
		eRenameLayer("Rename Layer", "renameLayer"),
		eLock("Lock", "lock"),
		eGroupLayer("Group Layer", "groupLayer"),
		eUnGroupLayer("UnGroup Layer", "unGroupLayer"),
		eMerge("Merge", "merge"),
		eHide("Hide", "hide");

		private String menu;
		private String methodname;
		ELayerMenuItem(String menu, String methodname) {
			this.menu = menu;
			this.methodname = methodname;
		}
		public String getText() {
			return this.menu;
		}
		public String getMethodName() {
			return this.methodname;
		}
	}

	public enum ESelectMenuItem {
		eAll("All", "all"),
		eDeselect("Deselect", "deselect"),
		eReselect("ReSelect", "reselect"),
		eAllLayer("All Layer", "allLayer");

		private String menu;
		private String methodname;
		ESelectMenuItem(String menu, String methodname) {
			this.menu = menu;
			this.methodname = methodname;
		}
		public String getText() {
			return this.menu;
		}
		public String getMethodName() {
			return this.methodname;
		}
	}

	public enum EFilterMenuItem {
		eBlur("Blur", "blur"),
		eNoise("Noise", "noise");
		private String menu;
		private String methodname;
		EFilterMenuItem(String menu, String methodname) {
			this.menu = menu;
			this.methodname = methodname;
		}
		public String getText() {
			return this.menu;
		}
		public String getMethodName() {
			return this.methodname;
		}
	}

	public enum ETypeMenuItem {
		ePanel("Panel", "panel"),
		eLanguage("Language", "language"),
		eForward("Forward", "forward");

		private String menu;
		private String methodname;
		ETypeMenuItem(String menu, String methodname) {
			this.menu = menu;
			this.methodname = methodname;
		}
		public String getText() {
			return this.menu;
		}
		public String getMethodName() {
			return this.methodname;
		}
	}

	public enum EWindowMenuItem {
		eProperty("Workspace", "workspace"),
		eExtension("Extension", "extension"),
		eOption("Option", "option"),
		eTool("Tool", "tool"),
		eHistory("History", "history"),
		ePreFerence("Preference", "preference");

		private String menu;
		private String methodname;
		EWindowMenuItem(String menu, String methodname) {
			this.menu = menu;
			this.methodname = methodname;
		}
		public String getText() {
			return this.menu;
		}
		public String getMethodName() {
			return this.methodname;
		}
	}
}