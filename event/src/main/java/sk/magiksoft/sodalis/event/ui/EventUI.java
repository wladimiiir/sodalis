package sk.magiksoft.sodalis.event.ui;

import net.sf.jasperreports.engine.JRRewindableDataSource;
import sk.magiksoft.sodalis.category.CategoryDataManager;
import sk.magiksoft.sodalis.category.CategoryManager;
import sk.magiksoft.sodalis.category.report.CategoryWrapperDataSource;
import sk.magiksoft.sodalis.category.ui.CategoryTreeComboBox;
import sk.magiksoft.sodalis.category.ui.CategoryTreeComponent;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.MessageAction;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.Entity;
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyJRDataSource;
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.filter.action.FilterEvent;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.printing.DefaultSettingsTableSettingsListener;
import sk.magiksoft.sodalis.core.printing.JRExtendedDataSource;
import sk.magiksoft.sodalis.core.printing.PrintPreview;
import sk.magiksoft.sodalis.core.printing.TablePrintDialog;
import sk.magiksoft.sodalis.core.ui.AbstractContext;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.controlpanel.InfoPanel;
import sk.magiksoft.sodalis.core.utils.UIUtils;
import sk.magiksoft.sodalis.event.EventContextManager;
import sk.magiksoft.sodalis.event.EventModule;
import sk.magiksoft.sodalis.event.action.*;
import sk.magiksoft.sodalis.event.data.EventDataManager;
import sk.magiksoft.sodalis.event.entity.Event;
import sk.magiksoft.sodalis.event.settings.EventSettings;
import sk.magiksoft.sodalis.event.ui.event.TimePanelListener;
import sk.magiksoft.swing.CalendarPanel;
import sk.magiksoft.swing.HideableSplitPane;
import sk.magiksoft.swing.calendar.DayChangedEvent;
import sk.magiksoft.swing.calendar.DayChangedListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EventUI extends AbstractContext implements PropertyChangeListener, DataListener {

    private static Calendar calendar = Calendar.getInstance();

    static {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
    }

    private CalendarPanel calendarPanel = new CalendarPanel(calendar);
    private JList lstDayEvents = new JList();
    private JList lstWeekEvents = new JList();
    private JList lstMonthEvents = new JList();
    private JTabbedPane tbpEvents = new JTabbedPane(JTabbedPane.BOTTOM);
    private TimePanel timePanel = new TimePanel();
    private Calendar currentDay = calendar;
    private Event selectedEvent = null;
    private EventListModel dayModel = new EventListModel();
    private EventListModel weekModel = new EventListModel();
    private EventListModel monthModel = new EventListModel();
    private JPanel leftCenterPanel = new JPanel(new BorderLayout());
    private EventTableContext eventTableContext = new EventTableContext();
    private FilterEvent filterEvent;

    private JTabbedPane tabbedPane = new JTabbedPane();
    private List<MessageAction> actions = new LinkedList<MessageAction>();
    private SwingWorker<Void, List<Event>> reloadWorker;

    public EventUI() {
        super(Event.class);
        initComponents();
        SodalisApplication.get().getStorageManager().registerComponent("eventUI", this);
        EventSettings.getInstance().addPropertyChangeListener(this);
        setupKeys();
    }

    public void addToCalendar(int field, int value) {
        Calendar day = (Calendar) currentDay.clone();

        day.add(field, value);
        calendarPanel.setSelectedDay(day);
    }

    private void setSelectedEntities(List<? extends Entity> entities, boolean selectTableContext, boolean scrollToEvent) {
        final Event event = (Event) (entities.isEmpty() ? null : entities.get(0));

        selectedEvent = event;
        if (selectTableContext) {
            adjusting = true;
            eventTableContext.setSelectedEntities(entities);
            adjusting = false;
        }
        timePanel.setSelectedEvent(event);
        lstDayEvents.setSelectedValue(event, true);
        lstWeekEvents.setSelectedValue(event, true);
        lstMonthEvents.setSelectedValue(event, true);
        if (scrollToEvent) {
            scrollToEvent(event);
        }
        setupControlPanel(event);
        currentObjectChanged();
    }

    @Override
    public void setSelectedEntities(List<? extends Entity> entities) {
        setSelectedEntities(entities, true, true);
    }

    @Override
    public List<? extends Entity> getSelectedEntities() {
        return isTimePanelShown() ? selectedEvent == null ? Collections.EMPTY_LIST : Collections.singletonList(selectedEvent) : eventTableContext.getSelectedEntities();
    }

    public EventTableContext getEventTableContext() {
        return eventTableContext;
    }

    public Calendar getCurrentDay() {
        return currentDay;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void addDayChangedListener(DayChangedListener listener) {
        calendarPanel.addDayChangedListener(listener);
    }

    @Override
    protected void currentObjectChanged() {
        for (MessageAction action : actions) {
            final ActionMessage actionMessage = action.getActionMessage(getSelectedEntities());
            action.setEnabled(actionMessage.isAllowed());
            action.putValue(Action.SHORT_DESCRIPTION, actionMessage.getMessage());
        }
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        eventTableContext.entitiesAdded(entities);

        boolean found = false;
        for (Object record : entities) {
            if (!(record instanceof Event) || !acceptEntity((DatabaseEntity) record, true)) {
                continue;
            }
            Event event = (Event) record;

            if (event.acceptDay(currentDay)) {
                for (int i = 0; i < dayModel.getSize(); i++) {
                    Event modelEvent = (Event) dayModel.get(i);
                    if (modelEvent.getId().equals(event.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    dayModel.addElement(event);
                }
            }
            if (event.acceptWeek(currentDay)) {
                found = false;
                for (int i = 0; i < weekModel.getSize(); i++) {
                    Event modelEvent = (Event) weekModel.get(i);
                    if (modelEvent.getId().equals(event.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    weekModel.addElement(event);
                }
            }
            if (event.acceptMonth(currentDay)) {
                found = false;
                for (int i = 0; i < monthModel.getSize(); i++) {
                    Event modelEvent = (Event) monthModel.get(i);
                    if (modelEvent.getId().equals(event.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    monthModel.addElement(event);
                }
            }
            found = false;
            for (EventPanel eventPanel : timePanel.getEventPanels()) {
                if (eventPanel.getEvent().getId().equals(event.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found && event.acceptDays(currentDay, EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT))) {
                timePanel.addEventPanel(new EventPanel(event));
            }
        }

    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        eventTableContext.entitiesRemoved(entities);

        for (Object record : entities) {
            if (!(record instanceof Event)) {
                continue;
            }
            Event event = (Event) record;
            for (int i = 0; i < dayModel.getSize(); i++) {
                Event modelEvent = (Event) dayModel.get(i);
                if (modelEvent.getId().equals(event.getId())) {
                    dayModel.removeElement(modelEvent);
                    break;
                }
            }
            for (int i = 0; i < weekModel.getSize(); i++) {
                Event modelEvent = (Event) weekModel.get(i);
                if (modelEvent.getId().equals(event.getId())) {
                    weekModel.removeElement(modelEvent);
                    break;
                }
            }
            for (int i = 0; i < monthModel.getSize(); i++) {
                Event modelEvent = (Event) monthModel.get(i);
                if (modelEvent.getId().equals(event.getId())) {
                    monthModel.removeElement(modelEvent);
                    break;
                }
            }
            timePanel.removeEvent(event.getId());
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        eventTableContext.entitiesUpdated(entities);

        for (final Object record : entities) {
            if (!(record instanceof Event)) {
                continue;
            }
            final Event event = (Event) record;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    boolean found = false;
                    for (int i = 0; i < dayModel.getSize(); i++) {
                        Event modelEvent = (Event) dayModel.get(i);
                        if (modelEvent.getId().equals(event.getId())) {
                            found = true;
                            if (!event.acceptDay(currentDay)) {
                                dayModel.removeElement(modelEvent);
                                break;
                            }
                            modelEvent.updateFrom(event);
                            dayModel.sort();
                            lstDayEvents.setSelectedValue(selectedEvent, true);
                            lstDayEvents.repaint();
                        }
                    }
                    if (!found && event.acceptDay(currentDay)) {
                        dayModel.addElement(record);
                    }
                    found = false;
                    for (int i = 0; i < weekModel.getSize(); i++) {
                        Event modelEvent = (Event) weekModel.get(i);
                        if (modelEvent.getId().equals(event.getId())) {
                            found = true;
                            if (!event.acceptWeek(currentDay)) {
                                weekModel.removeElement(modelEvent);
                                break;
                            }
                            modelEvent.updateFrom(event);
                            weekModel.sort();
                            lstWeekEvents.setSelectedValue(selectedEvent, true);
                            lstWeekEvents.repaint();
                        }
                    }
                    if (!found && event.acceptWeek(currentDay)) {
                        weekModel.addElement(record);
                    }
                    found = false;
                    for (int i = 0; i < monthModel.getSize(); i++) {
                        Event modelEvent = (Event) monthModel.get(i);
                        if (modelEvent.getId().equals(event.getId())) {
                            found = true;
                            if (!event.acceptMonth(currentDay)) {
                                monthModel.removeElement(modelEvent);
                                break;
                            }
                            modelEvent.updateFrom(event);
                            monthModel.sort();
                            lstMonthEvents.setSelectedValue(selectedEvent, true);
                            lstMonthEvents.repaint();
                        }
                    }
                    if (!found && event.acceptMonth(currentDay)) {
                        monthModel.addElement(record);
                    }
                    found = false;
                    for (EventPanel eventPanel : timePanel.getEventPanels()) {
                        if (eventPanel.getEvent().getId().equals(event.getId())) {
                            found = true;
                            if (!event.acceptDays(currentDay, EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT))) {
                                timePanel.removeEventPanel(eventPanel);
                                break;
                            }
                            eventPanel.getEvent().updateFrom(event);
                            timePanel.repaint();
                        }
                    }
                    if (!found && event.acceptDays(currentDay, EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT))) {
                        timePanel.addEventPanel(new EventPanel(event));
                    }

                    //refresh of control panel if the event has been dragged
                    if (selectedEvent != null && event.getId().equals(selectedEvent.getId())) {
                        controlPanel.setupControlPanel(event);
                    }
                }
            });

        }
    }

    public void filter(final FilterEvent event) {
        final String query = event.getQuery();
        final List entities = eventTableContext.getEntities();
        final List selected = eventTableContext.getSelectedEntities();

        this.filterEvent = event;
        if (event.getAction() == FilterEvent.ACTION_RESET || query == null || query.trim().isEmpty() || query.trim().equalsIgnoreCase("from")) {
            final Calendar startDate = (Calendar) currentDay.clone();
            final Calendar endDate = (Calendar) currentDay.clone();

            endDate.add(Calendar.DATE, EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT) - 1);
            new SwingWorker<Void, Void>() {

                private List<Event> events;

                @Override
                protected Void doInBackground() throws Exception {
                    events = EventDataManager.getInstance().getEventsInDate(startDate, endDate);
                    return null;
                }

                @Override
                protected void done() {
                    eventTableContext.removeAllRecords();
                    if (events != null) {
                        eventTableContext.entitiesAdded(events);
                        eventTableContext.setSelectedEntities(selected);
                    }
                }
            }.execute();
        } else {
            new SwingWorker<Void, Void>() {

                private List<Event> events;

                @Override
                protected Void doInBackground() throws Exception {
                    events = event.getAction() == FilterEvent.ACTION_FILTER_ALL
                            ? EventDataManager.getInstance().getDatabaseEntities(query)
                            : EventDataManager.getInstance().getDatabaseEntities(entities, query);
                    return null;
                }

                @Override
                protected void done() {
                    eventTableContext.removeAllRecords();
                    if (events != null) {
                        eventTableContext.entitiesAdded(events);
                        eventTableContext.setSelectedEntities(selected);
                    }
                }
            }.execute();
        }
    }

    @Override
    public void removeAllRecords() {
        timePanel.removeAllEventPanels();
        dayModel.removeAllElements();
        weekModel.removeAllElements();
        monthModel.removeAllElements();
        eventTableContext.removeAllRecords();
    }

    private void initComponents() {
        JPanel leftPanel = new JPanel(new BorderLayout());

        setLayout(new BorderLayout());
        leftPanel.setMaximumSize(new Dimension(180, Integer.MAX_VALUE));
        leftPanel.setMinimumSize(new Dimension(180, 0));
        leftPanel.setPreferredSize(new Dimension(180, 600));
        lstDayEvents.setCellRenderer(new EventListCellRenderer(EventListCellRenderer.FORMAT_TIME));
        lstDayEvents.setModel(dayModel);
        lstWeekEvents.setCellRenderer(new EventListCellRenderer(EventListCellRenderer.FORMAT_DATETIME));
        lstWeekEvents.setModel(weekModel);
        lstMonthEvents.setCellRenderer(new EventListCellRenderer(EventListCellRenderer.FORMAT_DATETIME));
        lstMonthEvents.setModel(monthModel);
        final MouseAdapter clickListener = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    setSelectedEntities(Collections.singletonList((Entity) ((JList) e.getSource()).getSelectedValue()));
                }
            }
        };
        lstDayEvents.addMouseListener(clickListener);
        lstWeekEvents.addMouseListener(clickListener);
        lstMonthEvents.addMouseListener(clickListener);
        categoryTreeComboBox = new CategoryTreeComboBox(EventModule.class);
        categoryTreeComboBox.addChangeListener(new CategoryTreeComboBoxChangeListener(EventSettings.getInstance(), EventContextManager.getInstance()));

        JScrollPane listDayScrollPane = new JScrollPane(lstDayEvents);
        JScrollPane listWeekScrollPane = new JScrollPane(lstWeekEvents);
        JScrollPane listMonthScrollPane = new JScrollPane(lstMonthEvents);
        tbpEvents.addTab(null, listDayScrollPane);
        tbpEvents.addTab(null, listWeekScrollPane);
        tbpEvents.addTab(null, listMonthScrollPane);
        tbpEvents.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        reloadTabNames();

        controlPanel = new EventControlPanel();
        JPanel centerTimePanel = new JPanel(new BorderLayout());
        JScrollPane tpScrollPane = new JScrollPane(timePanel.getTimeLinePanel());
        JScrollPane timePanelScrollPane = new JScrollPane(timePanel);
        HideableSplitPane centerSplitPane = new HideableSplitPane(HideableSplitPane.VERTICAL_SPLIT, tabbedPane, (Component) controlPanel);
        JPanel centerPanel = new JPanel(new BorderLayout());

        timePanel.getTopLeftCorner().setPreferredSize(new Dimension(100, 20));
        tpScrollPane.setColumnHeaderView(timePanel.getTopLeftCorner());
        tpScrollPane.getColumnHeader().setBackground(timePanel.getTimeLinePanel().getBackground());
        timePanelScrollPane.setVerticalScrollBar(tpScrollPane.getVerticalScrollBar());
        timePanelScrollPane.setColumnHeaderView(timePanel.getTimePanelHeader());
        timePanelScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, timePanel.getScaleControler());
        timePanelScrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, timePanel.getDayCountControler());
        timePanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        timePanelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        timePanelScrollPane.setHorizontalScrollBar(timePanel.getTimePanelFooter());
        timePanelScrollPane.getViewport().setBackground(Color.WHITE);
        timePanelScrollPane.getVerticalScrollBar().setUnitIncrement(50);
        timePanelScrollPane.getColumnHeader().setBackground(timePanel.getBackground());
        centerTimePanel.add(timePanelScrollPane, BorderLayout.CENTER);
        centerTimePanel.add(tpScrollPane, BorderLayout.WEST);
        calendarPanel.setBackground(ColorList.BLUE_1);
        leftPanel.add(calendarPanel, BorderLayout.NORTH);
        leftPanel.add(leftCenterPanel, BorderLayout.CENTER);
        timePanel.setPreferredSize(new Dimension(400, 1000));
        centerSplitPane.setName("eventUI.centerSplitPane");
        centerSplitPane.setLeftText(LocaleManager.getString("calendar"));
        centerSplitPane.setRightText(LocaleManager.getString("details"));
        centerSplitPane.setDividerLocation((int) 500);

        centerPanel.add(centerSplitPane, BorderLayout.CENTER);

        add(createToolBar(), BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);
        if (EventContextManager.getInstance().getFilterPanel() != null) {
            add(EventContextManager.getInstance().getFilterPanel(), BorderLayout.EAST);
        }
        leftCenterPanel.add(tbpEvents, BorderLayout.CENTER);

        tabbedPane.add(LocaleManager.getString("review"), centerTimePanel);
        tabbedPane.add(LocaleManager.getString("table"), eventTableContext);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (EventContextManager.getInstance().getFilterPanel() != null) {
                    EventContextManager.getInstance().getFilterPanel().setVisible(isEventTableShown());
                }
            }
        });

        eventTableContext.setCategoryTreeComboBox(categoryTreeComboBox);
        eventTableContext.addTableSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() || adjusting) {
                    return;
                }
                setSelectedEntities(eventTableContext.getSelectedEntities(), false, false);
            }
        });

        timePanel.addTimePanelListener(new TimePanelListener() {

            @Override
            public void eventAdded(Event event) {
                EventDataManager.getInstance().addDatabaseEntity(event);
            }

            @Override
            public void eventUpdated(Event event) {
                EventDataManager.getInstance().updateDatabaseEntity(event);
            }

            @Override
            public void eventRemoved(Event event) {
                EventDataManager.getInstance().removeDatabaseEntity(event);
            }

            @Override
            public void eventSelected(Event event) {
                selectedEvent = event;
                lstDayEvents.clearSelection();
                lstWeekEvents.clearSelection();
                lstMonthEvents.clearSelection();
                if (event != null) {
                    lstDayEvents.setSelectedValue(event, true);
                    lstWeekEvents.setSelectedValue(event, true);
                    lstMonthEvents.setSelectedValue(event, true);
                }
                adjusting = true;
                eventTableContext.setSelectedEntities(event == null ? Collections.<Entity>emptyList() : Collections.singletonList(event));
                adjusting = false;
                controlPanel.setupControlPanel(event);
                currentObjectChanged();
            }

            @Override
            public void eventDoubleClicked(Event event) {
                setupControlPanel(event);
            }

            @Override
            public boolean eventWillBeSelected(Event event) {
                if (controlPanel == null || !controlPanel.isEditing()) {
                    return true;
                }

                int result = ISOptionPane.showConfirmDialog(EventUI.this, LocaleManager.getString("saveChangesQuestion"),
                        selectedEvent == null ? "" : selectedEvent.toString(), ISOptionPane.YES_NO_CANCEL_OPTION);
                if (result == ISOptionPane.YES_OPTION) {
                    if (controlPanel != null) {
                        controlPanel.doUpdate();
                    }
                } else if (result == ISOptionPane.NO_OPTION) {
                    if (controlPanel != null) {
                        controlPanel.cancelEditing();
                    }
                }
                return result != ISOptionPane.CANCEL_OPTION;
            }
        });

        addDayChangedListener(new DayChangedListener() {

            @Override
            public void dayChanged(DayChangedEvent e) {
                currentDay = e.getNewDay();
                timePanel.setCurrentDay(e.getNewDay());
                reloadTabNames();
                reload();
            }
        });

        EventSettings.getInstance().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(EventSettings.I_DAY_COUNT)) {
                    reload();
                }
            }
        });
    }

    private void reloadTabNames() {
        tbpEvents.setTitleAt(0, currentDay.get(Calendar.DATE) + "." + LocaleManager.getString("day"));
        tbpEvents.setTitleAt(1, currentDay.get(Calendar.WEEK_OF_YEAR) + "." + LocaleManager.getString("week"));
        tbpEvents.setTitleAt(2, currentDay.get(Calendar.MONTH) + 1 + "." + LocaleManager.getString("month"));
    }

    private void setupKeys() {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), ActionFactory.ACTION_PREVIOUS_DAY);
        getActionMap().put(ActionFactory.ACTION_PREVIOUS_DAY, ActionFactory.getInstance().getAction(ActionFactory.ACTION_PREVIOUS_DAY));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), ActionFactory.ACTION_NEXT_DAY);
        getActionMap().put(ActionFactory.ACTION_NEXT_DAY, ActionFactory.getInstance().getAction(ActionFactory.ACTION_NEXT_DAY));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), ActionFactory.ACTION_PREVIOUS_WEEK);
        getActionMap().put(ActionFactory.ACTION_PREVIOUS_WEEK, ActionFactory.getInstance().getAction(ActionFactory.ACTION_PREVIOUS_WEEK));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), ActionFactory.ACTION_NEXT_WEEK);
        getActionMap().put(ActionFactory.ACTION_NEXT_WEEK, ActionFactory.getInstance().getAction(ActionFactory.ACTION_NEXT_WEEK));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "removeEvent");
        getActionMap().put("removeEvent", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedEvent();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), ActionFactory.ACTION_TOGGLE_SNAP);
        getActionMap().put(ActionFactory.ACTION_TOGGLE_SNAP, ActionFactory.getInstance().getAction(ActionFactory.ACTION_TOGGLE_SNAP));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), ActionFactory.ACTION_CHANGE_COLOR);
        getActionMap().put(ActionFactory.ACTION_CHANGE_COLOR, ActionFactory.getInstance().getAction(ActionFactory.ACTION_CHANGE_COLOR));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), ActionFactory.ACTION_DEFAULT_EVENT);
        getActionMap().put(ActionFactory.ACTION_DEFAULT_EVENT, ActionFactory.getInstance().getAction(ActionFactory.ACTION_DEFAULT_EVENT));
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), ActionFactory.ACTION_EVENT_DURATION);
        getActionMap().put(ActionFactory.ACTION_EVENT_DURATION, ActionFactory.getInstance().getAction(ActionFactory.ACTION_EVENT_DURATION));
    }

    private void removeSelectedEvent() {
        int result;

        if (getSelectedEvent() == null) {
            return;
        }
        result = ISOptionPane.showConfirmDialog(SodalisApplication.get().getMainFrame(),
                LocaleManager.getString("removeEventConfirm"),
                LocaleManager.getString("event"),
                ISOptionPane.YES_NO_OPTION);
        if (result != ISOptionPane.YES_OPTION) {
            return;
        }

        EventDataManager.getInstance().removeDatabaseEntity(getSelectedEvent());
    }

    private void scrollToEvent(Event event) {
        if (event == null || event.isRepeating() || event.isDeleted()) {
            return;
        }
        if (!timePanel.containsEvent(event)) {
            calendarPanel.setSelectedDay(event.getStartTime());
            return;
        }
        timePanel.scrollToEvent(event);
    }

    private void setupControlPanel(final Event event) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                controlPanel.setupControlPanel(event);
            }
        });
    }

    public void reload() {
        final Event selectedEvent = this.selectedEvent;
        final Calendar startDate = (Calendar) currentDay.clone();
        final Calendar endDate = (Calendar) currentDay.clone();

        endDate.add(Calendar.DATE, EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT) - 1);
        if (reloadWorker != null && !reloadWorker.isDone()) {
            reloadWorker.cancel(true);
        }
        reloadWorker = new SwingWorker<Void, List<Event>>() {

            private List<Event> timePanelEvents;
            private List<Event> weekEvents;
            private List<Event> monthEvents;

            @Override
            protected Void doInBackground() throws Exception {
                if (isCancelled()) {
                    return null;
                }
                timePanelEvents = EventDataManager.getInstance().getEventsInDate(startDate, endDate);
                if (isCancelled()) {
                    return null;
                }
                publish(timePanelEvents);
                monthEvents = EventDataManager.getInstance().getEventsForMonth(currentDay);
                if (isCancelled()) {
                    return null;
                }
                weekEvents = EventDataManager.getInstance().getEventsForWeek(currentDay);
                return null;
            }

            @Override
            protected void process(List<List<Event>> chunks) {
                if (isCancelled()) {
                    return;
                }
                removeAllRecords();
                if (timePanelEvents != null) {
                    entitiesAdded(chunks.get(0));
                }
                if (selectedEvent != null && selectedEvent.acceptDays(startDate, EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT))) {
                    setSelectedEntities(Collections.singletonList(selectedEvent));
                } else {
                    setSelectedEntities(Collections.EMPTY_LIST);
                }
            }

            @Override
            protected void done() {
                if (isCancelled()) {
                    return;
                }
                weekModel.removeAllElements();
                if (weekEvents != null) {
                    for (Event weekEvent : weekEvents) {
                        weekModel.addElement(weekEvent);
                    }
                }
                monthModel.removeAllElements();
                if (monthEvents != null) {
                    for (Event monthEvent : monthEvents) {
                        monthModel.addElement(monthEvent);
                    }
                }
            }
        };
        reloadWorker.execute();
    }

    @Override
    public boolean canChangeEntity() {
        if (!controlPanel.isEditing()) {
            return true;
        }
        int result = ISOptionPane.showConfirmDialog(this,
                sk.magiksoft.sodalis.core.locale.LocaleManager.getString("saveChangesQuestion"), selectedEvent.toString(), ISOptionPane.YES_NO_CANCEL_OPTION);
        if (result == ISOptionPane.YES_OPTION) {
            if (controlPanel.isEditing()) {
                controlPanel.doUpdate();
            }
        }
        return result != ISOptionPane.CANCEL_OPTION;
    }

    @Override
    public void setSelectedInfoPanelClass(Class<? extends InfoPanel> infoPanelClass) {

    }

    @Override
    public Class<? extends InfoPanel> getSelectedInfoPanelClass() {
        return null;
    }

    @Override
    public List<? extends Entity> getEntities() {
        List events = new ArrayList();

        for (EventPanel eventPanel : timePanel.getEventPanels()) {
            events.add(eventPanel.getEvent());
        }

        return events;
    }

    private JToolBar createToolBar() {
        final JToolBar toolBar = UIUtils.createToolBar();
        final GridBagConstraints c = new GridBagConstraints();
        final AddEventAction addEventAction = new AddEventAction();
        final RemoveEventAction removeEventAction = new RemoveEventAction();
        final PrintEventsAction printEventsAction = new PrintEventsAction();
        final EventExportAction eventExportAction = new EventExportAction();
        final EventImportAction eventImportAction = new EventImportAction();

        actions.add(addEventAction);
        actions.add(removeEventAction);
        actions.add(printEventsAction);
        actions.add(eventExportAction);
        actions.add(eventImportAction);

        for (MessageAction action : actions) {
            action.setEnabled(false);
        }

        toolBar.setLayout(new GridBagLayout());
        toolBar.setFloatable(false);

        c.gridx = c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        toolBar.add(initToolbarButton(new JButton(addEventAction)), c);
        c.gridx++;
        toolBar.add(initToolbarButton(new JButton(removeEventAction)), c);
        c.gridx++;
        toolBar.add(initToolbarButton(new JButton(printEventsAction)), c);
        c.gridx++;
        toolBar.add(initToolbarButton(new JButton(eventExportAction)), c);
        c.gridx++;
        toolBar.add(initToolbarButton(new JButton(eventImportAction)), c);
        c.gridx++;
        toolBar.add(initToolbarButton(eventTableContext.getCategoryTreeComponent().getShowCategoryTreeButton()), c);
        eventTableContext.getCategoryTreeComponent().getShowCategoryTreeButton().setEnabled(true);
        c.gridx++;
        c.weightx = 1.0;
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        toolBar.add(panel, c);
        c.gridx++;
        c.weightx = 0.0;
        toolBar.add(categoryTreeComboBox, c);

        return toolBar;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(EventSettings.O_SELECTED_CATEGORIES)) {
            final List<Long> ids = (List<Long>) EventSettings.getInstance().getValue(EventSettings.O_SELECTED_CATEGORIES);

            categoryTreeComboBox.setSelectedCategories(CategoryDataManager.getInstance().getCategories(ids));
        }
    }

    private boolean isTimePanelShown() {
        return tabbedPane.getSelectedIndex() == 0;
    }

    private boolean isEventTableShown() {
        return tabbedPane.getSelectedIndex() == 1;
    }

    private class PrintEventsAction extends MessageAction {
        private PrintEventsAction() {
            super(null, IconFactory.getInstance().getIcon("print"));
        }

        @Override
        public ActionMessage getActionMessage(List objects) {
            return new ActionMessage(true, LocaleManager.getString("print"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isTimePanelShown()) {
                new PrintPreview(timePanel).setVisible(true);
            } else if (isEventTableShown()) {
                final CategoryTreeComponent categoryTreeComponent = eventTableContext.getCategoryTreeComponent();
                final boolean categoryShown = categoryTreeComponent.isComponentShown();
                final List objects = categoryShown
                        ? CategoryManager.getInstance().getCategoryPathWrappers(categoryTreeComponent.getRoot())
                        : eventTableContext.getEntities();
                JRRewindableDataSource dataSource = new EntityPropertyJRDataSource<Event>(scala.collection.JavaConversions.asScalaBuffer(objects).toList());

                if (categoryShown) {
                    dataSource = new CategoryWrapperDataSource(objects, (JRExtendedDataSource) dataSource);
                }
                final TablePrintDialog dialog = new TablePrintDialog(EventSettings.getInstance(),
                        EntityPropertyTranslatorManager.getTranslator(Event.class), dataSource);

                if (categoryShown) {
                    dialog.setGroups(categoryTreeComponent.getSelectedCategoryPath());
                }
                dialog.addTableSettingsListener(new DefaultSettingsTableSettingsListener(EventSettings.getInstance()));
                dialog.setVisible(true);


            }
        }

    }
}
