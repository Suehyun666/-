package menus.file;

import javax.swing.*;
import java.awt.*;

public class GNewFileDialog extends JDialog {
    private JTextField nameField;
    private JTextField widthField;
    private JTextField heightField;
    private JComboBox<String> colorModeCombo;
    private JComboBox<String> bitDepthCombo;
    private JComboBox<String> backgroundCombo;
    private boolean confirmed = false;

    public GNewFileDialog(Frame parent) {
        super(parent, "New", true);
        initializeDialog();
    }

    private void initializeDialog() {
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(getParent());

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField("Untitled-1", 20);
        mainPanel.add(nameField, gbc);

        // Width field
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Width:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        widthField = new JTextField("800", 10);
        mainPanel.add(widthField, gbc);
        gbc.gridx = 2;
        mainPanel.add(new JLabel("pixels"), gbc);

        // Height field
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Height:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        heightField = new JTextField("600", 10);
        mainPanel.add(heightField, gbc);
        gbc.gridx = 2;
        mainPanel.add(new JLabel("pixels"), gbc);

        // Color Mode
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Color Mode:"), gbc);
        gbc.gridx = 1;
        colorModeCombo = new JComboBox<>(new String[]{"RGB Color", "CMYK Color", "Grayscale", "Bitmap"});
        mainPanel.add(colorModeCombo, gbc);

        // Bit Depth
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Bit Depth:"), gbc);
        gbc.gridx = 1;
        bitDepthCombo = new JComboBox<>(new String[]{"8 bit", "16 bit", "32 bit"});
        mainPanel.add(bitDepthCombo, gbc);

        // Background Contents
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Background:"), gbc);
        gbc.gridx = 1;
        backgroundCombo = new JComboBox<>(new String[]{"White", "Background Color", "Transparent"});
        mainPanel.add(backgroundCombo, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
    }

    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }

    public String getFileName() { return nameField.getText(); }
    public int getImageWidth() { return Integer.parseInt(widthField.getText()); }
    public int getImageHeight() { return Integer.parseInt(heightField.getText()); }
    public String getColorMode() { return (String) colorModeCombo.getSelectedItem(); }
    public String getBitDepth() { return (String) bitDepthCombo.getSelectedItem(); }
    public String getBackgroundContent() {
        return (String) backgroundCombo.getSelectedItem();
    }
}