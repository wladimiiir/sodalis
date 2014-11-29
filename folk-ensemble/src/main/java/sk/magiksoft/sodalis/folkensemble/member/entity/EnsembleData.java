package sk.magiksoft.sodalis.folkensemble.member.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.person.entity.PersonData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EnsembleData extends AbstractDatabaseEntity implements PersonData {

    private EnsembleGroup ensembleGroup = new EnsembleGroup();
    private List<EnsembleMemberHistory> memberHistories = new ArrayList<EnsembleMemberHistory>();

    public void setEnsembleGroup(EnsembleGroup group) {
        this.ensembleGroup = group;
    }

    public void setMemberHistories(List<EnsembleMemberHistory> memberHistories) {
        this.memberHistories = memberHistories;
    }

    public EnsembleGroup getEnsembleGroup() {
        return ensembleGroup;
    }

    public List<EnsembleMemberHistory> getMemberHistories() {
        return memberHistories;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof EnsembleData)) {
            return;
        }
        EnsembleData data = (EnsembleData) entity;
        this.ensembleGroup = data.ensembleGroup;
        this.memberHistories = new ArrayList<EnsembleMemberHistory>(data.memberHistories);
    }
}
