package sk.magiksoft.sodalis.folkensemble.member.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData.MemberStatus;

import java.util.Calendar;

/**
 * @author wladimiiir
 */
public class StatusHistory extends AbstractDatabaseEntity {
    private Calendar date;
    private MemberStatus status;

    public StatusHistory() {
    }

    public StatusHistory(Calendar date, MemberStatus status) {
        this.date = date;
        this.status = status;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }


    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
    }
}
