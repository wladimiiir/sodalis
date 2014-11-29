package sk.magiksoft.sodalis.folkensemble.member.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.person.entity.PersonData;

/**
 * @author wladimiiir
 */
public class MemberData extends AbstractDatabaseEntity implements PersonData {

    public enum MemberStatus {
        ACTIVE,
        NON_ACTIVE;

        @Override
        public String toString() {
            switch (this) {
                case ACTIVE:
                    return LocaleManager.getString("MemberStatus.active");
                case NON_ACTIVE:
                    return LocaleManager.getString("MemberStatus.nonActive");
            }
            return "";
        }
    }

    private String memberID = "";
    private MemberStatus status = MemberStatus.ACTIVE;


    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof MemberData)) {
            return;
        }

        MemberData data = (MemberData) entity;

        this.memberID = data.memberID;
        this.status = data.status;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }
}
