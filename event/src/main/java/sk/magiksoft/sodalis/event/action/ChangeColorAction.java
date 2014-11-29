package sk.magiksoft.sodalis.event.action;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.event.EventContextManager;
import sk.magiksoft.sodalis.event.data.EventDataManager;
import sk.magiksoft.sodalis.event.entity.Event;
import sk.magiksoft.sodalis.event.settings.EventSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author wladimiiir
 */
public class ChangeColorAction extends AbstractAction implements PropertyChangeListener {

    private Color[] colors;
    private Color currentColor;
    private JPopupMenu popupMenu;

    public ChangeColorAction() {
        super("<html><b>F10</b> " + LocaleManager.getString("eventColor") + "</html>");
        currentColor = (Color) EventSettings.getInstance().getValue(EventSettings.O_EVENT_COLOR);
        putValue(AbstractAction.SMALL_ICON, new ColorIcon(currentColor));
        initColors();
        initPopupMenu();
        EventSettings.getInstance().addPropertyChangeListener(this);
    }

    private void changeColor(Color newColor) {
        Event selEvent;


        if (!EventContextManager.getInstance().getContext().getSelectedEntities().isEmpty()) {
            selEvent = (Event) EventContextManager.getInstance().getContext().getSelectedEntities().get(0);
            if (newColor.equals(selEvent.getColor())) {
                return;
            }
            selEvent.setColor(newColor);
            EventDataManager.getInstance().updateDatabaseEntity(selEvent);
        }
        EventSettings.getInstance().setValue(EventSettings.O_EVENT_COLOR, newColor);
        EventSettings.getInstance().save();
    }

    private void setColor(Color color) {
        this.currentColor = color;
        putValue(AbstractAction.SMALL_ICON, new ColorIcon(currentColor));
        firePropertyChange(AbstractAction.SMALL_ICON, null, getValue(AbstractAction.SMALL_ICON));
    }

    private void initColors() {
        colors = new Color[]{
                Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.RED, Color.GRAY, Color.MAGENTA, Color.ORANGE
        };
    }

    private void initPopupMenu() {
        popupMenu = new JPopupMenu();

        for (final Color color : colors) {
            popupMenu.add(new AbstractAction(" ", new ColorIcon(color, 100)) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    changeColor(color);
                }
            });
        }
        popupMenu.addSeparator();
        popupMenu.add(new AbstractAction(LocaleManager.getString("customColor")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "", currentColor);

                if (color == null) {
                    return;
                }

                changeColor(color);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i;
        Color newColor;

        if (e.getSource() instanceof JButton) {
            popupMenu.show((JButton) e.getSource(), 0, 0);
        } else {
            for (i = 0; i < colors.length; i++) {
                Color color = colors[i];
                if (color == currentColor) {
                    break;
                }
            }
            if (i + 1 > colors.length - 1) {
                newColor = colors[0];
            } else {
                newColor = colors[++i];
            }
            changeColor(newColor);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals(EventSettings.O_EVENT_COLOR)) {
            return;
        }
        Color newColor = (Color) evt.getNewValue();

        if (newColor.equals(this.currentColor)) {
            return;
        }

        setColor(newColor);
    }

    private class ColorIcon extends JComponent implements Icon {

        private Color color;
        private int width;

        public ColorIcon(Color color) {
            this.color = color;
            width = 12;
        }

        public ColorIcon(Color color, int width) {
            this.color = color;
            this.width = width;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, getIconWidth(), getIconHeight());
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return 12;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ColorIcon other = (ColorIcon) obj;
            if (this.color != other.color && (this.color == null || !this.color.equals(other.color))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + (this.color != null ? this.color.hashCode() : 0);
            return hash;
        }
    }
}
