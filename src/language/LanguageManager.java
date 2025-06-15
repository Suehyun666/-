// 강화된 LanguageManager.java
package language;

import java.io.*;
import java.util.*;
import java.util.List;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class LanguageManager {
    private static LanguageManager instance;
    private Map<String, String> messages;
    private Map<String, String> fallbackMessages; // 영어 기본값
    private String currentLanguage = "en";
    private List<LanguageChangeListener> listeners = new ArrayList<>();
    private boolean debugMode = true;

    public interface LanguageChangeListener {
        void onLanguageChanged();
    }

    private LanguageManager() {
        loadFallbackMessages(); // 영어 기본값 먼저 로드
        loadMessages();
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public void addLanguageChangeListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }

    public void removeLanguageChangeListener(LanguageChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyLanguageChange() {
        for (LanguageChangeListener listener : listeners) {
            listener.onLanguageChanged();
        }
    }

    public void setLanguage(String languageCode) {
        if (debugMode) {
            System.out.println("=== Setting language to: " + languageCode + " ===");
        }
        this.currentLanguage = languageCode;
        loadMessages();
        notifyLanguageChange();
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public String getText(String key) {
        String value = messages.get(key);

        if (value == null) {
            // 영어 기본값에서 찾기
            value = fallbackMessages.get(key);
            if (debugMode && value == null) {
                System.out.println("WARNING: Key '" + key + "' not found in any language files");
            }
        }

        return value != null ? value : "[" + key + "]";
    }

    private void loadFallbackMessages() {
        fallbackMessages = new HashMap<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("messages_en.xml");
            if (inputStream != null) {
                parseXMLMessages(inputStream, fallbackMessages);
                if (debugMode) {
                    System.out.println("Loaded " + fallbackMessages.size() + " fallback messages from XML");
                }
            } else {
                loadHardcodedEnglish();
                if (debugMode) {
                    System.out.println("Using hardcoded English fallback messages");
                }
            }
        } catch (Exception e) {
            if (debugMode) {
                System.out.println("Error loading English fallback, using hardcoded: " + e.getMessage());
            }
            loadHardcodedEnglish();
        }
    }

    private void loadMessages() {
        messages = new HashMap<>();

        if ("en".equals(currentLanguage)) {
            messages.putAll(fallbackMessages);
            if (debugMode) {
                System.out.println("Using English messages (" + messages.size() + " entries)");
            }
            return;
        }

        String fileName = "messages_" + currentLanguage + ".xml";
        boolean xmlLoaded = false;

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

            if (inputStream != null) {
                parseXMLMessages(inputStream, messages);
                xmlLoaded = true;
                if (debugMode) {
                    System.out.println("Loaded " + messages.size() + " messages from " + fileName);
                }
            } else {
                if (debugMode) {
                    System.out.println("XML file not found: " + fileName);
                }
            }
        } catch (Exception e) {
            if (debugMode) {
                System.out.println("Error loading " + fileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (!xmlLoaded) {
            // XML 로드 실패 시
            loadHardcodedMessages();
            if (debugMode) {
                System.out.println("Using hardcoded messages for " + currentLanguage);
            }
        }
        int addedCount = 0;
        for (Map.Entry<String, String> entry : fallbackMessages.entrySet()) {
            if (!messages.containsKey(entry.getKey())) {
                messages.put(entry.getKey(), entry.getValue());
                addedCount++;
            }
        }

        if (debugMode && addedCount > 0) {
            System.out.println("Added " + addedCount + " missing keys from English fallback");
        }

        // 로드된 키들 출력
        if (debugMode) {
            System.out.println("Final message count: " + messages.size());
            System.out.println("Sample loaded keys:");
            int count = 0;
            for (String key : messages.keySet()) {
                if (key.startsWith("tool.") || key.startsWith("file.") || key.endsWith(".menu")) {
                    System.out.println("  " + key + " = " + messages.get(key));
                    if (++count >= 10) break;
                }
            }
        }
    }

    private void parseXMLMessages(InputStream inputStream, Map<String, String> targetMap) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        NodeList entries = document.getElementsByTagName("entry");

        if (debugMode) {
            System.out.println("Parsing XML: found " + entries.getLength() + " entries");
        }

        for (int i = 0; i < entries.getLength(); i++) {
            Element entry = (Element) entries.item(i);
            String key = entry.getAttribute("key");
            String value = entry.getTextContent().trim();

            if (key != null && !key.trim().isEmpty()) {
                targetMap.put(key.trim(), value);
                if (debugMode && i < 3) {
                    System.out.println("  Sample: " + key + " = " + value);
                }
            }
        }

        inputStream.close();
    }

    private void loadHardcodedEnglish() {
        fallbackMessages.put("file.menu", "File");
        fallbackMessages.put("edit.menu", "Edit");
        fallbackMessages.put("select.menu", "Select");
        fallbackMessages.put("graphic.menu", "Graphic");
        fallbackMessages.put("image.menu", "Image");
        fallbackMessages.put("layer.menu", "Layer");
        fallbackMessages.put("filter.menu", "Filter");
        fallbackMessages.put("window.menu", "Window");
        fallbackMessages.put("help.menu", "Help");

        fallbackMessages.put("tool.select", "Select");
        fallbackMessages.put("tool.rectangle", "Rectangle");
        fallbackMessages.put("tool.triangle", "Triangle");
        fallbackMessages.put("tool.ellipse", "Ellipse");
        fallbackMessages.put("tool.line", "Line");
        fallbackMessages.put("tool.polygon", "Polygon");
        fallbackMessages.put("tool.pen", "Pen");
        fallbackMessages.put("tool.brush", "Brush");
        fallbackMessages.put("tool.erase", "Eraser");

        fallbackMessages.put("app.title", "Drawing Application");
    }

    private void loadHardcodedMessages() {
        if ("ko".equals(currentLanguage)) {
            messages.put("file.menu", "파일");
            messages.put("edit.menu", "편집");
            messages.put("select.menu", "선택");
            messages.put("graphic.menu", "그래픽");
            messages.put("image.menu", "이미지");
            messages.put("layer.menu", "레이어");
            messages.put("filter.menu", "필터");
            messages.put("window.menu", "창");
            messages.put("help.menu", "도움말");

            messages.put("tool.select", "선택");
            messages.put("tool.rectangle", "사각형");
            messages.put("tool.triangle", "삼각형");
            messages.put("tool.ellipse", "타원");
            messages.put("tool.line", "선");
            messages.put("tool.polygon", "다각형");
            messages.put("tool.pen", "펜");
            messages.put("tool.brush", "브러시");
            messages.put("tool.erase", "지우개");

            messages.put("app.title", "그림판 애플리케이션");
        }
    }

    // 디버깅 메서드들
    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }

    public void printAllMessages() {
        System.out.println("=== All loaded messages for " + currentLanguage + " ===");
        TreeMap<String, String> sortedMessages = new TreeMap<>(messages);
        for (Map.Entry<String, String> entry : sortedMessages.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("=== Total: " + messages.size() + " messages ===");
    }

    public boolean hasKey(String key) {
        return messages.containsKey(key) || fallbackMessages.containsKey(key);
    }

    public void testKeys(String... keys) {
        System.out.println("=== Testing keys ===");
        for (String key : keys) {
            System.out.println(key + " = " + getText(key));
        }
    }
}