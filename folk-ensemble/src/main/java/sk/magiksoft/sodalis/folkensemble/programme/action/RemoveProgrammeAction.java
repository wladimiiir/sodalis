package sk.magiksoft.sodalis.folkensemble.programme.action;

import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.MessageAction;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.folkensemble.programme.data.ProgrammeDataManager;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class RemoveProgrammeAction extends MessageAction {

    private List<Programme> programmes;

    public RemoveProgrammeAction() {
        super("", IconFactory.getInstance().getIcon("remove"));
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        programmes = getAcceptedProgrammes(objects);
        return programmes.isEmpty() ? new ActionMessage(false, LocaleManager.getString("noProgrammeSelected"))
                : new ActionMessage(true, LocaleManager.getString("removeProgramme"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (programmes == null || programmes.isEmpty()) {
            return;
        }

        int result;
        if (programmes.size() == 1) {
            result = ISOptionPane.showConfirmDialog(null, LocaleManager.getString("removeProgrammeConfirm"), programmes.get(0).getName(), JOptionPane.YES_NO_OPTION);
        } else {
            result = ISOptionPane.showConfirmDialog(null, LocaleManager.getString("removeProgrammesConfirm"), "", JOptionPane.YES_NO_OPTION);
        }

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        for (Programme programme : programmes) {
            ProgrammeDataManager.getInstance().removeDatabaseEntity(programme);
        }
    }

    private List<Programme> getAcceptedProgrammes(List objects) {
        List<Programme> accepted = new ArrayList<Programme>();

        if (objects == null) {
            return accepted;
        }
        for (Object object : objects) {
            if (object instanceof Programme) {
                accepted.add((Programme) object);
            }
        }

        return accepted;
    }
}
