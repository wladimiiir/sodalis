
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.inventory.ui;

import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.folkensemble.inventory.InventoryModule;

/**
 *
 * @author wladimiiir
 */
public class CategorizedInventoryInfoPanel extends CategorizedEntityInfoPanel{

    @Override
    protected Class<? extends Module> getModuleClass() {
        return InventoryModule.class;
    }

}