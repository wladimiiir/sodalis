
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
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
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 5, 2010
 * Time: 11:25:16 AM
 * To change this template use File | Settings | File Templates.
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