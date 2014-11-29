package sk.magiksoft.sodalis.folkensemble.repertory.ui;

import sk.magiksoft.sodalis.core.action.ContextTransferAction;
import sk.magiksoft.sodalis.core.action.GoToEntityAction;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.folkensemble.member.MemberModule;
import sk.magiksoft.sodalis.folkensemble.repertory.RepertoryModule;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;
import sk.magiksoft.swing.ISTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wladimiiir
 */
public class InterpretationInfoPanel extends AbstractInfoPanel {

    private Song song;
    private ObjectTableModel<PersonWrapper> model = new InterpreterTableModel();
    private ISTable interpertatorsTable = new ISTable(model);
    private JButton btnAddInterpreters = new JButton(new AddInterpretersContextAction());
    private JButton btnRemoveInterpreters = new JButton(new RemoveInterpretersAction());
    private GoToEntityAction goToPersonAction = new GoToEntityAction(MemberModule.class);
    private final List<AbstractButton> controlPanelButtons = new ArrayList<AbstractButton>(Arrays.asList(btnAddInterpreters, btnRemoveInterpreters));

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new BorderLayout());
        final JScrollPane scrollPane = new JScrollPane(interpertatorsTable);

        scrollPane.getViewport().setBackground(ColorList.SCROLLPANE_BACKGROUND);
        layoutPanel.add(scrollPane, BorderLayout.CENTER);
        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                fireEditing();
            }
        });
        interpertatorsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btnRemoveInterpreters.setEnabled(interpertatorsTable.getSelectedRow() != -1);
            }
        });
        interpertatorsTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }

                int row = interpertatorsTable.rowAtPoint(e.getPoint());
                if (row == -1) {
                    return;
                }

                PersonWrapper pw = model.getObject(row);
                if (pw.getPerson() != null) {
                    goToPersonAction.goTo(pw.getPerson());
                }
            }
        });
        scrollPane.setPreferredSize(new Dimension(100, 50));
        btnRemoveInterpreters.setEnabled(false);

        return layoutPanel;
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("interpretation");
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Song)) {
            return;
        }

        Song s = (Song) object;
        s.setInterpreters(model.getObjects());
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Song)) {
            return;
        }

        this.song = (Song) object;
        this.initialized = false;
    }

    @Override
    public void initData() {
        if (initialized || song == null) {
            return;
        }

        model.setObjects(song.getInterpreters());

        initialized = true;
    }

    @Override
    public List<AbstractButton> getControlPanelButtons() {
        return controlPanelButtons;
    }

    private class AddInterpretersContextAction extends ContextTransferAction {

        public AddInterpretersContextAction() {
            super(RepertoryModule.class, MemberModule.class);
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
        }
    }

    private class RemoveInterpretersAction extends AbstractAction {
        private RemoveInterpretersAction() {
            super(LocaleManager.getString("remove"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final int[] selectedRows = interpertatorsTable.getSelectedRows();

            if (selectedRows.length == 0) {
                return;
            }

            if (ISOptionPane.showConfirmDialog(
                    InterpretationInfoPanel.this,
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
}
