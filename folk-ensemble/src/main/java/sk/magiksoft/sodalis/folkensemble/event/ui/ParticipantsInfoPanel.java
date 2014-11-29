package sk.magiksoft.sodalis.folkensemble.event.ui;

import sk.magiksoft.sodalis.core.action.ContextTransferAction;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.entity.Entity;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.event.EventModule;
import sk.magiksoft.sodalis.event.data.EventTypeSubject;
import sk.magiksoft.sodalis.event.entity.EventEntityData;
import sk.magiksoft.sodalis.event.entity.EventType;
import sk.magiksoft.sodalis.event.entity.Event;
import sk.magiksoft.sodalis.folkensemble.event.entity.EnsembleEventData;
import sk.magiksoft.sodalis.folkensemble.member.MemberModule;
import sk.magiksoft.sodalis.folkensemble.member.data.MemberDataManager;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;
import sk.magiksoft.swing.ISTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * @author wladimiiir
 */
@EventTypeSubject
public class ParticipantsInfoPanel extends AbstractInfoPanel {

    private ParticipantsTableModel model = new ParticipantsTableModel();
    private ISTable participantsTable = new ISTable(model);
    private JButton btnAddParticipants = new JButton(new AddParticipantsAction());
    private JButton btnRemoveParticipants = new JButton(new RemoveParticipantsAction());
    private List<AbstractButton> controlPanelButtons = new ArrayList<AbstractButton>(Arrays.asList(btnAddParticipants, btnRemoveParticipants));
    private Event event;

    @Override
    public void initData() {
        if (initialized) {
            return;
        }

        model.setObjects(event.getEventData(EnsembleEventData.class) == null
                ? Collections.EMPTY_LIST
                : event.getEventData(EnsembleEventData.class).getParticipants());
        initialized = true;
    }

    @Override
    public List<AbstractButton> getControlPanelButtons() {
        return controlPanelButtons;
    }

    @Override
    public boolean isWizardSupported() {
        return false;
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane scrollPane = new JScrollPane(participantsTable);

        participantsTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row;
                if (e.getClickCount() != 2 || (row = participantsTable.rowAtPoint(e.getPoint())) < 0) {
                    return;
                }
                goToMember(model.getObject(row));
            }
        });
        participantsTable.getColumnModel().getColumn(0).setMaxWidth(25);
        scrollPane.getViewport().setBackground(ColorList.SCROLLPANE_BACKGROUND);
        layoutPanel.add(scrollPane, BorderLayout.CENTER);
        layoutPanel.setPreferredSize(new Dimension(100, 200));

        return layoutPanel;
    }

    private void goToMember(final Entity member) {
        if (member == null) {
            return;
        }
        new ContextTransferAction(null, MemberModule.class) {

            @Override
            protected boolean initialize(Context context) {
                context.setSelectedEntities(Collections.singletonList(member));
                return true;
            }

            @Override
            protected void finalize(Context context) {
            }
        }.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "goToMember"));
    }

    @Override
    public boolean acceptObject(Object object) {
        if (!(object instanceof Event)) {
            return false;
        }

        final EventType eventType = ((Event) object).getEventType();
        for (String infoPanelClass : eventType.getInfoPanelClassNames()) {
            if (infoPanelClass.equals(getClass().getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("ParticipantsInfoPanel.name");
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Event)) {
            return;
        }
        Event event = (Event) object;

        if (event.getEventData(EnsembleEventData.class) == null) {
            event.getEventDatas().put(EnsembleEventData.class, new EnsembleEventData());
        }
        event.getEventData(EnsembleEventData.class).setParticipants(model.getObjects());
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Event)) {
            return;
        }
        event = (Event) object;
        initialized = false;
    }

    private class AddParticipantsAction extends ContextTransferAction {

        public AddParticipantsAction() {
            super(EventModule.class, MemberModule.class);
            this.putValue(AbstractAction.NAME, LocaleManager.getString("add"));
        }

        @Override
        protected boolean initialize(Context context) {
            return true;
        }

        @Override
        protected void finalize(Context context) {
            final List<Person> selectedMembers = context == null ? null : (List<Person>) context.getSelectedEntities();
            if (selectedMembers == null) {
                return;
            }

            final List<PersonWrapper> personWrappers = new ArrayList<PersonWrapper>(selectedMembers.size());

            for (Person person : selectedMembers) {
                if (person.getPersonData(EventEntityData.class) == null) {
                    person.putPersonData(new EventEntityData());
                    MemberDataManager.updateDatabaseEntity(person);
                }
                personWrappers.add(new PersonWrapper(person));
            }

            for (PersonWrapper personWrapper : personWrappers) {
                boolean found = false;
                for (PersonWrapper modelWrapper : model.getObjects()) {
                    if (personWrapper.getPerson().getId().equals(modelWrapper.getPerson().getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    model.addObject(personWrapper);
                }
            }

            model.sort(1, true);
            fireEditing();
        }
    }

    private class RemoveParticipantsAction extends AbstractAction {
        private RemoveParticipantsAction() {
            super(LocaleManager.getString("remove"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final int[] selectedRows = participantsTable.getSelectedRows();

            if (selectedRows.length == 0) {
                return;
            }

            if (ISOptionPane.showConfirmDialog(
                    ParticipantsInfoPanel.this,
                    selectedRows.length == 1 ? LocaleManager.getString("removeRecordConfirm") : LocaleManager.getString("removeRecordsConfirm"),
                    LocaleManager.getString("remove"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
            Arrays.sort(selectedRows);
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                model.removeObject(selectedRows[i]);
            }
        }
    }

    private class ParticipantsTableModel extends ObjectTableModel<PersonWrapper> {

        private final Comparator<PersonWrapper> FULL_NAME_COMPARATOR = new Comparator<PersonWrapper>() {

            @Override
            public int compare(PersonWrapper o1, PersonWrapper o2) {
                return o1.getPersonName().compareTo(o2.getPersonName());
            }
        };

        public ParticipantsTableModel() {
            super(new Object[]{
                    LocaleManager.getString("ParticipantsInfoPanel.number"),
                    LocaleManager.getString("ParticipantsInfoPanel.name")});
        }

        @Override
        public Comparator getComparator(int column) {
            switch (column) {
                case 1:
                    return FULL_NAME_COMPARATOR;
            }
            return null;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            PersonWrapper wrapper = getObject(rowIndex);
            if (wrapper == null) {
                return "";
            }

            switch (columnIndex) {
                case 0:
                    return rowIndex + 1;
                case 1:
                    return wrapper.getPersonName();
            }
            return "";
        }

    }
}
