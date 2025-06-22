package language;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.InputStream;
import java.util.*;

public class LanguageManager {
    private static LanguageManager instance = new LanguageManager();

    private final Map<String, String> fallbackMessages = new HashMap<>();
    private Map<String, String> messages = new HashMap<>();
    private String currentLanguage = "en";
    private final List<LanguageChangeListener> listeners = new ArrayList<>();

    public interface LanguageChangeListener {
        void onLanguageChanged();
    }

    private LanguageManager() {
        loadMessagesFor(currentLanguage);
    }

    public static LanguageManager getInstance() {
        return instance;
    }

    public void setLanguage(String languageCode) {
        this.currentLanguage = languageCode;
        loadMessagesFor(languageCode);
        notifyLanguageChange();
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public String getText(String key) {
        return messages.getOrDefault(key, fallbackMessages.getOrDefault(key, "[" + key + "]"));
    }

    public void addLanguageChangeListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyLanguageChange() {
        for (LanguageChangeListener listener : listeners) {
            listener.onLanguageChanged();
        }
    }

    private void loadMessagesFor(String languageCode) {
        messages = new HashMap<>();
        String path = "./resources/messages_" + languageCode + ".xml";
        InputStream input = getClass().getClassLoader().getResourceAsStream(path);
        if (input != null) {
            parseXMLMessages(input, messages);
        } else {
            System.err.println("[LanguageManager] No Language File: " + path);
        }
        for (Map.Entry<String, String> entry : fallbackMessages.entrySet()) {
            messages.putIfAbsent(entry.getKey(), entry.getValue());
        }
    }

    private void parseXMLMessages(InputStream input, Map<String, String> targetMap) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(input);

            NodeList entries = document.getElementsByTagName("entry");
            for (int i = 0; i < entries.getLength(); i++) {
                Element entry = (Element) entries.item(i);
                String key = entry.getAttribute("key");
                String value = entry.getTextContent().trim();

                if (key != null && !key.isEmpty()) {
                    targetMap.put(key, value);
                }
            }

            input.close();
        } catch (Exception e) {
            System.err.println("[LanguageManager] XML 파싱 실패: " + e.getMessage());
        }
    }
}
