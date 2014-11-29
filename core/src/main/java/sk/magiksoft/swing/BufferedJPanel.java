package sk.magiksoft.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author wladimiiir
 */
public class BufferedJPanel extends JPanel {

    public BufferedJPanel() {
        super(true);
    }

    public BufferedJPanel(LayoutManager layout) {
        super(layout, true);
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage image = new BufferedImage(g.getClipBounds().width, g.getClipBounds().height, BufferedImage.TYPE_INT_RGB);
        Graphics imageG = image.getGraphics();
        super.paint(imageG);
        g.drawImage(image, 0, 0, null);
    }
}
