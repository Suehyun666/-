package frames;

import generator.ImageGenerator;
import generator.PollinationsImageGenerator;
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
        setFloatable(false);
        setBorderPainted(true);
        setBackground(Color.GRAY);
        setLayout(new BorderLayout(5, 0));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBackground(Color.LIGHT_GRAY);

        JLabel promptLabel = new JLabel("Prompt: ");
        promptField = new JTextField(20);

        inputPanel.add(promptLabel, BorderLayout.WEST);
        inputPanel.add(promptField, BorderLayout.CENTER);

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

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setBackground(Color.LIGHT_GRAY);

        statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
        add(statusPanel, BorderLayout.WEST);

        setVisible(true);
    }

    private void generateImage(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            statusLabel.setText("Enter the prompt");
            return;
        }
        statusLabel.setText("Generating image...");
        generateButton.setEnabled(false);

        new SwingWorker<File, Void>() {
            @Override
            protected File doInBackground() throws Exception {
                try {
                    // Stability AI API
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

    public void initialize() {
        promptField.setText("");
        statusLabel.setText("Ready");
        generateButton.setEnabled(true);
    }
}