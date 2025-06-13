package frames;

import generator.ImageGenerator;
import generator.PollinationsImageGenerator;
import shapes.GPicture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GPictureToolBar extends JToolBar {
    private static final long serialVersionUID = 1L;

    private ImageGenerator generator;
    private JTextField promptField;
    private JButton generateButton;
    private JLabel statusLabel;
    private GMainFrame mainFrame;

    public GPictureToolBar(GMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.generator=new PollinationsImageGenerator(this.mainFrame);
        // 툴바 설정
        setFloatable(false);
        setBorderPainted(true);
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout(5, 0));

        // 프롬프트 입력 패널
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBackground(Color.LIGHT_GRAY);

        JLabel promptLabel = new JLabel("Prompt: ");
        promptField = new JTextField(20);

        inputPanel.add(promptLabel, BorderLayout.WEST);
        inputPanel.add(promptField, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        generateButton = new JButton("Generate");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateImage(promptField.getText());
            }
        });

        buttonPanel.add(generateButton);

        // 상태 패널
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setBackground(Color.LIGHT_GRAY);

        statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel);

        // 툴바에 패널 추가
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
        add(statusPanel, BorderLayout.WEST);

        // 기본적으로 숨김 상태로 시작
        setVisible(true);
    }

    private void generateImage(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            statusLabel.setText("Enter the prompt");
            return;
        }

        statusLabel.setText("Generating image...");
        generateButton.setEnabled(false);

        // 백그라운드 작업으로 API 호출 수행
        new SwingWorker<File, Void>() {
            @Override
            protected File doInBackground() throws Exception {
                try {
                    // Stability AI API 호출 - 다이얼로그는 generator 내부에서 자동으로 표시됨
                    return generator.generateImage(prompt);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    File imageFile = get();
                    if (imageFile != null && imageFile.exists()) {
                        statusLabel.setText("Completed generation");
                    } else {
                        statusLabel.setText("Failed to generate image");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    statusLabel.setText("Error: " + e.getMessage());
                } finally {
                    generateButton.setEnabled(true);
                }
            }
        }.execute();
    }

    // 이미지를 메인 패널에 추가하는 메소드
    private void addPictureToMainPanel(GPicture picture) {
        GMainPanel mainPanel = mainFrame.getCurrentPanel();

        // 이 부분은 GMainPanel에 적절한 메소드가 필요함
        // 가정: mainPanel에 다음과 같은 메소드가 있거나 추가될 예정
        mainPanel.addShape(picture);

        // 대안으로, 리플렉션이나 직접 액세스를 사용할 수 있지만 권장하지 않음
        // 임시로 예외 발생
        JOptionPane.showMessageDialog(mainFrame,
                "Image generated successfully at: " + picture.getBounds().x + ", " + picture.getBounds().y,
                "Image Generated",
                JOptionPane.INFORMATION_MESSAGE);

        mainPanel.repaint();
    }

    public void showToolBar(boolean show) {
        setVisible(show);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void initialize() {
        promptField.setText("");
        statusLabel.setText("Ready");
        generateButton.setEnabled(true);
    }
}