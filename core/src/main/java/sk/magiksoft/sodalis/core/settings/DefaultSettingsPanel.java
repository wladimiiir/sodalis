
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.settings;

import javax.security.auth.Subject;
import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.core.settings.valuecomponent.ValueComponent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.settings.valuecomponent.IntegerValueComponent;
import sk.magiksoft.sodalis.core.settings.valuecomponent.WrongValueException;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;

/**
 *
 * @author wladimiiir
 */
public class DefaultSettingsPanel extends JPanel implements SettingsPanel {

    private String name;
    private int columns;
    private Class settingsClass;
    private Map<String, SettingsValueComponent> componentFieldMap = new HashMap<String, SettingsValueComponent>();

    public DefaultSettingsPanel(String name, int columns, Class settingsClass) {
        this.name = name;
        this.columns = columns;
        this.settingsClass = settingsClass;
        initComponents();
    }

    @Override
    public String getSettingsPanelName() {
        return name;
    }

    @Override
    public JComponent getSwingComponent(){
        return this;
    }

    @Override
    public void reloadSettings() {
        try {
            Field[] values = settingsClass.getFields();
            SettingsValueComponent settingsValueComponent;
            Settings settings = (Settings) settingsClass.getMethod("getInstance").invoke(null);

            for (int i = 0; i < values.length; i++) {
                Field field = values[i];
                settingsValueComponent = componentFieldMap.get(field.getName());
                if (settingsValueComponent == null) {
                    continue;
                }
                settingsValueComponent.component.setValue(settings.getValue(field.get(null).toString()));
            }
        } catch (IllegalAccessException ex) {
            LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
        } catch (IllegalArgumentException ex) {
            LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
        } catch (InvocationTargetException ex) {
            LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
        } catch (NoSuchMethodException ex) {
            LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
        } catch (SecurityException ex) {
            LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
        }
    }

    @Override
    public boolean saveSettings() {
        try {
            Field[] values = settingsClass.getFields();
            SettingsValueComponent settingsValueComponent;
            Settings settings = (Settings) settingsClass.getMethod("getInstance").invoke(null);

            //checking user settings
            for (int i = 0; i < values.length; i++) {
                Field field = values[i];
                settingsValueComponent = componentFieldMap.get(field.getName());
                if (settingsValueComponent == null) {
                    continue;
                }
                try {
                    settingsValueComponent.component.checkValue();
                } catch (WrongValueException ex) {
                    ISOptionPane.showMessageDialog(this, ex.getMessage());
                    settingsValueComponent.component.getComponent().grabFocus();
                    return false;
                }
            }

            //saving settings values
            for (int i = 0; i < values.length; i++) {
                Field field = values[i];
                settingsValueComponent = componentFieldMap.get(field.getName());
                if (settingsValueComponent == null) {
                    continue;
                }
                settings.setValue(field.get(null).toString(), settingsValueComponent.component.getValue());
            }
            settings.save();
            return true;
        } catch (IllegalAccessException ex) {
            LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
        } catch (IllegalArgumentException ex) {
            LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
        } catch (InvocationTargetException ex) {
            LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
        } catch (NoSuchMethodException ex) {
            LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
        } catch (SecurityException ex) {
            LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
        }
        return false;
    }

    private ValueComponent createComponent(final Field field, final Class customComponentClass, final int minValue, final int maxValue) {
        if (customComponentClass != Object.class) {
            try {
                return (ValueComponent) customComponentClass.newInstance();
            } catch (InstantiationException ex) {
                LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
            } catch (IllegalAccessException ex) {
                LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
            }
        } else {
            if (field.getName().startsWith("I_")) {
                return new IntegerValueComponent(minValue, maxValue);
            }
        }

        return null;
    }

    private void initComponents() {
        List<SettingsValueComponent> settingsComponents = getSettingsComponents();
        GridBagConstraints c = new GridBagConstraints();

        setLayout(new GridBagLayout());

        
        for (int i = 0; i < settingsComponents.size(); i++) {
            SettingsValueComponent settingsValueComponent = settingsComponents.get(i);

            c.gridy = i / columns;
            c.gridx = (i % columns)*2;
            if (settingsValueComponent.label != null) {
                c.insets = new Insets(c.gridy>0 ? 2 : 10, c.gridx>0 ? 0 : 10, 2, 3);
                c.gridwidth = 1;
                c.weightx = 0.0;
                c.fill = GridBagConstraints.NONE;
                c.anchor = GridBagConstraints.EAST;
                add(new JLabel(settingsValueComponent.label), c);
                c.gridx++;
                c.insets = new Insets(c.gridy>0 ? 2 : 10, 0, 2, 3);
                c.gridwidth = 2 * settingsValueComponent.columnWidth - 1;
                c.weightx = 1.0;
                c.anchor = GridBagConstraints.CENTER;
                c.fill = GridBagConstraints.HORIZONTAL;
                add(settingsValueComponent.component.getComponent(), c);
            } else {
                c.insets = new Insets(c.gridy>0 ? 2 : 10, c.gridx>0 ? 0 : 10, 2, 10);
                c.gridwidth = 2 * settingsValueComponent.columnWidth;
                c.weighty = 1.0;
                c.fill = GridBagConstraints.HORIZONTAL;
                add(settingsValueComponent.component.getComponent(), c);
                c.gridx++;
            }
        }
    }

    private List<SettingsValueComponent> getSettingsComponents() {
        List<SettingsValueComponent> components = new ArrayList<SettingsValueComponent>();
        Field[] values = settingsClass.getFields();
        SettingsValue settingsValue;
        String settingLabel;
        ValueComponent component;
        SettingsValueComponent settingsValueComponent;

        for (int i = 0; i < values.length; i++) {
            Field field = values[i];
            if ((settingsValue = field.getAnnotation(SettingsValue.class)) == null) {
                continue;
            }
            try {
                settingLabel = LocaleManager.getString(field.get(null).toString());
                component = createComponent(field, settingsValue.customComponentClass(), settingsValue.minValue(), settingsValue.maxValue());
                settingsValueComponent = new SettingsValueComponent(settingLabel, component, settingsValue.columnWidth());
                components.add(settingsValueComponent);
                componentFieldMap.put(field.getName(), settingsValueComponent);
            } catch (IllegalAccessException ex) {
                LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
            } catch (IllegalArgumentException ex) {
                LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
            } catch (SecurityException ex) {
                LoggerManager.getInstance().error(DefaultSettingsPanel.class, ex);
            }
        }

        return components;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void setup(Subject subject) throws VetoException {
    }

    private class SettingsValueComponent {

        private String label;
        private ValueComponent component;
        private int columnWidth;

        public SettingsValueComponent(String label, ValueComponent component, int columnWidth) {
            this.label = label;
            this.component = component;
            this.columnWidth = columnWidth;
        }
    }
}