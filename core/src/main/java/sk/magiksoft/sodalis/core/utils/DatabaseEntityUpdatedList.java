
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.utils;

import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 12/3/10
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseEntityUpdatedList<T extends DatabaseEntity> implements List<T>, DataListener {
    private Class<T> entityClass;
    private List<T> entities;

    public DatabaseEntityUpdatedList(Class<T> entityClass, List<T> entities) {
        this.entities = entities;
        this.entityClass = entityClass;
        DefaultDataManager.getInstance().addDataListener(this);
    }

    public DatabaseEntityUpdatedList(Class<T> entityClass) {
        this(entityClass, DefaultDataManager.getInstance().getDatabaseEntities(entityClass));
    }

    @Override
    public int size() {
        return entities.size();
    }

    @Override
    public boolean isEmpty() {
        return entities.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return entities.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return entities.iterator();
    }

    @Override
    public Object[] toArray() {
        return entities.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return entities.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return entities.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return entities.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return entities.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return entities.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return entities.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return entities.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return entities.retainAll(c);
    }

    @Override
    public void clear() {
        entities.clear();
    }

    @Override
    public T get(int index) {
        return entities.get(index);
    }

    @Override
    public T set(int index, T element) {
        return entities.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        entities.add(index, element);
    }

    @Override
    public T remove(int index) {
        return entities.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return entities.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return entities.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return entities.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return entities.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return entities.subList(fromIndex, toIndex);
    }


    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        for (DatabaseEntity entity : entities) {
            if (entityClass == entity.getClass()) {
                add((T) entity);
            }
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        boolean found;
        for (DatabaseEntity entity : entities) {
            found = false;
            for (T thisEntity : this.entities) {
                if (entity.getId().equals(thisEntity.getId())) {
                    thisEntity.updateFrom(entity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                add((T) entity);
            }
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        for (DatabaseEntity entity : entities) {
            for (T thisEntity : this.entities) {
                if (entity.getId().equals(thisEntity.getId())) {
                    remove(thisEntity);
                    break;
                }
            }
        }
    }
}