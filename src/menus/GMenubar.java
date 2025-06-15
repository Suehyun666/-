package menus;

import global.GConstants;
import language.LanguageSupport;

import javax.swing.JMenuBar;

import static java.awt.Color.GRAY;
import global.GConstants.EMenuType;
public class GMenubar extends JMenuBar implements LanguageSupport {
    // attributes
    private static final long serialVersionUID = 1L;

    //components
    private GFileMenu fileMenu;
    private GEditMenu editMenu;
    private GGraphicMenu graphicMenu;
    private GImageMenu imagemenu;
    private GLayerMenu layermenu;
    private GSelectMenu selectmenu;
    private GFilterMenu filtermenu;
    private GWindowMenu windowmenu;
    private GHelpMenu helpmenu;
    //associate

    // constructor
    public GMenubar() {
        this.setBackground(GRAY);
        createMenus();
    }
    private void createMenus() {
        this.fileMenu = new GFileMenu(EMenuType.eFile.getText());
        this.add(fileMenu);

        this.editMenu = new GEditMenu(EMenuType.eEdit.getText());
        this.add(editMenu);

        this.graphicMenu = new GGraphicMenu(EMenuType.eGraphic.getText());
        this.add(graphicMenu);

        this.imagemenu = new GImageMenu(EMenuType.eImage.getText());
        this.add(imagemenu);

        this.layermenu = new GLayerMenu(EMenuType.eLayer.getText());
        this.add(layermenu);

        this.selectmenu = new GSelectMenu(EMenuType.eSelect.getText());
        this.add(selectmenu);

        this.filtermenu = new GFilterMenu(EMenuType.eFilter.getText());
        this.add(filtermenu);

        this.windowmenu = new GWindowMenu(EMenuType.eWindow.getText());
        this.add(windowmenu);

        this.helpmenu = new GHelpMenu(EMenuType.eHelp.getText());
        this.add(helpmenu);
    }
    // method
    // initialize
    public void initialize() {
        fileMenu.initialize();
        editMenu.initialize();
        graphicMenu.initialize();
        imagemenu.initialize();
        graphicMenu.initialize();
        layermenu.initialize();
        selectmenu.initialize();
        filtermenu.initialize();
        windowmenu.initialize();
        helpmenu.initialize();
        updateLanguage();
    }

    public GFileMenu getFileMenu() {
        return this.fileMenu;
    }
    public GEditMenu getEditMenu() {return this.editMenu;}
    public GSelectMenu getSelectMenu() {return this.selectmenu;}
    public GFilterMenu getFilterMenu() {return this.filtermenu;}
    public GImageMenu getImageMenu() {return this.imagemenu;}
    public GGraphicMenu getGraphicMenu() {return this.graphicMenu;}
    public GLayerMenu getLayerMenu() {return this.layermenu;}
    public GHelpMenu getHelpMenu() {return this.helpmenu;}
    @Override
    public void updateLanguage() {
        // 각 메뉴의 제목 업데이트
        if (fileMenu != null) {
            fileMenu.setText(EMenuType.eFile.getText());
        }
        if (editMenu != null) {
            editMenu.setText(EMenuType.eEdit.getText());
        }
        if (selectmenu != null) {
            selectmenu.setText(EMenuType.eSelect.getText());
        }
        if (graphicMenu != null) {
            graphicMenu.setText(EMenuType.eGraphic.getText());
        }
        if (imagemenu != null) {
            imagemenu.setText(EMenuType.eImage.getText());
        }
        if (layermenu != null) {
            layermenu.setText(EMenuType.eLayer.getText());
        }
        if (filtermenu != null) {
            filtermenu.setText(EMenuType.eFilter.getText());
        }
        if (windowmenu != null) {
            windowmenu.setText(EMenuType.eWindow.getText());
        }
        if (helpmenu != null) {
            helpmenu.setText(EMenuType.eHelp.getText());
        }
        updateMenuItems();
        repaint();
    }
    private void updateMenuItems() {
        if (fileMenu instanceof LanguageSupport) {
            ((LanguageSupport) fileMenu).updateLanguage();
        }
        if (editMenu instanceof LanguageSupport) {
            ((LanguageSupport) editMenu).updateLanguage();
        }
        if (selectmenu instanceof LanguageSupport) {
            ((LanguageSupport) selectmenu).updateLanguage();
        }
        if (graphicMenu instanceof LanguageSupport) {
            ((LanguageSupport) graphicMenu).updateLanguage();
        }
        if (imagemenu instanceof LanguageSupport) {
            ((LanguageSupport) imagemenu).updateLanguage();
        }
        if (layermenu instanceof LanguageSupport) {
            ((LanguageSupport) layermenu).updateLanguage();
        }
        if (filtermenu instanceof LanguageSupport) {
            ((LanguageSupport) filtermenu).updateLanguage();
        }
        if (windowmenu instanceof LanguageSupport) {
            ((LanguageSupport) windowmenu).updateLanguage();
        }
        if (helpmenu instanceof LanguageSupport) {
            ((LanguageSupport) helpmenu).updateLanguage();
        }
    }

}