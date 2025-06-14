package menus.edit;

import frames.GMainFrame;
import frames.GMainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 간단한 색상 도구모음
 */
public class GSimpleColorToolBar extends JToolBar {
    private static final long serialVersionUID = 1L;

    private GMainFrame mainFrame;
    private Color currentFillColor = Color.BLACK;
    private Color currentStrokeColor = Color.BLACK;
    private int currentStrokeWidth = 1;
    private boolean fillEnabled = true;
    private boolean strokeEnabled = true;

    private JButton fillColorButton;
    private JButton strokeColorButton;
    private JButton colorDialogButton;

    public GSimpleColorToolBar(GMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeToolBar();
    }

    private void initializeToolBar() {
        setFloatable(false);
        setBorderPainted(true);
        setBackground(Color.LIGHT_GRAY);

        // 구분선
        addSeparator();

        // 채우기 색상 버튼
        fillColorButton = new JButton();
        fillColorButton.setPreferredSize(new Dimension(30, 30));
        fillColorButton.setBackground(currentFillColor);
        fillColorButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        fillColorButton.setToolTipText("채우기 색상 (좌클릭: 빠른 선택, 우클릭: 대화상자)");

        fillColorButton.addActionListener(e -> quickFillColorChange());
        fillColorButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showColorDialog();
                }
            }
        });

        add(fillColorButton);

        // 테두리 색상 버튼
        strokeColorButton = new JButton();
        strokeColorButton.setPreferredSize(new Dimension(30, 30));
        strokeColorButton.setBackground(currentStrokeColor);
        strokeColorButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        strokeColorButton.setToolTipText("테두리 색상 (좌클릭: 빠른 선택, 우클릭: 대화상자)");

        strokeColorButton.addActionListener(e -> quickStrokeColorChange());
        strokeColorButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showColorDialog();
                }
            }
        });

        add(strokeColorButton);

        // 색상 설정 대화상자 버튼
        colorDialogButton = new JButton("색상");
        colorDialogButton.setToolTipText("색상 및 속성 설정");
        colorDialogButton.addActionListener(e -> showColorDialog());

        add(colorDialogButton);

        // 구분선
        addSeparator();

        // 빠른 색상 버튼들
        addQuickColorButtons();
    }

    private void addQuickColorButtons() {
        Color[] quickColors = {
                Color.BLACK, Color.WHITE, Color.RED, Color.GREEN,
                Color.BLUE, Color.YELLOW, Color.ORANGE, Color.MAGENTA
        };

        for (Color color : quickColors) {
            JButton quickButton = new JButton();
            quickButton.setPreferredSize(new Dimension(20, 20));
            quickButton.setBackground(color);
            quickButton.setBorder(BorderFactory.createRaisedBevelBorder());
            quickButton.setToolTipText("빠른 색상: " + getColorName(color));

            quickButton.addActionListener(e -> {
                setFillColor(color);
                applyToSelectedShape();
            });

            quickButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        setStrokeColor(color);
                        applyToSelectedShape();
                    }
                }
            });

            add(quickButton);
        }
    }

    private String getColorName(Color color) {
        if (color.equals(Color.BLACK)) return "검정";
        if (color.equals(Color.WHITE)) return "흰색";
        if (color.equals(Color.RED)) return "빨강";
        if (color.equals(Color.GREEN)) return "초록";
        if (color.equals(Color.BLUE)) return "파랑";
        if (color.equals(Color.YELLOW)) return "노랑";
        if (color.equals(Color.ORANGE)) return "주황";
        if (color.equals(Color.MAGENTA)) return "자홍";
        return "색상";
    }

    private void quickFillColorChange() {
        Color newColor = JColorChooser.showDialog(this, "채우기 색상 선택", currentFillColor);
        if (newColor != null) {
            setFillColor(newColor);
            applyToSelectedShape();
        }
    }

    private void quickStrokeColorChange() {
        Color newColor = JColorChooser.showDialog(this, "테두리 색상 선택", currentStrokeColor);
        if (newColor != null) {
            setStrokeColor(newColor);
            applyToSelectedShape();
        }
    }

    private void showColorDialog() {
        GColorDialog2.ColorProperties current = new GColorDialog2.ColorProperties(
                currentFillColor, currentStrokeColor, currentStrokeWidth,
                fillEnabled, strokeEnabled
        );

        GColorDialog2.ColorProperties result = GColorDialog2.showColorDialog(mainFrame, current);

        if (result != null) {
            currentFillColor = result.fillColor;
            currentStrokeColor = result.strokeColor;
            currentStrokeWidth = result.strokeWidth;
            fillEnabled = result.fillEnabled;
            strokeEnabled = result.strokeEnabled;

            updateButtonColors();
            applyToSelectedShape();
        }
    }

    private void setFillColor(Color color) {
        currentFillColor = color;
        fillEnabled = true;
        updateButtonColors();
    }

    private void setStrokeColor(Color color) {
        currentStrokeColor = color;
        strokeEnabled = true;
        updateButtonColors();
    }

    private void updateButtonColors() {
        fillColorButton.setBackground(currentFillColor);
        strokeColorButton.setBackground(currentStrokeColor);
        repaint();
    }

    private void applyToSelectedShape() {
        if (mainFrame != null) {
            GMainPanel currentPanel = mainFrame.getCurrentPanel();
            if (currentPanel != null) {
                currentPanel.updateSelectedShapeColors(
                        currentFillColor, currentStrokeColor, currentStrokeWidth,
                        fillEnabled, strokeEnabled
                );
            }
        }
    }

    // 새 도형 생성 시 현재 색상 속성을 가져오는 메서드
    public GColorDialog2.ColorProperties getCurrentColorProperties() {
        return new GColorDialog2.ColorProperties(
                currentFillColor, currentStrokeColor, currentStrokeWidth,
                fillEnabled, strokeEnabled
        );
    }

    public void initialize() {
        currentFillColor = Color.BLACK;
        currentStrokeColor = Color.BLACK;
        currentStrokeWidth = 1;
        fillEnabled = true;
        strokeEnabled = true;
        updateButtonColors();
    }
}
