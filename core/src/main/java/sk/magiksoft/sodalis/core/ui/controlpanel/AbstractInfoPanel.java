
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.ui.controlpanel;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntityContainer;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.ui.ImagePanel;
import sk.magiksoft.swing.itemcomponent.ItemComponentListener;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author wladimiiir
 */
public abstract class AbstractInfoPanel extends JPanel implements InfoPanel {

    protected ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            fireEditing();
        }
    };
    protected DocumentListener documentListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            fireEditing();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            fireEditing();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            fireEditing();
        }
    };
    protected ChangeListener changeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            fireEditing();
        }
    };
    protected ListDataListener listDataListener = new ListDataListener() {

        @Override
        public void intervalAdded(ListDataEvent e) {
            fireEditing();
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            fireEditing();
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            fireEditing();
        }
    };
    protected ItemComponentListener itemComponentListener = new ItemComponentListener() {
        @Override
        public void itemAdded(Object item) {
            fireEditing();
        }

        @Override
        public void itemRemoved(Object item) {
            fireEditing();
        }

        @Override
        public void itemUpdated(Object item) {
            fireEditing();
        }

        @Override
        public void selectionChanged() {
        }
    };
    protected ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditing();
        }
    };
    protected PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fireEditing();
        }
    };
    protected ImagePanel.ImagePanelListener imagePanelListener = new ImagePanel.ImagePanelListener() {

        @Override
        public void imageChanged() {
            fireEditing();
        }
    };
    protected TableModelListener tableModelListener = new TableModelListener() {

        @Override
        public void tableChanged(TableModelEvent e) {
            fireEditing();
        }
    };
    private List<InfoPanelListener> infoPanelListeners = new ArrayList<InfoPanelListener>();
    private JLabel lblLoading;
    private Class infoPanelClass;
    protected boolean initialized;
    private boolean layoutInitialized;

    public AbstractInfoPanel() {
        this(null);
    }

    public AbstractInfoPanel(Class infoPanelClass) {
        this.infoPanelClass = infoPanelClass;
        initComponents();
    }

    private void initComponents() {
        lblLoading = new JLabel(LocaleManager.getString("loading"));
        lblLoading.setFont(lblLoading.getFont().deriveFont(20f));
        lblLoading.setHorizontalTextPosition(SwingConstants.CENTER);
        setLayout(new GridBagLayout());
        add(lblLoading, new GridBagConstraints());
    }

    protected void fireEditing() {
        InfoPanelStateEvent stateEvent = new InfoPanelStateEvent(InfoPanelStateEvent.STATE_TYPE_UPDATE, true);
        for (InfoPanelListener listener : infoPanelListeners) {
            listener.stateChanged(stateEvent);
        }
    }

    protected void fireValid(boolean valid) {
        InfoPanelStateEvent stateEvent = new InfoPanelStateEvent(InfoPanelStateEvent.STATE_TYPE_VALIDITY, valid);
        for (InfoPanelListener listener : infoPanelListeners) {
            listener.stateChanged(stateEvent);
        }
    }

    @Override
    public void addInfoPanelListener(InfoPanelListener listener) {
        if (!infoPanelListeners.contains(listener)) {
            infoPanelListeners.add(listener);
        }
    }

    @Override
    public List<AbstractButton> getControlPanelButtons() {
        return new ArrayList<AbstractButton>();
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public JComponent getComponentPanel() {
        return this;
    }

    @Override
    public boolean isReloadNeeded(List<? extends DatabaseEntity> entities) {
        return false;
    }

    @Override
    public boolean isMultiSaveSupported() {
        return false;
    }

    @Override
    public boolean isWizardSupported() {
        return true;
    }

    @Override
    public void initLayout() {
        if (layoutInitialized) {
            return;
        }

        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Component layout;

            @Override
            protected Void doInBackground() throws Exception {
                layout = createLayout();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    removeAll();
                    setLayout(new BorderLayout());
                    add(layout, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                    if (getClientProperty(InfoPanel.PROPERTY_STORAGE_KEY) != null) {
                        SodalisApplication.get().getStorageManager().registerComponent(getClientProperty(InfoPanel.PROPERTY_STORAGE_KEY).toString(), layout);
                    }
                    layoutInitialized = true;
                } catch (InterruptedException e) {
                    LoggerManager.getInstance().error(getClass(), e);
                } catch (ExecutionException e) {
                    LoggerManager.getInstance().error(getClass(), e);
                }
            }
        };
        worker.execute();
        try {
            worker.get();
        } catch (InterruptedException e) {
            LoggerManager.getInstance().error(getClass(), e);
        } catch (ExecutionException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }

    }

    protected abstract Component createLayout();

    @Override
    public boolean acceptObject(Object object) {
        if (infoPanelClass == null) {
            return true;
        }

        return infoPanelClass.isAssignableFrom(object.getClass())
                || (object instanceof DatabaseEntityContainer && ((DatabaseEntityContainer) object).getDatabaseEntity(infoPanelClass) != null);
    }

    protected <T extends DatabaseEntity> T getNormalizedObject(Class<T> clazz, Object object) {
        if (object.getClass() == clazz) {
            return (T) object;
        }
        if (object instanceof DatabaseEntityContainer) {
            return ((DatabaseEntityContainer) object).getDatabaseEntity(clazz);
        }

        return null;
    }


}