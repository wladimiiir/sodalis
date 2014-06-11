
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.ui.model;

import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public class DatabaseEntityComboBoxModel<T extends DatabaseEntity> extends DefaultComboBoxModel implements DataListener {

    private Class<T> clazz;
    private Vector<T> entities;

    public DatabaseEntityComboBoxModel(Class<T> clazz, Vector<T> v) {
        this(clazz, v, false);
    }

    public DatabaseEntityComboBoxModel(Class<T> clazz, Vector<T> v, boolean registerDataListener) {
        super(v);
        this.clazz = clazz;
        this.entities = v;
        if(registerDataListener){
            DefaultDataManager.getInstance().addDataListener(this);
        }
    }

    public static <T extends DatabaseEntity> DatabaseEntityComboBoxModel<T> createModel(Class<T> entityClass){
        final Vector<T> entities = new Vector<T>(DefaultDataManager.getInstance().getDatabaseEntities(entityClass));
        return new DatabaseEntityComboBoxModel<T>(entityClass, entities, true);
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