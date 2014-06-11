
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.context;

import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.entity.Entity;
import sk.magiksoft.sodalis.core.ui.controlpanel.InfoPanel;

import java.util.List;

/**
 * @author wladimiiir
 */
public interface Context {
    public boolean canChangeEntity();

    public void setSelectedCategories(List<Category> categories);

    public List<Category> getSelectedCategories();

    public void setSelectedInfoPanelClass(Class<? extends InfoPanel> infoPanelClass);

    public Class<? extends InfoPanel> getSelectedInfoPanelClass();

    public void setSelectedEntities(List<? extends Entity> entities);

    public List<? extends Entity> getSelectedEntities();

    public List<? extends Entity> getEntities();

    public void removeAllRecords();
}