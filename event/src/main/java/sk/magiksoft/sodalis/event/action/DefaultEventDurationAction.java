package sk.magiksoft.sodalis.event.action;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.event.settings.EventSettings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author wladimiiir
 */
public class DefaultEventDurationAction extends AbstractAction implements PropertyChangeListener {

    private int defaultEventDuration;
    private int[] durations;

    public DefaultEventDurationAction() {
        defaultEventDuration = EventSettings.getInstance().getInt(EventSettings.I_EVENT_DURATION);
        initDurations();
        refreshName();
        EventSettings.getInstance().addPropertyChangeListener(this);
    }

    private void initDurations() {
        durations = new int[]{15, 20, 25, 30, 45, 60, 90, 120};
    }

    private void refreshName() {
        putValue(AbstractAction.NAME, "<html><b>F12 </b>" + LocaleManager.getString("defaultDuration") + ": " + defaultEventDuration + " min.</html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i;

        for (i = 0; i < durations.length; i++) {
            int duration = durations[i];
            if (duration == defaultEventDuration) {
                break;
            }
        }
        i++;
        if (i >= durations.length) {
            i = 0;
        }
        EventSettings.getInstance().setValue(EventSettings.I_EVENT_DURATION, durations[i]);
        EventSettings.getInstance().save();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals(EventSettings.I_EVENT_DURATION)) {
            return;
        }

        defaultEventDuration = (Integer) evt.getNewValue();
        refreshName();
    }


}
