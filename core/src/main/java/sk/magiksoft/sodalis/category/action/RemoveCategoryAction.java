
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.category.action;

import java.awt.event.ActionEvent;
import java.util.List;
import sk.magiksoft.sodalis.category.CategoryDataManager;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.MessageAction;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;

/**
 *
 * @author wladimiiir
 */
public class RemoveCategoryAction extends MessageAction {

    private Category category;

    public RemoveCategoryAction() {
        super(null, IconFactory.getInstance().getIcon("remove"));
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        category = (Category) (objects==null || objects.isEmpty() || !(objects.get(0) instanceof Category)
                ? null : objects.get(0));

        if (category==null) {
            return new ActionMessage(false, LocaleManager.getString("noCategorySelected"));
        } else if (category.getParentCategory() == null) {
            return new ActionMessage(false, LocaleManager.getString("parentCategoryCannotBeDeleted"));
        } else if (!category.getChildCategories().isEmpty()) {
            return new ActionMessage(false, LocaleManager.getString("categoryContainsSubCategories"));
        } else {
            return new ActionMessage(true, LocaleManager.getString("removeCategory"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (category == null) {
            return;
        }

        if(!CategoryDataManager.getInstance().canDelete(category)){
            ISOptionPane.showMessageDialog(null, LocaleManager.getString("cannotRemoveReferencedCategory"));
            return;
        }

        if (ISOptionPane.showConfirmDialog(null,
                LocaleManager.getString("removeCategoryConfirm"), category.getName(),
                ISOptionPane.YES_NO_OPTION) != ISOptionPane.YES_OPTION) {
            return;
        }

        CategoryDataManager.getInstance().removeDatabaseEntity(category);
    }
}