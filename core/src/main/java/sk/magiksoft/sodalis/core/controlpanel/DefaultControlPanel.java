package sk.magiksoft.sodalis.core.controlpanel;

import scala.collection.JavaConversions;
import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.Entity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.icon.IconManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class DefaultControlPanel extends JPanel implements InfoPanelListener, ControlPanel {
    private final String[] keys;
    //---UI---
    protected JButton btnCancel;
    protected JButton btnSave;
    protected JTabbedPane tbpControlPanel = new JTabbedPane(JTabbedPane.BOTTOM);
    private JPanel controlButtonsPanel = new JPanel(new GridBagLayout());
    //--------
    private List<InfoPanel> allInfoPanels = new LinkedList<>();
    private List<InfoPanel> infoPanels = new LinkedList<>();
    protected Object currentObject = null;
    protected List<Object> additionalObjects = null;
    protected boolean editing = false;
    protected boolean valid = true;
    protected boolean adjusting = false;
    private boolean updating = false;

    public DefaultControlPanel(String... keys) {
        this.keys = keys;
        initComponents();
        initInfoPanels();
    }

    public void addControlPanelListener(ControlPanelListener listener) {
        listenerList.add(ControlPanelListener.class, listener);
    }

    protected void fireSaved() {
        ControlPanelListener[] listeners = listenerList.getListeners(ControlPanelListener.class);

        for (ControlPanelListener listener : listeners) {
            listener.saved();
        }
    }

    protected void fireCancelled() {
        ControlPanelListener[] listeners = listenerList.getListeners(ControlPanelListener.class);

        for (ControlPanelListener listener : listeners) {
            listener.cancelled();
        }
    }

    private void addInfoPanelTab(InfoPanel panel) {
        if (panel instanceof Component) {
            final JScrollPane scrollPane = new JScrollPane((Component) panel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            tbpControlPanel.add(scrollPane, panel.getPanelName());
        }
    }

    private void initComponents() {
        btnSave = new JButton(new SaveAction());
        btnCancel = new JButton(new CancelAction());
        setEditing(false);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.add(controlButtonsPanel, BorderLayout.NORTH);
        rightPanel.setPreferredSize(new Dimension(100, 100));
        GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0);
        buttonPanel.add(btnSave, c);
        c.gridy++;
        c.weighty = 0.0;
        buttonPanel.add(btnCancel, c);
        this.setLayout(new BorderLayout());
        this.add(tbpControlPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);

        tbpControlPanel.addChangeListener(e -> {
            if (adjusting || tbpControlPanel.getSelectedComponent() == null) {
                return;
            }

            final InfoPanel infoPanel = (InfoPanel) ((JScrollPane) tbpControlPanel.getSelectedComponent()).getViewport().getView();
            final boolean oldEditing = editing;

            SwingUtilities.invokeLater(() -> {
                infoPanel.initLayout();
                infoPanel.initData();
                setControlButtons(infoPanel.getControlPanelButtons());
                setEditing(oldEditing);
            });
        });
    }

    @Override
    public void setSelectedInfoPanelClass(Class<? extends InfoPanel> infoPanelClass) {
        for (int i = 0, infoPanelsSize = infoPanels.size(); i < infoPanelsSize; i++) {
            InfoPanel infoPanel = infoPanels.get(i);
            if (infoPanel.getClass() == infoPanelClass) {
                tbpControlPanel.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public Class<? extends InfoPanel> getSelectedInfoPanelClass() {
        final InfoPanel infoPanel = tbpControlPanel.getSelectedIndex() >= 0
                ? (InfoPanel) ((JScrollPane) tbpControlPanel.getSelectedComponent()).getViewport().getView()
                : null;

        return infoPanel == null ? null : infoPanel.getClass();
    }

    public List<InfoPanel> getAllInfoPanels() {
        return allInfoPanels;
    }

    private void initInfoPanels() {
        for (String key : keys) {
            final Collection<Class<? extends InfoPanel>> infoPanelClasses = JavaConversions.asJavaCollection(ControlPanelRegistry.getEntityInfoPanelClasses(key));

            for (Class<? extends InfoPanel> infoPanelClass : infoPanelClasses) {
                try {
                    final InfoPanel infoPanel = infoPanelClass.newInstance();

                    infoPanel.getComponentPanel().putClientProperty(InfoPanel.PROPERTY_STORAGE_KEY, key + '.' + infoPanelClass.getName() + '.' + infoPanelClass.getName());
                    infoPanel.addInfoPanelListener(this);
                    allInfoPanels.add(infoPanel);
                } catch (InstantiationException | IllegalAccessException ex) {
                    LoggerManager.getInstance().error(getClass(), ex);
                }
            }
        }
    }

    protected void setControlButtons(List<AbstractButton> buttons) {
        GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0);

        controlButtonsPanel.removeAll();
        c.gridx = 0;
        c.gridy = 0;

        for (AbstractButton button : buttons) {
            controlButtonsPanel.add(button, c);
            c.gridy++;
        }
        controlButtonsPanel.revalidate();
        controlButtonsPanel.repaint();
    }

    @Override
    public boolean isEditing() {
        return editing;
    }

    protected void setEditing(boolean editing) {
        if (adjusting) {
            return;
        }
        btnCancel.setEnabled(editing);
        btnSave.setEnabled(editing && valid);
        this.editing = editing;
    }

    protected void setValid(boolean valid) {
        this.valid = valid;

        btnSave.setEnabled(editing && valid);
    }

    @Override
    public void cancelEditing() {
        setEditing(false);
    }

    @Override
    public synchronized void setupControlPanel(final Object object) {
        final Class<? extends InfoPanel> infoPanelClass = getSelectedInfoPanelClass();

        currentObject = object;
        additionalObjects = null;
        infoPanels = new LinkedList<>();

        if (object == null) {
            setVisible(false);
            return;
        }
        for (InfoPanel infoPanel : allInfoPanels) {
            if (!infoPanel.acceptObject(object)) {
                continue;
            }
            infoPanels.add(infoPanel);
        }

        SwingUtilities.invokeLater(() -> {
            adjusting = true;
            tbpControlPanel.removeAll();
            for (InfoPanel infoPanel : infoPanels) {
                infoPanel.setupPanel(object);
                addInfoPanelTab(infoPanel);
            }
            editing = false;
            adjusting = false;

            tbpControlPanel.getModel().clearSelection();
            if (!infoPanels.isEmpty()) {
                if (infoPanelClass == null) {
                    setSelectedInfoPanelClass(infoPanels.get(0).getClass());
                } else {
                    setSelectedInfoPanelClass(infoPanelClass);
                }
            }

            setVisible(true);
        });
    }

    @Override
    public void setAdditionalObjects(List<Object> objects) {
        this.additionalObjects = objects;
    }

    @Override
    public JComponent getControlComponent() {
        return this;
    }

    @Override
    public boolean doUpdate() {
        final List<Object> toUpdate = new LinkedList<Object>();

        toUpdate.add(currentObject);
        for (InfoPanel infoPanel : infoPanels) {
            if (infoPanel.isInitialized()) {
                infoPanel.setupObject(currentObject);

                if (additionalObjects != null && !additionalObjects.isEmpty() && infoPanel.isMultiSaveSupported()) {
                    final Object[] options = {LocaleManager.getString("forCurrentObject"), LocaleManager.getString("forAllObjects")};
                    if (ISOptionPane.showOptionDialog(this, LocaleManager.getString("controlPanelMultiSaveQuestion"), infoPanel.getPanelName(), JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                            options, options[0]) == 0) {
                        continue;
                    }
                    for (Object additionalObject : additionalObjects) {
                        infoPanel.setupObject(additionalObject);
                        if (!toUpdate.contains(additionalObject)) {
                            toUpdate.add(additionalObject);
                        }
                    }
                }
            }
        }
        toUpdate.forEach(this::saveObject);

        setEditing(false);
        return true;
    }

    protected void saveObject(Object object) {
        if (object instanceof DatabaseEntity) {
            DefaultDataManager.getInstance().updateDatabaseEntity((DatabaseEntity) object);
        }
    }

    @Override
    public void stateChanged(InfoPanelStateEvent event) {
        switch (event.getStateType()) {
            case InfoPanelStateEvent.STATE_TYPE_UPDATE:
                if (editing) {
                    return;
                }
                setEditing(event.getStateValue());
                break;
            case InfoPanelStateEvent.STATE_TYPE_VALIDITY:
                setValid(event.getStateValue());
                break;
        }
    }

    private void checkReload(List<? extends DatabaseEntity> entities) {
        if (updating || currentObject == null) {
            return;
        }
        if (editing) {
            //TODO: what if edited object has been changed
            return;
        }

        boolean reloadNeeded = false;
        if (currentObject instanceof DatabaseEntity) {
            for (DatabaseEntity entity : entities) {
                if (entity.getId().equals(((DatabaseEntity) currentObject).getId())) {
                    reloadNeeded = true;
                    break;
                }
            }
        }

        for (InfoPanel infoPanel : infoPanels) {
            if (infoPanel.isInitialized() && (reloadNeeded || infoPanel.isReloadNeeded(entities))) {
                infoPanel.setupPanel(currentObject);
                infoPanel.initData();
            }
        }
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        checkReload(entities);
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        checkReload(entities);
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        checkReload(entities);
    }

    private class SaveAction extends AbstractAction {

        public SaveAction() {
            super(LocaleManager.getString("saveAction"), IconManager.getInstance().getIcon("ok"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updating = true;
            try {
                doUpdate();
                fireSaved();
            } finally {
                updating = false;
            }
        }
    }

    private class CancelAction extends AbstractAction {

        public CancelAction() {
            super(LocaleManager.getString("cancelAction"), IconManager.getInstance().getIcon("cancel"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setEditing(false);
            fireCancelled();
            setupControlPanel(currentObject);
        }
    }
}
