
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
import java.awt.*;
import java.awt.event.*;

/**
 * @author wladimiiir
 */
public class ExtendedTabbedPane extends JTabbedPane {

    public static final String PROPERTY_TAB_TITLE = "tabTitle";

    private boolean editable = true;
    private JTextField tabEditor;
    private int editingTabIndex = -1;

    public ExtendedTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
        init();
    }

    public ExtendedTabbedPane(int tabPlacement) {
        super(tabPlacement);
        init();
    }

    public ExtendedTabbedPane() {
        init();
    }

    private void init() {
        tabEditor = new JTextField();
        tabEditor.setFont(tabEditor.getFont().deriveFont(10f));
        tabEditor.setPreferredSize(new Dimension(75, 18));
        tabEditor.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stopEditing();
            }
        });
        tabEditor.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                stopEditing();
            }
        });
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);

        if (isEditable() && e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            editTab(getSelectedIndex());
        }
    }

    protected void stopEditing() {
        if (editingTabIndex == -1) {
            return;
        }

        String oldTitle = getTitleAt(editingTabIndex);

        setTitleAt(editingTabIndex, tabEditor.getText());
        setTabComponentAt(editingTabIndex, null);
        firePropertyChange(PROPERTY_TAB_TITLE, oldTitle, getTitleAt(editingTabIndex));
        editingTabIndex = -1;
    }

    protected void editTab(int tabIndex) {
        editingTabIndex = tabIndex;
        tabEditor.setText(getTitleAt(tabIndex));
        tabEditor.selectAll();
        setTabComponentAt(tabIndex, tabEditor);
        tabEditor.grabFocus();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}