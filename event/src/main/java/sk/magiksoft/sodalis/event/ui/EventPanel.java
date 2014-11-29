package sk.magiksoft.sodalis.event.ui;

import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.event.entity.Event;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;

/**
 * @author wladimiiir
 */
public class EventPanel extends JComponent {

    private static final DateFormat TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);
    private static final Image REPEATING_IMAGE = ((ImageIcon) IconFactory.getInstance().getIcon("repeatEvent")).getImage();

    private Event event;
    private boolean selected = false;

    public EventPanel(Event event) {
        this.event = event;
        setToolTipText(event.createTooltipText());
    }

    public Event getEvent() {
        return event;
    }

    public void paint(Graphics g, int x, int y, int width, int height) {
        String value;
        Composite composite = ((Graphics2D) g).getComposite();
        Shape clip;

        g.setColor(event.getColor().darker().darker());
        g.drawLine(x + 1, y + height, x + width, y + height);
        g.drawLine(x + 2, y + height + 1, x + width + 1, y + height + 1);
        g.drawLine(x + 3, y + height + 2, x + width + 2, y + height + 2);
        g.drawLine(x + width, y, x + width, y + height);
        g.drawLine(x + width + 1, y + 1, x + width + 1, y + height + 1);
        g.drawLine(x + width + 2, y + 2, x + width + 2, y + height + 2);
        ((Graphics2D) g).setPaint(new GradientPaint(x + width / 2, y, event.getColor(), x + width / 2, y + height, event.getColor().darker()));
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.8));
        g.fillRect(x, y, width, height);
        ((Graphics2D) g).setComposite(composite);
        g.setColor(Color.BLACK);
        if (isSelected()) {
            g.setColor(Color.BLUE.darker().darker().darker());
            g.drawRect(x + 1, y + 1, width, height);
            g.drawRect(x, y, width, height);
            g.setColor(Color.BLACK);
        } else {
            g.drawRect(x, y, width, height);
        }
        if (event.isRepeating()) {
            g.drawImage(REPEATING_IMAGE, x + 7, y + 17, null);
        }

        clip = g.getClip();
        g.setClip(x, clip.getBounds().y, width - 1, clip.getBounds().height);
        value = TIME_FORMAT.format(event.getStartTime().getTime()) + " - " + TIME_FORMAT.format(event.getEndTime().getTime());
        g.setFont(g.getFont().deriveFont(Font.BOLD, 12f));
        g.drawString(event.getEventName(), x + 5, y + 15);
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 10f));
        g.drawString(value, (event.isRepeating() ? 15 : 0) + x + 5, y + 26);
        g.setClip(clip);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return event.toString();
    }
}
