package global;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileData implements Serializable {
    private static final long serialVersionUID = 1L;
    private File currentFile;
    private String fileName;
    private boolean hasUnsavedChanges;

    private int width;
    private int height;
    private String background;

    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private String fileExtension;

    // Constructors
    public FileData(String fileName, int width, int height, String background) {
        this.fileName = fileName;
        this.width = width;
        this.height = height;
        this.background = background;
        this.hasUnsavedChanges = false;
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = this.createdTime;
        this.fileExtension = "dat";
    }

    public FileData(File file, int width, int height, String background) {
        this(extractFileName(file), width, height, background);
        this.currentFile = file;
        this.fileExtension = extractExtension(file);
        // 파일이 존재하면 생성 시간을 파일 수정 시간으로
        if (file.exists()) {
            this.lastModifiedTime = LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli(file.lastModified()),
                    java.time.ZoneId.systemDefault()
            );
        }
    }

    public FileData(FileData other) {
        this.currentFile = other.currentFile;
        this.fileName = other.fileName;
        this.width = other.width;
        this.height = other.height;
        this.background = other.background;
        this.hasUnsavedChanges = other.hasUnsavedChanges;
        this.createdTime = other.createdTime;
        this.lastModifiedTime = LocalDateTime.now();
        this.fileExtension = other.fileExtension;
    }

    // getters & setters
    public File getCurrentFile() {return currentFile;}
    public void setCurrentFile(File file) {
        this.currentFile = file;
        if (file != null) {
            this.fileName = extractFileName(file);
            this.fileExtension = extractExtension(file);
            this.hasUnsavedChanges = false;
            updateLastModified();
        }
    }

    public String getFileName() {return fileName;}
    public void setFileName(String name) {
        if (name != null && !name.equals(this.fileName)) {
            this.fileName = name;
            setUnsavedChanges(true);
        }
    }

    public String getFullFileName() {
        if (fileName == null) return null;
        if (fileExtension == null || fileExtension.isEmpty()) return fileName;
        return fileName + "." + fileExtension;
    }

    public String getFilePath() {
        return currentFile != null ? currentFile.getAbsolutePath() : null;
    }
    public File getFileDirectory() {
        return currentFile != null ? currentFile.getParentFile() : null;
    }

    public boolean fileExists() {
        return currentFile != null && currentFile.exists();
    }

    public boolean isNewFile() {
        return currentFile == null;
    }

    //  Canvas Properties

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (this.width != width) {
            this.width = width;
            setUnsavedChanges(true);
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (this.height != height) {
            this.height = height;
            setUnsavedChanges(true);
        }
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        if (!java.util.Objects.equals(this.background, background)) {
            this.background = background;
            setUnsavedChanges(true);
        }
    }

    public void setSize(int width, int height) {
        if (this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            setUnsavedChanges(true);
        }
    }

    public java.awt.Dimension getSize() {
        return new java.awt.Dimension(width, height);
    }

    // Change
    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    public void setUnsavedChanges(boolean changed) {
        if (this.hasUnsavedChanges != changed) {
            this.hasUnsavedChanges = changed;
            if (changed) {
                updateLastModified();
            }
        }
    }

    public void markAsSaved() {
        this.hasUnsavedChanges = false;
        updateLastModified();
    }

    public void markAsModified() {
        setUnsavedChanges(true);
    }

    // Metadata

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    private void updateLastModified() {
        this.lastModifiedTime = LocalDateTime.now();
    }

    public String getFormattedCreatedTime() {
        return createdTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getFormattedLastModifiedTime() {
        return lastModifiedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Utility
    private static String extractFileName(File file) {
        if (file == null) return null;
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return lastDot > 0 ? name.substring(0, lastDot) : name;
    }

    private static String extractExtension(File file) {
        if (file == null) return null;
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return lastDot > 0 ? name.substring(lastDot + 1) : "";
    }
    public boolean isValid() {
        return fileName != null && !fileName.trim().isEmpty() &&
                width > 0 && height > 0 && background != null;
    }

    public void resetToDefaults() {
        this.width = 800;
        this.height = 600;
        this.background = "White";
        this.hasUnsavedChanges = true;
        updateLastModified();
    }

    public FileData copy() {
        return new FileData(this);
    }

    public long getFileSize() {
        return currentFile != null && currentFile.exists() ? currentFile.length() : 0;
    }

    public String getFormattedFileSize() {
        long size = getFileSize();
        if (size == 0) return "0 B";

        String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = 0;
        double fileSize = size;

        while (fileSize >= 1024 && unitIndex < units.length - 1) {
            fileSize /= 1024;
            unitIndex++;
        }

        return String.format("%.1f %s", fileSize, units[unitIndex]);
    }

    public String getDisplayTitle() {
        String title = fileName != null ? fileName : "Untitled";
        return hasUnsavedChanges ? title + "*" : title;
    }

    public String getTabTitle() {
        return getDisplayTitle();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        FileData fileData = (FileData) obj;
        return width == fileData.width &&
                height == fileData.height &&
                java.util.Objects.equals(fileName, fileData.fileName) &&
                java.util.Objects.equals(background, fileData.background) &&
                java.util.Objects.equals(currentFile, fileData.currentFile);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(fileName, width, height, background, currentFile);
    }

    // === String Representation ===

    @Override
    public String toString() {
        return String.format("FileData{name='%s', size=%dx%d, background='%s', modified=%s}",
                fileName, width, height, background, hasUnsavedChanges);
    }

    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FileData Details:\n");
        sb.append(String.format("  File Name: %s\n", fileName));
        sb.append(String.format("  Canvas Size: %dx%d\n", width, height));
        sb.append(String.format("  Background: %s\n", background));
        sb.append(String.format("  Has Changes: %s\n", hasUnsavedChanges));
        sb.append(String.format("  Created: %s\n", getFormattedCreatedTime()));
        sb.append(String.format("  Modified: %s\n", getFormattedLastModifiedTime()));

        if (currentFile != null) {
            sb.append(String.format("  File Path: %s\n", getFilePath()));
            sb.append(String.format("  File Size: %s\n", getFormattedFileSize()));
            sb.append(String.format("  File Exists: %s\n", fileExists()));
        } else {
            sb.append("  File Path: (not saved)\n");
            sb.append("  File Size: 0 B\n");
            sb.append("  File Exists: false\n");
        }

        return sb.toString();
    }
}