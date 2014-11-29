package sk.magiksoft.sodalis.event.ui;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.remote.DataManagerProvider;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.event.data.EventDataManager;
import sk.magiksoft.sodalis.event.entity.Event;
import sk.magiksoft.sodalis.event.entity.EventType;
import sk.magiksoft.sodalis.event.ui.event.EventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EventPopupMenu extends JPopupMenu implements DataListener {

    private List<EventListener> listeners = new ArrayList<EventListener>();
    private JMenu addEvent;
    private JMenuItem removeEvent;
    private JMenuItem removeFromRepeating;
    private Point point;

    public EventPopupMenu() {
        initPopupMenu();
        DataManagerProvider.addDataListener(this);
    }

    protected void initPopupMenu() {

        addEvent = new JMenu(LocaleManager.getString("addEvent"));
        removeEvent = new JMenuItem(new RemoveEventAction());
        removeFromRepeating = new JMenuItem(new RemoveFromRepeating());
        add(addEvent);
        add(removeEvent);
        add(removeFromRepeating);

        for (EventType eventType : EventDataManager.getInstance().getDatabaseEntities(EventType.class)) {
            addEvent.add(new EventTypeMenuItem(eventType));
        }
    }

    @Override
    public void show(Component invoker, int x, int y) {
        point = new Point(x, y);
        super.show(invoker, x, y);
    }

    public void addEventListener(EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (!(object instanceof EventType)) {
                continue;
            }
            addEvent.add(new EventTypeMenuItem((EventType) object));
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (!(object instanceof EventType)) {
                continue;
            }
            for (int i = 0; i < addEvent.getItemCount(); i++) {
                EventTypeMenuItem item = (EventTypeMenuItem) addEvent.getItem(i);

                if (item.eventType.getId().equals(((EventType) object).getId())) {
                    item.eventType.updateFrom((EventType) object);
                    item.setAction();
                    break;
                }
            }
        }

    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (!(object instanceof EventType)) {
                continue;
            }
            for (int i = 0; i < addEvent.getItemCount(); i++) {
                EventTypeMenuItem item = (EventTypeMenuItem) addEvent.getItem(i);

                if (item.eventType.getId().equals(((EventType) object).getId())) {
                    addEvent.remove(i);
                    break;
                }
            }
        }

    }

    public void setEventPanel(EventPanel eventPanel) {
        removeEvent.setEnabled(eventPanel != null);
        removeFromRepeating.setEnabled(eventPanel != null && eventPanel.getEvent().isRepeating());
    }

    private class RemoveEventAction extends AbstractAction {

        public RemoveEventAction() {
            super(LocaleManager.getString("removeEvent"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int result;

            result = ISOptionPane.showConfirmDialog(SodalisApplication.get().getMainFrame(),
                    LocaleManager.getString("removeEventConfirm"),
                    LocaleManager.getString("event"),
                    ISOptionPane.YES_NO_OPTION);
            if (result != ISOptionPane.YES_OPTION) {
                return;
            }
            for (EventListener eventListener : listeners) {
                eventListener.removeEvent(point);
            }
        }
    }

    private class EventTypeMenuItem extends JMenuItem {

        private EventType eventType;

        public EventTypeMenuItem(final EventType eventType) {
            this.eventType = eventType;
            setAction();
        }

        private void setAction() {
            setAction(new AbstractAction(eventType.getName()) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Event event = eventType.createEvent();
                    for (EventListener eventListener : listeners) {
                        eventListener.addEvent(event, point);
                    }
                }
            });
        }
    }

    private class RemoveFromRepeating extends AbstractAction {
        private RemoveFromRepeating() {
            super(LocaleManager.getString("removeFromRepeating"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (EventListener listener : listeners) {
                listener.removeFromRepeating(point);
            }
        }
    }
}
