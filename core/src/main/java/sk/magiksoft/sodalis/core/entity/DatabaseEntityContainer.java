
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

import java.util.List;

/**
 * @author wladimiiir
 */
public interface DatabaseEntityContainer {
    <T extends DatabaseEntity> boolean acceptDatabaseEntity(Class<T> clazz);

    <T extends DatabaseEntity> T getDatabaseEntity(Class<T> clazz);

    <T extends DatabaseEntity> List<T> getDatabaseEntities(Class<T> clazz);
}