package generator;

import frames.GMainFrame;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PollinationsImageGenerator implements ImageGenerator {
    private GMainFrame frame;
    private String apikey = null;

    // Pollinations AI 무료 엔드포인트
    private static final String POLLINATIONS_URL = "https://image.pollinations.ai/prompt/";

    public PollinationsImageGenerator(GMainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void setApiKey(String key) {
        this.apikey = key;
    }

    @Override
    public String getApiKey() {
        return this.apikey;
    }

    @Override
    public File generateImage(String prompt) throws Exception {
        return generateImage(prompt, this.apikey);
    }

    @Override
    public File generateImage(String prompt, String apiKey) throws Exception {
        File outputFile = File.createTempFile("pollinations-", ".png");
        File generatedFile = generateImage(prompt, apiKey, outputFile.getAbsolutePath());

        if (generatedFile != null && generatedFile.exists()) {
            showGeneratedImageInDialog(generatedFile);
        }

        return generatedFile;
    }

    @Override
    public File generateImage(String prompt, String apiKey, String outputPath) throws Exception {
        try {
            // URL 인코딩
            String encodedPrompt = URLEncoder.encode(prompt, StandardCharsets.UTF_8.toString());

            // 이미지 크기와 품질 파라미터 추가
            String fullUrl = POLLINATIONS_URL + encodedPrompt +
                    "?width=1024&height=1024&nologo=true&nofeed=true";

            System.out.println("Generating image with Pollinations AI...");
            System.out.println("URL: " + fullUrl);

            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            connection.setConnectTimeout(30000); // 30초 타임아웃
            connection.setReadTimeout(30000);

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // 이미지 저장
                try (InputStream is = connection.getInputStream();
                     FileOutputStream fos = new FileOutputStream(new File(outputPath))) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    long totalBytes = 0;

                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        totalBytes += bytesRead;

                        if (totalBytes % (10 * 1024 * 1024) == 0) {
                            System.out.println("Downloaded: " + (totalBytes / (1024 * 1024)) + " MB");
                        }
                    }

                    System.out.println("Image saved successfully to " + outputPath);
                    System.out.println("Total size: " + (totalBytes / 1024) + " KB");
                }

                connection.disconnect();
                return new File(outputPath);

            } else {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                JOptionPane.showMessageDialog(this.frame.getCurrentPanel(),
                        "읽어올 수 없습니다. ",
                        "오류", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.frame.getCurrentPanel(),
                    "인터넷이 연결되지 않았습니다. " + e.getMessage(),
                    "오류", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }


    private void showGeneratedImageInDialog(File imageFile) {
        new GPictureViewerDialog(frame, "Generated Image", imageFile, frame.getCurrentPanel());
    }
}