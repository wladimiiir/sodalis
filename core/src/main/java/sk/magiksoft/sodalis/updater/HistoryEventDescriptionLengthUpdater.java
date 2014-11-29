package sk.magiksoft.sodalis.updater;

import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.update.updater.Updater;

/**
 * @author wladimiiir
 * @since 2011/2/6
 */
public class HistoryEventDescriptionLengthUpdater implements Updater {
    @Override
    public void runUpdate() throws Exception {
        DefaultDataManager.getInstance().executeSQLQuery("alter table historyevent alter column description varchar(65536)");
    }
}
