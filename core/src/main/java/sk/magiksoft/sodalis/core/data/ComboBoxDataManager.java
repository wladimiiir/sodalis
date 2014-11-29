package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.enumeration.DefaultEnumerationInfo;
import sk.magiksoft.sodalis.core.enumeration.Enumeration;
import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;
import sk.magiksoft.sodalis.core.enumeration.EnumerationFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class ComboBoxDataManager extends ClientDataManager {

    private static ComboBoxDataManager instance;
    private Map<String, JComboBox> comboBoxMap = new HashMap<String, JComboBox>();
    private Map<String, Enumeration> enumerationMap = new HashMap<String, Enumeration>();
    private boolean adjusting = false;

    protected ComboBoxDataManager() {
        initEnumerations();
        initListener();
    }

    public static ComboBoxDataManager getInstance() {
        if (instance == null) {
            instance = new ComboBoxDataManager();
        }

        return instance;
    }

    private void initComboBoxActions(final JComboBox comboBox, final String key) {
        comboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_INSERT) {
                    Object item = comboBox.getEditor().getItem();
                    //adding new item if CTRL+ENTER has been pressed
                    enumerationMap.get(key).addEntry(new EnumerationEntry(item.toString()));
                    addOrUpdateEntity(enumerationMap.get(key));
                    comboBox.getEditor().setItem(item);
                } else if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_DELETE) {
                    Object item = comboBox.getEditor().getItem();
                    //adding new item if CTRL+DELETE has been pressed
                    for (EnumerationEntry enumerationEntry : enumerationMap.get(key).getEntries()) {
                        if (enumerationEntry.getText().equals(item.toString())) {
                            enumerationMap.get(key).removeEntry(enumerationEntry);
                            break;
                        }
                    }
                    addOrUpdateEntity(enumerationMap.get(key));
                }
            }
        });
    }

    private void initEnumerations() {
        List<Enumeration> enumerations = EnumerationFactory.getInstance().getEnumerations();

        for (Enumeration enumeration : enumerations) {
            enumerationMap.put(enumeration.getName(), enumeration);
        }
    }

    private void initListener() {
        addDataListener(new DataAdapter() {

            @Override
            public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
                for (Object object : entities) {
                    if (!(object instanceof Enumeration)
                            || !enumerationMap.containsKey(((Enumeration) object).getName())
                            || !comboBoxMap.containsKey(((Enumeration) object).getName())) {
                        continue;
                    }
                    enumerationMap.put(((Enumeration) object).getName(), (Enumeration) object);
                    refreshComboBox(((Enumeration) object).getName());
                }
            }
        });
    }

    private void refreshComboBox(String key) {
        final JComboBox comboBox = comboBoxMap.get(key);
        final Enumeration enumeration = enumerationMap.get(key);
        final Object selectedItem;

        if (comboBox == null) {
            return;
        }
        selectedItem = comboBox.getSelectedItem();
        adjusting = true;
        comboBox.removeAllItems();
        for (EnumerationEntry enumerationEntry : enumeration.getEntries()) {
            comboBox.addItem(enumerationEntry);
        }
        comboBox.setSelectedItem(selectedItem);
        adjusting = false;
    }

    private void refreshEnumeration(String key, JComboBox comboBox) {
        final Enumeration enumeration = enumerationMap.get(key);

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i) == null || comboBox.getItemAt(i).toString().isEmpty()) {
                continue;
            }
            enumeration.addEntry(new EnumerationEntry(comboBox.getItemAt(i).toString()));
        }
    }

    public void registerComboBox(final String key, final JComboBox comboBox) {
        final Enumeration enumeration;

        if (!enumerationMap.containsKey(key)) {
            enumerationMap.put(key, enumeration = new Enumeration(key));
            enumeration.setEnumerationInfoClass(DefaultEnumerationInfo.class);
            refreshEnumeration(key, comboBox);
            addOrUpdateEntity(enumeration);
        } else {
            enumeration = enumerationMap.get(key);
            for (EnumerationEntry enumerationEntry : enumeration.getEntries()) {
                comboBox.addItem(enumerationEntry);
            }
        }
        comboBoxMap.put(key, comboBox);

        comboBox.getModel().addListDataListener(new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent e) {
                if (adjusting) {
                    return;
                }
//                refreshEnumeration(key, comboBox);
//                addOrUpdateEntity(enumeration);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                if (adjusting) {
                    return;
                }
//                refreshEnumeration(key, comboBox);
//                addOrUpdateEntity(enumeration);
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                if (adjusting) {
                    return;
                }
//                refreshEnumeration(key, comboBox);
//                addOrUpdateEntity(enumeration);
            }
        });
        if (comboBox.getToolTipText() == null) {
            comboBox.setToolTipText(LocaleManager.getString("enumerationComboBox.tooltip"));
        }

        initComboBoxActions(comboBox, key);
    }
}
