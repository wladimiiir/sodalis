package sk.magiksoft.sodalis.person.ui;

import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.ui.model.DatabaseEntityComboBoxModel;
import sk.magiksoft.sodalis.core.utils.ListUpdater;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;
import sk.magiksoft.swing.ComponentsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public class PersonWrapperTextFieldsPanel extends ComponentsPanel {

    private Vector persons;
    private ListUpdater<Person> personsUpdater;

    public PersonWrapperTextFieldsPanel() {
        personsUpdater = new ListUpdater<Person>(Person.class, persons) {

            @Override
            protected boolean acceptObject(Object object) {
                return super.acceptObject(object) && PersonWrapperTextFieldsPanel.this.acceptObject(object);
            }
        };
        DefaultDataManager.getInstance().addDataListener(personsUpdater);
    }

    protected boolean acceptObject(Object object) {
        return true;
    }

    @Override
    protected boolean isEmpty(Component component) {
        return ((JComboBox) component).getEditor().getItem() == null || ((JComboBox) component).getEditor().getItem().toString().trim().isEmpty();
    }

    @Override
    protected Component createPanelComponent(Object value) {
        if (persons == null) {
            initPersons();
        }
        final JComboBox comboBox = new JComboBox(new DatabaseEntityComboBoxModel(Person.class, persons));

        comboBox.setEditable(true);
        comboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                enterAction((Component) e.getSource());
            }
        });
        if (value instanceof PersonWrapper) {
            if (((PersonWrapper) value).getPerson() != null) {
                comboBox.setSelectedItem(((PersonWrapper) value).getPerson());
            } else {
                comboBox.setSelectedItem(null);
            }
            comboBox.getEditor().setItem(value.toString());
        } else {
            comboBox.setSelectedItem(null);
        }

        return comboBox;
    }

    @Override
    public List getValues() {
        List values = new ArrayList();

        for (int i = 0; i < getPanelComponents().length; i++) {
            JComboBox comboBox = (JComboBox) getPanelComponents()[i];

            if (comboBox.getSelectedItem() == null && (comboBox.getEditor().getItem() == null || comboBox.getEditor().getItem().toString().trim().isEmpty())) {
                continue;
            }

            PersonWrapper personWrapper = new PersonWrapper();
            if (comboBox.getSelectedItem() instanceof Person) {
                personWrapper.setPerson((Person) comboBox.getSelectedItem());
            } else {
                personWrapper.setPersonName(comboBox.getEditor().getItem().toString());
            }
            values.add(personWrapper);
        }

        return values;
    }

    /**
     * Override to optimize querying
     *
     * @return query for persons of interest or null to all persons
     */
    protected String getPersonQuery() {
        return null;
    }

    private void initPersons() {
        final String query = getPersonQuery();
        persons = new Vector(query == null
                ? DefaultDataManager.getInstance().getDatabaseEntities(Person.class)
                : DefaultDataManager.getInstance().getDatabaseEntities(query));

        for (int index = persons.size() - 1; index >= 0; index--) {
            Object object = persons.get(index);
            if (!acceptObject(object)) {
                persons.remove(index);
            }
        }
    }
}
