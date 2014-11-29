package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.core.factory.EntityFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.settings.SettingsPanel;
import sk.magiksoft.sodalis.core.ui.controlpanel.InfoPanelListener;
import sk.magiksoft.sodalis.core.ui.controlpanel.InfoPanelStateEvent;
import sk.magiksoft.sodalis.person.entity.ManagerData;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PrivatePersonData;
import sk.magiksoft.swing.ExtendedTabbedPane;

import javax.security.auth.Subject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EnsembleManagerSettingsPanel extends JPanel implements SettingsPanel {

    private ExtendedTabbedPane tbpManagers;

    public EnsembleManagerSettingsPanel() {
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddManager = new JButton("") {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawLine(3, 6, 9, 6);
                g.drawLine(6, 3, 6, 9);
            }
        };
        JButton btnRemoveManager = new JButton("") {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawLine(3, 6, 9, 6);
            }
        };

        btnAddManager.setPreferredSize(new Dimension(14, 14));
        btnAddManager.setFocusPainted(false);
        btnRemoveManager.setPreferredSize(new Dimension(14, 14));
        btnRemoveManager.setFocusPainted(false);

        pnlButtons.add(btnAddManager);
        pnlButtons.add(btnRemoveManager);

        setLayout(new GridBagLayout());
        tbpManagers = new ExtendedTabbedPane();
        tbpManagers.setEditable(false);

        c.gridx = c.gridy = 0;
        c.weightx = c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(3, 3, 0, 3);
        add(tbpManagers, c);

        c.gridy++;
        c.weighty = 0.0;
        c.insets = new Insets(0, 3, 3, 3);
        add(pnlButtons, c);

        btnAddManager.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addManagerPanel(createPerson());
                tbpManagers.setSelectedIndex(tbpManagers.getTabCount() - 1);
            }
        });
        btnRemoveManager.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeManagerPanel();
            }
        });
    }

    private EnsembleManagerPanel addManagerPanel(Person person) {
        final EnsembleManagerPanel panel = new EnsembleManagerPanel(person);

        tbpManagers.addTab("", panel);
        panel.addInfoPanelListener(new InfoPanelListener() {

            @Override
            public void stateChanged(InfoPanelStateEvent event) {
                if (event.getStateType() != InfoPanelStateEvent.STATE_TYPE_UPDATE) {
                    return;
                }
                reloadTabTitles();
            }
        });

        return panel;
    }

    private Person createPerson() {
        return EntityFactory.getInstance().createEntity(Person.class, new Object[]{PrivatePersonData.class, ManagerData.class});
    }

    private void reloadTabTitles() {
        Person person = createPerson();
        EnsembleManagerPanel panel;

        for (int index = 0; index < tbpManagers.getTabCount(); index++) {
            panel = (EnsembleManagerPanel) tbpManagers.getComponent(index);
            panel.setupPerson(person);
            tbpManagers.setTitleAt(index, getTabTitle(person));
        }
    }

    private String getTabTitle(Person person) {
        StringBuilder title = new StringBuilder();
        ManagerData data = person.getPersonData(ManagerData.class);


        title.append(data.getPosition());
        if (title.length() > 0) {
            title.append(": ");
        }
        title.insert(0, "<html><b>");
        title.append("</b>");
        title.append(person.getFullName(true));
        title.append("</html>");

        return title.toString();
    }

    private void removeManagerPanel() {
        int selectedIndex = tbpManagers.getSelectedIndex();

        tbpManagers.removeTabAt(selectedIndex);
    }

    @Override
    public String getSettingsPanelName() {
        return LocaleManager.getString("ensembleManagers");
    }

    @Override
    public JComponent getSwingComponent() {
        return this;
    }

    @Override
    public void reloadSettings() {
        DefaultDataManager manager = DefaultDataManager.getInstance();
        List<Person> managers = manager.getDatabaseEntities("SELECT p FROM Person p LEFT JOIN p.personDatas as pd " +
                "WHERE pd IS NOT NULL AND pd.class=" + ManagerData.class.getName());

        tbpManagers.removeAll();
        for (Person person : managers) {
            addManagerPanel(person);
        }
        if (managers.isEmpty()) {
            addManagerPanel(createPerson());
        }
        reloadTabTitles();
    }

    @Override
    public boolean saveSettings() {
        List<Person> persons = new ArrayList<Person>();
        Person person;
        EnsembleManagerPanel panel;

        for (int index = 0; index < tbpManagers.getTabCount(); index++) {
            panel = (EnsembleManagerPanel) tbpManagers.getComponent(index);
            person = panel.getPerson();
            panel.setupPerson(person);
            persons.add(person);
        }
        DefaultDataManager.getInstance().addOrUpdateEntities(persons);

        return true;
    }

    @Override
    public void setup(Subject subject) throws VetoException {
    }
}
