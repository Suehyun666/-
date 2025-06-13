package generator;

import frames.GMainPanel;
import shapes.GPicture;
import shapes.GShape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Vector;

public class GPictureViewerDialog extends JDialog {
    private BufferedImage image;
    private GMainPanel mainPanel;
    private File imageFile;

    public GPictureViewerDialog(JFrame owner, String title, File imageFile, GMainPanel mainPanel) {
        super(owner, title, true);
        this.mainPanel = mainPanel;
        this.imageFile = imageFile;

        System.out.println("File road: " + imageFile.getAbsolutePath());
        System.out.println("File Exist: " + imageFile.exists());
        System.out.println("File Size: " + imageFile.length() + " bytes");
        System.out.println("File Read Possible: " + imageFile.canRead());

        try {
            String contentType = Files.probeContentType(imageFile.toPath());
            System.out.println("Contents type: " + contentType);
        } catch (IOException e) {
            System.out.println("Contents type Check Failed: " + e.getMessage());
        }

        String[] readerFormatNames = ImageIO.getReaderFormatNames();
        System.out.println("ImageIO Supported Format: " + String.join(", ", readerFormatNames));

        try {
            Thread.sleep(500);
            this.image = ImageIO.read(imageFile);

            if (this.image == null) {
                System.out.println("ImageIO.read() returned null");
                Image toolkitImage = Toolkit.getDefaultToolkit().getImage(imageFile.getAbsolutePath());
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(toolkitImage, 0);
                tracker.waitForAll();

                if (toolkitImage.getWidth(null) > 0) {
                    // Toolkit으로 로드된 이미지를 BufferedImage로 변환
                    this.image = new BufferedImage(
                            toolkitImage.getWidth(null),
                            toolkitImage.getHeight(null),
                            BufferedImage.TYPE_INT_ARGB
                    );
                    Graphics2D g2d = this.image.createGraphics();
                    g2d.drawImage(toolkitImage, 0, 0, null);
                    g2d.dispose();
                    System.out.println("Image Load Success");
                }
            } else {
                System.out.println("ImageIO.read() Success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Image Load Failed : " + e.getMessage() + "\nFile: " + imageFile.getAbsolutePath(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (this.image == null) {
            JOptionPane.showMessageDialog(this,
                    "Can not load the fole .\nfile: " + imageFile.getAbsolutePath(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        initComponents();
        int width = Math.max(1200, Math.min(image.getWidth() + 40, 800));
        int height = Math.max(800, Math.min(image.getHeight() + 120, 600));
        setSize(width, height);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel(new ImageIcon(image));
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton addToPanelButton = new JButton("Add");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(saveButton);
        buttonPanel.add(addToPanelButton);
        buttonPanel.add(closeButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("generated_image.png"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                try {
                    Files.copy(imageFile.toPath(), saveFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    JOptionPane.showMessageDialog(this, "Image Saved",
                            "Save Completed", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Save Failed: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addToPanelButton.addActionListener(e -> {
            try {
                GPicture picture = new GPicture(imageFile);
                if (!picture.hasImage()) {
                    JOptionPane.showMessageDialog(this,
                            "Image loading failed. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int centerX = Math.max(0, mainPanel.getWidth() / 2 - picture.getImageSize().width / 2);
                int centerY = Math.max(0, mainPanel.getHeight() / 2 - picture.getImageSize().height / 2);

                picture.setLocation(centerX, centerY);

                mainPanel.addShape(picture);
                mainPanel.setUpdate(true);

                System.out.println("MainPanel shapes after add: " + mainPanel.getshapes().size());

                mainPanel.revalidate();
                mainPanel.repaint();

                JOptionPane.showMessageDialog(this,
                        "Image added successfully at (" + centerX + ", " + centerY + ")",
                        "Add Complete", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Failed to add image: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        closeButton.addActionListener(e -> dispose());
        add(contentPanel);
        pack();
    }
}