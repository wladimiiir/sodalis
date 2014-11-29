package sk.magiksoft.sodalis.event.ui;

import com.toedter.calendar.JDateChooser;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.event.data.EventDataManager;
import sk.magiksoft.sodalis.event.entity.Attendee;
import sk.magiksoft.sodalis.event.entity.Event;
import sk.magiksoft.sodalis.event.entity.EventType;
import sk.magiksoft.sodalis.person.PersonModule;
import sk.magiksoft.swing.calendar.JTextFieldDateTimeEditor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EventInfoPanel extends AbstractInfoPanel {

    private JTextField tfdEventName = new JTextField();
    private JTextField tfdPlace = new JTextField();
    private Event event;
    private JDateChooser dchStartTime;
    private JDateChooser dchEndTime;
    private JComboBox cbxEventType = new JComboBox();
    private JPanel pnlColor = new JPanel();
    private AttendeeComponent attendeeComponent;
    private JCheckBox chbRepeatEvent = new JCheckBox(LocaleManager.getString("repeatingEvent"));
    private RepeatingEventPanel repeatingEventPanel;

    public EventInfoPanel() {
        super(Event.class);
    }

    @Override
    public void initData() {
        if (initialized) {
            return;
        }
        tfdEventName.setText(event.getEventName());
        tfdPlace.setText(event.getPlace());
        dchStartTime.setCalendar((Calendar) event.getStartTime().clone());
        dchEndTime.setCalendar((Calendar) event.getEndTime().clone());
        dchEndTime.setMinSelectableDate((Date) dchStartTime.getCalendar().getTime().clone());
        pnlColor.setBackground(event.getColor());
        pnlColor.repaint();
        initEventTypes(event);
        chbRepeatEvent.setSelected(event.getRepeatMask() != Event.REPEAT_NONE);
        attendeeComponent.setItems((List) (event.getAttendees() == null ? Collections.emptyList() : event.getAttendees()));
        repeatingEventPanel.setupPanel(event);
        initialized = true;
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel leftPanel = new JPanel(new GridBagLayout());
        final JPanel rightPanel = new JPanel(new BorderLayout());

        dchStartTime = new JDateChooser(new JTextFieldDateTimeEditor("dd.MM.yyyy HH:mm", "##.##.#### ##:##", '-'));
        dchEndTime = new JDateChooser(new JTextFieldDateTimeEditor("dd.MM.yyyy HH:mm", "##.##.#### ##:##", '-'));
        repeatingEventPanel = new RepeatingEventPanel();
        chbRepeatEvent.setFont(UIManager.getFont("TitledBorder.font"));
        chbRepeatEvent.setForeground(UIManager.getColor("TitledBorder.titleColor"));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0),
                BorderFactory.createTitledBorder(LocaleManager.getString("generalSettings"))));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(9, 0, 2, 0),
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(((LineBorder) UIManager.getBorder("TitledBorder.border")).getLineColor()),
                        BorderFactory.createEmptyBorder(2, 10, 5, 10))));

        pnlColor.setBorder(new LineBorder(Color.GRAY));

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        leftPanel.add(new JLabel(LocaleManager.getString("eventName")), c);

        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 3, 0, 5);
        leftPanel.add(tfdEventName, c);
        tfdEventName.setNextFocusableComponent(tfdPlace);

        c.gridy++;
        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(3, 5, 0, 0);
        leftPanel.add(new JLabel(LocaleManager.getString("place")), c);

        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(3, 3, 0, 5);
        leftPanel.add(tfdPlace, c);

        c.gridy++;
        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(3, 5, 0, 0);
        leftPanel.add(new JLabel(LocaleManager.getString("startTime")), c);

        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(3, 3, 0, 5);
        leftPanel.add(dchStartTime, c);

        c.gridy++;
        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(3, 5, 0, 0);
        leftPanel.add(new JLabel(LocaleManager.getString("endTime")), c);

        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(3, 3, 0, 5);
        leftPanel.add(dchEndTime, c);

        c.gridy++;
        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(3, 5, 0, 0);
        leftPanel.add(new JLabel(LocaleManager.getString("eventType")), c);

        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(3, 3, 0, 5);
        leftPanel.add(cbxEventType, c);

        c.gridy++;
        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(3, 5, 5, 0);
        leftPanel.add(new JLabel(LocaleManager.getString("eventColor")), c);

        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(3, 3, 5, 5);
        leftPanel.add(pnlColor, c);

        final JPanel attendeePanel = new JPanel(new BorderLayout());
        attendeePanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("attendees")));
        final PersonModule personModule = getPersonModule();
        attendeePanel.add(attendeeComponent = new AttendeeComponent(personModule == null ? null : personModule.getClass()), BorderLayout.CENTER);
        c.gridx++;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 3, 0);
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        c.gridheight = 6;
        leftPanel.add(attendeePanel, c);

        attendeeComponent.setMinimumSize(new Dimension(230, 100));

        rightPanel.add(chbRepeatEvent, BorderLayout.NORTH);
        rightPanel.add(repeatingEventPanel, BorderLayout.CENTER);
        rightPanel.setMinimumSize(new Dimension(205, 100));
        repeatingEventPanel.setEnabled(chbRepeatEvent.isSelected());

        tfdEventName.setMinimumSize(new Dimension(140, 22));
        tfdPlace.setMinimumSize(new Dimension(140, 22));
        dchStartTime.setMinimumSize(new Dimension(140, 23));
        dchEndTime.setMinimumSize(new Dimension(140, 23));
        cbxEventType.setMinimumSize(new Dimension(140, 22));
        pnlColor.setMinimumSize(new Dimension(140, 21));


        c.gridx = c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 1.0;
        c.weightx = 1.0;
        layoutPanel.add(leftPanel, c);
        c.gridx++;
        c.weightx = 0.0;
        layoutPanel.add(rightPanel, c);

        initListeners();
        layoutPanel.setPreferredSize(new Dimension(200, 200));

        return layoutPanel;
    }

    protected PersonModule getPersonModule() {
        return SodalisApplication.get().getModuleManager().getModuleBySuperClass(PersonModule.class);
    }

    private void initEventTypes(Event event) {
        EventType selected = null;

        cbxEventType.removeAllItems();
        for (EventType eventType : EventDataManager.getInstance().getDatabaseEntities(EventType.class)) {
            cbxEventType.addItem(eventType);
            if (eventType.getId().equals(event.getEventType().getId())) {
                selected = eventType;
            }
        }
        cbxEventType.setSelectedItem(selected);
    }

    private void initListeners() {
        pnlColor.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    Color color = JColorChooser.showDialog(SodalisApplication.get().getMainFrame(), "", pnlColor.getBackground());
                    if (color == null) {
                        return;
                    }
                    pnlColor.setBackground(color);
                    pnlColor.repaint();
                    fireEditing();
                }
            }
        });
        tfdEventName.getDocument().addDocumentListener(documentListener);
        tfdPlace.getDocument().addDocumentListener(documentListener);
        dchEndTime.addPropertyChangeListener("date", propertyChangeListener);
        dchStartTime.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                final Calendar endTime = dchEndTime.getCalendar();

                if (endTime == null || !endTime.after(dchStartTime.getCalendar())) {
                    dchEndTime.setCalendar((Calendar) dchStartTime.getCalendar().clone());
                }
                dchEndTime.setMinSelectableDate((Date) dchStartTime.getCalendar().getTime().clone());
                fireEditing();
            }
        });
        cbxEventType.addItemListener(itemListener);
        chbRepeatEvent.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                fireEditing();
                repeatingEventPanel.setEnabled(chbRepeatEvent.isSelected());
            }
        });
        repeatingEventPanel.addChangeListener(changeListener);
        attendeeComponent.addItemComponentListener(itemComponentListener);
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("event");
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Event)) {
            return;
        }
        Event event = (Event) object;
        Calendar startTime = dchStartTime.getCalendar() == null ? null : (Calendar) dchStartTime.getCalendar().clone();
        Calendar endTime = dchEndTime.getCalendar() == null ? null : (Calendar) dchEndTime.getCalendar().clone();

        event.setColor(pnlColor.getBackground());
        event.setEventName(tfdEventName.getText());
        event.setPlace(tfdPlace.getText());
        if (startTime != null) {
            event.setStartTime(startTime);
        }
        if (endTime != null) {
            event.setEndTime(endTime);
        }
        event.setEventType((EventType) cbxEventType.getSelectedItem());
        if (event.getAttendees() == null) {
            event.setAttendees(new LinkedList<Attendee>());
        }
        event.getAttendees().clear();
        event.getAttendees().addAll((Collection<? extends Attendee>) attendeeComponent.getItems());
        if (chbRepeatEvent.isSelected()) {
            repeatingEventPanel.setupObject(object);
        } else {
            event.setRepeatMask(Event.REPEAT_NONE);
        }
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Event)) {
            return;
        }
        event = (Event) object;
        initialized = false;
    }
}
