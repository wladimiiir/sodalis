package sk.magiksoft.sodalis.core.history;

import sk.magiksoft.sodalis.core.logger.LogInfo;

import java.util.List;

/**
 * @author wladimiiir
 */
public interface Historizable extends LogInfo {
    void addHistoryEvent(HistoryEvent event);

    List<HistoryEvent> getHistoryEvents(Long entityID);

    String getUpdater();
}
