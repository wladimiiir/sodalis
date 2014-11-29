package sk.magiksoft.sodalis.folkensemble.member.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

/**
 * @author wladimiiir
 */
public class EnsembleMemberHistory extends AbstractDatabaseEntity {

    private String ensembleName;
    private String period;
    private String position;

    public EnsembleMemberHistory() {
        ensembleName = "";
        period = "";
        position = "";
    }

    public String getEnsembleName() {
        return ensembleName;
    }

    public String getPeriod() {
        return period;
    }

    public String getPosition() {
        return position;
    }

    public void setEnsembleName(String ensembleName) {
        this.ensembleName = ensembleName;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof EnsembleMemberHistory)) {
            return;
        }
        EnsembleMemberHistory history = (EnsembleMemberHistory) entity;
        this.ensembleName = history.ensembleName;
        this.period = history.period;
        this.position = history.position;
    }
}
