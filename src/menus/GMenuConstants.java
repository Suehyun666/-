package menus;

import language.LanguageManager;

public class GMenuConstants {
	public enum EFileMenuItem {
		eNew("file.new","create"),
		eOpen("file.open","open"),
		eSave("file.save","save"),
		eSaveAs("file.saveas","saveas"),
		eClose("file.close","close"),
		closeCurrentTab("file.closeTab", "closeCurrentTab"),
		ePrint("file.print","print"),
		eImport("file.import","importFile"),
		eExport("file.export","exportFile"),
		eExit("file.exit","exit");

		private String messageKey;
		private String methodname;

		EFileMenuItem(String messageKey, String methodname) {
			this.messageKey = messageKey;
			this.methodname = methodname;
		}

		public String getText() {
			return LanguageManager.getInstance().getText(this.messageKey);
		}

		public String getMethodName() {
			return this.methodname;
		}
	}

	public enum EEditMenuItem {
		eProperty("edit.property", "property"),
		eUndo("edit.undo", "undo"),
		eRedo("edit.redo", "redo"),
		eForward("edit.forward", "forward"),
		eBackward("edit.backward", "backward"),
		eToBackward("edit.toBackward", "toBackward"),
		eToForward("edit.toForward", "toForward"),
		eFade("edit.fade", "fade"),
		eCut("edit.cut", "cut"),
		eCopy("edit.copy", "copy"),
		ePaste("edit.paste", "paste"),
		eClear("edit.clear", "clear"),
		eFill("edit.fill", "fill"),
		eColorSetting("edit.colorSetting", "colorSetting");

		private String messageKey;
		private String methodname;

		EEditMenuItem(String messageKey, String methodname) {
			this.messageKey = messageKey;
			this.methodname = methodname;
		}

		public String getText() {
			return LanguageManager.getInstance().getText(this.messageKey);
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
		eAll("select.all", "selectall"),
		eDeselect("select.deselect", "deselect"),
		eReselect("select.reselect", "reselect"),
		eAllLayer("select.allLayer", "allLayer");

		private String messageKey;
		private String methodname;

		ESelectMenuItem(String messageKey, String methodname) {
			this.messageKey = messageKey;
			this.methodname = methodname;
		}

		public String getText() {
			return LanguageManager.getInstance().getText(this.messageKey);
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

	public enum EWindowMenuItem {
		eProperty("Workspace", "workspace"),
		eExtension("Extension", "extension"),
		eOption("Option", "option"),
		eTool("Tool", "tool"),
		eHistory("History", "history"),
		ePreFerence("Preference", "preference"),
		eLanguage("Language", "language");
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