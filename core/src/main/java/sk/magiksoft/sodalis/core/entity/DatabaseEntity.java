
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.entity;

import java.io.Serializable;

/**
 * @author wladimiiir
 */
public interface DatabaseEntity extends Serializable, Cloneable, Entity {
    void setId(Long id);

    Long getId();

    void setInternalID(Long internalID);

    Long getInternalID();

    void setUpdater(String updater);

    String getUpdater();

    void updateFrom(DatabaseEntity entity);

    boolean isDeleted();

    void clearIDs();

    Object clone();
}