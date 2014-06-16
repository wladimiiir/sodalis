
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.swing;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * @author wladimiiir
 */
public class DefaultTreeComboBoxModel extends DefaultTreeModel implements ComboBoxModel {


    public DefaultTreeComboBoxModel(TreeNode root) {
        this(root, false);
    }

    public DefaultTreeComboBoxModel(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);

    }

    @Override
    public void setSelectedItem(Object anItem) {
    }

    @Override
    public Object getSelectedItem() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Object getElementAt(int index) {
        return null;
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listenerList.add(ListDataListener.class, l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listenerList.remove(ListDataListener.class, l);
    }

}