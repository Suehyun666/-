package shapes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class GPicture extends GShape {
    private static final long serialVersionUID = 1L;
    private Rectangle2D rectangle;
    private String imagePath;
    private transient Image image;
    private int imageWidth = 0;
    private int imageHeight = 0;

    public GPicture(File imageFile) {
        super(new Rectangle2D.Float(0, 0, 0, 0));
        this.rectangle = (Rectangle2D) this.getShape();
        this.imagePath = imageFile.getAbsolutePath();
        loadImage(imageFile);
        this.isFillEnabled=false;
    }

    private void loadImage(File imageFile) {
        try {
            if (!imageFile.exists()) {
                System.err.println("File does not exist: " + imageFile.getAbsolutePath());
                return;
            }
            System.out.println("Loading image: " + imageFile.getAbsolutePath());
            this.image = ImageIO.read(imageFile);

            if (this.image == null) {
                System.out.println("ImageIO.read() returned null. Using Toolkit...");
                this.image = Toolkit.getDefaultToolkit().getImage(imageFile.getAbsolutePath());
                MediaTracker tracker = new MediaTracker(new Canvas());
                tracker.addImage(this.image, 0);
                tracker.waitForAll();
            }

            if (this.image != null) {
                this.imageWidth = this.image.getWidth(null);
                this.imageHeight = this.image.getHeight(null);

                System.out.println("Image loaded successfully: " + imageWidth + "x" + imageHeight);

                updateRectangle(0, 0);
            } else {
                System.err.println("Failed to load image");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.image = null;
        }
    }

    private void updateRectangle(int x, int y) {
        if (imageWidth > 0 && imageHeight > 0) {
            this.rectangle.setFrame(x, y, imageWidth, imageHeight);
            // 부모 클래스의 shape도 업데이트
            this.shape = new Rectangle2D.Float(x, y, imageWidth, imageHeight);
        }
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        updateRectangle(x, y);
        if (this.transform == null) {
            this.transform = new AffineTransform();
        }
    }

    @Override
    public void addPoint(int x, int y) {
        // 이미지는 addPoint 사용 안함
    }

    @Override
    public void dragPoint(int x, int y) {
        // 이미지 리사이즈는 별도 구현)
        setPoint(x, y);
    }

    public void setLocation(int x, int y) {
        this.startX = x;
        this.startY = y;
        updateRectangle(x, y);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (this.image != null) {
            this.transformedShape = this.transform.createTransformedShape(this.shape);
            Rectangle bounds = transformedShape.getBounds();
            g2d.drawImage(this.image, bounds.x, bounds.y, bounds.width, bounds.height, null);

            if (isSelected) {
                Color prevColor = g2d.getColor();
                g2d.setColor(Color.BLUE);
                g2d.draw(transformedShape);
                g2d.setColor(prevColor);
                drawAnchors(g2d);
            }
        } else {
            Shape transformedShape = getTransformedShape();
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fill(transformedShape);
            g2d.setColor(Color.BLACK);
            g2d.draw(transformedShape);

            Rectangle bounds = transformedShape.getBounds();
            g2d.drawString("No Image", bounds.x + 10, bounds.y + 20);
        }
    }

    public boolean hasImage() {
        return this.image != null;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public Dimension getImageSize() {
        return new Dimension(imageWidth, imageHeight);
    }
    @Override
    public GPicture clone() {
        GPicture cloned = (GPicture) super.clone();
        cloned.rectangle = new Rectangle2D.Float(
                (float) this.rectangle.getX(),
                (float) this.rectangle.getY(),
                (float) this.rectangle.getWidth(),
                (float) this.rectangle.getHeight()
        );
        cloned.shape = cloned.rectangle;
        cloned.imagePath = this.imagePath;
        cloned.imageWidth = this.imageWidth;
        cloned.imageHeight = this.imageHeight;
        // 이미지는 다시 로드해야 함 (transient)
        if (this.imagePath != null) {
            cloned.loadImage(new File(this.imagePath));
        }
        return cloned;
    }
}