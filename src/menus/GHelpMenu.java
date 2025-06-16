package menus;

import language.LanguageManager;
import language.LanguageSupport;
import menus.GMenuConstants.EHelpMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
public class GHelpMenu extends JMenu implements LanguageSupport {
    // attributes
    private static final long serialVersionUID = 1L;

    private JMenuItem systeminfo;
    private JMenuItem about;
    private JMenuItem online;

    // constructor
    public GHelpMenu(String label) {
        super(label);
        ActionHandler actionHandler = new ActionHandler();

        for (EHelpMenuItem eEditMenuItem : EHelpMenuItem.values()) {
            JMenuItem menuItem = new JMenuItem(eEditMenuItem.getText());
            menuItem.setActionCommand(eEditMenuItem.name());
            menuItem.addActionListener(actionHandler);
            switch (eEditMenuItem) {
                case eOnline:
                    menuItem.setAccelerator(KeyStroke.getKeyStroke(
                            KeyEvent.VK_F1, 0));
                    break;
                default:
                    break;
            }
            this.add(menuItem);
        }

        // 언어 변경 리스너 등록
        LanguageManager.getInstance().addLanguageChangeListener(() -> updateLanguage());
    }

    @Override
    public void updateLanguage() {
        // 메뉴 제목 업데이트
        setText(LanguageManager.getInstance().getText("help.menu"));

        // 각 메뉴 아이템 텍스트 업데이트
        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item != null) {
                String command = item.getActionCommand();
                if (command != null) {
                    try {
                        EHelpMenuItem menuItem = EHelpMenuItem.valueOf(command);
                        item.setText(menuItem.getText());
                    } catch (IllegalArgumentException e) {
                        // 무시 - 유효하지 않은 명령어
                    }
                }
            }
        }
    }
    // method
    public void initialize() {}

    public void about() {
        String aboutMessage = "Photoshop Version: 1.0 \n" +
                "Made by Suehyun Park\n"+
                "Copyright (C) 2025-2077 Systems Incorporated.\n All rights reserved.";
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), aboutMessage, "About Photoshop", JOptionPane.INFORMATION_MESSAGE);
    }

    public void online() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.adobe.com/products/photoshop.html"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Could not open online Photoshop.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void info() {
        StringBuilder infoText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("systeminfo.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                infoText.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Could not read system info file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextArea textArea = new JTextArea(infoText.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        //Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("OK");
        JButton copyButton = new JButton("Copy");
        buttonPanel.add(okButton);
        buttonPanel.add(copyButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "System Info", true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        okButton.addActionListener(e -> dialog.dispose());
        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(infoText.toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            JOptionPane.showMessageDialog(dialog, "System info copied to clipboard.", "Copied", JOptionPane.INFORMATION_MESSAGE);
        });

        dialog.setVisible(true);
    }


    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EHelpMenuItem eMenuItem = EHelpMenuItem.valueOf(e.getActionCommand());
            switch (eMenuItem) {
                case eAbout:
                    about();
                    break;
                case eOnline:
                    online();
                    break;
                case eSystemInfo:
                    info();
                    break;
                default:
                    break;
            }
        }
    }
}