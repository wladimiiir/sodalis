package sk.magiksoft.sodalis.core.context;

import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.remote.DataManagerProvider;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.icon.IconManager;
import sk.magiksoft.sodalis.core.filter.FilterColumnComponentsFactory;
import sk.magiksoft.sodalis.core.filter.action.FilterEvent;
import sk.magiksoft.sodalis.core.filter.action.FilterListener;
import sk.magiksoft.sodalis.core.filter.ui.FilterPanel;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.search.FullTextSearchManager;
import sk.magiksoft.sodalis.core.ui.OkCancelDialog;
import sk.magiksoft.swing.LazyPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author wladimiiir
 */
public abstract class AbstractContextManager implements ContextManager, DataListener, FilterListener {

    private Context context;
    protected boolean contextInitialized = false;
    protected FilterPanel filterPanel;
    protected String filterQuery = "";
    protected String fullText;
    protected int filterAction = FilterEvent.ACTION_FILTER_ALL;
    private LazyPanel lazyPanel;

    public AbstractContextManager() {
        DataManagerProvider.addDataListener(this);
        lazyPanel = new LazyPanel() {
            @Override
            protected JComponent initLazyComponent() {
                return (JComponent) getContext();
            }

            @Override
            protected void fireInitializationFinished() {
                contextInitializationFinished();
            }
        };
    }

    private void contextInitializationFinished() {
        contextInitialized = true;
        initPopupActions();
        reloadData();
    }

    protected void initPopupActions() {
    }

    @Override
    public boolean isContextInitialized() {
        return contextInitialized;
    }

    @Override
    public Component getMainComponent() {
        lazyPanel.loadLazyComponent();
        return lazyPanel;
    }

    @Override
    public Component getStatusPanel() {
        return null;
    }

    @Override
    public synchronized Context getContext() {
        if (context == null) {
            context = createContext();
            if (context instanceof JComponent && isFullTextActive()) {
                initFullText();
            }
        }
        return context;
    }

    protected void initFullText() {
        final InputMap inputMap = ((JComponent) context).getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        final ActionMap actionMap = ((JComponent) context).getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK), "fulltext");
        actionMap.put("fulltext", new AbstractAction() {
            private OkCancelDialog fullTextDialog;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (fullTextDialog == null) {
                    final JPanel mainPanel = new JPanel(new GridBagLayout());
                    final JTextField fullText = new JTextField();
                    final GridBagConstraints c = new GridBagConstraints();
                    final class RefreshAction extends AbstractAction {
                        RefreshAction() {
                            super(LocaleManager.getString("refreshEntities"), IconManager.getInstance().getIcon("refresh"));
                            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
                        }

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            fullText.setText(null);
                            fullTextDialog.setVisible(false);
                            findFullText(null);
                        }
                    }

                    fullTextDialog = new OkCancelDialog(LocaleManager.getString("search")) {
                        @Override
                        public void setVisible(boolean b) {
                            super.setVisible(b);
                            if (b) {
                                fullText.requestFocus();
                            }
                        }
                    };
                    fullTextDialog.setMainPanel(mainPanel);
                    fullTextDialog.setModal(true);
                    fullTextDialog.setSize(300, 120);
                    fullTextDialog.setLocationRelativeTo(null);
                    fullTextDialog.addAdditionalActions(new RefreshAction());
                    c.gridx = c.gridy = 0;
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.weightx = 1.0;
                    c.insets = new Insets(0, 5, 0, 5);
                    mainPanel.add(fullText, c);
                    final ActionListener actionListener = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            final String text = fullText.getText().trim();
                            fullText.setText(null);
                            fullTextDialog.setVisible(false);
                            findFullText(text);
                        }
                    };
                    fullText.addActionListener(actionListener);
                    fullTextDialog.getOkButton().addActionListener(actionListener);
                }
                fullTextDialog.setVisible(true);
            }
        });
    }

    protected void findFullText(final String fullText) {
        this.fullText = fullText;
        reloadData();
    }

    protected abstract boolean isFullTextActive();

    protected abstract Context createContext();

    protected abstract String getDefaultQuery();

    protected abstract DataManager getDataManager();

    protected URL getFilterConfigFileURL() {
        return null;
    }

    @Override
    public void reloadData() {
        final List selected = getContext().getSelectedEntities();
        final List objects = getContext().getEntities();
        final String query;
        List<DatabaseEntity> entities;

        if (filterQuery.trim().isEmpty() || filterQuery.trim().equalsIgnoreCase("from")) {
            query = getDefaultQuery();
        } else {
            query = filterQuery;
        }

        if (query == null) {
            return;
        }

        try {
            entities = filterAction == FilterEvent.ACTION_FILTER_ALL
                    ? getDataManager().getDatabaseEntities(query)
                    : getDataManager().getDatabaseEntities(objects, query);
            if (fullText != null && !fullText.isEmpty()) {
                for (int index = entities.size() - 1; index >= 0; index--) {
                    DatabaseEntity entity = entities.get(index);
                    if (!FullTextSearchManager.find(entity, fullText)) {
                        entities.remove(index);
                    }
                }
                fullText = null;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(AbstractContextManager.class.getName()).log(Level.SEVERE, null, ex);
            entities = Collections.emptyList();
        }

        getContext().removeAllRecords();
        entitiesAdded(entities);
        if (!entities.isEmpty() && (selected.isEmpty() || !entities.containsAll(selected))) {
            selected.add(entities.get(0));
        }
        getContext().setSelectedEntities(selected);
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        if (context == null) {
            return;
        }
        ((DataListener) context).entitiesAdded(entities);
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        if (context == null) {
            return;
        }
        ((DataListener) context).entitiesUpdated(entities);
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        if (context == null) {
            return;
        }
        ((DataListener) context).entitiesRemoved(entities);
    }

    @Override
    public FilterPanel getFilterPanel() {
        if (filterPanel == null) {
            initFilterPanel();
        }
        return filterPanel;
    }

    @Override
    public void queryChanged(FilterEvent event) {
        if (event.getAction() == FilterEvent.ACTION_RESET) {
            filterAction = FilterEvent.ACTION_FILTER_ALL;
            filterQuery = "";
        } else {
            filterAction = event.getAction();
            filterQuery = event.getQuery();
        }
        reloadData();
    }

    protected void initFilterPanel() {
        final URL filterURL = getFilterConfigFileURL();

        if (filterURL == null)
            return;
        try {
            filterURL.openConnection().connect();
        } catch (Exception e) {
            return;
        }

        filterPanel = new FilterPanel(new FilterColumnComponentsFactory(filterURL).getColumnComponents());
        filterPanel.addFilterListener(this);
        filterPanel.setPreferredSize(new Dimension(180, 100));
        filterPanel.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 0, ColorList.BORDER));
    }
}
