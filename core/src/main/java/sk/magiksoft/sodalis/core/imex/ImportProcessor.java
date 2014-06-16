
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.imex;

import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/21/10
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ImportProcessor<T extends DatabaseEntity> {
    T processImport(T entity);

    T findSimilarEntity(T entity);
}