
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.category.ui;

import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.core.treetable.ObjectTreeTableModel;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author wladimiiir
 */
public class CategoryTreeTableModelWrapper extends ObjectTreeTableModel {

    private ObjectTableModel tableModel;

    public CategoryTreeTableModelWrapper(ObjectTableModel tableModel) {
        super(new DefaultMutableTreeNode(), new String[]{LocaleManager.getString("category")});
        this.tableModel = tableModel;
    }

    @Override
    public int getColumnCount() {
        return tableModel.getColumnCount() + 1;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return column==0 ? super.getColumnClass(column) : tableModel.getColumnClass(column-1);
    }

    @Override
    public String getColumnName(int column) {
        return column==0 ? super.getColumnName(column) : tableModel.getColumnName(column-1);
    }

    @Override
    public Object getValueAt(Object node, int column) {
        Object object = ((DefaultMutableTreeNode) node).getUserObject();

        if (object instanceof Category) {
            switch (column) {
                case 0:
                    return ((Category) object).getName() + " ("+getObjectCount((DefaultMutableTreeNode) node)+")";
                default:
                    return "";
            }
        } else if(object instanceof String){
            switch (column){
                case 0:
                    return object.toString();
                default:
                    return "";
            }
        } else {
            List objects = tableModel.getObjects();
            tableModel.setObjects(Arrays.asList(object), true);
            try {
                switch (column) {
                    case 0:
                        return getCategoryPath((DefaultMutableTreeNode)node);
                    default:
                        return tableModel.getValueAt(0, column - 1);

                }
            } finally {
                tableModel.setObjects(objects, true);
            }
        }
    }

    private String getCategoryPath(DefaultMutableTreeNode node) {
        final StringBuilder path = new StringBuilder();

        while (node!=null){
            if(node.getUserObject() instanceof Category){
                if(path.length()>0){
                    path.insert(0, " \u2192 ");
                }
                path.insert(0, ((Category) node.getUserObject()).getName());
            }
            node = (DefaultMutableTreeNode) node.getParent();
        }

        return path.toString();
    }

    private int getObjectCount(DefaultMutableTreeNode node){
        int count = 0;

        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);

            if(childNode.getUserObject() instanceof Category){
                count += getObjectCount(childNode);
            }else{
                count ++;
            }
        }

        return count;
    }
}