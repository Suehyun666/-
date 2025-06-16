package controller;

import dialog.edit.GColorSettingsDialog;
import dialog.edit.GPropertyDialog;
import global.CanvasInfo;
import frames.GTabManager;
import global.ColorData;
import global.FileData;
import dialog.DialogManager;
import dialog.DialogResult;
import frames.GMainPanel;
import shapes.GPicture;
import shapes.GShape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.Vector;

public class FileController {
    private final TabController tabController;
    private final GTabManager tabManager;

    public FileController(TabController tabController, GTabManager tabManager) {
        this.tabController = tabController;
        this.tabManager = tabManager;
    }

    public void handleNewFile() {
        DialogResult<FileData> result = DialogManager.newFile();
        if (!result.isConfirmed()) return;

        FileData fileData = result.getResult();

        // 중복된 이름
        if (tabManager.isTitleExists(fileData.getFileName())) {
            String uniqueName = generateUniqueFileName(fileData.getFileName());
            fileData.setFileName(uniqueName);
        }

        tabController.createNewTab(fileData);
    }

    public void handleOpenFile() {
        DialogResult<File> result = DialogManager.openFile();
        if (!result.isConfirmed()) return;

        File selectedFile = result.getResult();
        if (tabManager.isTabOpenWithFile(selectedFile)) {
            DialogManager.info("이미 열려있는 파일입니다.", "중복");
            tabController.focusTabWithFile(selectedFile);
            return;
        }
        try {
            loadFileData(selectedFile);
        } catch (Exception e) {
            DialogManager.error("파일을 열 수 없습니다: " + e.getMessage(), "오류");
        }
    }

    public void handleSaveFile() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null) {
            DialogManager.warning("저장할 파일이 없습니다.", "경고");
            return;
        }

        FileData fileData = currentPanel.getFileData();
        if (fileData.getCurrentFile() == null) {
            handleSaveAsFile();
            return;
        }

        try {
            saveToFile(currentPanel, fileData.getCurrentFile());
            tabManager.setModified(false);
            DialogManager.info("파일이 저장되었습니다.", "저장 완료");
        } catch (Exception e) {
            DialogManager.error("파일 저장 중 오류가 발생했습니다: " + e.getMessage(), "오류");
        }
    }

    public void handleSaveAsFile() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null) {
            DialogManager.warning("저장할 파일이 없습니다.", "경고");
            return;
        }

        DialogResult<File> result = DialogManager.saveAsFile();
        if (!result.isConfirmed()) return;

        File selectedFile = result.getResult();

        try {
            saveToFile(currentPanel, selectedFile);

            FileData fileData = currentPanel.getFileData();
            fileData.setCurrentFile(selectedFile);

            tabManager.setTabTitle(currentPanel, fileData.getFileName());
            tabManager.setModified(false);

            DialogManager.info("파일이 저장되었습니다.", "저장 완료");
        } catch (Exception e) {
            DialogManager.error("파일 저장 중 오류가 발생했습니다: " + e.getMessage(), "오류");
        }
    }

    public void handleCloseFile() {
        tabController.handleCloseCurrentTab();
    }

    public void handlePrintFile() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null) {
            DialogManager.warning("인쇄할 파일이 없습니다.", "경고");
            return;
        }

        DialogResult<Void> result = DialogManager.showPrintDialog();
        if (result.isConfirmed()) {
            try {
                printPanel(currentPanel);
                DialogManager.info("인쇄가 완료되었습니다.", "인쇄 완료");
            } catch (Exception e) {
                DialogManager.error("인쇄 중 오류가 발생했습니다: " + e.getMessage(), "오류");
            }
        }
    }

    public void handleImportFile() {
        DialogResult<File> result = DialogManager.importFile();
        if (!result.isConfirmed()) return;

        File selectedFile = result.getResult();
        GMainPanel currentPanel = tabManager.getCurrentPanel();

        if (currentPanel == null) {
            DialogManager.warning("가져올 대상 캔버스가 없습니다.", "경고");
            return;
        }

        try {
            importToPanel(currentPanel, selectedFile);
            tabManager.setModified(true);
            DialogManager.info("파일을 가져왔습니다.", "가져오기 완료");
        } catch (Exception e) {
            DialogManager.error("파일 가져오기 중 오류가 발생했습니다: " + e.getMessage(), "오류");
        }
    }

    public void handleExportFile() {
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel == null) {
            DialogManager.warning("내보낼 파일이 없습니다.", "경고");
            return;
        }

        DialogResult<File> result = DialogManager.exportFile();
        if (!result.isConfirmed()) return;

        File selectedFile = result.getResult();

        try {
            exportPanel(currentPanel, selectedFile);
            DialogManager.info("파일을 내보냈습니다.", "내보내기 완료");
        } catch (Exception e) {
            DialogManager.error("파일 내보내기 중 오류가 발생했습니다: " + e.getMessage(), "오류");
        }
    }

    public void handleExit() {
        if (tabManager.checkSaveAllTabs()) {
            System.exit(0);
        }
    }


    private String generateUniqueFileName(String baseName) {
        String uniqueName = baseName;
        int counter = 1;
        while (tabManager.isTitleExists(uniqueName)) {
            uniqueName = baseName + " (" + counter + ")";
            counter++;
        }
        return uniqueName;
    }

    private FileData loadFileData(File file) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            FileData fileData = (FileData) ois.readObject();
            CanvasInfo canvasInfo = (CanvasInfo) ois.readObject();
            Vector<GShape> shapes = (Vector<GShape>) ois.readObject();
            tabController.createNewTab(fileData);
            fileData.setCurrentFile(file);
            fileData.setUnsavedChanges(false);
            GMainPanel newPanel = tabManager.getCurrentPanel();
            if (newPanel != null) {
                newPanel.setShapes(shapes);

                for (GShape shape : shapes) {
                    if (shape instanceof GPicture) {
                        GPicture picture = (GPicture) shape;
                        if (picture.getImagePath() != null) {
                            File imageFile = new File(picture.getImagePath());
                            if (imageFile.exists()) {
                                picture.loadImage(imageFile);
                            }
                        }
                    }
                }

                newPanel.repaint();
                newPanel.revalidate();
                }
            System.out.println("Loaded: " + fileData);
            return fileData;

        } catch (Exception e) {
            System.err.println("Load error: " + e.getMessage());
            throw new Exception("Failed to load file: " + e.getMessage());
        }
    }

    private void saveToFile(GMainPanel panel, File file) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            FileData fileData = panel.getFileData();
            oos.writeObject(fileData);

            CanvasInfo canvasInfo = panel.getCanvasState();
            oos.writeObject(canvasInfo);

            Vector<GShape> shapes = panel.getshapes();
            oos.writeObject(shapes);

            fileData.setCurrentFile(file);
            panel.setUpdated(false);
        } catch (Exception e) {
            System.err.println("Save error: " + e.getMessage());
            throw new Exception("Failed to save file: " + e.getMessage());
        }
    }

    private void printPanel(GMainPanel panel) throws Exception {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                double scaleX = pageFormat.getImageableWidth() / panel.getWidth();
                double scaleY = pageFormat.getImageableHeight() / panel.getHeight();
                double scale = Math.min(scaleX, scaleY);
                g2d.scale(scale, scale);

                panel.paint(g2d);

                return Printable.PAGE_EXISTS;
            });

            if (job.printDialog()) {
                job.print();
            }
        } catch (Exception e) {
            throw new Exception("Print Failed " + e.getMessage());
        }
    }

    private void importToPanel(GMainPanel panel, File file) throws Exception {
        try {
            String fileName = file.getName().toLowerCase();
            if (!(fileName.endsWith(".png") || fileName.endsWith(".jpg") ||
                    fileName.endsWith(".jpeg") || fileName.endsWith(".gif") ||
                    fileName.endsWith(".bmp"))) {
                throw new Exception("지원하지 않는 이미지 형식입니다.");
            }

            GPicture picture = new GPicture(file);

            Dimension canvasSize = panel.getCanvasSize();
            if (canvasSize != null) {
                Dimension imageSize = picture.getImageSize();
                int x = (canvasSize.width - imageSize.width) / 2;
                int y = (canvasSize.height - imageSize.height) / 2;
                picture.setLocation(x, y);
            }

            panel.addShape(picture);
            panel.repaint();

            System.out.println("이미지 가져오기 완료: " + picture.getImageSize());

        } catch (Exception e) {
            throw new Exception("이미지 가져오기 실패: " + e.getMessage());
        }
    }
    private void exportPanel(GMainPanel panel, File file) throws Exception {
        try {
            Dimension canvasSize = panel.getCanvasSize();
            if (canvasSize == null) {
                canvasSize = panel.getSize();
            }

            BufferedImage image = new BufferedImage(
                    canvasSize.width, canvasSize.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(panel.getBackground());
            g2d.fillRect(0, 0, canvasSize.width, canvasSize.height);

            for (GShape shape : panel.getshapes()) {
                shape.draw(g2d);
            }

            g2d.dispose();

            String fileName = file.getName().toLowerCase();
            String format = "png";
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                format = "jpg";
            } else if (fileName.endsWith(".gif")) {
                format = "gif";
            }

            ImageIO.write(image, format, file);

        } catch (Exception e) {
            throw new Exception("Fail to Export: " + e.getMessage());
        }
    }
    public void handleColorSetting() {
        try {
            Frame parentFrame = null;
            if (tabManager != null && tabManager.getMainFrame() instanceof Frame) {
                parentFrame = (Frame) tabManager.getMainFrame();
            }

            GColorSettingsDialog dialog = new GColorSettingsDialog(parentFrame);

            if (dialog.showDialog()) {
                var settings = dialog.getSettings();
                System.out.println("Color settings applied:");
                for (var entry : settings.entrySet()) {
                    System.out.println("  " + entry.getKey() + ": " + entry.getValue());
                }

                applyColorSettings(settings);

                DialogManager.info("색상 설정이 적용되었습니다.", "색상 설정");
            }

        } catch (Exception e) {
            System.err.println("Error opening color settings dialog: " + e.getMessage());
            e.printStackTrace();
            DialogManager.error("색상 설정 대화상자를 열 수 없습니다: " + e.getMessage(), "오류");
        }
    }

    private void applyColorSettings(java.util.Map<String, String> settings) {
        // 색상 설정을 현재 작업 환경에 적용
        GMainPanel currentPanel = tabManager.getCurrentPanel();
        if (currentPanel != null) {
            // RGB 워킹 스페이스 설정
            String rgbSpace = settings.get("rgb");
            if (rgbSpace != null) {
                System.out.println("RGB working space set to: " + rgbSpace);
                // TODO: 실제 RGB 프로파일 적용
            }

            // CMYK 워킹 스페이스 설정
            String cmykSpace = settings.get("cmyk");
            if (cmykSpace != null) {
                System.out.println("CMYK working space set to: " + cmykSpace);
                // TODO: 실제 CMYK 프로파일 적용
            }

            // 그레이스케일 설정
            String graySpace = settings.get("gray");
            if (graySpace != null) {
                System.out.println("Gray working space set to: " + graySpace);
                // TODO: 실제 그레이 프로파일 적용
            }

            // 스팟 컬러 설정
            String spotSpace = settings.get("spot");
            if (spotSpace != null) {
                System.out.println("Spot working space set to: " + spotSpace);
                // TODO: 실제 스팟 컬러 프로파일 적용
            }

            // 화면 갱신
            currentPanel.repaint();
        }
    }

    public void bringForward() {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            GShape selectedShape = currentPanel.getSelectedShape();
            if (selectedShape != null) {
                Vector<GShape> shapes = currentPanel.getshapes();
                int currentIndex = shapes.indexOf(selectedShape);
                if (currentIndex < shapes.size() - 1) {
                    System.out.println("Bringing forward");
                    shapes.remove(currentIndex);
                    shapes.add(currentIndex + 1, selectedShape);
                    currentPanel.repaint();
                    currentPanel.setUpdated(true);
                }
            }
        }
    }

    public void sendBackward() {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            GShape selectedShape = currentPanel.getSelectedShape();
            if (selectedShape != null) {
                Vector<GShape> shapes = currentPanel.getshapes();
                int currentIndex = shapes.indexOf(selectedShape);

                if (currentIndex > 0) {
                    // 한 단계 뒤로
                    shapes.remove(currentIndex);
                    shapes.add(currentIndex - 1, selectedShape);
                    currentPanel.repaint();
                    currentPanel.setUpdated(true);
                }
            }
        }
    }

    public void bringToFront() {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            GShape selectedShape = currentPanel.getSelectedShape();
            if (selectedShape != null) {
                Vector<GShape> shapes = currentPanel.getshapes();
                shapes.remove(selectedShape);
                shapes.add(selectedShape);
                currentPanel.repaint();
                currentPanel.setUpdated(true);
            }
        }
    }

    public void sendToBack() {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            GShape selectedShape = currentPanel.getSelectedShape();
            if (selectedShape != null) {
                Vector<GShape> shapes = currentPanel.getshapes();
                shapes.remove(selectedShape);
                shapes.add(0, selectedShape); // 맨 앞으로 (처음에 그려짐 = 맨 뒤)
                currentPanel.repaint();
                currentPanel.setUpdated(true);
            }
        }
    }

    // Shape Color Properties
    public void setSelectedShapesFillColor(Color color) {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            GShape selectedShape = currentPanel.getSelectedShape();
            if (selectedShape != null) {
                Color strokeColor = selectedShape.getStrokeColor();
                int strokeWidth = selectedShape.getStrokeWidth();
                boolean fillEnabled = true;
                boolean strokeEnabled = selectedShape.isStrokeEnabled();

                currentPanel.updateSelectedShapeColors(color, strokeColor, strokeWidth, fillEnabled, strokeEnabled);
            }
        }
    }

    public void setSelectedShapesStrokeColor(Color color) {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            GShape selectedShape = currentPanel.getSelectedShape();
            if (selectedShape != null) {
                Color fillColor = selectedShape.getFillColor();
                int strokeWidth = selectedShape.getStrokeWidth();
                boolean fillEnabled = selectedShape.isFillEnabled();
                boolean strokeEnabled = true;

                currentPanel.updateSelectedShapeColors(fillColor, color, strokeWidth, fillEnabled, strokeEnabled);
            }
        }
    }

    public void setSelectedShapesOpacity(int opacity) {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            GShape selectedShape = currentPanel.getSelectedShape();
            if (selectedShape != null) {
                Color fillColor = selectedShape.getFillColor();
                Color strokeColor = selectedShape.getStrokeColor();

                if (fillColor != null) {
                    int alpha = (int) (opacity * 2.55); // 0-100을 0-255로 변환
                    Color newFillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), alpha);

                    int strokeWidth = selectedShape.getStrokeWidth();
                    boolean fillEnabled = selectedShape.isFillEnabled();
                    boolean strokeEnabled = selectedShape.isStrokeEnabled();

                    currentPanel.updateSelectedShapeColors(newFillColor, strokeColor, strokeWidth, fillEnabled, strokeEnabled);
                }
            }
        }
    }

    public Color getCurrentShapeFillColor() {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            GShape selectedShape = currentPanel.getSelectedShape();
            if (selectedShape != null) {
                return selectedShape.getFillColor();
            }
        }
        return Color.BLACK; // 기본값
    }

    // Image Menu Methods
    public void mode() {
        System.out.println("Image mode change - To be implemented");
    }

    public void imageSize() {
        showImageSizeDialog();
    }

    public void canvasSize() {
        showCanvasSizeDialog();
    }

    public void rotateImage() {
    }

    public void crop() {
        // TODO: 이미지 자르기
        System.out.println("Crop - To be implemented");
    }

    public void trim() {
        // TODO: 여백 제거
        System.out.println("Trim - To be implemented");
    }

    public void duplicate() {
    }

    public void generateImage() {
        // TODO: AI 이미지 생성 (이미 구현된 GPictureToolBar 활용)
        System.out.println("Generate Image - Use Picture Toolbar");
    }

    private void showImageSizeDialog() {
        String widthStr = JOptionPane.showInputDialog("Enter new width:");
        String heightStr = JOptionPane.showInputDialog("Enter new height:");

        try {
            int width = Integer.parseInt(widthStr);
            int height = Integer.parseInt(heightStr);

            // TODO: 선택된 이미지의 크기 변경
            System.out.println("Image size changed to: " + width + "x" + height);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid size values", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCanvasSizeDialog() {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null) {
            String widthStr = JOptionPane.showInputDialog("Enter new canvas width:",
                    currentPanel.getCanvasSize().width);
            String heightStr = JOptionPane.showInputDialog("Enter new canvas height:",
                    currentPanel.getCanvasSize().height);

            try {
                int width = Integer.parseInt(widthStr);
                int height = Integer.parseInt(heightStr);

                currentPanel.setCanvasSize(width, height);
                currentPanel.setUpdated(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid size values", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private GMainPanel getCurrentPanel() {
        return tabController.getCurrentPanel();
    }

    public void handlefill() {
        GMainPanel currentPanel = getCurrentPanel();
        if (currentPanel != null && currentPanel.hasSelection()) {
            GShape selectedShape = currentPanel.getSelectedShape();
            if (selectedShape != null) {
                Color currentColor = selectedShape.getFillColor();
                if (currentColor == null) currentColor = Color.BLACK;

                Color newColor = JColorChooser.showDialog(
                        null,
                        "Choose Fill Color",
                        currentColor
                );

                if (newColor != null) {
                    Color strokeColor = selectedShape.getStrokeColor();
                    int strokeWidth = selectedShape.getStrokeWidth();
                    boolean fillEnabled = true;
                    boolean strokeEnabled = selectedShape.isStrokeEnabled();

                    currentPanel.updateSelectedShapeColors(newColor, strokeColor, strokeWidth, fillEnabled, strokeEnabled);
                    currentPanel.setUpdated(true);
                }
            }
        }
    }
}