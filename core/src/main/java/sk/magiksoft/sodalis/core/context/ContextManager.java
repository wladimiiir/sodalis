package sk.magiksoft.sodalis.core.context;

import sk.magiksoft.sodalis.core.filter.ui.FilterPanel;

import java.awt.*;

/**
 * @author wladimiiir
 */
public interface ContextManager {
    public Component getMainComponent();

    public Component getStatusPanel();

    public Context getContext();

    public void reloadData();

    boolean isContextInitialized();

    FilterPanel getFilterPanel();
}
