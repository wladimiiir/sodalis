package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.history.HistoryEvent;
import sk.magiksoft.sodalis.core.history.HistoryInfoPanel;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberHistoryEvent;

import javax.swing.*;

/**
 * @author wladimiiir
 */
public class MemberHistoryInfoPanel extends HistoryInfoPanel {

    protected JCheckBox chbActivate;
    protected JCheckBox chbDeactivate;

    @Override
    protected void initControlPanelButtons() {
        super.initControlPanelButtons();
        chbActivate = new JCheckBox(LocaleManager.getString("activated"), true);
        chbDeactivate = new JCheckBox(LocaleManager.getString("deactivated"), true);
        chbActivate.addActionListener(buttonListener);
        chbDeactivate.addActionListener(buttonListener);
        controlPanelButtons.add(chbActivate);
        controlPanelButtons.add(chbDeactivate);

        chbCreate.setName("MemberHistoryInfoPanel.create");
        chbUpdate.setName("MemberHistoryInfoPanel.update");
        chbActivate.setName("MemberHistoryInfoPanel.activate");
        chbDeactivate.setName("MemberHistoryInfoPanel.deactivate");

        for (AbstractButton abstractButton : controlPanelButtons) {
            SodalisApplication.get().getStorageManager().registerComponentForStorage(abstractButton.getName(), abstractButton);
            abstractButton.setName(null);
        }
    }

    @Override
    protected boolean acceptHistoryEvent(HistoryEvent event) {
        switch (event.getAction()) {
            case MemberHistoryEvent.ACTIVATE:
                return chbActivate.isSelected();
            case MemberHistoryEvent.DEACTIVATE:
                return chbDeactivate.isSelected();
        }


        return super.acceptHistoryEvent(event);
    }
}
