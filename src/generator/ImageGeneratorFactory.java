package generator;

import frames.GMainFrame;

public class ImageGeneratorFactory {

    public enum GeneratorType {
        POLLINATIONS,      // 완전 무료
        FREE_AI           // 여러 무료 서비스 통합
    }

    public static ImageGenerator createGenerator(GeneratorType type, GMainFrame frame) {
        switch (type) {
            case FREE_AI:
                return new FreeAIImageGenerator(frame);
            default:
                return new PollinationsImageGenerator(frame);
        }
    }
}