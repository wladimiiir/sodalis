package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.folkensemble.member.action.ChangeMemberStatusAction;
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData;
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData.MemberStatus;
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberHistoryEvent;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.ui.PersonalDataInfoPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * goo
 *
 * @author wladimiiir
 */
public class MemberPersonalDataInfoPanel extends PersonalDataInfoPanel {
    private Action activateAction = new ChangeMemberStatusAction(MemberHistoryEvent.ACTIVATE);
    private Action deactivateAction = new ChangeMemberStatusAction(MemberHistoryEvent.DEACTIVATE);

    private JButton btnSetActive = new JButton();

    @Override
    public List<AbstractButton> getControlPanelButtons() {
        List<AbstractButton> buttons = new ArrayList<AbstractButton>();

        buttons.add(btnSetActive);
        return buttons;
    }

    @Override
    public void setupPanel(Object object) {
        super.setupPanel(object);
        if (!(object instanceof Person) || ((Person) object).getPersonData(MemberData.class) == null) {
            return;
        }
        Person member = (Person) object;

        final Action action = member.getPersonData(MemberData.class).getStatus() == MemberStatus.ACTIVE
                ? deactivateAction : activateAction;
        ((ChangeMemberStatusAction) action).setMember(member);
        btnSetActive.setAction(action);
    }
}
