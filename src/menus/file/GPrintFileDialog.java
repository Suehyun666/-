package menus.file;

import javax.print.PrintService;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

public class GPrintFileDialog extends JDialog {
    private JComboBox<PrintService> printerCombo;
    private JButton printerPropertiesButton;
    private JComboBox<String> copiesCombo;
    private JCheckBox collateCheck;
    private JComboBox<String> sizeCombo;
    private JComboBox<String> sourceCombo;
    private JRadioButton portraitRadio;
    private JRadioButton landscapeRadio;
    private JCheckBox centerCheck;
    private JCheckBox scaleToFitMediaCheck;
    private JTextField scaleField;
    private JTextField heightField;
    private JTextField widthField;
    private JTextField topField;
    private JTextField leftField;
    private JComboBox<String> unitsCombo;
    private JCheckBox printSelectedAreaCheck;
    private JTextField selectedAreaField;

    private boolean confirmed = false;
    private PrinterJob printerJob;
    private PageFormat pageFormat;

    public GPrintFileDialog(Frame parent) {
        super(parent, "Print", true);
        initializePrinterJob();
        initializeDialog();
    }

    private void initializePrinterJob() {
        printerJob = PrinterJob.getPrinterJob();
        pageFormat = printerJob.defaultPage();
    }

    private void initializeDialog() {
        setLayout(new BorderLayout());
        setSize(650, 700);
        setLocationRelativeTo(getParent());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Position and Size Tab
        JPanel positionPanel = createPositionPanel();
        tabbedPane.addTab("Position and Size", positionPanel);

        // Print Settings Tab
        JPanel settingsPanel = createPrintSettingsPanel();
        tabbedPane.addTab("Print Settings", settingsPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Bottom button panel
        add(createButtonPanel(), BorderLayout.SOUTH);

        getRootPane().setDefaultButton(findButton("Print"));
    }

    private JPanel createPrintSettingsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Printer Section
        JPanel printerPanel = new JPanel(new GridBagLayout());
        printerPanel.setBorder(BorderFactory.createTitledBorder("Printer"));

        gbc.gridx = 0; gbc.gridy = 0;
        printerPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        printerCombo = new JComboBox<>(PrinterJob.lookupPrintServices());
        printerCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof PrintService) {
                    value = ((PrintService) value).getName();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        printerPanel.add(printerCombo, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        printerPropertiesButton = new JButton("Properties...");
        printerPropertiesButton.addActionListener(e -> showPrinterProperties());
        printerPanel.add(printerPropertiesButton, gbc);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        panel.add(printerPanel, gbc);

        // Print Range
        JPanel rangePanel = new JPanel(new GridBagLayout());
        rangePanel.setBorder(BorderFactory.createTitledBorder("Print Range"));

        ButtonGroup rangeGroup = new ButtonGroup();
        JRadioButton allRadio = new JRadioButton("All", true);
        JRadioButton currentRadio = new JRadioButton("Current Layer");
        JRadioButton selectionRadio = new JRadioButton("Selection");

        rangeGroup.add(allRadio);
        rangeGroup.add(currentRadio);
        rangeGroup.add(selectionRadio);

        gbc.gridx = 0; gbc.gridy = 0;
        rangePanel.add(allRadio, gbc);
        gbc.gridy = 1;
        rangePanel.add(currentRadio, gbc);
        gbc.gridy = 2;
        rangePanel.add(selectionRadio, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(rangePanel, gbc);

        // Copies
        JPanel copiesPanel = new JPanel(new GridBagLayout());
        copiesPanel.setBorder(BorderFactory.createTitledBorder("Copies"));

        gbc.gridx = 0; gbc.gridy = 0;
        copiesPanel.add(new JLabel("Number of Copies:"), gbc);
        gbc.gridx = 1;
        String[] copiesOptions = {"1", "2", "3", "4", "5", "10", "20"};
        copiesCombo = new JComboBox<>(copiesOptions);
        copiesCombo.setEditable(true);
        copiesPanel.add(copiesCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        collateCheck = new JCheckBox("Collate");
        copiesPanel.add(collateCheck, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(copiesPanel, gbc);

        // Media
        JPanel mediaPanel = new JPanel(new GridBagLayout());
        mediaPanel.setBorder(BorderFactory.createTitledBorder("Media"));

        gbc.gridx = 0; gbc.gridy = 0;
        mediaPanel.add(new JLabel("Size:"), gbc);
        gbc.gridx = 1;
        String[] sizes = {"Letter", "Legal", "A4", "A3", "Tabloid", "Custom"};
        sizeCombo = new JComboBox<>(sizes);
        mediaPanel.add(sizeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mediaPanel.add(new JLabel("Source:"), gbc);
        gbc.gridx = 1;
        String[] sources = {"Automatically Select", "Tray 1", "Tray 2", "Manual Feed"};
        sourceCombo = new JComboBox<>(sources);
        mediaPanel.add(sourceCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panel.add(mediaPanel, gbc);

        return panel;
    }

    private JPanel createPositionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Preview area
        JPanel previewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(50, 50, 200, 280);
                g.setColor(Color.BLACK);
                g.drawRect(50, 50, 200, 280);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(75, 75, 150, 200);
                g.setColor(Color.BLACK);
                g.drawString("Preview", 130, 175);
            }
        };
        previewPanel.setPreferredSize(new Dimension(300, 380));
        previewPanel.setBackground(Color.LIGHT_GRAY);
        previewPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridheight = 6;
        panel.add(previewPanel, gbc);

        // Position section
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1;
        centerCheck = new JCheckBox("Center Image");
        panel.add(centerCheck, gbc);

        gbc.gridy = 1;
        panel.add(new JLabel("Top:"), gbc);
        gbc.gridx = 2;
        topField = new JTextField("0.5", 5);
        panel.add(topField, gbc);
        gbc.gridx = 3;
        unitsCombo = new JComboBox<>(new String[]{"inches", "cm", "mm"});
        panel.add(unitsCombo, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(new JLabel("Left:"), gbc);
        gbc.gridx = 2;
        leftField = new JTextField("0.5", 5);
        panel.add(leftField, gbc);

        // Scaled Print Size section
        JPanel scalePanel = new JPanel(new GridBagLayout());
        scalePanel.setBorder(BorderFactory.createTitledBorder("Scaled Print Size"));

        gbc.gridx = 0; gbc.gridy = 0;
        scaleToFitMediaCheck = new JCheckBox("Scale to Fit Media");
        scalePanel.add(scaleToFitMediaCheck, gbc);

        gbc.gridy = 1;
        scalePanel.add(new JLabel("Scale:"), gbc);
        gbc.gridx = 1;
        scaleField = new JTextField("100", 5);
        scalePanel.add(scaleField, gbc);
        gbc.gridx = 2;
        scalePanel.add(new JLabel("%"), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        scalePanel.add(new JLabel("Height:"), gbc);
        gbc.gridx = 1;
        heightField = new JTextField("11", 5);
        scalePanel.add(heightField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        scalePanel.add(new JLabel("Width:"), gbc);
        gbc.gridx = 1;
        widthField = new JTextField("8.5", 5);
        scalePanel.add(widthField, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3;
        panel.add(scalePanel, gbc);

        // Orientation
        JPanel orientationPanel = new JPanel(new GridLayout(2, 1));
        orientationPanel.setBorder(BorderFactory.createTitledBorder("Orientation"));
        ButtonGroup orientationGroup = new ButtonGroup();
        portraitRadio = new JRadioButton("Portrait", true);
        landscapeRadio = new JRadioButton("Landscape");
        orientationGroup.add(portraitRadio);
        orientationGroup.add(landscapeRadio);
        orientationPanel.add(portraitRadio);
        orientationPanel.add(landscapeRadio);

        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(orientationPanel, gbc);

        // Selected Area
        JPanel selectedAreaPanel = new JPanel(new GridBagLayout());
        selectedAreaPanel.setBorder(BorderFactory.createTitledBorder("Print Selected Area"));

        gbc.gridx = 0; gbc.gridy = 0;
        printSelectedAreaCheck = new JCheckBox("Print Selected Area");
        selectedAreaPanel.add(printSelectedAreaCheck, gbc);

        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 3;
        panel.add(selectedAreaPanel, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton printButton = new JButton("Print");
        printButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        JButton pageSetupButton = new JButton("Page Setup...");
        pageSetupButton.addActionListener(e -> showPageSetup());

        panel.add(pageSetupButton);
        panel.add(printButton);
        panel.add(cancelButton);

        return panel;
    }

    private void showPrinterProperties() {
        PrintService selectedService = (PrintService) printerCombo.getSelectedItem();
        if (selectedService != null) {
            PrinterJob job = PrinterJob.getPrinterJob();
            try {
                job.setPrintService(selectedService);
                job.printDialog();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Cannot display printer properties: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showPageSetup() {
        pageFormat = printerJob.pageDialog(pageFormat);
        updatePageFormatFields();
    }

    private void updatePageFormatFields() {
        if (pageFormat.getOrientation() == PageFormat.PORTRAIT) {
            portraitRadio.setSelected(true);
        } else {
            landscapeRadio.setSelected(true);
        }
    }

    private JButton findButton(String text) {
        return findButton(this, text);
    }

    private JButton findButton(Container container, String text) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton && ((JButton) comp).getText().equals(text)) {
                return (JButton) comp;
            } else if (comp instanceof Container) {
                JButton button = findButton((Container) comp, text);
                if (button != null) return button;
            }
        }
        return null;
    }

    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }

    public PrinterJob getPrinterJob() { return printerJob; }
    public PageFormat getPageFormat() { return pageFormat; }
}