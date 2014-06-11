
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.utils;

import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public class ListUpdater<T extends DatabaseEntity> implements DataListener {

    private Class<T> clazz;
    private Vector<T> entities;

    public ListUpdater(Class<T> clazz, Vector<T> v) {
        this.clazz = clazz;
        this.entities = v;
    }

    protected boolean acceptObject(Object object) {
        return object.getClass().equals(clazz);
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        records:
        for (DatabaseEntity entity : entities) {
            if (!acceptObject(entity)) {
                continue;
            }
            for (T thisEntity : this.entities) {
                if (thisEntity.getId().equals(entity.getId())) {
                    continue records;
                }
            }
            this.entities.add((T) entity);
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        List<DatabaseEntity> toRemove = new ArrayList<DatabaseEntity>();

        for (DatabaseEntity entity : entities) {
            if (!acceptObject(entity)) {
                continue;
            }
            for (T thisEntity : this.entities) {
                if (thisEntity.getId().equals(entity.getId())) {
                    if (entity.isDeleted()) {
                        toRemove.add(entity);
                    } else {
                        thisEntity.updateFrom(entity);
                    }
                    break;
                }
            }
        }

        entitiesRemoved(toRemove);
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        for (DatabaseEntity entity : entities) {
            if (!acceptObject(entity)) {
                continue;
            }
            for (T thisEntity : this.entities) {
                if (thisEntity.getId().equals(entity.getId())) {
                    entities.remove(thisEntity);
                    break;
                }
            }
        }
    }
}