
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.treetable;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * @author wladimiiir
 */
public abstract class ObjectTreeTableModel extends AbstractTreeTableModel {

    private String[] columnNames;
    private Class[] columnClasses;

    public ObjectTreeTableModel(TreeNode root, String[] columnNames) {
        super(root);
        this.columnNames = columnNames;
    }

    public ObjectTreeTableModel(TreeNode root, String[] columnNames, Class[] columnClasses) {
        this(root, columnNames);
        this.columnClasses = columnClasses;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return columnClasses == null ? super.getColumnClass(column) : columnClasses[column];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((TreeNode) parent).getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((TreeNode) parent).getChildCount();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((TreeNode) parent).getIndex((TreeNode) child);
    }

    public void fireTreeTableModelChanged(TreePath treePath) {
        modelSupport.fireTreeStructureChanged(treePath);
    }
}