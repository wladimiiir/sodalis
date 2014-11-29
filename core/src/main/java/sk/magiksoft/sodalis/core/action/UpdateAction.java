package sk.magiksoft.sodalis.core.action;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.update.UpdateManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wladimiiir
 */
public class UpdateAction extends AbstractAction {

    public UpdateAction() {
        super(LocaleManager.getString("update"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UpdateManager.getInstance().doUpdate();
    }

}
