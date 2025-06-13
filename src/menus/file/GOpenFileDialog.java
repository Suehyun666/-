package menus.file;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GOpenFileDialog extends JDialog {
    private JFileChooser fileChooser;
    private JComboBox<String> fileTypeCombo;
    private JCheckBox showAllFilesCheckbox;
    private JPanel previewPanel;
    private JLabel previewLabel;
    private JLabel fileInfoLabel;
    private boolean confirmed = false;
    private File selectedFile;

    public GOpenFileDialog(Frame parent) {
        super(parent, "Open", true);
        initializeDialog();
    }

    private void initializeDialog() {
        setLayout(new BorderLayout());
        setSize(900, 600);
        setLocationRelativeTo(getParent());

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // File chooser with preview
        fileChooser = new JFileChooser();
        fileChooser.setControlButtonsAreShown(false);

        // Custom file filters
        setupFileFilters();

        mainPanel.add(fileChooser, BorderLayout.CENTER);

        // Preview panel (오른쪽)
        previewPanel = createPreviewPanel();
        mainPanel.add(previewPanel, BorderLayout.EAST);

        // Bottom panel
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // File selection listener
        fileChooser.addPropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, evt -> {
            File file = fileChooser.getSelectedFile();
            if (file != null && file.isFile()) {
                updatePreview(file);
            }
        });
    }

    private void setupFileFilters() {
        // All supported formats
        FileNameExtensionFilter allSupported = new FileNameExtensionFilter(
                "All Supported Formats", "psd", "pdd", "png", "jpg", "jpeg", "gif", "bmp", "tiff", "tif");

        // Individual format filters
        FileNameExtensionFilter psdFilter = new FileNameExtensionFilter(
                "Photoshop (*.PSD;*.PDD)", "psd", "pdd");
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter(
                "PNG (*.PNG)", "png");
        FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter(
                "JPEG (*.JPG;*.JPEG;*.JPE)", "jpg", "jpeg", "jpe");
        FileNameExtensionFilter gifFilter = new FileNameExtensionFilter(
                "GIF (*.GIF)", "gif");
        FileNameExtensionFilter bmpFilter = new FileNameExtensionFilter(
                "BMP (*.BMP;*.RLE;*.DIB)", "bmp", "rle", "dib");
        FileNameExtensionFilter tiffFilter = new FileNameExtensionFilter(
                "TIFF (*.TIF;*.TIFF)", "tif", "tiff");

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(allSupported);
        fileChooser.addChoosableFileFilter(psdFilter);
        fileChooser.addChoosableFileFilter(pngFilter);
        fileChooser.addChoosableFileFilter(jpgFilter);
        fileChooser.addChoosableFileFilter(gifFilter);
        fileChooser.addChoosableFileFilter(bmpFilter);
        fileChooser.addChoosableFileFilter(tiffFilter);
        fileChooser.setFileFilter(allSupported);
    }

    private JPanel createPreviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 400));
        panel.setBorder(BorderFactory.createTitledBorder("Preview"));

        // Preview image area
        previewLabel = new JLabel();
        previewLabel.setHorizontalAlignment(JLabel.CENTER);
        previewLabel.setVerticalAlignment(JLabel.CENTER);
        previewLabel.setBackground(Color.WHITE);
        previewLabel.setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(previewLabel);
        panel.add(scrollPane, BorderLayout.CENTER);

        // File info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        fileInfoLabel = new JLabel(" ");
        fileInfoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.add(fileInfoLabel);

        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Left side - file type combo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(new JLabel("Files of type:"));

        fileTypeCombo = new JComboBox<>();
        for (FileFilter filter : fileChooser.getChoosableFileFilters()) {
            fileTypeCombo.addItem(filter.getDescription());
        }
        fileTypeCombo.addActionListener(e -> {
            int index = fileTypeCombo.getSelectedIndex();
            if (index >= 0 && index < fileChooser.getChoosableFileFilters().length) {
                fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[index]);
            }
        });
        leftPanel.add(fileTypeCombo);

        panel.add(leftPanel, BorderLayout.WEST);

        // Right side - buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton openButton = new JButton("Open");
        openButton.addActionListener(e -> {
            selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null && selectedFile.exists()) {
                confirmed = true;
                dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(openButton);
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private void updatePreview(File file) {
        try {
            // Update file info
            StringBuilder info = new StringBuilder("<html>");
            info.append("<b>File:</b> ").append(file.getName()).append("<br>");
            info.append("<b>Size:</b> ").append(formatFileSize(file.length())).append("<br>");

            // Try to load and display image
            String extension = getFileExtension(file);
            if (isImageFile(extension)) {
                BufferedImage image = ImageIO.read(file);
                if (image != null) {
                    info.append("<b>Dimensions:</b> ").append(image.getWidth())
                            .append(" × ").append(image.getHeight()).append(" pixels<br>");
                    info.append("<b>Type:</b> ").append(getImageType(image)).append("<br>");

                    // Scale image for preview
                    Image scaledImage = scaleImage(image, 200, 200);
                    previewLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    previewLabel.setIcon(null);
                    previewLabel.setText("Cannot preview this file");
                }
            } else {
                previewLabel.setIcon(null);
                previewLabel.setText("No preview available");
            }

            info.append("</html>");
            fileInfoLabel.setText(info.toString());

        } catch (Exception e) {
            previewLabel.setIcon(null);
            previewLabel.setText("Error loading preview");
            fileInfoLabel.setText("<html><b>Error:</b> " + e.getMessage() + "</html>");
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return lastDot > 0 ? name.substring(lastDot + 1).toLowerCase() : "";
    }

    private boolean isImageFile(String extension) {
        return extension.equals("png") || extension.equals("jpg") ||
                extension.equals("jpeg") || extension.equals("gif") ||
                extension.equals("bmp") || extension.equals("tiff") ||
                extension.equals("tif");
    }

    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " bytes";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0);
        } else {
            return String.format("%.1f MB", size / (1024.0 * 1024.0));
        }
    }

    private String getImageType(BufferedImage image) {
        switch (image.getType()) {
            case BufferedImage.TYPE_INT_RGB:
                return "RGB (24-bit)";
            case BufferedImage.TYPE_INT_ARGB:
                return "ARGB (32-bit)";
            case BufferedImage.TYPE_BYTE_GRAY:
                return "Grayscale (8-bit)";
            default:
                return "Unknown";
        }
    }

    private Image scaleImage(BufferedImage image, int maxWidth, int maxHeight) {
        int width = image.getWidth();
        int height = image.getHeight();

        double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);

        if (scale < 1.0) {
            int newWidth = (int) (width * scale);
            int newHeight = (int) (height * scale);
            return image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        }

        return image;
    }

    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }

    public File getSelectedFile() {
        return selectedFile;
    }
}