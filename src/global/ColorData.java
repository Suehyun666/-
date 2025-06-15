package global;

import java.awt.*;

public class ColorData {
    public final Color fillColor;
    public final Color strokeColor;
    public final int strokeWidth;
    public final boolean fillEnabled;
    public final boolean strokeEnabled;

    public ColorData(){
        this.fillColor = Color.BLACK;
        this.strokeColor=Color.BLACK;
        this.strokeWidth=1;
        this.fillEnabled=true;
        this.strokeEnabled=true;
    }

    public ColorData(Color fillColor, Color strokeColor, int strokeWidth, boolean fillEnabled, boolean strokeEnabled) {
        this.fillColor = fillColor;
        this.strokeColor= strokeColor;
        this.strokeWidth=strokeWidth;
        this.fillEnabled=fillEnabled;
        this.strokeEnabled =strokeEnabled;
    }
}
