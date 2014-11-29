package sk.magiksoft.sodalis.category.action;

import sk.magiksoft.sodalis.category.CategoryDataManager;
import sk.magiksoft.sodalis.category.CategoryManager;
import sk.magiksoft.sodalis.core.action.AbstractExportAction;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 * @since 2010/08/05
 */
public class CategoryExportAction extends AbstractExportAction {
    @Override
    protected List<? extends Object> getExportItems(int exportType) {
        switch (exportType) {
            case EXPORT_TYPE_ALL:
                return CategoryDataManager.getInstance().getRootCategory().getChildCategories();
            case EXPORT_TYPE_SELECTED:
                return CategoryManager.getInstance().getContext().getSelectedEntities();
            default:
                return new ArrayList<Object>(0);
        }
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        return new ActionMessage(true, LocaleManager.getString("exportCategories"));
    }
}
