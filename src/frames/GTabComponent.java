package frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GTabComponent extends JPanel {
    private final JTabbedPane pane;
    private final GMainFrame mainFrame;
    private final JLabel titleLabel;
    public GTabComponent(final JTabbedPane pane, String title, GMainFrame mainFrame) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.pane = pane;
        this.mainFrame = mainFrame;
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        setOpaque(false);

        // tab title
        this.titleLabel = new JLabel(title);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(titleLabel);

        // close bt
        JButton closeButton = new TabButton();
        add(closeButton);
        closeButton.addActionListener(e -> {
            int index = pane.indexOfTabComponent(this);
            if (index != -1) {
                mainFrame.closeTab(index);
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
    public void setTitle(String newTitle) {titleLabel.setText(newTitle);}
    public String getTitle() {return titleLabel.getText();}
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

                JMenuItem close = new JMenuItem("close tab");
                close.addActionListener(ev -> {
                    int index = pane.indexOfTabComponent(GTabComponent.this);
                    if (index != -1) mainFrame.closeTab(index);
                });

                JMenuItem closeOthers = new JMenuItem("close others");
                closeOthers.addActionListener(ev -> {
                    int currentIndex = pane.indexOfTabComponent(GTabComponent.this);
                    for (int i = pane.getTabCount() - 1; i >= 0; i--) {
                        if (i != currentIndex) mainFrame.closeTab(i);
                    }
                });

                popup.add(close);
                popup.add(closeOthers);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }
}