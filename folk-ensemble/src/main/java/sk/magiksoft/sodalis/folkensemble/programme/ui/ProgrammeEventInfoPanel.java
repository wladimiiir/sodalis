package sk.magiksoft.sodalis.folkensemble.programme.ui;

import sk.magiksoft.sodalis.core.action.GoToEntityAction;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.event.EventModule;
import sk.magiksoft.sodalis.event.data.EventDataManager;
import sk.magiksoft.sodalis.event.entity.Event;
import sk.magiksoft.sodalis.event.ui.EventTableModel;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.swing.ISTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

/**
 * @author wladimiiir
 */
public class ProgrammeEventInfoPanel extends AbstractInfoPanel {

    private final GoToEntityAction goToEventAction = new GoToEntityAction(EventModule.class);
    private Programme programme;
    private EventTableModel eventTableModel;

    public ProgrammeEventInfoPanel() {
        super(Programme.class);
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("events");
    }

    @Override
    public void setupObject(Object object) {
    }

    @Override
    public void setupPanel(Object object) {
        if (!acceptObject(object)) {
            return;
        }

        programme = (Programme) object;
        initialized = false;
    }

    @Override
    public void initData() {
        if (initialized || !acceptObject(programme)) {
            return;
        }

        List<Event> events =
                EventDataManager.getInstance().getEvents(Collections.singleton(programme.getId()));

        eventTableModel.setObjects(events);

        initialized = true;
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new BorderLayout());
        final ISTable table = new ISTable(eventTableModel = new EventTableModel());
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.getViewport().setBackground(ColorList.SCROLLPANE_BACKGROUND);
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2 || !SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }

                int row = table.rowAtPoint(e.getPoint());
                if (row != -1) {
                    goToEventAction.goTo(eventTableModel.getObject(row));
                }
            }
        });

        layoutPanel.add(scrollPane, BorderLayout.CENTER);
        layoutPanel.setPreferredSize(new Dimension(100, 100));

        return layoutPanel;
    }
}
