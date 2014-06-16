
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.folkensemble.programme.action;

import sk.magiksoft.sodalis.core.action.AbstractExportAction;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.programme.ProgrammeContextManager;
import sk.magiksoft.sodalis.folkensemble.programme.data.ProgrammeDataManager;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 5, 2010
 * Time: 11:22:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProgrammeExportAction extends AbstractExportAction {
    @Override
    protected List<? extends Object> getExportItems(int exportType) {
        switch (exportType) {
            case EXPORT_TYPE_ALL:
                return ProgrammeDataManager.getInstance().getDatabaseEntities(Programme.class);
            case EXPORT_TYPE_SELECTED:
                return ProgrammeContextManager.getInstance().getContext().getSelectedEntities();
            default:
                return new ArrayList<Object>(0);
        }
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        return new ActionMessage(true, LocaleManager.getString("exportProgrammes"));
    }
}