package menus.file;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GSaveFileDialog extends JDialog {
    private JTextField fileNameField;
    private JComboBox<FileFormat> formatCombo;
    private JButton saveButton;
    private JButton cancelButton;
    private JFileChooser fileChooser;
    private boolean confirmed = false;
    private File selectedFile;

    // 파일 포맷 enum
    public enum FileFormat {
        PSD("Photoshop (*.PSD;*.PDD)", "psd", "Photoshop"),
        BMP("BMP (*.BMP;*.RLE;*.DIB)", "bmp", "BMP"),
        JPG("JPEG (*.JPG;*.JPEG;*.JPE)", "jpg", "JPEG"),
        PNG("PNG (*.PNG)", "png", "PNG"),
        GIF("CompuServe GIF (*.GIF)", "gif", "GIF"),
        TIFF("TIFF (*.TIF;*.TIFF)", "tiff", "TIFF"),
        PDF("Photoshop PDF (*.PDF;*.PDP)", "pdf", "PDF");

        private String description;
        private String extension;
        private String formatName;

        FileFormat(String description, String extension, String formatName) {
            this.description = description;
            this.extension = extension;
            this.formatName = formatName;
        }

        public String getDescription() { return description; }
        public String getExtension() { return extension; }
        public String getFormatName() { return formatName; }

        @Override
        public String toString() { return description; }
    }

    public GSaveFileDialog(Frame parent, String defaultFileName) {
        super(parent, "Save As", true);
        initializeDialog(defaultFileName);
    }

    private void initializeDialog(String defaultFileName) {
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(getParent());

        // Custom file chooser panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // File chooser
        fileChooser = new JFileChooser();
        fileChooser.setControlButtonsAreShown(false);
        mainPanel.add(fileChooser, BorderLayout.CENTER);

        // Bottom panel with file name and format
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // File name
        gbc.gridx = 0; gbc.gridy = 0;
        bottomPanel.add(new JLabel("File name:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        fileNameField = new JTextField(defaultFileName, 40);
        bottomPanel.add(fileNameField, gbc);

        // Format
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        bottomPanel.add(new JLabel("Format:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        formatCombo = new JComboBox<>(FileFormat.values());
        formatCombo.setSelectedItem(FileFormat.PSD);
        bottomPanel.add(formatCombo, gbc);

        // Buttons
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        saveButton = new JButton("Save");
        bottomPanel.add(saveButton, gbc);

        gbc.gridy = 1;
        cancelButton = new JButton("Cancel");
        bottomPanel.add(cancelButton, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add format-specific options panel
        JPanel optionsPanel = createFormatOptionsPanel();
        mainPanel.add(optionsPanel, BorderLayout.EAST);

        add(mainPanel);

        // Event handlers
        formatCombo.addActionListener(e -> updateFileExtension());

        saveButton.addActionListener(e -> {
            confirmed = true;
            File currentDir = fileChooser.getCurrentDirectory();
            String fileName = fileNameField.getText();
            FileFormat format = (FileFormat) formatCombo.getSelectedItem();

            // Add extension if not present
            if (!fileName.toLowerCase().endsWith("." + format.getExtension())) {
                fileName += "." + format.getExtension();
            }

            selectedFile = new File(currentDir, fileName);
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        // File selection listener
        fileChooser.addPropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, evt -> {
            File file = fileChooser.getSelectedFile();
            if (file != null && file.isFile()) {
                fileNameField.setText(file.getName());
            }
        });
    }

    private JPanel createFormatOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Save Options"));
        panel.setPreferredSize(new Dimension(250, 400));

        // Format-specific options (예시)
        JCheckBox embedColorProfile = new JCheckBox("Embed Color Profile", true);
        JCheckBox maximizeCompatibility = new JCheckBox("Maximize Compatibility", true);
        JCheckBox asACopy = new JCheckBox("As a Copy", false);
        JCheckBox layers = new JCheckBox("Layers", true);

        panel.add(embedColorProfile);
        panel.add(maximizeCompatibility);
        panel.add(asACopy);
        panel.add(layers);

        // Quality options for JPEG
        JPanel jpegOptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jpegOptions.setBorder(BorderFactory.createTitledBorder("JPEG Options"));
        jpegOptions.add(new JLabel("Quality:"));
        JSlider qualitySlider = new JSlider(1, 12, 10);
        qualitySlider.setMajorTickSpacing(1);
        qualitySlider.setPaintTicks(true);
        qualitySlider.setPaintLabels(true);
        jpegOptions.add(qualitySlider);

        panel.add(jpegOptions);

        // Update visibility based on format
        formatCombo.addActionListener(e -> {
            FileFormat format = (FileFormat) formatCombo.getSelectedItem();
            jpegOptions.setVisible(format == FileFormat.JPG);
            layers.setEnabled(format == FileFormat.PSD || format == FileFormat.TIFF);
        });

        return panel;
    }

    private void updateFileExtension() {
        String fileName = fileNameField.getText();
        FileFormat format = (FileFormat) formatCombo.getSelectedItem();

        // Remove old extension
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            fileName = fileName.substring(0, dotIndex);
        }

        // Add new extension
        fileName += "." + format.getExtension();
        fileNameField.setText(fileName);
    }

    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }

    public File getSelectedFile() { return selectedFile; }
    public FileFormat getSelectedFormat() { return (FileFormat) formatCombo.getSelectedItem(); }
}