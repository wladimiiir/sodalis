
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.enumeration;

import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/19/10
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnumerationList implements List<EnumerationEntry>, DataListener {
    private static final Enumeration EMPTY_ENUMERATION = new Enumeration();

    private Enumeration enumeration;

    public EnumerationList(Enumeration enumeration) {
        this.enumeration = enumeration == null ? EMPTY_ENUMERATION : enumeration;
    }

    @Override
    public int size() {
        return enumeration.getEntries().size();
    }

    @Override
    public boolean isEmpty() {
        return enumeration.getEntries().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return enumeration.getEntries().contains(o);
    }

    @Override
    public Iterator<EnumerationEntry> iterator() {
        return enumeration.getEntries().iterator();
    }

    @Override
    public Object[] toArray() {
        return enumeration.getEntries().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return enumeration.getEntries().toArray(a);
    }

    @Override
    public boolean add(EnumerationEntry enumerationEntry) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return enumeration.getEntries().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends EnumerationEntry> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends EnumerationEntry> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public EnumerationEntry get(int index) {
        return enumeration.getEntries().get(index);
    }

    @Override
    public EnumerationEntry set(int index, EnumerationEntry element) {
        return null;
    }

    @Override
    public void add(int index, EnumerationEntry element) {
    }

    @Override
    public EnumerationEntry remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return enumeration.getEntries().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return enumeration.getEntries().lastIndexOf(o);
    }

    @Override
    public ListIterator<EnumerationEntry> listIterator() {
        return enumeration.getEntries().listIterator();
    }

    @Override
    public ListIterator<EnumerationEntry> listIterator(int index) {
        return enumeration.getEntries().listIterator(index);
    }

    @Override
    public List<EnumerationEntry> subList(int fromIndex, int toIndex) {
        return enumeration.getEntries().subList(fromIndex, toIndex) ;
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        for (DatabaseEntity entity : entities) {
            if(entity instanceof Enumeration && entity.getId().equals(enumeration.getId())){
                enumeration.setEntries(((Enumeration) entity).getEntries());
            }
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        for (DatabaseEntity entity : entities) {
            if(entity instanceof Enumeration && entity.getId().equals(enumeration.getId())){
                ((Enumeration) entity).setEntries(Collections.<EnumerationEntry>emptyList());
            }
        }
    }
}