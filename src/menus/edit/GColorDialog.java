package menus.edit;

import frames.GMainPanel;

import javax.swing.*;
import java.awt.*;

public class GColorDialog extends JDialog {
    private JButton fillColorButton;
    private JButton strokeColorButton;
    private JButton bgColorButton;
    private JSpinner strokeWidthSpinner;
    private JComboBox<String> strokeStyleCombo;
    private Color selectedFillColor;
    private Color selectedStrokeColor;
    private Color selectedBgColor;
    private boolean confirmed = false;
    private GMainPanel mainPanel;

    public GColorDialog(Frame parent, GMainPanel mainPanel) {
        super(parent, "Color Settings", true);
        this.mainPanel = mainPanel;
        initializeDialog();
    }

    private void initializeDialog() {
        setLayout(new BorderLayout());
        setSize(400, 350);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Fill Color
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Fill Color:"), gbc);
        gbc.gridx = 1;
        fillColorButton = new JButton("      ");
        fillColorButton.setOpaque(true);
        fillColorButton.setBorderPainted(false);
        selectedFillColor = Color.WHITE;
        fillColorButton.setBackground(selectedFillColor);
        fillColorButton.addActionListener(e -> chooseFillColor());
        mainPanel.add(fillColorButton, gbc);

        // Stroke Color
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Stroke Color:"), gbc);
        gbc.gridx = 1;
        strokeColorButton = new JButton("      ");
        strokeColorButton.setOpaque(true);
        strokeColorButton.setBorderPainted(false);
        selectedStrokeColor = Color.BLACK;
        strokeColorButton.setBackground(selectedStrokeColor);
        strokeColorButton.addActionListener(e -> chooseStrokeColor());
        mainPanel.add(strokeColorButton, gbc);

        // Background Color
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Background Color:"), gbc);
        gbc.gridx = 1;
        bgColorButton = new JButton("      ");
        bgColorButton.setOpaque(true);
        bgColorButton.setBorderPainted(false);
        selectedBgColor = Color.WHITE;
        bgColorButton.setBackground(selectedBgColor);
        bgColorButton.addActionListener(e -> chooseBgColor());
        mainPanel.add(bgColorButton, gbc);

        // Stroke Width
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Stroke Width:"), gbc);
        gbc.gridx = 1;
        SpinnerModel model = new SpinnerNumberModel(1, 1, 20, 0.5);
        strokeWidthSpinner = new JSpinner(model);
        mainPanel.add(strokeWidthSpinner, gbc);

        // Stroke Style
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Stroke Style:"), gbc);
        gbc.gridx = 1;
        String[] styles = {"Solid", "Dashed", "Dotted", "Dash-Dot"};
        strokeStyleCombo = new JComboBox<>(styles);
        mainPanel.add(strokeStyleCombo, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            confirmed = true;
            applySettings();
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
    }

    private void chooseFillColor() {
        Color color = JColorChooser.showDialog(this, "Choose Fill Color", selectedFillColor);
        if (color != null) {
            selectedFillColor = color;
            fillColorButton.setBackground(color);
        }
    }

    private void chooseStrokeColor() {
        Color color = JColorChooser.showDialog(this, "Choose Stroke Color", selectedStrokeColor);
        if (color != null) {
            selectedStrokeColor = color;
            strokeColorButton.setBackground(color);
        }
    }

    private void chooseBgColor() {
        Color color = JColorChooser.showDialog(this, "Choose Background Color", selectedBgColor);
        if (color != null) {
            selectedBgColor = color;
            bgColorButton.setBackground(color);
        }
    }

    private void applySettings() {
//        if (mainPanel != null) {
//            mainPanel.setDefaultFillColor(selectedFillColor);
//            mainPanel.setDefaultStrokeColor(selectedStrokeColor);
//            mainPanel.setCanvasBackground(selectedBgColor);
//
//            float strokeWidth = ((Number) strokeWidthSpinner.getValue()).floatValue();
//            mainPanel.setDefaultStrokeWidth(strokeWidth);
//
//            String strokeStyle = (String) strokeStyleCombo.getSelectedItem();
//            mainPanel.setDefaultStrokeStyle(strokeStyle);
//        }
    }

    public boolean showDialog() {
//         Initialize with current settings
//        if (mainPanel != null) {
//            selectedFillColor = mainPanel.getDefaultFillColor();
//            selectedStrokeColor = mainPanel.getDefaultStrokeColor();
//            selectedBgColor = mainPanel.getCanvasBackground();
//            fillColorButton.setBackground(selectedFillColor);
//            strokeColorButton.setBackground(selectedStrokeColor);
//            bgColorButton.setBackground(selectedBgColor);
//            strokeWidthSpinner.setValue(mainPanel.getDefaultStrokeWidth());
//        }

        setVisible(true);
        return confirmed;
    }
}