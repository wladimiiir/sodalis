
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.action;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.module.Module;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author wladimiiir
 */
public abstract class ContextTransferAction extends AbstractAction {

    private Class fromModuleClass;
    private Class toModuleClass;
    private Module module;

    public ContextTransferAction(Class fromModuleClass, Class toModuleClass) {
        this.fromModuleClass = fromModuleClass;
        this.toModuleClass = toModuleClass;
    }

    protected abstract boolean initialize(Context context);

    /**
     * Finalizes action
     *
     * @param context used context if OK action, null if action has been canceled
     */
    protected abstract void finalize(Context context);

    public Action[] getContextActions() {
        return new Action[]{new ContextOKAction(), new ContextCancelAction()};
    }

    public void contextActionPerformed() {
        removeActionsFromInputMap();
        if (SodalisApplication.get().loadModule(fromModuleClass) == null) {
            return;
        }

        finalize(module.getContextManager().getContext());
    }

    protected void removeActionsFromInputMap() {
        for (Action contextAction : getContextActions()) {
            if (contextAction.getValue(KeyStroke.class.getName()) instanceof KeyStroke) {
                SodalisApplication.get().getInputMap().remove((KeyStroke) contextAction.getValue(KeyStroke.class.getName()));
            }
        }
    }

    public void contextActionCanceled() {
        removeActionsFromInputMap();
        if (SodalisApplication.get().loadModule(fromModuleClass) == null) {
            return;
        }

        finalize(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        module = SodalisApplication.get().loadModule(toModuleClass);

        if (module == null) {
            return;
        }
        initialize(module.getContextManager().getContext());
        if (fromModuleClass != null) {
            SodalisApplication.get().registerContextTransferAction(this);
        }
    }

    private class ContextOKAction extends AbstractAction {

        public ContextOKAction() {
            super("<html><b>" + KeyEvent.getKeyText(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0).getKeyCode()) + "</b> " + LocaleManager.getString("okAction") + "</html>");
            putValue(KeyStroke.class.getName(), KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
            putValue(Action.SHORT_DESCRIPTION, LocaleManager.getString("okAction"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            contextActionPerformed();
        }
    }

    private class ContextCancelAction extends AbstractAction {

        public ContextCancelAction() {
            super("<html><b>" + KeyEvent.getKeyText(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0).getKeyCode()) + "</b> " + LocaleManager.getString("cancelAction") + "</html>");
            putValue(KeyStroke.class.getName(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
            putValue(Action.SHORT_DESCRIPTION, LocaleManager.getString("cancelAction"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            contextActionCanceled();
        }
    }

}