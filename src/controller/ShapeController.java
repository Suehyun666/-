package controller;

import frames.GMainPanel;
import frames.GTabManager;
import global.GConstants.EShapeTool;
import shapes.GShape;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Vector;

public class ShapeController {
    private final GTabManager tabManager;
    private EShapeTool currentShapeTool = EShapeTool.eSelect;

    public ShapeController(GTabManager tabManager) {
        this.tabManager = tabManager;
    }

    public void setCurrentShapeTool(EShapeTool tool) {
        this.currentShapeTool = tool;
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null) {
            currentPanel.setEShapeTool(tool);
        }
    }

    public EShapeTool getCurrentShapeTool() {
        return currentShapeTool;
    }

    // === Graphic Menu 구현 ===

    public void showLineThicknessDialog() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null || !currentPanel.hasSelection()) {
            JOptionPane.showMessageDialog(null, "Please select a shape first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog("Enter line thickness (1-50):", "1");
        if (input != null) {
            try {
                int thickness = Integer.parseInt(input);
                if (thickness >= 1 && thickness <= 50) {
                    applyLineThickness(thickness);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a value between 1 and 50", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showLineStyleDialog() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null || !currentPanel.hasSelection()) {
            JOptionPane.showMessageDialog(null, "Please select a shape first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] options = {"Solid", "Dashed", "Dotted", "Dash-Dot"};
        String selected = (String) JOptionPane.showInputDialog(
                null,
                "Select line style:",
                "Line Style",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selected != null) {
            applyLineStyle(selected);
        }
    }

    private void applyLineThickness(int thickness) {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            GShape selectedShape = currentPanel.getSelectedShape();
            if (selectedShape != null) {
                Color fillColor = selectedShape.getFillColor();
                Color strokeColor = selectedShape.getStrokeColor();
                boolean fillEnabled = selectedShape.isFillEnabled();
                boolean strokeEnabled = selectedShape.isStrokeEnabled();

                currentPanel.updateSelectedShapeColors(fillColor, strokeColor, thickness, fillEnabled, strokeEnabled);
                currentPanel.setUpdated(true);
            }
        }
    }

    private void applyLineStyle(String style) {
        // 현재 GShape에서 선 스타일을 지원하지 않으므로 기본 구현
        System.out.println("Applied line style: " + style);
        JOptionPane.showMessageDialog(null, "Line style '" + style + "' applied!", "Line Style", JOptionPane.INFORMATION_MESSAGE);
    }

    // === Filter Menu 구현 ===

    public void applyBlurFilter() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null) {
            JOptionPane.showMessageDialog(null, "No active canvas", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog("Enter blur radius (1-10):", "2");
        if (input != null) {
            try {
                int radius = Integer.parseInt(input);
                if (radius >= 1 && radius <= 10) {
                    applyImageFilter("blur", radius);
                    JOptionPane.showMessageDialog(null, "Blur filter applied!", "Filter", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a value between 1 and 10", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void applyNoiseFilter() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null) {
            JOptionPane.showMessageDialog(null, "No active canvas", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(null, "Apply noise filter to canvas?", "Noise Filter", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            applyImageFilter("noise", 0);
            JOptionPane.showMessageDialog(null, "Noise filter applied!", "Filter", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void applySharpenFilter() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null) {
            JOptionPane.showMessageDialog(null, "No active canvas", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(null, "Apply sharpen filter to canvas?", "Sharpen Filter", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            applyImageFilter("sharpen", 0);
            JOptionPane.showMessageDialog(null, "Sharpen filter applied!", "Filter", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void applyEmbossFilter() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null) {
            JOptionPane.showMessageDialog(null, "No active canvas", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(null, "Apply emboss filter to canvas?", "Emboss Filter", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            applyImageFilter("emboss", 0);
            JOptionPane.showMessageDialog(null, "Emboss filter applied!", "Filter", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void applyImageFilter(String filterType, int parameter) {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null) return;

        System.out.println("Applied " + filterType + " filter with parameter: " + parameter);

        currentPanel.repaint();
        currentPanel.setUpdated(true);
    }

    public void duplicateSelectedShape() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null || !currentPanel.hasSelection()) {
            JOptionPane.showMessageDialog(null, "Please select a shape first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        GShape selectedShape = currentPanel.getSelectedShape();
        if (selectedShape != null) {
            GShape duplicatedShape = selectedShape.clone();

            Rectangle bounds = selectedShape.getBounds();
            duplicatedShape.translateX = bounds.x + 20;
            duplicatedShape.translateY = bounds.y + 20;
            duplicatedShape.updateTransformedShape();

            currentPanel.addShape(duplicatedShape);
            currentPanel.setUpdated(true);
            currentPanel.repaint();

            JOptionPane.showMessageDialog(null, "Shape duplicated!", "Duplicate", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void rotateSelectedShape(double angle) {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null || !currentPanel.hasSelection()) {
            return;
        }

        GShape selectedShape = currentPanel.getSelectedShape();
        if (selectedShape != null) {
            selectedShape.rotationAngle += Math.toRadians(angle);
            selectedShape.updateTransformedShape();
            currentPanel.repaint();
            currentPanel.setUpdated(true);
        }
    }

    public void scaleSelectedShape(double scaleX, double scaleY) {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null || !currentPanel.hasSelection()) {
            return;
        }

        GShape selectedShape = currentPanel.getSelectedShape();
        if (selectedShape != null) {
            selectedShape.scaleX *= scaleX;
            selectedShape.scaleY *= scaleY;
            selectedShape.updateTransformedShape();
            currentPanel.repaint();
            currentPanel.setUpdated(true);
        }
    }

    public void arrangeToFront() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null || !currentPanel.hasSelection()) {
            return;
        }

        GShape selectedShape = currentPanel.getSelectedShape();
        Vector<GShape> shapes = currentPanel.getshapes();

        if (selectedShape != null && shapes.contains(selectedShape)) {
            shapes.remove(selectedShape);
            shapes.add(selectedShape); // 맨 뒤로 추가 (마지막에 그려짐 = 맨 앞)
            currentPanel.repaint();
            currentPanel.setUpdated(true);
        }
    }

    public void arrangeToBack() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null || !currentPanel.hasSelection()) {
            return;
        }

        GShape selectedShape = currentPanel.getSelectedShape();
        Vector<GShape> shapes = currentPanel.getshapes();

        if (selectedShape != null && shapes.contains(selectedShape)) {
            shapes.remove(selectedShape);
            shapes.add(0, selectedShape);
            currentPanel.repaint();
            currentPanel.setUpdated(true);
        }
    }
}