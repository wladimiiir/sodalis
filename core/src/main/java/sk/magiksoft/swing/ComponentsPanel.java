
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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author wladimiiir
 */
public abstract class ComponentsPanel extends JPanel {

    private ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            enterAction((Component) e.getSource());
        }
    };
    private GridBagConstraints componentsConstraints = new GridBagConstraints();
    private JPanel componentsPanel;
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    protected boolean adjusting = false;

    public ComponentsPanel() {
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints c = new GridBagConstraints();
        componentsPanel = new JPanel(new GridBagLayout());
        setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.0;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        add(componentsPanel, c);
        c.gridy++;
        c.weighty = 1.0;
        add(new JPanel(), c);
        componentsConstraints.gridx = 0;
        componentsConstraints.fill = GridBagConstraints.HORIZONTAL;
        componentsConstraints.anchor = GridBagConstraints.NORTH;
        componentsConstraints.weightx = 1.0;
        componentsConstraints.insets = new Insets(getVerticalGap(), 3, 0, 3);
        setValues(new ArrayList<String>());
    }

    protected int getVerticalGap(){
        return 3;
    }

    private void addPanelComponent(Object value) {
        //last component is empty
        if (componentsPanel.getComponentCount() > 0 && isEmpty(componentsPanel.getComponent(componentsPanel.getComponentCount() - 1))) {
            return;
        }
        componentsConstraints.gridy++;
        componentsPanel.add(createPanelComponent(value), componentsConstraints);
        componentsPanel.updateUI();
    }

    protected void enterAction(Component c) {
        if(adjusting){
            return;
        }

        if (getPanelComponents().length > 0 && c != componentsPanel.getComponent(componentsPanel.getComponentCount() - 1) && isEmpty(c)) {
            removePanelComponent(c);
        } else {
            adjusting = true;
            addPanelComponent(null);
            adjusting = false;
        }

        fireStateChanged();
    }

    protected abstract boolean isEmpty(Component component);

    protected abstract Component createPanelComponent(Object value);

    protected ActionListener getActionListener() {
        return actionListener;
    }

    protected Component[] getPanelComponents() {
        return componentsPanel.getComponents();
    }

    private void removePanelComponent(Component panelComponent) {
        for (int i = 0; i < componentsPanel.getComponents().length; i++) {
            Component component = componentsPanel.getComponents()[i];
            if (panelComponent == component) {
                componentsPanel.remove(component);
                componentsConstraints.gridy--;
                break;
            }
        }
        componentsPanel.updateUI();
    }

    public void setValues(List values) {
        adjusting = true;
        componentsPanel.removeAll();
        componentsConstraints.gridy = -1;
        for (Object value : values) {
            addPanelComponent(value);
        }
        addPanelComponent(null);
        adjusting = false;
    }

    public abstract List getValues();

    public void addChangeListener(ChangeListener listener) {
        this.changeListeners.add(listener);
    }

    protected void fireStateChanged() {
        ChangeEvent e = new ChangeEvent(this);

        for (ChangeListener changeListener : changeListeners) {
            changeListener.stateChanged(e);
        }
    }
}