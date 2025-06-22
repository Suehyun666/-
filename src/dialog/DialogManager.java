package dialog;

import dialog.edit.GColorDialog;
import dialog.edit.GPropertyDialog;
import dialog.file.*;
import global.ColorData;
import global.FileData;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DialogManager {
    private static DialogManager instance;
    private Component parentComponent;

    // constructor
    public DialogManager(Component parent) {
        this.parentComponent = parent;
    }
    public static void setDefaultParent(Component parent) {
        instance = new DialogManager(parent);
    }

    public static DialogManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DialogManager not initialized. Call setDefaultParent() first.");
        }return instance;
    }

    // File
    public DialogResult<FileData> showNewFileDialog() {
        GNewFileDialog dialog = new GNewFileDialog(getParentFrame());
        GNewFileDialog.NewFileResult result = GNewFileDialog.showNewFileDialog(parentComponent);

        if (result.confirmed) {
            FileData fileData = new FileData(
                    result.fileName,
                    result.width,
                    result.height,
                    result.background
            );
            return DialogResult.confirmed(fileData);
        }
        return DialogResult.cancelled();
    }

    public DialogResult<File> showSaveFileDialog(String defaultFileName) {
        GSaveFileDialog dialog = new GSaveFileDialog(getParentFrame(), defaultFileName);
        if (dialog.showDialog()) {
            return DialogResult.confirmed(dialog.getSelectedFile());
        }
        return DialogResult.cancelled();
    }

    public DialogResult<File> showOpenFileDialog() {
        GOpenFileDialog dialog = new GOpenFileDialog(getParentFrame());
        if (dialog.showDialog()) {
            return DialogResult.confirmed(dialog.getSelectedFile());
        }
        return DialogResult.cancelled();
    }

    public DialogResult<Void> doshowPrintDialog() {
        GPrintFileDialog dialog = new GPrintFileDialog(getParentFrame());
        if (dialog.showDialog()) {
            return DialogResult.confirmed(null);
        }
        return DialogResult.cancelled();
    }

    // Edit
    public DialogResult<ColorData> showColorDialog(ColorData currentColor) {
        GColorDialog dialog = new GColorDialog(getParentFrame());
        if (currentColor != null) {
            dialog.setInitialData(currentColor);
        }
        if (dialog.showDialog()) {
            return DialogResult.confirmed(dialog.getColorData());
        }
        return DialogResult.cancelled();
    }


    // Confirm

    public SaveAction showSaveConfirmDialog(String tabTitle) {
        int result = GSaveConfirmDialog.showSaveConfirmDialog(getParentFrame(), tabTitle);

        switch (result) {
            case GSaveConfirmDialog.SAVE_OPTION:
                return SaveAction.SAVE;
            case GSaveConfirmDialog.DONT_SAVE_OPTION:
                return SaveAction.DONT_SAVE;
            default:
                return SaveAction.CANCEL;
        }
    }

    public boolean showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(
                getParentFrame(),
                message,
                title,
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }

    //Message Dialog

    public void showError(String message, String title) {
        JOptionPane.showMessageDialog(
                getParentFrame(),
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void showInfo(String message, String title) {
        JOptionPane.showMessageDialog(
                getParentFrame(),
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void showWarning(String message, String title) {
        JOptionPane.showMessageDialog(
                getParentFrame(),
                message,
                title,
                JOptionPane.WARNING_MESSAGE
        );
    }

    public DialogResult<String> showInputDialog(String message, String title, String initialValue) {
        String result = JOptionPane.showInputDialog(
                getParentFrame(),
                message,
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                initialValue
        ).toString();

        if (result != null && !result.trim().isEmpty()) {
            return DialogResult.confirmed(result.trim());
        }
        return DialogResult.cancelled();
    }

    /**
     * Static
     */
    public static DialogResult<FileData> newFile() {
        return getInstance().showNewFileDialog();
    }

    public static DialogResult<File> saveFile(String defaultFileName) {
        return getInstance().showSaveFileDialog(defaultFileName);
    }
    public static DialogResult<File> saveAsFile() {
        return getInstance().showSaveFileDialog(null);
    }

    public static DialogResult<File> importFile() {
        return getInstance().showOpenFileDialog();//filechooser
    }

    public static DialogResult<File> exportFile() {
        return getInstance().showOpenFileDialog(); //filechooser
    }

    public static DialogResult<Void> showPrintDialog(){
        return getInstance().doshowPrintDialog();
    }

    public static DialogResult<File> openFile() {
        return getInstance().showOpenFileDialog();
    }

    public static DialogResult<ColorData> selectColor(ColorData current) {
        return getInstance().showColorDialog(current);
    }

    public static SaveAction confirmSave(String tabTitle) {
        return getInstance().showSaveConfirmDialog(tabTitle);
    }

    public static boolean confirm(String message, String title) {return getInstance().showConfirmDialog(message, title);}

    public static void error(String message, String title) {
        getInstance().showError(message, title);
    }

    public static void info(String message, String title) {
        getInstance().showInfo(message, title);
    }

    public static void warning(String message, String title) {
        getInstance().showWarning(message, title);
    }

    public static DialogResult<String> input(String message, String title, String initialValue) {
        return getInstance().showInputDialog(message, title, initialValue);
    }

    private Frame getParentFrame() {
        if (parentComponent == null) {
            return null;
        }
        return (Frame) SwingUtilities.getWindowAncestor(parentComponent);
    }

    public enum SaveAction {
        SAVE,
        DONT_SAVE,
        CANCEL
    }
}

