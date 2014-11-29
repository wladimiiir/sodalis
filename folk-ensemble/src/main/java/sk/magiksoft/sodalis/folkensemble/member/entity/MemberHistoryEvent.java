package sk.magiksoft.sodalis.folkensemble.member.entity;

import sk.magiksoft.sodalis.core.history.HistoryEvent;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

/**
 * @author wladimiiir
 */
public class MemberHistoryEvent extends HistoryEvent {
    public static final int DEACTIVATE = 4;
    public static final int ACTIVATE = 5;

    public MemberHistoryEvent() {
    }

    public MemberHistoryEvent(int action) {
        super(action);

        switch (action) {
            case DEACTIVATE:
                this.actionName = LocaleManager.getString("deactivated");
                break;
            case ACTIVATE:
                this.actionName = LocaleManager.getString("activated");
                break;
        }
    }
}
