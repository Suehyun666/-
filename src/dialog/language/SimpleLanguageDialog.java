package dialog.language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SimpleLanguageDialog extends JDialog {
    private String selectedLanguage = null;
    private JList<LanguageOption> languageList;
    private JButton startButton;
    private JButton cancelButton;

    // 언어 옵션 클래스
    private static class LanguageOption {
        private String code;
        private String displayName;
        private String flag;

        public LanguageOption(String code, String displayName, String flag) {
            this.code = code;
            this.displayName = displayName;
            this.flag = flag;
        }

        public String getCode() { return code; }
        public String getDisplayName() { return displayName; }

        @Override
        public String toString() {
            return flag + " " + displayName;
        }
    }

    public SimpleLanguageDialog() {
        super((Frame) null, "Select Language", true);
        initializeDialog();
        createComponents();
        layoutComponents();
        setupEventHandlers();

        setVisible(true);
    }

    private void initializeDialog() {
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        // 아이콘 설정 (선택사항)
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage("resources/icon.png");
            setIconImage(icon);
        } catch (Exception e) {
            // 아이콘 파일이 없어도 계속 진행
        }
    }

    private void createComponents() {
        // 언어 옵션들
        LanguageOption[] languages = {
                new LanguageOption("en", "English", "🇺🇸"),
                new LanguageOption("ko", "한국어", "🇰🇷"),
                new LanguageOption("ja", "日本語", "🇯🇵")
        };

        languageList = new JList<>(languages);
        languageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        languageList.setSelectedIndex(0); // 기본값: English
        languageList.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        languageList.setCellRenderer(new LanguageCellRenderer());

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(80, 30));
        startButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // 제목 레이블
        JLabel titleLabel = new JLabel("Choose your language:", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        // 리스트 패널
        JScrollPane scrollPane = new JScrollPane(languageList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        scrollPane.setPreferredSize(new Dimension(300, 120));

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        // Start 버튼 클릭
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectCurrentLanguage();
            }
        });

        // Cancel 버튼 클릭
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedLanguage = null;
                dispose();
            }
        });

        // 리스트 더블클릭
        languageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectCurrentLanguage();
                }
            }
        });

        // Enter 키 처리
        getRootPane().setDefaultButton(startButton);

        // ESC 키로 취소
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedLanguage = null;
                dispose();
            }
        });
    }

    private void selectCurrentLanguage() {
        LanguageOption selected = languageList.getSelectedValue();
        if (selected != null) {
            selectedLanguage = selected.getCode();
            dispose();
        }
    }

    // 커스텀 셀 렌더러
    private static class LanguageCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

            if (isSelected) {
                setBackground(new Color(100, 150, 255));
                setForeground(Color.WHITE);
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }

    public static String showDialog() {
        SimpleLanguageDialog dialog = new SimpleLanguageDialog();
        return dialog.selectedLanguage;
    }
}