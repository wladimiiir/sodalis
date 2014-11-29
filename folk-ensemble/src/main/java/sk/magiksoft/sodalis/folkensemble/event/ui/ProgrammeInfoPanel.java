package sk.magiksoft.sodalis.folkensemble.event.ui;

import sk.magiksoft.sodalis.core.action.ContextTransferAction;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.event.EventModule;
import sk.magiksoft.sodalis.event.data.EventTypeSubject;
import sk.magiksoft.sodalis.event.entity.Event;
import sk.magiksoft.sodalis.event.entity.EventType;
import sk.magiksoft.sodalis.folkensemble.event.entity.EnsembleEventData;
import sk.magiksoft.sodalis.folkensemble.programme.ProgrammeModule;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.sodalis.folkensemble.programme.entity.ProgrammeSong;
import sk.magiksoft.swing.ISTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wladimiiir
 */
@EventTypeSubject
public class ProgrammeInfoPanel extends AbstractInfoPanel {

    //--UI--
    private JLabel lblProgrammeName;
    private ProgrammeTableModel programmeTableModel;
    private JLabel lblTotalTime;
    private JButton btnChooseProgramme;
    private ISTable programmeTable;
    private JScrollPane programmeScrollPane;
    //--data--
    private Event event;
    private Programme programme;

    @Override
    public List<AbstractButton> getControlPanelButtons() {
        return Collections.<AbstractButton>singletonList(btnChooseProgramme);
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        lblProgrammeName = new JLabel();
        lblTotalTime = new JLabel();
        programmeTable = new ISTable(programmeTableModel = new ProgrammeTableModel());
        programmeTable.removeSortFunction();
        programmeScrollPane = new JScrollPane(programmeTable);
        btnChooseProgramme = new JButton(new ChooseProgrammeAction());
        programmeScrollPane.getViewport().setBackground(ColorList.SCROLLPANE_BACKGROUND);
        programmeScrollPane.setPreferredSize(new Dimension(100, 20));

        programmeTable.getColumnModel().getColumn(0).setMinWidth(25);
        programmeTable.getColumnModel().getColumn(0).setMaxWidth(25);
        programmeTable.getColumnModel().getColumn(2).setMinWidth(45);
        programmeTable.getColumnModel().getColumn(2).setMaxWidth(45);

        c.gridx = c.gridy = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(2, 2, 2, 2);
        layoutPanel.add(lblProgrammeName, c);

        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        layoutPanel.add(programmeScrollPane, c);

        c.gridy++;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        layoutPanel.add(lblTotalTime, c);

        return layoutPanel;
    }

    @Override
    public boolean isWizardSupported() {
        return false;
    }

    @Override
    public boolean acceptObject(Object object) {
        if (!(object instanceof Event)) {
            return false;
        }

        EventType eventType = ((Event) object).getEventType();

        for (String infoPanelClass : eventType.getInfoPanelClassNames()) {
            if (infoPanelClass.equals(getClass().getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("programme");
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Event)) {
            return;
        }
        Event objEvent = (Event) object;
        if (event.getEventData(EnsembleEventData.class) == null) {
            event.getEventDatas().put(EnsembleEventData.class, new EnsembleEventData());
        }
        objEvent.getEventData(EnsembleEventData.class).setProgramme(programme);
        if (objEvent.getEventName().isEmpty() && programme != null) {
            objEvent.setEventName(programme.getName());
        }
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Event)) {
            return;
        }

        this.event = (Event) object;
        initialized = false;
    }

    @Override
    public void initData() {
        if (initialized || event == null) {
            return;
        }

        this.programme = event.getEventData(EnsembleEventData.class) == null
                ? null
                : event.getEventData(EnsembleEventData.class).getProgramme();
        refreshProgramme();
        initialized = true;
    }

    private void refreshProgramme() {
        programmeTableModel.setObjects(programme == null
                ? new ArrayList<ProgrammeSong>()
                : programme.getProgrammeSongs());
        programmeScrollPane.setViewportView(programme == null ? new JPanel() : programmeTable);
        programmeScrollPane.setBorder(programme == null ? null : new JScrollPane().getBorder());
        lblProgrammeName.setText(programme == null
                ? " "
                : programme.getName());
        lblTotalTime.setText(programme == null
                ? " "
                : LocaleManager.getString("totalTimeValue", programme.getDurationString()));
    }

    private class ChooseProgrammeAction extends ContextTransferAction {

        public ChooseProgrammeAction() {
            super(EventModule.class, ProgrammeModule.class);
            putValue(Action.NAME, LocaleManager.getString("chooseProgram"));
        }

        @Override
        protected boolean initialize(Context context) {
            context.setSelectedEntities(Collections.singletonList(programme));

            return true;
        }

        @Override
        protected void finalize(Context context) {
            List<Programme> selected = context == null ? null : (List<Programme>) context.getSelectedEntities();

            if (selected == null) {
                return;
            }
            programme = selected.isEmpty()
                    ? null
                    : selected.get(0);
            refreshProgramme();
            fireEditing();
        }
    }
}
