package generator;

import frames.GMainFrame;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FreeAIImageGenerator implements ImageGenerator {
    private GMainFrame frame;
    private String apikey = null;

    // 무료 AI 이미지 생성 서비스들
    private enum FreeService {
        POLLINATIONS("https://image.pollinations.ai/prompt/"),
        LEXICA("https://lexica.art/api/v1/search?q="),
        CRAIYON("https://backend.craiyon.com/generate");

        public final String url;
        FreeService(String url) {
            this.url = url;
        }
    }

    private FreeService currentService = FreeService.POLLINATIONS;

    public FreeAIImageGenerator(GMainFrame frame) {
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
        File outputFile = File.createTempFile("free-ai-", ".png");
        File generatedFile = generateImage(prompt, apiKey, outputFile.getAbsolutePath());

        if (generatedFile != null && generatedFile.exists()) {
            showGeneratedImageInDialog(generatedFile);
        }

        return generatedFile;
    }

    @Override
    public File generateImage(String prompt, String apiKey, String outputPath) throws Exception {
        Exception lastException = null;
        for (FreeService service : FreeService.values()) {
            try {
                System.out.println("Attempting with service: " + service.name());

                switch (service) {
                    case POLLINATIONS:
                        return generateWithPollinations(prompt, outputPath);
                    case CRAIYON:
                        return generateWithCraiyon(prompt, outputPath);
                    default:
                        continue;
                }

            } catch (Exception e) {
                lastException = e;
                System.out.println("Failed with " + service.name() + ": " + e.getMessage());
            }
        }

        if (lastException != null) {
            throw new Exception("All services failed. Last error: " + lastException.getMessage());
        }

        throw new Exception("No service available");
    }

    private File generateWithPollinations(String prompt, String outputPath) throws Exception {
        String encodedPrompt = URLEncoder.encode(prompt, StandardCharsets.UTF_8.toString());
        String fullUrl = FreeService.POLLINATIONS.url + encodedPrompt +
                "?width=1024&height=1024&nologo=true&nofeed=true";

        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(60000);

        if (connection.getResponseCode() == 200) {
            try (InputStream is = connection.getInputStream();
                 FileOutputStream fos = new FileOutputStream(new File(outputPath))) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                System.out.println("Image saved successfully to " + outputPath);
                return new File(outputPath);
            }
        }

        throw new Exception("Failed to generate image with Pollinations");
    }

    private File generateWithCraiyon(String prompt, String outputPath) throws Exception {
        URL url = new URL(FreeService.CRAIYON.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setDoOutput(true);

        JSONObject payload = new JSONObject();
        payload.put("prompt", prompt);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        if (connection.getResponseCode() == 200) {
            // Craiyon은 base64 인코딩된 이미지를 반환
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            // JSON 파싱하여 첫 번째 이미지 추출
            JSONObject jsonResponse = new JSONObject(response.toString());
            String base64Image = jsonResponse.getJSONArray("images").getString(0);

            // Base64 디코딩하여 파일로 저장
            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
            try (FileOutputStream fos = new FileOutputStream(new File(outputPath))) {
                fos.write(imageBytes);
            }

            System.out.println("Image saved successfully to " + outputPath);
            return new File(outputPath);
        }

        throw new Exception("Failed to generate image with Craiyon");
    }

    private void showGeneratedImageInDialog(File imageFile) {
        new GPictureViewerDialog(frame, "Generated Image", imageFile, frame.getCurrentPanel());
    }
}