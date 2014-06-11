
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.context;

import javax.swing.AbstractAction;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;

/**
 *
 * @author wladimiiir
 */
public class SubContext {

    private String name;
    private ObjectTableModel tableModel;
    private Class objectClass;
    private AbstractAction addAction;
    private DatabaseEntity selectedObject = null;

    public SubContext(String name, ObjectTableModel tableModel, Class objectClass, AbstractAction addAction) {
        this.name = name;
        this.tableModel = tableModel;
        this.objectClass = objectClass;
        this.addAction = addAction;
    }

    public AbstractAction getAddAction() {
        return addAction;
    }

    public String getName() {
        return name;
    }

    public Class getObjectClass() {
        return objectClass;
    }

    public ObjectTableModel getTableModel() {
        return tableModel;
    }

    @Override
    public String toString() {
        return name;
    }

    public DatabaseEntity getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(DatabaseEntity selectedObject) {
        this.selectedObject = selectedObject;
    }
}