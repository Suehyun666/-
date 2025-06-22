package frames;

import controller.AppController;
import dialog.edit.GColorDialog;
import shapes.GShape;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GColorPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private Color fillColor = null;
    private Color strokeColor = Color.BLACK;
    private int strokeWidth = 1;
    private boolean fillEnabled = true;
    private boolean strokeEnabled = true;

    private JButton fillColorButton;
    private JButton strokeColorButton;
    private JSpinner strokeWidthSpinner;
    private JCheckBox fillCheckBox;
    private JCheckBox strokeCheckBox;
    private JLabel fillPreviewLabel;
    private JLabel strokePreviewLabel;

    private GMainPanel currentPanel;
    private AppController controller;

    // Default Paret
    private static final Color[] BASIC_COLORS = {
            Color.BLACK, Color.WHITE, Color.RED, Color.GREEN,
            Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.GRAY, Color.LIGHT_GRAY,
            Color.DARK_GRAY, new Color(128, 0, 128), // Purple
            new Color(165, 42, 42), new Color(0, 128, 0) // Brown, Dark Green
    };

    public GColorPanel() {
        this.currentPanel=null;
        initializePanel();
    }
    private void initializePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setPreferredSize(new Dimension(180, 400));
        setBackground(Color.GRAY);

        // Title
        JLabel titleLabel = new JLabel("Color Tool");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(10));

        // preview current color
        add(createPreviewSection());
        add(Box.createVerticalStrut(10));

        // fill color
        add(createFillSection());
        add(Box.createVerticalStrut(8));

        // stroke color
        add(createStrokeSection());
        add(Box.createVerticalStrut(10));

        //default parte
        add(createColorPalette());
        add(Box.createVerticalStrut(10));

        add(createQuickActionButtons());

        add(Box.createVerticalGlue());
    }

    private JPanel createPreviewSection() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Current Color"));
        panel.setLayout(new GridLayout(2, 2, 5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // preview fill color
        panel.add(new JLabel("Fill:"));
        fillPreviewLabel = new JLabel("■");
        fillPreviewLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        fillPreviewLabel.setForeground(fillColor);
        fillPreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(fillPreviewLabel);

        //preview stroke
        panel.add(new JLabel("Stroke:"));
        strokePreviewLabel = new JLabel("□");
        strokePreviewLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        strokePreviewLabel.setForeground(strokeColor);
        strokePreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(strokePreviewLabel);

        return panel;
    }

    private JPanel createFillSection() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Fill"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        //check box
        fillCheckBox = new JCheckBox("Use Fill", fillEnabled);
        fillCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        fillCheckBox.addActionListener(e -> {
            fillEnabled = fillCheckBox.isSelected();
            fillColorButton.setEnabled(fillEnabled);
            updatePreview();
            applyToSelectedShape();
        });
        panel.add(fillCheckBox);

        // Color Select bt
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        fillColorButton = new JButton("Color Select");
        fillColorButton.addActionListener(e -> chooseFillColor());
        buttonPanel.add(fillColorButton);
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createStrokeSection() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Stroke"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // checkbox
        strokeCheckBox = new JCheckBox("Use Stroke", strokeEnabled);
        strokeCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        strokeCheckBox.addActionListener(e -> {
            strokeEnabled = strokeCheckBox.isSelected();
            strokeColorButton.setEnabled(strokeEnabled);
            strokeWidthSpinner.setEnabled(strokeEnabled);
            updatePreview();
            applyToSelectedShape();
        });
        panel.add(strokeCheckBox);

        // fill select bt
        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        strokeColorButton = new JButton("Select Color");
        strokeColorButton.addActionListener(e -> chooseStrokeColor());
        colorPanel.add(strokeColorButton);
        panel.add(colorPanel);

        // Width
        JPanel widthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        widthPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        widthPanel.add(new JLabel("Width:"));
        strokeWidthSpinner = new JSpinner(new SpinnerNumberModel(strokeWidth, 1, 50, 1));
        strokeWidthSpinner.setPreferredSize(new Dimension(60, 25));
        strokeWidthSpinner.addChangeListener(e -> {
            strokeWidth = (Integer) strokeWidthSpinner.getValue();
            applyToSelectedShape();
        });
        widthPanel.add(strokeWidthSpinner);
        panel.add(widthPanel);

        return panel;
    }

    private JPanel createColorPalette() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Default ColorPalette"));
        panel.setLayout(new GridLayout(4, 4, 2, 2));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        for (Color color : BASIC_COLORS) {
            JButton colorButton = new JButton();
            colorButton.setPreferredSize(new Dimension(25, 25));
            colorButton.setBackground(color);
            colorButton.setBorder(BorderFactory.createRaisedBevelBorder());
            colorButton.setToolTipText("<html>LClick: Fill<br>RClick: Stroke</html>");

            //left click
            colorButton.addActionListener(e -> {
                setFillColor(color);
                applyToSelectedShape();
            });

            // right click
            colorButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        setStrokeColor(color);
                        applyToSelectedShape();
                    }
                }
            });

            panel.add(colorButton);
        }

        return panel;
    }

    private JPanel createQuickActionButtons() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Quick"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // shift color
        JButton swapButton = new JButton("Shift");
        swapButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        swapButton.addActionListener(e -> swapColors());
        panel.add(swapButton);

        panel.add(Box.createVerticalStrut(5));

        // Default
        JButton resetButton = new JButton("Default");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> resetToDefaults());
        panel.add(resetButton);

        return panel;
    }

    private void chooseFillColor() {
        Color newColor = JColorChooser.showDialog(this, "Select Fill Color", fillColor);
        if (newColor != null) {
            setFillColor(newColor);
            applyToSelectedShape();
        }
    }

    private void chooseStrokeColor() {
        Color newColor = JColorChooser.showDialog(this, "Select Stroke Color", strokeColor);
        if (newColor != null) {
            setStrokeColor(newColor);
            applyToSelectedShape();
        }
    }

    private void setFillColor(Color color) {
        this.fillColor = color;
        if (!fillEnabled) {
            fillEnabled = true;
            fillCheckBox.setSelected(true);
            fillColorButton.setEnabled(true);
        }
        updatePreview();
    }

    private void setStrokeColor(Color color) {
        this.strokeColor = color;
        if (!strokeEnabled) {
            strokeEnabled = true;
            strokeCheckBox.setSelected(true);
            strokeColorButton.setEnabled(true);
            strokeWidthSpinner.setEnabled(true);
        }
        updatePreview();
    }

    private void swapColors() {
        Color temp = fillColor;
        setFillColor(strokeColor);
        setStrokeColor(temp);
        applyToSelectedShape();
    }

    // 새 도형 생성 시 현재 색상 속성
    public GColorDialog.ColorProperties getCurrentColorProperties() {
        return new GColorDialog.ColorProperties(
                fillColor, strokeColor, strokeWidth,
                fillEnabled, strokeEnabled
        );
    }

    // 선택된 도형의 색상을 패널에 반영
    public void updateFromShape(GShape shape) {
        if (shape != null) {
            fillColor = shape.getFillColor();
            strokeColor = shape.getStrokeColor();
            strokeWidth = shape.getStrokeWidth();
            fillEnabled = shape.isFillEnabled();
            strokeEnabled = shape.isStrokeEnabled();

            fillCheckBox.setSelected(fillEnabled);
            strokeCheckBox.setSelected(strokeEnabled);
            fillColorButton.setEnabled(fillEnabled);
            strokeColorButton.setEnabled(strokeEnabled);
            strokeWidthSpinner.setValue(strokeWidth);
            strokeWidthSpinner.setEnabled(strokeEnabled);

            updatePreview();
        }
    }

    // Getter
    public Color getFillColor() { return fillColor; }
    public Color getStrokeColor() { return strokeColor; }
    public int getStrokeWidth() { return strokeWidth; }
    public boolean isFillEnabled() { return fillEnabled; }
    public boolean isStrokeEnabled() { return strokeEnabled; }

    //initialize
    public void initialize() {
        resetToDefaults();
    }
    private void resetToDefaults() {
        fillColor = Color.WHITE;
        strokeColor = Color.BLACK;
        strokeWidth = 1;
        fillEnabled = true;
        strokeEnabled = true;

        fillCheckBox.setSelected(fillEnabled);
        strokeCheckBox.setSelected(strokeEnabled);
        fillColorButton.setEnabled(fillEnabled);
        strokeColorButton.setEnabled(strokeEnabled);
        strokeWidthSpinner.setValue(strokeWidth);
        strokeWidthSpinner.setEnabled(strokeEnabled);

        updatePreview();
        applyToSelectedShape();
    }
    private void updatePreview() {
        fillPreviewLabel.setForeground(fillEnabled ? fillColor : Color.LIGHT_GRAY);
        strokePreviewLabel.setForeground(strokeEnabled ? strokeColor : Color.LIGHT_GRAY);
        repaint();
    }
    private void applyToSelectedShape() {
        if (currentPanel != null) {
            currentPanel.updateSelectedShapeColors(
                    fillColor, strokeColor, strokeWidth,
                    fillEnabled, strokeEnabled
            );
        }
    }
    public void associate(GMainPanel panel) {
        this.currentPanel=panel;
    }

    public void setController(AppController appController) {
        this.controller =appController;
    }
}
