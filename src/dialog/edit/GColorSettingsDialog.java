package dialog.edit;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GColorSettingsDialog extends JDialog {
    private JComboBox<String> settingsCombo;
    private JComboBox<String> rgbCombo;
    private JComboBox<String> cmykCombo;
    private JComboBox<String> grayCombo;
    private JComboBox<String> spotCombo;
    private JComboBox<String> engineCombo;
    private JComboBox<String> intentCombo;
    private JCheckBox profileMismatchesCheck;
    private JCheckBox missingProfilesCheck;
    private JCheckBox useBlackPointCheck;
    private JCheckBox useDitherCheck;
    private JTextArea descriptionArea;

    private boolean confirmed = false;
    private Map<String, String> savedSettings;

    public GColorSettingsDialog(Frame parent) {
        super(parent, "Color Settings", true);
        savedSettings = new HashMap<>();
        initializeDialog();
    }

    private void initializeDialog() {
        setLayout(new BorderLayout());
        setSize(700, 800);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Settings dropdown
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Settings:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        String[] settings = {
                "Custom",
                "General Purpose 3",
                "Prepress - Europe",
                "Prepress - Japan",
                "Prepress - North America",
                "Web/Internet",
                "Monitor Color",
                "ColorSync Workflow",
                "Emulate Photoshop 4"
        };
        settingsCombo = new JComboBox<>(settings);
        mainPanel.add(settingsCombo, gbc);

        // Working Spaces
        JPanel workingSpacesPanel = new JPanel(new GridBagLayout());
        workingSpacesPanel.setBorder(BorderFactory.createTitledBorder("Working Spaces"));

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 0;
        workingSpacesPanel.add(new JLabel("RGB:"), gbc);
        gbc.gridx = 1;
        String[] rgbOptions = {
                "sRGB IEC61966-2.1",
                "Adobe RGB (1998)",
                "Apple RGB",
                "ColorMatch RGB",
                "ProPhoto RGB"
        };
        rgbCombo = new JComboBox<>(rgbOptions);
        workingSpacesPanel.add(rgbCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        workingSpacesPanel.add(new JLabel("CMYK:"), gbc);
        gbc.gridx = 1;
        String[] cmykOptions = {
                "U.S. Web Coated (SWOP) v2",
                "U.S. Web Uncoated v2",
                "U.S. Sheetfed Coated v2",
                "Japan Color 2001 Coated",
                "Europe ISO Coated FOGRA27"
        };
        cmykCombo = new JComboBox<>(cmykOptions);
        workingSpacesPanel.add(cmykCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        workingSpacesPanel.add(new JLabel("Gray:"), gbc);
        gbc.gridx = 1;
        String[] grayOptions = {
                "Gray Gamma 2.2",
                "Gray Gamma 1.8",
                "Dot Gain 10%",
                "Dot Gain 15%",
                "Dot Gain 20%"
        };
        grayCombo = new JComboBox<>(grayOptions);
        workingSpacesPanel.add(grayCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        workingSpacesPanel.add(new JLabel("Spot:"), gbc);
        gbc.gridx = 1;
        String[] spotOptions = {
                "Dot Gain 10%",
                "Dot Gain 15%",
                "Dot Gain 20%",
                "Dot Gain 25%",
                "Dot Gain 30%"
        };
        spotCombo = new JComboBox<>(spotOptions);
        workingSpacesPanel.add(spotCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        mainPanel.add(workingSpacesPanel, gbc);

        // Color Management Policies
        JPanel policiesPanel = new JPanel(new GridBagLayout());
        policiesPanel.setBorder(BorderFactory.createTitledBorder("Color Management Policies"));

        String[] policyOptions = {"Off", "Preserve Embedded Profiles", "Convert to Working"};

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 0;
        policiesPanel.add(new JLabel("RGB:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> rgbPolicyCombo = new JComboBox<>(policyOptions);
        policiesPanel.add(rgbPolicyCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        policiesPanel.add(new JLabel("CMYK:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cmykPolicyCombo = new JComboBox<>(policyOptions);
        policiesPanel.add(cmykPolicyCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        policiesPanel.add(new JLabel("Gray:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> grayPolicyCombo = new JComboBox<>(policyOptions);
        policiesPanel.add(grayPolicyCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        profileMismatchesCheck = new JCheckBox("Profile Mismatches: Ask When Opening");
        policiesPanel.add(profileMismatchesCheck, gbc);

        gbc.gridy = 4;
        missingProfilesCheck = new JCheckBox("Missing Profiles: Ask When Opening");
        policiesPanel.add(missingProfilesCheck, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        mainPanel.add(policiesPanel, gbc);

        // Conversion Options
        JPanel conversionPanel = new JPanel(new GridBagLayout());
        conversionPanel.setBorder(BorderFactory.createTitledBorder("Conversion Options"));

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 0;
        conversionPanel.add(new JLabel("Engine:"), gbc);
        gbc.gridx = 1;
        String[] engineOptions = {
                "Adobe (ACE)",
                "Microsoft ICM",
                "Apple CMM"
        };
        engineCombo = new JComboBox<>(engineOptions);
        conversionPanel.add(engineCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        conversionPanel.add(new JLabel("Intent:"), gbc);
        gbc.gridx = 1;
        String[] intentOptions = {
                "Perceptual",
                "Saturation",
                "Relative Colorimetric",
                "Absolute Colorimetric"
        };
        intentCombo = new JComboBox<>(intentOptions);
        conversionPanel.add(intentCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        useBlackPointCheck = new JCheckBox("Use Black Point Compensation");
        conversionPanel.add(useBlackPointCheck, gbc);

        gbc.gridy = 3;
        useDitherCheck = new JCheckBox("Use Dither (8-bit/channel images)");
        conversionPanel.add(useDitherCheck, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        mainPanel.add(conversionPanel, gbc);

        // Description area
        gbc.gridy = 4;
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionArea = new JTextArea(5, 50);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setText("The current color settings file is recommended for general-purpose desktop printing in North America.");
        descriptionPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        mainPanel.add(descriptionPanel, gbc);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton moreOptionsButton = new JButton("More Options");
        JButton loadButton = new JButton("Load...");
        JButton saveButton = new JButton("Save...");
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            confirmed = true;
            saveSettings();
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        loadButton.addActionListener(e -> loadSettings());
        saveButton.addActionListener(e -> saveSettingsToFile());

        buttonPanel.add(moreOptionsButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        settingsCombo.addActionListener(e -> updateDescription());
        rgbPolicyCombo.setSelectedIndex(1);
        cmykPolicyCombo.setSelectedIndex(1);
        grayPolicyCombo.setSelectedIndex(1);
        useBlackPointCheck.setSelected(true);
        useDitherCheck.setSelected(true);

        getRootPane().setDefaultButton(okButton);
    }

    private void updateDescription() {
        String selected = (String) settingsCombo.getSelectedItem();
        String description = "";

        switch (selected) {
            case "General Purpose 3":
                description = "The current color settings file is recommended for general-purpose desktop printing in North America.";
                break;
            case "Prepress - Europe":
                description = "Settings for prepress professionals in Europe. Includes FOGRA color profiles.";
                break;
            case "Web/Internet":
                description = "Settings optimized for web and internet use with sRGB as the working space.";
                break;
            default:
                description = "Custom color settings configuration.";
        }

        descriptionArea.setText(description);
    }

    private void saveSettings() {
        savedSettings.put("settings", (String) settingsCombo.getSelectedItem());
        savedSettings.put("rgb", (String) rgbCombo.getSelectedItem());
        savedSettings.put("cmyk", (String) cmykCombo.getSelectedItem());
        savedSettings.put("gray", (String) grayCombo.getSelectedItem());
        savedSettings.put("spot", (String) spotCombo.getSelectedItem());
    }

    private void loadSettings() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Color Settings");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Load settings implementation
            JOptionPane.showMessageDialog(this, "Settings loaded successfully!");
        }
    }

    private void saveSettingsToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Color Settings");
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Save settings implementation
            JOptionPane.showMessageDialog(this, "Settings saved successfully!");
        }
    }

    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }

    public Map<String, String> getSettings() {
        return savedSettings;
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        GColorSettingsDialog dialog= new GColorSettingsDialog(frame);
//        dialog.setVisible(true);
//        dialog.initializeDialog();
//    }
}