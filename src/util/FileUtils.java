package util;

import java.io.File;

public class FileUtils {
    public static String removeExtension(String fileName) {
        return fileName.replaceFirst("[.][^.]+$", "");
    }

    public static String getExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return lastDot > 0 ? name.substring(lastDot + 1).toLowerCase() : "";
    }

    public static boolean isImageFile(String extension) {
        return extension.matches("png|jpg|jpeg|gif|bmp|tiff|tif");
    }

    public static String formatFileSize(long size) {
        if (size < 1024) return size + " bytes";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        return String.format("%.1f MB", size / (1024.0 * 1024.0));
    }
}