package dialog.edit;

import global.ColorData;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GColorDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private Color fillColor = Color.BLACK;
    private Color strokeColor = Color.BLACK;
    private int strokeWidth = 1;
    private boolean fillEnabled = true;
    private boolean strokeEnabled = true;
    private boolean cancelled = false;

    private JButton fillColorButton;
    private JButton strokeColorButton;
    private JSpinner strokeWidthSpinner;
    private JCheckBox fillCheckBox;
    private JCheckBox strokeCheckBox;

    // 기본 색상 팔레트
    private static final Color[] BASIC_COLORS = {
            Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE,
            Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK,
            Color.GRAY, Color.LIGHT_GRAY, Color.DARK_GRAY, new Color(128, 0, 128),
            new Color(165, 42, 42), new Color(0, 128, 0)
    };

    public GColorDialog(Frame parent) {
        super(parent, "색상 및 속성 설정", true);
        initializeDialog();
    }

    private void initializeDialog() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(createFillSection());
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(createStrokeSection());
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(createColorPalette());

        add(mainPanel, BorderLayout.CENTER);

        add(createButtonPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    private JPanel createFillSection() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("채우기"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fillCheckBox = new JCheckBox("채우기 사용", fillEnabled);
        fillCheckBox.addActionListener(e -> {
            fillEnabled = fillCheckBox.isSelected();
            fillColorButton.setEnabled(fillEnabled);
        });
        checkPanel.add(fillCheckBox);

        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colorPanel.add(new JLabel("색상: "));

        fillColorButton = new JButton("     ");
        fillColorButton.setPreferredSize(new Dimension(60, 30));
        fillColorButton.setBackground(fillColor);
        fillColorButton.setBorder(BorderFactory.createRaisedBevelBorder());
        fillColorButton.addActionListener(e -> chooseFillColor());
        colorPanel.add(fillColorButton);

        JButton fillPaletteButton = new JButton("팔레트");
        fillPaletteButton.addActionListener(e -> showColorPalette(true));
        colorPanel.add(fillPaletteButton);

        panel.add(checkPanel);
        panel.add(colorPanel);

        return panel;
    }

    private JPanel createStrokeSection() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("테두리"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        strokeCheckBox = new JCheckBox("테두리 사용", strokeEnabled);
        strokeCheckBox.addActionListener(e -> {
            strokeEnabled = strokeCheckBox.isSelected();
            strokeColorButton.setEnabled(strokeEnabled);
            strokeWidthSpinner.setEnabled(strokeEnabled);
        });
        checkPanel.add(strokeCheckBox);

        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colorPanel.add(new JLabel("색상: "));

        strokeColorButton = new JButton("     ");
        strokeColorButton.setPreferredSize(new Dimension(60, 30));
        strokeColorButton.setBackground(strokeColor);
        strokeColorButton.setBorder(BorderFactory.createRaisedBevelBorder());
        strokeColorButton.addActionListener(e -> chooseStrokeColor());
        colorPanel.add(strokeColorButton);

        JButton strokePaletteButton = new JButton("팔레트");
        strokePaletteButton.addActionListener(e -> showColorPalette(false));
        colorPanel.add(strokePaletteButton);

        colorPanel.add(Box.createHorizontalStrut(10));
        colorPanel.add(new JLabel("두께: "));

        strokeWidthSpinner = new JSpinner(new SpinnerNumberModel(strokeWidth, 1, 50, 1));
        strokeWidthSpinner.setPreferredSize(new Dimension(60, 25));
        strokeWidthSpinner.addChangeListener(e -> {
            strokeWidth = (Integer) strokeWidthSpinner.getValue();
        });
        colorPanel.add(strokeWidthSpinner);

        panel.add(checkPanel);
        panel.add(colorPanel);

        return panel;
    }

    private JPanel createColorPalette() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("빠른 색상 선택"));
        panel.setLayout(new GridLayout(2, 8, 3, 3));

        for (Color color : BASIC_COLORS) {
            JButton colorButton = new JButton();
            colorButton.setPreferredSize(new Dimension(25, 25));
            colorButton.setBackground(color);
            colorButton.setBorder(BorderFactory.createRaisedBevelBorder());
            colorButton.setToolTipText("좌클릭: 채우기, 우클릭: 테두리");

            colorButton.addActionListener(e -> setFillColor(color));

            colorButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        setStrokeColor(color);
                    }
                }
            });

            panel.add(colorButton);
        }

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton okButton = new JButton("확인");
        okButton.addActionListener(e -> {
            cancelled = false;
            dispose();
        });

        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(e -> {
            cancelled = true;
            dispose();
        });

        JButton resetButton = new JButton("기본값");
        resetButton.addActionListener(e -> resetToDefaults());

        panel.add(okButton);
        panel.add(cancelButton);
        panel.add(resetButton);

        // enter
        getRootPane().setDefaultButton(okButton);

        return panel;
    }

    private void chooseFillColor() {
        Color newColor = JColorChooser.showDialog(this, "채우기 색상 선택", fillColor);
        if (newColor != null) {
            setFillColor(newColor);
        }
    }

    private void chooseStrokeColor() {
        Color newColor = JColorChooser.showDialog(this, "테두리 색상 선택", strokeColor);
        if (newColor != null) {
            setStrokeColor(newColor);
        }
    }

    private void showColorPalette(boolean forFill) {
        String title = forFill ? "채우기 색상 선택" : "테두리 색상 선택";
        JDialog paletteDialog = new JDialog(this, title, true);
        paletteDialog.setLayout(new BorderLayout());

        JPanel palettePanel = new JPanel(new GridLayout(4, 4, 5, 5));
        palettePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Color color : BASIC_COLORS) {
            JButton colorButton = new JButton();
            colorButton.setPreferredSize(new Dimension(40, 40));
            colorButton.setBackground(color);
            colorButton.setBorder(BorderFactory.createRaisedBevelBorder());
            colorButton.addActionListener(e -> {
                if (forFill) {
                    setFillColor(color);
                } else {
                    setStrokeColor(color);
                }
                paletteDialog.dispose();
            });
            palettePanel.add(colorButton);
        }

        paletteDialog.add(palettePanel, BorderLayout.CENTER);
        paletteDialog.pack();
        paletteDialog.setLocationRelativeTo(this);
        paletteDialog.setVisible(true);
    }

    private void setFillColor(Color color) {
        this.fillColor = color;
        fillColorButton.setBackground(color);
        if (!fillEnabled) {
            fillEnabled = true;
            fillCheckBox.setSelected(true);
            fillColorButton.setEnabled(true);
        }
    }

    private void setStrokeColor(Color color) {
        this.strokeColor = color;
        strokeColorButton.setBackground(color);
        if (!strokeEnabled) {
            strokeEnabled = true;
            strokeCheckBox.setSelected(true);
            strokeColorButton.setEnabled(true);
            strokeWidthSpinner.setEnabled(true);
        }
    }

    private void resetToDefaults() {
        fillColor = Color.BLACK;
        strokeColor = Color.BLACK;
        strokeWidth = 1;
        fillEnabled = true;
        strokeEnabled = true;

        fillColorButton.setBackground(fillColor);
        strokeColorButton.setBackground(strokeColor);
        strokeWidthSpinner.setValue(strokeWidth);
        fillCheckBox.setSelected(fillEnabled);
        strokeCheckBox.setSelected(strokeEnabled);
        fillColorButton.setEnabled(fillEnabled);
        strokeColorButton.setEnabled(strokeEnabled);
        strokeWidthSpinner.setEnabled(strokeEnabled);
    }

    public void setInitialData(ColorData current) {
        // 구현 X
        if (current != null) {
            this.fillColor = current.fillColor;
            this.strokeColor = current.strokeColor;
            this.strokeWidth = current.strokeWidth;
            this.fillEnabled = current.fillEnabled;
            this.strokeEnabled = current.strokeEnabled;

            // UI 업데이트
            fillColorButton.setBackground(fillColor);
            strokeColorButton.setBackground(strokeColor);
            strokeWidthSpinner.setValue(strokeWidth);
            fillCheckBox.setSelected(fillEnabled);
            strokeCheckBox.setSelected(strokeEnabled);
        }
    }

    public ColorData getColorData() {
        // 구현 X
        return new ColorData(fillColor, strokeColor, strokeWidth, fillEnabled, strokeEnabled);
    }



    // static method
    public static ColorProperties showColorDialog(Frame parent, ColorProperties current) {
        GColorDialog dialog = new GColorDialog(parent);

        if (current != null) {
            dialog.fillColor = current.fillColor;
            dialog.strokeColor = current.strokeColor;
            dialog.strokeWidth = current.strokeWidth;
            dialog.fillEnabled = current.fillEnabled;
            dialog.strokeEnabled = current.strokeEnabled;

            dialog.fillColorButton.setBackground(dialog.fillColor);
            dialog.strokeColorButton.setBackground(dialog.strokeColor);
            dialog.strokeWidthSpinner.setValue(dialog.strokeWidth);
            dialog.fillCheckBox.setSelected(dialog.fillEnabled);
            dialog.strokeCheckBox.setSelected(dialog.strokeEnabled);
            dialog.fillColorButton.setEnabled(dialog.fillEnabled);
            dialog.strokeColorButton.setEnabled(dialog.strokeEnabled);
            dialog.strokeWidthSpinner.setEnabled(dialog.strokeEnabled);
        }

        dialog.setVisible(true);

        if (dialog.cancelled) {
            return null;
        }

        return new ColorProperties(
                dialog.fillColor,
                dialog.strokeColor,
                dialog.strokeWidth,
                dialog.fillEnabled,
                dialog.strokeEnabled
        );
    }

    public boolean showDialog() {
        return true;
    }

    // properties
    public static class ColorProperties {
        public final Color fillColor;
        public final Color strokeColor;
        public final int strokeWidth;
        public final boolean fillEnabled;
        public final boolean strokeEnabled;

        public ColorProperties(Color fillColor, Color strokeColor, int strokeWidth,
                               boolean fillEnabled, boolean strokeEnabled) {
            this.fillColor = fillColor;
            this.strokeColor = strokeColor;
            this.strokeWidth = strokeWidth;
            this.fillEnabled = fillEnabled;
            this.strokeEnabled = strokeEnabled;
        }
    }
}
