package sk.magiksoft.sodalis.event.action;

import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.remote.DataManagerProvider;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.event.data.EventDataManager;
import sk.magiksoft.sodalis.event.entity.EventType;
import sk.magiksoft.sodalis.event.settings.EventSettings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * @author wladimiiir
 */
public class DefaultEventTypeAction extends AbstractAction implements PropertyChangeListener, DataListener {

    private EventType defaultEventType;
    private List<EventType> eventTypes;

    public DefaultEventTypeAction() {
        eventTypes = EventDataManager.getInstance().getDatabaseEntities(EventType.class);
        EventSettings.getInstance().addPropertyChangeListener(this);
        DataManagerProvider.addDataListener(this);
        setDefaultEventType();
    }

    private void refreshName() {
        putValue(AbstractAction.NAME, "<html><b>F11 </b>" + LocaleManager.getString("defaultType") + ": "
                + (defaultEventType == null ? "" : defaultEventType.getName()) + "</html>");
    }

    private void setDefaultEventType() {
        Long eventTypeID = EventSettings.getInstance().getLong(EventSettings.L_DEFAULT_EVENT_TYPE_ID);

        defaultEventType = null;
        for (EventType eventType : eventTypes) {
            if (eventType.getId().equals(eventTypeID)) {
                defaultEventType = eventType;
                break;
            }
        }
        refreshName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i;

        for (i = 0; i < eventTypes.size(); i++) {
            if (defaultEventType == null) {
                break;
            }

            EventType eventType = eventTypes.get(i);
            if (eventType.getId().equals(defaultEventType.getId())) {
                i++;
                break;
            }
        }
        if (i >= eventTypes.size()) {
            i = 0;
        }
        EventSettings.getInstance().setValue(EventSettings.L_DEFAULT_EVENT_TYPE_ID, eventTypes.get(i).getId());
        EventSettings.getInstance().save();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals(EventSettings.L_DEFAULT_EVENT_TYPE_ID)) {
            return;
        }
        setDefaultEventType();
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof EventType) {
                eventTypes = EventDataManager.getInstance().getDatabaseEntities(EventType.class);
                setDefaultEventType();
                return;
            }
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof EventType) {
                eventTypes = EventDataManager.getInstance().getDatabaseEntities(EventType.class);
                setDefaultEventType();
                return;
            }
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof EventType) {
                eventTypes = EventDataManager.getInstance().getDatabaseEntities(EventType.class);
                setDefaultEventType();
                return;
            }
        }
    }
}
