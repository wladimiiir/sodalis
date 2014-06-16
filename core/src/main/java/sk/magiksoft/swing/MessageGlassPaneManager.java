
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.swing;

import sk.magiksoft.sodalis.core.factory.ColorList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class MessageGlassPaneManager extends JPanel implements ActionListener {

    private JFrame frame;
    private boolean installed = false;
    private Component previousGlassPane;
    private Timer timer;
    private int delay = 3000;
    private JLabel label;
    private boolean firstTime = true;

    public MessageGlassPaneManager(JFrame frame) {
        this.frame = frame;
        initComponents();
    }

    private void initComponents() {
        label = new JLabel();
        label.setOpaque(false);
        label.setFont(label.getFont().deriveFont(20f));
        setOpaque(false);
        setLayout(new GridBagLayout());
        add(label, new GridBagConstraints());
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int aDelay) {
        delay = aDelay;
    }

    public void showMessage(String message) {
        showMessage(message, delay, null);
    }

    public void showMessage(String message, Color color) {
        showMessage(message, delay, color);
    }

    public void showMessage(final String message, final int delay, final Color color) {
        if (frame == null) {
            return;
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                label.setForeground(color == null ? Color.BLACK : color);
                label.setText(message);
                repaint();

                if (!installed) {
                    previousGlassPane = frame.getGlassPane();
                    frame.setGlassPane(MessageGlassPaneManager.this);
                    setVisible(true);
                    installed = true;
                    firstTime = true;
                }

                if (timer == null) {
                    timer = new Timer(delay, MessageGlassPaneManager.this);
                } else {
                    timer.stop();
                    timer.setDelay(delay);
                }

                timer.start();
            }
        });


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.stop();

        installed = false;
        setVisible(false);
        frame.setGlassPane(previousGlassPane);
        previousGlassPane = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = image.createGraphics();
        super.paintComponent(g2d);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension size = getSize();

        Font font = label.getFont();
        int arc = 5;
        int h = size.height;
        int w = size.width;

        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();
        Rectangle2D stringBounds = metrics.getStringBounds(label.getText(), g2d);

        int preferredWidth = label.getWidth() + 10;//(int) stringBounds.getWidth() + metrics.getHeight();
        int preferredHeight = label.getHeight() + 10;//(int) stringBounds.getHeight() + metrics.getHeight();

        w = Math.min(preferredWidth, w);
        h = Math.min(preferredHeight, h);

        int x = (size.width - w) / 2;
        int y = (size.height - h) / 2;

        g2d.setColor(new Color(ColorList.BLUE_1.getRed(), ColorList.BLUE_1.getGreen(), ColorList.BLUE_1.getBlue(), 100));
        g2d.fillRoundRect(x, y, w, h, arc, arc);

        g2d.setColor(Color.BLACK);
        x = (size.width - (int) stringBounds.getWidth()) / 2;
        y = (size.height / 2) + ((metrics.getAscent() - metrics.getDescent()) / 2);

        if (firstTime) {
            paintChildren(g2d);
            firstTime = false;
        }

        g.drawImage(image, 0, 0, null);
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JFrame getFrame() {
        return frame;
    }
}