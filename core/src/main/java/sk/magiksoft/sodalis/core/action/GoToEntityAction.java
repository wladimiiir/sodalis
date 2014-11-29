package sk.magiksoft.sodalis.core.action;

import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.entity.Entity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;

/**
 * @author wladimiiir
 */
public class GoToEntityAction extends ContextTransferAction {

    protected Entity entity;

    public GoToEntityAction(Class toModuleClass) {
        super(null, toModuleClass);
    }

    public GoToEntityAction(Class fromModuleClass, Class toModuleClass) {
        super(fromModuleClass, toModuleClass);
    }

    @Override
    public Action[] getContextActions() {
        return new Action[]{new BackAction()};
    }

    @Override
    protected boolean initialize(Context context) {
        context.setSelectedEntities(Collections.singletonList(entity));
        return true;
    }

    @Override
    protected void finalize(Context context) {
    }

    public void goTo(Entity entity) {
        this.entity = entity;
        actionPerformed(null);
    }

    private class BackAction extends AbstractAction {
        private BackAction() {
            super("<html><b>" + KeyEvent.getKeyText(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0).getKeyCode()) + "</b> " + LocaleManager.getString("backAction") + "</html>");
            putValue(KeyStroke.class.getName(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
            putValue(Action.SHORT_DESCRIPTION, LocaleManager.getString("backAction"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            contextActionPerformed();
        }
    }
}
