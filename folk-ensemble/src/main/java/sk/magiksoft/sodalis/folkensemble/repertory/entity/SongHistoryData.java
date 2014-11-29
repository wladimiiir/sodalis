package sk.magiksoft.sodalis.folkensemble.repertory.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.history.HistoryEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wladimiiir
 */
public class SongHistoryData extends AbstractDatabaseEntity implements SongData {
    private List<HistoryEvent> historyEvents = new ArrayList<HistoryEvent>();

    public SongHistoryData() {
    }

    public List<HistoryEvent> getHistoryEvents() {
        return historyEvents;
    }

    public void setHistoryEvents(List<HistoryEvent> historyEvents) {
        this.historyEvents = historyEvents;
    }

    public void addHistoryEvent(HistoryEvent historyEvent) {
        this.historyEvents.add(historyEvent);
    }

    public HistoryEvent getCurrentHistoryEvent() {
        if (historyEvents.isEmpty()) {
            return null;
        }
        Collections.sort(historyEvents);
        return historyEvents.get(historyEvents.size() - 1);
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof SongHistoryData)
                || this == entity) {
            return;
        }
        SongHistoryData historyData = (SongHistoryData) entity;

        this.historyEvents.clear();
        this.historyEvents.addAll(historyData.historyEvents);
    }
}
