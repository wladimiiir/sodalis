package sk.magiksoft.sodalis.folkensemble.programme.ui;

import sk.magiksoft.sodalis.core.action.GoToEntityAction;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.folkensemble.member.MemberModule;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.sodalis.person.entity.ManagerData;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;
import sk.magiksoft.sodalis.person.ui.PersonWrapperTextFieldsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author wladimiiir
 */
public class ProgrammeInfoPanel extends AbstractInfoPanel {

    private final GoToEntityAction goToPersonAction = new GoToEntityAction(MemberModule.class);
    private Programme programme;
    private JScrollPane spnDescription;
    private JTextField tfdProgrammeName;
    private JTextArea txaDescription;
    private PersonWrapperTextFieldsPanel pwtfpAuthors;
    private PersonWrapperTextFieldsPanel pwtfpComposers;
    private PersonWrapperTextFieldsPanel pwtfpChoreographers;
    private JList lstInterpreters;
    private boolean interpretersVisible;

    public ProgrammeInfoPanel() {
        this(true);
    }

    public ProgrammeInfoPanel(boolean interpretersVisible) {
        this.interpretersVisible = interpretersVisible;
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        JPanel pnlMiddle = new JPanel(new GridBagLayout());
        JPanel pnlRight = new JPanel(new BorderLayout());

        tfdProgrammeName = new JTextField();
        spnDescription = new JScrollPane();
        txaDescription = new JTextArea();
        pwtfpAuthors = new ManagerTextFieldsPanel();
        pwtfpChoreographers = new ManagerTextFieldsPanel();
        pwtfpComposers = new ManagerTextFieldsPanel();
        lstInterpreters = new JList(new DefaultListModel());

        pnlLeft.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("basicInfo")));

        jLabel1.setText(LocaleManager.getString("programmeName"));
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 2, 0, 5);
        pnlLeft.add(jLabel1, c);

        jLabel2.setText(LocaleManager.getString("description"));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(5, 2, 0, 5);
        pnlLeft.add(jLabel2, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0;
        c.insets = new Insets(5, 0, 0, 2);
        pnlLeft.add(tfdProgrammeName, c);

        spnDescription.setMinimumSize(new Dimension(22, 78));

        txaDescription.setColumns(10);
        txaDescription.setRows(5);
        spnDescription.setViewportView(txaDescription);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0;
        c.insets = new Insets(5, 0, 2, 2);
        pnlLeft.add(spnDescription, c);

        c.gridy++;
        c.weighty = 1.0;
        pnlLeft.add(new JPanel(), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(0, 0, 0, 0);
        JScrollPane scrollPane = new JScrollPane(pwtfpAuthors);
        scrollPane.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("authors")));
        scrollPane.setMaximumSize(new Dimension(1000, 60));
        scrollPane.setPreferredSize(new Dimension(100, 60));
        pnlMiddle.add(scrollPane, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        scrollPane = new JScrollPane(pwtfpChoreographers);
        scrollPane.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("choreography")));
        scrollPane.setMaximumSize(new Dimension(1000, 60));
        scrollPane.setPreferredSize(new Dimension(100, 60));
        pnlMiddle.add(scrollPane, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        scrollPane = new JScrollPane(pwtfpComposers);
        scrollPane.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("musicComposing")));
        scrollPane.setMaximumSize(new Dimension(1000, 60));
        scrollPane.setPreferredSize(new Dimension(100, 60));
        pnlMiddle.add(scrollPane, c);


        lstInterpreters.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lstInterpreters.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2 || !SwingUtilities.isLeftMouseButton(e) || lstInterpreters.
                        getSelectedIndex() == -1) {
                    return;
                }

                Person person = ((PersonWrapper) lstInterpreters.getSelectedValue()).getPerson();
                if (person != null) {
                    goToPersonAction.goTo(person);
                }
            }
        });
        pnlRight.setLayout(new BorderLayout());
        scrollPane = new JScrollPane(lstInterpreters);
        scrollPane.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("interpreters")));
        scrollPane.setMaximumSize(new Dimension(1000, 60));
        scrollPane.setPreferredSize(new Dimension(100, 60));
        pnlRight.add(scrollPane, BorderLayout.CENTER);


        c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        c.weightx = c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;

        layoutPanel.add(pnlLeft, c);
        c.gridx++;
        layoutPanel.add(pnlMiddle, c);
        if (interpretersVisible) {
            c.gridx++;
            layoutPanel.add(pnlRight, c);
        }

        initListeners();

        return layoutPanel;
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("programme");
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Programme)) {
            return;
        }
        final Programme p = (Programme) object;

        p.setName(tfdProgrammeName.getText());
        p.setDescription(txaDescription.getText());
        p.setAuthors(pwtfpAuthors.getValues());
        p.setChoreographers(pwtfpChoreographers.getValues());
        p.setComposers(pwtfpComposers.getValues());
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Programme)) {
            return;
        }

        programme = (Programme) object;
        initialized = false;
    }

    @Override
    public void initData() {
        if (programme == null || initialized) {
            return;
        }

        tfdProgrammeName.setText(programme.getName());
        txaDescription.setText(programme.getDescription());
        pwtfpAuthors.setValues(programme.getAuthors());
        pwtfpChoreographers.setValues(programme.getChoreographers());
        pwtfpComposers.setValues(programme.getComposers());

        ((DefaultListModel) lstInterpreters.getModel()).clear();
        for (PersonWrapper interpreter : programme.getInterpreters()) {
            ((DefaultListModel) lstInterpreters.getModel()).addElement(interpreter);
        }

        initialized = true;
    }

    private void initListeners() {
        tfdProgrammeName.getDocument().addDocumentListener(documentListener);
        txaDescription.getDocument().addDocumentListener(documentListener);
        pwtfpComposers.addChangeListener(changeListener);
        pwtfpChoreographers.addChangeListener(changeListener);
        pwtfpAuthors.addChangeListener(changeListener);
    }

    private class ManagerTextFieldsPanel extends PersonWrapperTextFieldsPanel {
        @Override
        protected String getPersonQuery() {
            return "select p from Person p, ManagerData md where md in elements(p.personDatas)";
        }

        @Override
        protected boolean acceptObject(Object object) {
            if (!(object instanceof Person)) {
                return false;
            }

            Person person = (Person) object;

            return person.getPersonData(ManagerData.class) != null;
        }
    }
}
