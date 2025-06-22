package util;

import java.awt.*;

public class ColorUtils {
    public static Color getBackgroundColor(String backgroundType) {
        switch (backgroundType) {
            case "White": return Color.WHITE;
            case "Background Color": return Color.LIGHT_GRAY;
            case "Transparent": return new Color(0, 0, 0, 0);
            default: return Color.DARK_GRAY;
        }
    }

    public static final Color[] BASIC_COLORS = {
            Color.BLACK, Color.WHITE, Color.RED, Color.GREEN,
            Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.GRAY, Color.LIGHT_GRAY,
            Color.DARK_GRAY, new Color(128, 0, 128),
            new Color(165, 42, 42), new Color(0, 128, 0)
    };
}