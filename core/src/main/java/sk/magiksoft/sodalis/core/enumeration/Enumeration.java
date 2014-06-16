
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

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wladimiiir
 */
public class Enumeration extends AbstractDatabaseEntity {
    private static final long serialVersionUID = -1l;

    private String name;
    private Class<? extends EnumerationInfo> enumerationInfoClass;
    private transient EnumerationInfo enumerationInfo;
    private List<EnumerationEntry> entries = new ArrayList<EnumerationEntry>();

    public Enumeration() {
    }

    public Enumeration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        String description = LocaleManager.getString(name);

        return description.equals("ERROR") ? name : description;
    }

    public Class<? extends EnumerationInfo> getEnumerationInfoClass() {
        return enumerationInfoClass;
    }

    public void setEnumerationInfoClass(Class<? extends EnumerationInfo> enumerationInfoClass) {
        this.enumerationInfoClass = enumerationInfoClass;
    }

    public EnumerationInfo getEnumerationInfo() {
        if (enumerationInfo == null) {
            try {
                enumerationInfo = enumerationInfoClass.newInstance();
            } catch (InstantiationException ex) {
                LoggerManager.getInstance().error(getClass(), ex);
            } catch (IllegalAccessException ex) {
                LoggerManager.getInstance().error(getClass(), ex);
            }
        }
        return enumerationInfo;
    }

    public void setEntries(List<EnumerationEntry> entries) {
        this.entries = entries;
    }

    public Enumeration addEntry(EnumerationEntry entry) {
        if (!entries.contains(entry)) {
            entries.add(entry);
            Collections.sort(entries);
        }
        return this;
    }

    public Enumeration removeEntry(EnumerationEntry entry) {
        entries.remove(entry);
        return this;
    }

    public List<EnumerationEntry> getEntries() {
        return entries;
    }

    public void saveEnumeration() {
        EnumerationFactory.getInstance().saveEnumeration(this);
    }

    public void reloadEnumeration() {
        EnumerationFactory.getInstance().loadEnumeration(this);
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof Enumeration)) {
            return;
        }
        Enumeration enumeration = (Enumeration) entity;
        this.name = enumeration.name;
        this.enumerationInfoClass = enumeration.enumerationInfoClass;
        this.entries = new ArrayList<EnumerationEntry>(enumeration.entries);
    }

    @Override
    public String toString() {
        return "Enumeration[name=" + name + ", entries=" + Arrays.toString(entries.toArray()) + "]";
    }


}