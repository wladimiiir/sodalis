
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.factory;

import sk.magiksoft.sodalis.core.entity.PostCreation;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wladimiiir
 */
public class EntityFactory {
    private static EntityFactory instance = new EntityFactory();

    private Map<Class, Set<Object>> propertiesMap = new HashMap<Class, Set<Object>>();

    public EntityFactory() {
    }

    public static EntityFactory getInstance() {
        if (instance == null) {
            instance = new EntityFactory();
        }

        return instance;
    }

    public void registerEntityProperties(Class entityClass, Object... properties) {
        Set<Object> objects = propertiesMap.get(entityClass);
        if (objects == null) {
            objects = new HashSet<Object>();
            propertiesMap.put(entityClass, objects);
        }
        for (Object property : properties) {
            objects.add(property);
        }
    }

    public <T> T createEntity(Class<T> entityClass) {
        return createEntity(entityClass, propertiesMap.containsKey(entityClass) ? propertiesMap.get(entityClass).toArray() : new Object[0]);
    }

    public <T> T createEntity(Class<T> entityClass, Object[] properties) {
        try {
            T entity = entityClass.newInstance();

            for (Method method : entityClass.getMethods()) {
                if (method.getAnnotation(PostCreation.class) != null) {
                    if (method.getParameterTypes().length == 0) {
                        method.invoke(entity);
                    } else {
                        method.invoke(entity, (Object) properties);
                    }
                }
            }

            return entity;
        } catch (IllegalArgumentException ex) {
            LoggerManager.getInstance().error(EntityFactory.class, ex);
        } catch (InvocationTargetException ex) {
            LoggerManager.getInstance().error(EntityFactory.class, ex);
        } catch (InstantiationException ex) {
            LoggerManager.getInstance().error(EntityFactory.class, ex);
        } catch (IllegalAccessException ex) {
            LoggerManager.getInstance().error(EntityFactory.class, ex);
        }
        return null;
    }
}