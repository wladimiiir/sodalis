
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.programme.ui;

import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.folkensemble.programme.ProgrammeModule;

/**
 *
 * @author wladimiiir
 */
public class CategorizedProgrammeInfoPanel extends CategorizedEntityInfoPanel{

    @Override
    protected Class<? extends Module> getModuleClass() {
        return ProgrammeModule.class;
    }

}