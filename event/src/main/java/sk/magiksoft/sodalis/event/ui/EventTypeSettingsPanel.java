package sk.magiksoft.sodalis.event.ui;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.settings.SettingsManager;
import sk.magiksoft.sodalis.core.settings.SettingsPanel;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.controlpanel.InfoPanel;
import sk.magiksoft.sodalis.event.data.EventTypeSubject;
import sk.magiksoft.sodalis.event.entity.EventType;
import sk.magiksoft.swing.itemcomponent.ItemComponent;

import javax.security.auth.Subject;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author wladimiiir
 */
public class EventTypeSettingsPanel extends ItemComponent implements SettingsPanel {

    @Override
    public String getSettingsPanelName() {
        return LocaleManager.getString("eventTypes");
    }

    @Override
    public JComponent getSwingComponent() {
        return this;
    }

    @Override
    public void reloadSettings() {
        List<EventType> eventTypes = SettingsManager.getInstance().getDatabaseEntities(EventType.class);

        tableModel.setObjects(eventTypes);
    }

    @Override
    public boolean saveSettings() {
        List<EventType> eventTypes = SettingsManager.getInstance().getDatabaseEntities(EventType.class);
        List<EventType> modelEventTypes = tableModel.getObjects();
        boolean cannotDelete = false;
        boolean found;

        for (EventType eventType : eventTypes) {
            found = false;
            for (EventType modelEventType : modelEventTypes) {
                if (eventType.getId().equals(modelEventType.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (SettingsManager.getInstance().canDelete(eventType)) {
                    SettingsManager.getInstance().removeDatabaseEntity(eventType);
                } else {
                    cannotDelete = true;
                }
            }
        }
        for (EventType eventType : modelEventTypes) {
            SettingsManager.getInstance().addOrUpdateEntity(eventType);
        }

        if (cannotDelete) {
            ISOptionPane.showMessageDialog(this, LocaleManager.getString("someEventTypesCannotBeDeleted"));
        }

        return true;
    }

    @Override
    protected Object getNewItem() {
        return new EventType("", new String[0]);
    }

    @Override
    protected ObjectTableModel createTableModel() {
        return new EventTypeTableModel();
    }

    @Override
    protected TableCellEditor getCellEditor(int column) {
        switch (column) {
            case 1:
                return new EventTypeSettingsPanelCellEditor();
        }
        return super.getCellEditor(column);
    }

    @Override
    protected TableCellRenderer getCellRenderer(int column) {
        switch (column) {
            case 1:
                return new EventTypeSettingsPanelCellRenderer();
        }
        return super.getCellRenderer(column);
    }

    @Override
    public void setup(Subject subject) throws VetoException {
    }

    private class EventInfoPanelsCheckBoxPanel extends JPanel {

        protected List<InfoPanelCheckBox> infoPanels = new ArrayList<InfoPanelCheckBox>();

        public EventInfoPanelsCheckBoxPanel() {
            initInfoPanels();
            initComponents();
        }

        private void initInfoPanels() {
            try {
                Document configXMLDocument = new SAXBuilder().build(SodalisApplication.get().getConfigurationXMLFile());
                List<Element> controlPanelElements = configXMLDocument.getRootElement().getChild("control_panels").getChildren("control_panel");
                List<Element> infoPanelElements = null;
                for (Element element : controlPanelElements) {
                    if (element.getAttributeValue("key") != null && element.getAttributeValue("key").equals("event")) {
                        infoPanelElements = element.getChildren("info_panel");
                        break;
                    }
                }
                if (infoPanelElements == null) {
                    return;
                }
                for (Element infoPanelElement : infoPanelElements) {
                    try {
                        String className = infoPanelElement.getAttributeValue("class");
                        Class infoPanelClass = Class.forName(className);
                        InfoPanel infoPanel = (InfoPanel) infoPanelClass.newInstance();
                        if (infoPanelClass.isAnnotationPresent(EventTypeSubject.class)) {
                            infoPanels.add(new InfoPanelCheckBox(infoPanel));
                        }
                    } catch (InstantiationException ex) {
                        LoggerManager.getInstance().error(getClass(), ex);
                    } catch (IllegalAccessException ex) {
                        LoggerManager.getInstance().error(getClass(), ex);
                    } catch (ClassNotFoundException ex) {
                        LoggerManager.getInstance().error(getClass(), ex);
                    }
                }
            } catch (JDOMException ex) {
                Logger.getLogger(EventTypeSettingsPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EventTypeSettingsPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void initComponents() {
            GridBagConstraints c = new GridBagConstraints();

            setOpaque(false);
            setLayout(new GridBagLayout());
            c.gridx = c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            c.weightx = 1.0;
            c.insets = new Insets(2, 2, 2, 3);
            for (InfoPanelCheckBox checkBox : infoPanels) {
                add(checkBox, c);
                c.gridx++;
            }
        }
    }

    private class InfoPanelCheckBox extends JCheckBox {

        private InfoPanel infoPanel;

        public InfoPanelCheckBox(InfoPanel infoPanel) {
            this.infoPanel = infoPanel;
            setOpaque(false);
            setText(infoPanel.getPanelName());
        }
    }

    private class EventTypeSettingsPanelCellEditor extends AbstractCellEditor implements TableCellEditor {

        private EventInfoPanelsCheckBoxPanel panel = new EventInfoPanelsCheckBoxPanel();

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            String[] classNames = (String[]) value;

            for (InfoPanelCheckBox infoPanelCheckBox : panel.infoPanels) {
                infoPanelCheckBox.setSelected(false);
                for (String className : classNames) {
                    if (className.equals(infoPanelCheckBox.infoPanel.getClass().getName())) {
                        infoPanelCheckBox.setSelected(true);
                        break;
                    }
                }
            }

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            List<String> infoPanelsClassNames = new ArrayList<String>();

            for (InfoPanelCheckBox infoPanelCheckBox : panel.infoPanels) {
                if (infoPanelCheckBox.isSelected()) {
                    infoPanelsClassNames.add(infoPanelCheckBox.infoPanel.getClass().getName());
                }
            }
            return infoPanelsClassNames.toArray(new String[0]);
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }
    }

    private class EventTypeSettingsPanelCellRenderer extends EventInfoPanelsCheckBoxPanel implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String[] classNames = (String[]) value;

            for (InfoPanelCheckBox infoPanelCheckBox : infoPanels) {
                infoPanelCheckBox.setSelected(false);
                for (String className : classNames) {
                    if (className.equals(infoPanelCheckBox.infoPanel.getClass().getName())) {
                        infoPanelCheckBox.setSelected(true);
                        break;
                    }
                }
            }
            return this;
        }
    }

    private class EventTypeTableModel extends ObjectTableModel<EventType> {

        public EventTypeTableModel() {
            super(new String[]{
                    LocaleManager.getString("name"),
                    LocaleManager.getString("allowedPanels")
            });
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            EventType eventType = getObject(rowIndex);

            switch (columnIndex) {
                case 0:
                    return eventType.getName();
                case 1:
                    return eventType.getInfoPanelClassNames();
                default:
                    return "";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            EventType eventType = getObject(rowIndex);

            switch (columnIndex) {
                case 0:
                    eventType.setName(aValue.toString());
                    break;
                case 1:
                    eventType.setInfoPanelClassNames((String[]) aValue);
                    break;
            }
        }
    }
}
