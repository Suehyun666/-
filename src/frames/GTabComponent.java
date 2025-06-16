package frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GTabComponent extends JPanel {
    private final JTabbedPane pane;
    private final GTabManager tabManager;
    private final JLabel titleLabel;

    public GTabComponent(final JTabbedPane pane, String title, GTabManager tabManager) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.pane = pane;
        this.tabManager = tabManager;
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        // tab title
        this.titleLabel = new JLabel(title);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(titleLabel);

        // close button
        JButton closeButton = new TabButton();
        add(closeButton);
        closeButton.addActionListener(e -> {
            int index = pane.indexOfTabComponent(this);
            if (index != -1) {
                tabManager.closeTab(index);
            }
        });

        setupContextMenu();
    }

    private class TabButton extends JButton {
        private static final int SIZE = 17;

        public TabButton() {
            setPreferredSize(new Dimension(SIZE, SIZE));
            setToolTipText("close tab");
            setUI(new javax.swing.plaf.basic.BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            setRolloverEnabled(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    public void setTitle(String newTitle) {
        titleLabel.setText(newTitle);
    }

    public String getTitle() {
        return titleLabel.getText();
    }

    private void setupContextMenu() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showContextMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showContextMenu(e);
            }

            private void showContextMenu(MouseEvent e) {
                JPopupMenu popup = new JPopupMenu();

                // 새 탭
                JMenuItem newTab = new JMenuItem("New Tab");
                newTab.addActionListener(ev -> tabManager.createNewTab());

                // 탭 이름 변경
                JMenuItem renameTab = new JMenuItem("Rename Tab");
                renameTab.addActionListener(ev -> renameCurrentTab());

                // 탭 복제
                JMenuItem duplicateTab = new JMenuItem("Duplicate Tab");
                duplicateTab.addActionListener(ev -> duplicateCurrentTab());

                popup.add(newTab);
                popup.add(renameTab);
                popup.add(duplicateTab);
                popup.addSeparator();

                // 탭 닫기
                JMenuItem closeTab = new JMenuItem("Close Tab");
                closeTab.addActionListener(ev -> {
                    int index = pane.indexOfTabComponent(GTabComponent.this);
                    if (index != -1) tabManager.closeTab(index);
                });

                // 다른 탭들 닫기
                JMenuItem closeOthers = new JMenuItem("Close Other Tabs");
                closeOthers.addActionListener(ev -> {
                    int currentIndex = pane.indexOfTabComponent(GTabComponent.this);
                    tabManager.closeOtherTabs(currentIndex);
                });

                // 모든 탭 닫기
                JMenuItem closeAll = new JMenuItem("Close All Tabs");
                closeAll.addActionListener(ev -> tabManager.closeAllTabs());

                popup.add(closeTab);
                popup.add(closeOthers);
                popup.add(closeAll);

                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    private void renameCurrentTab() {
        String currentTitle = getTitle().replace("*", "");
        String newName = (String) JOptionPane.showInputDialog(
                this,
                "Enter new name:",
                "Rename Tab",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                currentTitle
        );

        if (newName != null && !newName.trim().isEmpty()) {
            String trimmedName = newName.trim();
            if (!trimmedName.equals(currentTitle)) {
                int tabIndex = pane.indexOfTabComponent(this);
                if (tabIndex != -1) {
                    GMainPanel panel = tabManager.getPanelAt(tabIndex);
                    if (panel != null) {
                        if (tabManager.setTabTitle(panel, trimmedName)) {
                            panel.setCurrentFile(null);
                        }
                    }
                }
            }
        }
    }

    private void duplicateCurrentTab() {
        int currentIndex = pane.indexOfTabComponent(this);
        if (currentIndex != -1) {
            GMainPanel sourcePanel = tabManager.getPanelAt(currentIndex);
            if (sourcePanel != null) {
                String baseName = sourcePanel.getFileName() + " copy";

                // 복제 로직은 아직.
                JOptionPane.showMessageDialog(this,
                        "Tab duplication feature will be implemented later.",
                        "Not Implemented",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}