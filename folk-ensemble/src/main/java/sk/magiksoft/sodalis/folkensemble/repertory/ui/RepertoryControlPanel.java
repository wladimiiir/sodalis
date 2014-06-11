
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.repertory.ui;

import sk.magiksoft.sodalis.core.ui.controlpanel.DefaultControlPanel;
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;

/**
 * @author wladimiiir
 */
public class RepertoryControlPanel extends DefaultControlPanel {

    public RepertoryControlPanel() {
        super("repertory");
    }

    @Override protected void saveObject(Object object) {
        RepertoryDataManager.getInstance().updateSong((Song) object);
    }
}