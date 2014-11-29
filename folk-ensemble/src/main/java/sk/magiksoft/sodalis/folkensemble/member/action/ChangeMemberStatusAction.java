package sk.magiksoft.sodalis.folkensemble.member.action;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.function.ResultFunction;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.security.LoginManagerService;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.utils.CollectionUtils;
import sk.magiksoft.sodalis.folkensemble.member.MemberContextManager;
import sk.magiksoft.sodalis.folkensemble.member.data.MemberDataManager;
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData;
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberHistoryEvent;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PersonHistoryData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class ChangeMemberStatusAction extends AbstractAction {

    private int action;
    private Person member;

    public ChangeMemberStatusAction(int action) {
        super(action == MemberHistoryEvent.ACTIVATE
                ? LocaleManager.getString("setActiveMemberStatus")
                : LocaleManager.getString("setNonActiveMemberStatus"));
        this.action = action;
    }

    public void setMember(Person member) {
        this.member = member;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (member == null) {
            return;
        }

        List<Person> members = (List<Person>) MemberContextManager.getContext().getSelectedEntities();
        members = CollectionUtils.filter(members, new ResultFunction<Boolean, Person>() {
            @Override
            public Boolean apply(Person object) {
                return object != member && object.getPersonData(MemberData.class) != null
                        && object.getPersonData(MemberData.class).getStatus() == member.getPersonData(MemberData.class).getStatus();
            }
        });

        int result;
        if (!members.isEmpty()) {
            final Object[] options = {LocaleManager.getString("currentMember"), LocaleManager.getString("allSelectedMembers")};
            result = ISOptionPane.showOptionDialog(SodalisApplication.get().getMainFrame(), member.getFullName(false), action == MemberHistoryEvent.ACTIVATE
                    ? LocaleManager.getString("activate") : LocaleManager.getString("deactivate"),
                    JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    options, options[0]);
        } else {
            result = 0;
        }

        if (result == 0) {
            members = new LinkedList<Person>();
        }
        members.add(0, member);

        for (Person member : members) {
            MemberHistoryEvent event = member.getPersonData(PersonHistoryData.class).getCurrentHistoryEvent(MemberHistoryEvent.class);
            event = event == null || event.getAction() == MemberHistoryEvent.ACTIVATE
                    ? new MemberHistoryEvent(MemberHistoryEvent.DEACTIVATE)
                    : new MemberHistoryEvent(MemberHistoryEvent.ACTIVATE);
            event.setUpdater(SodalisApplication.get().getService(LoginManagerService.class, LoginManagerService.SERVICE_NAME).getLoggedSubjectUID());
            member.getPersonData(PersonHistoryData.class).addHistoryEvent(event);
            member.getPersonData(MemberData.class).setStatus(event.getAction() == MemberHistoryEvent.ACTIVATE
                    ? MemberData.MemberStatus.ACTIVE
                    : MemberData.MemberStatus.NON_ACTIVE);
            MemberDataManager.updateDatabaseEntity(member);
        }
    }

}
