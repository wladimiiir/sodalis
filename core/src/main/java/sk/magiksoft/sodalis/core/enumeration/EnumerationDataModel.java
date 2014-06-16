
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.enumeration;

import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

import javax.swing.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EnumerationDataModel extends AbstractListModel implements ComboBoxModel, DataListener {

    private Object selectedItem;
    private Enumeration enumeration;
    private boolean emptyElement;

    public EnumerationDataModel(Enumeration enumeration) {
        this(enumeration, false);
    }

    public EnumerationDataModel(Enumeration enumeration, boolean emptyElement) {
        this.enumeration = enumeration;
        this.emptyElement = emptyElement;
        EnumerationFactory.getInstance().addDataListener(this);
    }

    private Enumeration getEnumeration() {
        return enumeration;
    }

    @Override
    public int getSize() {
        return getEnumeration().getEntries().size() + (emptyElement ? 1 : 0);
    }

    @Override
    public Object getElementAt(int index) {
        if (emptyElement) {
            return index == 0 ? null : getEnumeration().getEntries().get(index - 1);
        } else {
            return getEnumeration().getEntries().get(index);
        }
    }

    @Override
    public void setSelectedItem(Object anItem) {
        this.selectedItem = anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }


    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Enumeration
                    && ((Enumeration) object).getName().equals(enumeration.getName())) {
                reloadEnumeration();
            }
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Enumeration
                    && ((Enumeration) object).getName().equals(enumeration.getName())) {
                reloadEnumeration();
            }
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Enumeration
                    && ((Enumeration) object).getName().equals(enumeration.getName())) {
                reloadEnumeration();
            }
        }
    }

    private void reloadEnumeration() {
        enumeration = EnumerationFactory.getInstance().getEnumeration(enumeration.getName());
        fireContentsChanged(this, 0, enumeration.getEntries().size() - 1);
    }
}