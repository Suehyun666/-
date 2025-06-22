package util;

import frames.GMainPanel;
import frames.GTabComponent;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class TabUtils {
    public static String makeUniqueTitle(JTabbedPane tabbedPane, String baseTitle) {
        String uniqueTitle = baseTitle;
        int count = 1;

        while (isTitleExists(tabbedPane, uniqueTitle)) {
            count++;
            uniqueTitle = baseTitle + "-" + count;
        }
        return uniqueTitle;
    }

    public static boolean isTitleExists(JTabbedPane tabbedPane, String title) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component tab = tabbedPane.getTabComponentAt(i);
            if (tab instanceof GTabComponent) {
                String existingTitle = ((GTabComponent) tab).getTitle().replace("*", "");
                if (existingTitle.equals(title)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void updateTabTitle(JTabbedPane tabbedPane, Vector<GMainPanel> panels,
                                      GMainPanel panel, String title, boolean modified) {
        int index = panels.indexOf(panel);
        if (index >= 0) {
            Component tab = tabbedPane.getTabComponentAt(index);
            if (tab instanceof GTabComponent) {
                String displayTitle = title;
                if (modified && !title.endsWith("*")) {
                    displayTitle += "*";
                }
                ((GTabComponent) tab).setTitle(displayTitle);
            }
        }
    }
}
