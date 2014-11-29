package sk.magiksoft.sodalis.folkensemble.member.entity;

import sk.magiksoft.hibernate.IntEnumUserType;
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData.MemberStatus;

/**
 * @author wladimiiir
 */
public class MemberStatusEnumType extends IntEnumUserType<MemberStatus> {

    public MemberStatusEnumType() {
        super(MemberStatus.class, MemberStatus.values());
    }
}
