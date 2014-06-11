
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
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
        if(editingTabIndex==-1){
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