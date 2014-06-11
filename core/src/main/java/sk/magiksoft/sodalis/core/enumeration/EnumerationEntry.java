
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
import sk.magiksoft.sodalis.core.filter.action.FilterObject;

import java.text.Collator;

/**
 *
 * @author wladimiiir
 */
public class EnumerationEntry extends AbstractDatabaseEntity implements Comparable<EnumerationEntry>, FilterObject {

    private static final long serialVersionUID = -1491383092454134615L;
    private String text;
    private Long entryID;

    public EnumerationEntry() {
        this.text = "";
    }

    public EnumerationEntry(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getEntryID() {
        return entryID;
    }

    public void setEntryID(Long entryID) {
        this.entryID = entryID;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof EnumerationEntry)) {
            return;
        }
        text = ((EnumerationEntry) entity).text;
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnumerationEntry other = (EnumerationEntry) obj;
        if (this.text != other.text && (this.text == null || !this.text.equals(other.text))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.text != null ? this.text.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(EnumerationEntry o) {
        return Collator.getInstance().compare(this.getText(), o.getText());
    }

    @Override
    public long getFilterID() {
        return entryID==null ? 0 : entryID;
    }
}