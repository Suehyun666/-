package util;

import frames.GMainPanel;
import shapes.GShape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageUtils {
    public static BufferedImage createCanvasImage(GMainPanel panel) {
        Dimension canvasSize = panel.getCanvasSize();
        BufferedImage image;

        if (canvasSize != null && panel.isCanvasSizeLimited()) {
            image = new BufferedImage(canvasSize.width, canvasSize.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(panel.getBackground());
            g2d.fillRect(0, 0, canvasSize.width, canvasSize.height);

            for (GShape shape : panel.getshapes()) {
                shape.draw(g2d);
            }
            g2d.dispose();
        } else {
            image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            panel.paint(g2d);
            g2d.dispose();
        }

        return image;
    }

    public static void savePngImage(File datFile, BufferedImage image) throws Exception {
        String pngFileName = FileUtils.removeExtension(datFile.getName()) + ".png";
        File pngFile = new File(datFile.getParent(), pngFileName);
        ImageIO.write(image, "png", pngFile);
    }
}