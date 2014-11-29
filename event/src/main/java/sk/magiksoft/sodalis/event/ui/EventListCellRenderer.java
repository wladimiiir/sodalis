package sk.magiksoft.sodalis.event.ui;

import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.event.entity.Event;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * @author wladimiiir
 */
public class EventListCellRenderer extends DefaultListCellRenderer {

    public static final int FORMAT_TIME = 1;
    public static final int FORMAT_DATE = 2;
    public static final int FORMAT_DATETIME = 3;
    private static final DateFormat TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);
    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
    private int format;

    public EventListCellRenderer(int format) {
        this.format = format;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (!(value instanceof Event)) {
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
        Event event = (Event) value;
        EventCellPanel eventCellPanel = new EventCellPanel();
        String text;

        eventCellPanel.setPreferredSize(new Dimension(100, 60));
        if (isSelected) {
            eventCellPanel.setBackground(new Color(120, 120, 255));
        } else {
            eventCellPanel.setBackground((index % 2) == 0 ? ColorList.LIGHTER_BLUE : ColorList.LIGHT_BLUE);
        }
        eventCellPanel.color = event.getColor();
        eventCellPanel.setToolTipText(event.createTooltipText());
        eventCellPanel.lblRepeating.setVisible(event.isRepeating());
        eventCellPanel.lblTime.setText(formatTime(FORMAT_TIME, event.getStartTime(), event.getEndTime()));
        if (event.isRepeating()) {
            text = formatTime(FORMAT_DATE, event.getRepeatStart(), event.getRepeatEnd());
        } else {
            text = formatTime(FORMAT_DATE, event.getStartTime(), event.getEndTime());
        }
        eventCellPanel.lblDate.setText(text);
        eventCellPanel.lblEventName.setText(event.getEventName() == null || event.getEventName().isEmpty() ? " " : event.getEventName());

        return eventCellPanel;
    }

    private String formatTime(int format, Calendar from, Calendar to) {
        String result;
        switch (format) {
            case FORMAT_TIME:
                return TIME_FORMAT.format(from.getTime()) + "-" + TIME_FORMAT.format(to.getTime());
            case FORMAT_DATE:
                result = DATE_FORMAT.format(from.getTime());
                return DATE_FORMAT.format(to.getTime()).equals(result)
                        ? result
                        : result + "-" + DATE_FORMAT.format(to.getTime());
            case FORMAT_DATETIME:
                result = DATE_FORMAT.format(from.getTime());
                return DATE_FORMAT.format(to.getTime()).equals(result)
                        ? result + " " + formatTime(FORMAT_TIME, from, to)
                        : result + " " + TIME_FORMAT.format(from.getTime()) + "-" + DATE_FORMAT.format(to.getTime()) + " " + TIME_FORMAT.format(from.getTime());
        }
        return "";
    }

    private class EventCellPanel extends JPanel {

        private JLabel lblTime = new JLabel();
        private JLabel lblDate = new JLabel();
        private JLabel lblEventName = new JLabel();
        private JLabel lblRepeating = new JLabel(IconFactory.getInstance().getIcon("repeatEvent"));
        private Color color;

        public EventCellPanel() {
            initComponents();
        }

        private void initComponents() {
            JPanel datePanel = new JPanel(new GridBagLayout());
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
            setLayout(new GridBagLayout());

            datePanel.setOpaque(false);
            lblDate.setFont(lblDate.getFont().deriveFont(10f));
            lblTime.setFont(lblTime.getFont().deriveFont(10f));
            lblTime.setFont(lblTime.getFont().deriveFont(Font.BOLD));
            lblEventName.setFont(lblEventName.getFont().deriveFont(12f));
            lblEventName.setFont(lblEventName.getFont().deriveFont(Font.BOLD));
            lblDate.setForeground(Color.GRAY.darker());
            lblTime.setForeground(Color.GRAY.darker());

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(1, 3, 0, 0);
            c.weightx = 1.0;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            add(lblEventName, c);
            c.gridy++;
            c.gridwidth = 1;
            c.weightx = 0.0;
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.WEST;
            add(lblRepeating, c);
            c.gridx++;
            c.weightx = 1.0;
            c.insets = new Insets(3, 3, 0, 0);
            add(lblDate, c);
            c.gridx = 0;
            c.gridwidth = 2;
            c.gridy++;
            c.insets = new Insets(0, 3, 0, 0);
            add(lblTime, c);

            lblEventName.setPreferredSize(new Dimension(1, 50));
            lblEventName.setMaximumSize(new Dimension(20, 50));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            int[] xs = new int[]{
                    this.getWidth() - 20 - 1, this.getWidth() - 1, this.getWidth() - 1
            };
            int[] ys = new int[]{
                    1, 1, 20
            };
            Composite composite = ((Graphics2D) g).getComposite();

            ((Graphics2D) g).setPaint(
                    new GradientPaint(getWidth() / 2, 0, color, getWidth() / 2, getHeight(), color.darker()));
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.8));
            g.fillPolygon(xs, ys, 3);
            ((Graphics2D) g).setComposite(composite);
        }
    }
}
