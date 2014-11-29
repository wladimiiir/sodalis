package sk.magiksoft.sodalis.core.data.remote.server;

import sk.magiksoft.sodalis.core.service.ServiceEvent;

import java.util.List;

/**
 * @author wladimiiir
 */
public class DataServiceEvent extends ServiceEvent {

    public static final int ACTION_RECORDS_ADDED = 1;
    public static final int ACTION_RECORDS_UPDATED = 2;
    public static final int ACTION_RECORDS_REMOVED = 3;

    private List records;

    public DataServiceEvent(int action, List records) {
        super(action);
        this.records = records;
    }

    public List getRecords() {
        return records;
    }
}
