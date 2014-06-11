
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

import java.util.List;

/**
 * @author wladimiiir
 */
public interface DataListener {
    public void entitiesAdded(List<? extends DatabaseEntity> entities);

    public void entitiesUpdated(List<? extends DatabaseEntity> entities);

    public void entitiesRemoved(List<? extends DatabaseEntity> entities);
}