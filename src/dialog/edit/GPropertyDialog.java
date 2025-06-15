package dialog.edit;

import shapes.GShape;

import javax.swing.*;
import java.awt.*;

public class GPropertyDialog extends JDialog {
    private JTextField xField;
    private JTextField yField;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField rotationField;
    private JSlider opacitySlider;
    private JCheckBox visibleCheck;
    private JCheckBox lockedCheck;
    private boolean confirmed = false;
    private GShape shape;

    public GPropertyDialog(Frame parent, GShape shape) {
        super(parent, "Shape Properties", true);
        this.shape = shape;
        initializeDialog();
    }

    private void initializeDialog() {
        setLayout(new BorderLayout());
        setSize(400, 500);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Position Section
        JLabel positionLabel = new JLabel("Position");
        positionLabel.setFont(positionLabel.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(positionLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("X:"), gbc);
        gbc.gridx = 1;
        xField = new JTextField(String.valueOf(shape.getSize().getX()), 10);
        mainPanel.add(xField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Y:"), gbc);
        gbc.gridx = 1;
        yField = new JTextField(String.valueOf(shape.getSize().getY()), 10);
        mainPanel.add(yField, gbc);

        // Size Section
        JLabel sizeLabel = new JLabel("Size");
        sizeLabel.setFont(sizeLabel.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(sizeLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Width:"), gbc);
        gbc.gridx = 1;
        widthField = new JTextField(String.valueOf(shape.getSize().getWidth()), 10);
        mainPanel.add(widthField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Height:"), gbc);
        gbc.gridx = 1;
        heightField = new JTextField(String.valueOf(shape.getSize().getHeight()), 10);
        mainPanel.add(heightField, gbc);

        // Transform Section
        JLabel transformLabel = new JLabel("Transform");
        transformLabel.setFont(transformLabel.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        mainPanel.add(transformLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(new JLabel("Rotation:"), gbc);
        gbc.gridx = 1;
        rotationField = new JTextField(String.valueOf(shape.getRotation()), 10);
        mainPanel.add(rotationField, gbc);

        // Appearance Section
        JLabel appearanceLabel = new JLabel("Appearance");
        appearanceLabel.setFont(appearanceLabel.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        mainPanel.add(appearanceLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 9;
        mainPanel.add(new JLabel("Opacity:"), gbc);
        gbc.gridx = 1;
        opacitySlider = new JSlider(0, 100, 100);
        opacitySlider.setMajorTickSpacing(25);
        opacitySlider.setPaintTicks(true);
        opacitySlider.setPaintLabels(true);
        mainPanel.add(opacitySlider, gbc);

        // Properties Section
        JLabel propertiesLabel = new JLabel("Properties");
        propertiesLabel.setFont(propertiesLabel.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 10;
        gbc.gridwidth = 2;
        mainPanel.add(propertiesLabel, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 11;
        visibleCheck = new JCheckBox("Visible", shape.isVisible());
        mainPanel.add(visibleCheck, gbc);

        gbc.gridy = 12;
        lockedCheck = new JCheckBox("Locked",null);
        mainPanel.add(lockedCheck, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            if (validateInput()) {
                applyChanges();
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
    }

    private boolean validateInput() {
        try {
            Integer.parseInt(xField.getText());
            Integer.parseInt(yField.getText());
            Integer.parseInt(widthField.getText());
            Integer.parseInt(heightField.getText());
            Double.parseDouble(rotationField.getText());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for all fields.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void applyChanges() {
//        shape.setLocation(Integer.parseInt(xField.getText()),
//                Integer.parseInt(yField.getText()));
//        shape.setSize(Integer.parseInt(widthField.getText()),
//                Integer.parseInt(heightField.getText()));
//        shape.setRotation(Double.parseDouble(rotationField.getText()));
//
//        // Set opacity
//        Color currentFill = shape.getFillColor();
//        if (currentFill != null) {
//            int alpha = (int) (opacitySlider.getValue() * 2.55);
//            shape.setFillColor(new Color(currentFill.getRed(),
//                    currentFill.getGreen(),
//                    currentFill.getBlue(),
//                    alpha));
//        }
//
//        shape.setVisible(visibleCheck.isSelected());
//        shape.setLocked(lockedCheck.isSelected());
    }

    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }
}