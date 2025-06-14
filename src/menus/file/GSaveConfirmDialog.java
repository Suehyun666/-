package menus.file;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GSaveConfirmDialog extends JDialog {
    public static final int SAVE_OPTION = 0;
    public static final int DONT_SAVE_OPTION = 1;
    public static final int CANCEL_OPTION = 2;

    private int result = CANCEL_OPTION; // 기본값을 CANCEL로 설정

    public GSaveConfirmDialog(Frame parent, String tabTitle) {
        super(parent, "저장 확인", true);
        initializeDialog(tabTitle);
    }

    private void initializeDialog(String tabTitle) {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        // messege panel
        JPanel messagePanel = new JPanel(new FlowLayout());
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
        JLabel messageLabel = new JLabel(tabTitle + "의 변경내용을 저장할까요?");
        messagePanel.add(iconLabel);
        messagePanel.add(messageLabel);
        add(messagePanel, BorderLayout.CENTER);

        // button
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton saveButton = new JButton("저장");
        JButton dontSaveButton = new JButton("저장 안 함");
        JButton cancelButton = new JButton("취소");

        saveButton.addActionListener(e -> {
            result = SAVE_OPTION;
            dispose();
        });

        dontSaveButton.addActionListener(e -> {
            result = DONT_SAVE_OPTION;
            dispose();
        });

        cancelButton.addActionListener(e -> {
            result = CANCEL_OPTION;
            dispose();
        });
        //keyboard event
        getRootPane().setDefaultButton(saveButton);

        // ESC 키 처리
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = CANCEL_OPTION;
                dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(dontSaveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        //
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                result = CANCEL_OPTION;
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(getParent());
    }

    private int showDialog() {
        setVisible(true);
        return result;
    }

    public static int showSaveConfirmDialog(Frame parent, String tabTitle) {
        GSaveConfirmDialog dialog = new GSaveConfirmDialog(parent, tabTitle);
        return dialog.showDialog();
    }
}