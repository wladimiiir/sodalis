package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.InfoPanelListener;
import sk.magiksoft.sodalis.core.ui.controlpanel.InfoPanelStateEvent;
import sk.magiksoft.sodalis.person.entity.ManagerData;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.ui.PersonalDataInfoPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author wladimiiir
 */
public class EnsembleManagerPanel extends JPanel {

    private JTextField tfdManagerPosition;
    private PersonalDataInfoPanel personalDataInfoPanel;
    private Person person;

    public EnsembleManagerPanel(Person person) {
        initComponents();
        setupPanel(person);
    }

    public void addInfoPanelListener(InfoPanelListener listener) {
        personalDataInfoPanel.addInfoPanelListener(listener);
        listenerList.add(InfoPanelListener.class, listener);
    }

    private void fireInfoPanelEvent() {
        InfoPanelStateEvent event = new InfoPanelStateEvent(InfoPanelStateEvent.STATE_TYPE_UPDATE, true);
        InfoPanelListener[] listeners = listenerList.getListeners(InfoPanelListener.class);

        for (InfoPanelListener infoPanelListener : listeners) {
            infoPanelListener.stateChanged(event);
        }
    }

    private void initComponents() {
        GridBagConstraints c = new GridBagConstraints();

        tfdManagerPosition = new JTextField();
        personalDataInfoPanel = new PersonalDataInfoPanel();
        personalDataInfoPanel.initLayout();

        setLayout(new GridBagLayout());

        c.gridx = c.gridy = 0;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 3, 0);

        add(new JLabel(LocaleManager.getString("managerPosition") + ": "), c);
        c.gridx++;
        c.weightx = 1.0;
        c.insets = new Insets(5, 0, 3, 5);
        add(tfdManagerPosition, c);


        c.gridy++;
        c.gridx = 0;
        c.weighty = 1.0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 5, 5, 5);
        add(personalDataInfoPanel, c);

        tfdManagerPosition.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                fireInfoPanelEvent();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fireInfoPanelEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fireInfoPanelEvent();
            }
        });
    }

    private void setupPanel(Person person) {
        ManagerData data = person.getPersonData(ManagerData.class);

        this.person = person;
        tfdManagerPosition.setText(data.getPosition());
        personalDataInfoPanel.setupPanel(person);
        personalDataInfoPanel.initData();
    }

    public Person getPerson() {
        return person;
    }

    public void setupPerson(Person person) {
        ManagerData data = person.getPersonData(ManagerData.class);

        data.setPosition(tfdManagerPosition.getText());
        personalDataInfoPanel.setupObject(person);
    }
}
