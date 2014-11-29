package sk.magiksoft.sodalis.category.action;

import sk.magiksoft.sodalis.category.CategoryManager;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.AbstractImportAction;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 * @since 2010/08/05
 */
public class CategoryImportAction extends AbstractImportAction {
    @Override
    protected void importObjects(List objects) {
        List<Category> categories = new LinkedList<Category>();

        for (Object object : objects) {
            if (object instanceof Category) {
                categories.add((Category) object);
            }
        }

        CategoryManager.getInstance().importCategories(categories);
        SodalisApplication.get().showMessage(LocaleManager.getString("categoriesImported", categories.size()));
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        return new ActionMessage(true, LocaleManager.getString("importCategories"));
    }
}
