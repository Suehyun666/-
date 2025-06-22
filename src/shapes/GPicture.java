package shapes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

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
    public GPicture() {
        super(new Rectangle2D.Float(0, 0, 100, 100));
        this.rectangle = (Rectangle2D) this.getShape();
        this.isFillEnabled = false;
    }

    public void loadImage(File imageFile) {
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
            this.shape = new Rectangle2D.Float(x, y, imageWidth, imageHeight);
            this.originalShape = this.shape;
        }
    }

    @Override
    public void setPoint(int x, int y) {
        this.startX = x;
        this.startY = y;
        updateRectangle(x, y);
        if (this.transform == null) {
            this.transform = new AffineTransform();
        }updateTransformedShape();
    }

    @Override
    public void addPoint(int x, int y) {}

    @Override
    public void dragPoint(int x, int y) {
        setPoint(x, y);
    }

    public void setLocation(int x, int y) {
        this.startX = x;
        this.startY = y;
        updateRectangle(x, y);
        updateTransformedShape();
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (!visible) {
            return;
        }
        if (this.image != null) {
            // 변환된 모양 업데이트
            updateTransformedShape();

            Graphics2D g = (Graphics2D) g2d.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // 원본 바운드 정보
            Rectangle2D originalBounds = this.shape.getBounds2D();
            double originalCenterX = originalBounds.getCenterX();
            double originalCenterY = originalBounds.getCenterY();

            // 변환 적용 전 상태 저장
            AffineTransform oldTransform = g.getTransform();

            // 1. 먼저 평행이동 적용
            g.translate(translateX, translateY);

            // 2. 중심점 기준으로 회전 적용
            if (Math.abs(rotationAngle) > 0.001) {
                g.rotate(rotationAngle, originalCenterX, originalCenterY);
            }

            // 3. 중심점 기준으로 스케일 적용
            if (Math.abs(scaleX - 1.0) > 0.001 || Math.abs(scaleY - 1.0) > 0.001) {
                g.translate(originalCenterX, originalCenterY);
                g.scale(scaleX, scaleY);
                g.translate(-originalCenterX, -originalCenterY);
            }

            // 4. 이미지 그리기
            g.drawImage(this.image,
                    (int) originalBounds.getX(),
                    (int) originalBounds.getY(),
                    (int) originalBounds.getWidth(),
                    (int) originalBounds.getHeight(),
                    null);

            // 변환 복원
            g.setTransform(oldTransform);
            g.dispose();

            // 선택 표시
            if (isSelected) {
                Color prevColor = g2d.getColor();
                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(getTransformedShape());
                g2d.setColor(prevColor);
                drawAnchors(g2d);
            }
        } else {
            // 이미지가 없는 경우
            Shape transformedShape = getTransformedShape();
            if (transformedShape != null) {
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill(transformedShape);
                g2d.setColor(Color.BLACK);
                g2d.draw(transformedShape);
                Rectangle bounds = transformedShape.getBounds();
                g2d.drawString("No Image", bounds.x + 10, bounds.y + 20);
            }
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
        if (this.imagePath != null) {
            File imageFile = new File(this.imagePath);
            if (imageFile.exists()) {
                cloned.loadImage(imageFile);
            }
        }
        return cloned;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        if (imagePath != null) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                loadImage(imageFile);
            } else {
                System.err.println("Image file not found during deserialization: " + imagePath);
                if (imageWidth > 0 && imageHeight > 0) {
                    updateRectangle(startX, startY);
                }
            }
        }
        updateTransformedShape();
    }
}