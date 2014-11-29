package sk.magiksoft.sodalis.core.ui.controlpanel;

import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

import javax.swing.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public interface InfoPanel {
    public final static String PROPERTY_STORAGE_KEY = "storageKey";

    public String getPanelName();

    public List<AbstractButton> getControlPanelButtons();

    public boolean acceptObject(Object object);

    public void setupObject(Object object);

    public void setupPanel(Object object);

    public void addInfoPanelListener(InfoPanelListener listener);

    public boolean isInitialized();

    public boolean isMultiSaveSupported();

    public boolean isWizardSupported();

    public JComponent getComponentPanel();

    /**
     * Used because of lazy initialization of panel - Initializes data from entity to panel
     */
    public void initData();

    public void initLayout();

    /**
     * Should check if any of passed entities could change info panel entity
     *
     * @param entities modified entities
     * @return true if reload has to be called, false otherwise
     */
    public boolean isReloadNeeded(List<? extends DatabaseEntity> entities);


}
