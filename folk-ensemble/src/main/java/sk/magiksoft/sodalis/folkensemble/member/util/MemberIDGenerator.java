package sk.magiksoft.sodalis.folkensemble.member.util;

import sk.magiksoft.sodalis.folkensemble.member.settings.MemberSettings;

import java.text.DecimalFormat;

/**
 * @author wladimiiir
 */
public class MemberIDGenerator {

    private MemberIDGenerator() {
    }

    public static String getNextMemberID() {
        int memberIDNumber = MemberSettings.getInstance().getInt(MemberSettings.I_MEMBER_ID_NUMBER) + 1;
        String memberID = MemberSettings.getInstance().getString(MemberSettings.S_MEMBER_ID_PREFIX);

        memberID += new DecimalFormat("000000").format(memberIDNumber);
        MemberSettings.getInstance().setValue(MemberSettings.I_MEMBER_ID_NUMBER, memberIDNumber);
        MemberSettings.getInstance().save();
        return memberID;
    }
}
