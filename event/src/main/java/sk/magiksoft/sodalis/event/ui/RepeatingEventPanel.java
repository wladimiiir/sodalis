package sk.magiksoft.sodalis.event.ui;

import com.toedter.calendar.JDateChooser;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.event.entity.Event;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author wladimiiir
 */
public class RepeatingEventPanel extends JPanel {

    private DateFormatSymbols symbols = new DateFormatSymbols();
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    private JDateChooser dchStartTime = new JDateChooser("dd.MM.yyyy", "##.##.####", '-');
    private JDateChooser dchEndTime = new JDateChooser("dd.MM.yyyy", "##.##.####", '-');
    private JRadioButton rbtEveryDay = new JRadioButton(LocaleManager.getString("everyDay"));
    private JRadioButton rbtEveryWeek = new JRadioButton(LocaleManager.getString("everyWeek"));
    private JRadioButton rbtEveryMonth = new JRadioButton(LocaleManager.getString("everyMonth"));
    private JRadioButton rbtChoosenDays = new JRadioButton(LocaleManager.getString("choosenDays"));
    private JToggleButton tbnMonday = new JToggleButton(symbols.getShortWeekdays()[2]);
    private JToggleButton tbnTuesday = new JToggleButton(symbols.getShortWeekdays()[3]);
    private JToggleButton tbnWednesday = new JToggleButton(symbols.getShortWeekdays()[4]);
    private JToggleButton tbnThursday = new JToggleButton(symbols.getShortWeekdays()[5]);
    private JToggleButton tbnFriday = new JToggleButton(symbols.getShortWeekdays()[6]);
    private JToggleButton tbnSaturday = new JToggleButton(symbols.getShortWeekdays()[7]);
    private JToggleButton tbnSunday = new JToggleButton(symbols.getShortWeekdays()[1]);

    public RepeatingEventPanel() {
        initComponents();
    }

    private void initComponents() {
        final JPanel pnlWeekDays = new JPanel(new GridLayout(1, 7));
        GridBagConstraints c = new GridBagConstraints();
        ButtonGroup btnGroup = new ButtonGroup();

        btnGroup.add(rbtEveryDay);
        btnGroup.add(rbtEveryWeek);
        btnGroup.add(rbtEveryMonth);
        btnGroup.add(rbtChoosenDays);
        tbnMonday.setFont(Font.decode("Dialog Plain 9"));
        tbnTuesday.setFont(Font.decode("Dialog Plain 9"));
        tbnWednesday.setFont(Font.decode("Dialog Plain 9"));
        tbnThursday.setFont(Font.decode("Dialog Plain 9"));
        tbnFriday.setFont(Font.decode("Dialog Plain 9"));
        tbnSaturday.setFont(Font.decode("Dialog Plain 9"));
        tbnSunday.setFont(Font.decode("Dialog Plain 9"));
        tbnMonday.setFocusPainted(false);
        tbnTuesday.setFocusPainted(false);
        tbnWednesday.setFocusPainted(false);
        tbnThursday.setFocusPainted(false);
        tbnFriday.setFocusPainted(false);
        tbnSaturday.setFocusPainted(false);
        tbnSunday.setFocusPainted(false);
        pnlWeekDays.add(tbnMonday);
        pnlWeekDays.add(tbnTuesday);
        pnlWeekDays.add(tbnWednesday);
        pnlWeekDays.add(tbnThursday);
        pnlWeekDays.add(tbnFriday);
        pnlWeekDays.add(tbnSaturday);
        pnlWeekDays.add(tbnSunday);

        setLayout(new GridBagLayout());
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 0, 0, 0);
        add(new JLabel(LocaleManager.getString("from")), c);
        c.insets = new Insets(3, 5, 0, 0);
        c.gridx++;
        c.weightx = 1.0;
        add(dchStartTime, c);
        c.gridx = 0;
        c.gridy++;
        c.weightx = 0.0;
        c.insets = new Insets(2, 0, 0, 0);
        add(new JLabel(LocaleManager.getString("to")), c);
        c.insets = new Insets(2, 5, 0, 0);
        c.gridx++;
        c.weightx = 1.0;
        add(dchEndTime, c);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        c.insets = new Insets(3, 0, 0, 0);
        add(rbtEveryDay, c);
        c.insets = new Insets(1, 0, 0, 0);
        c.gridy++;
        add(rbtEveryWeek, c);
        c.gridy++;
        add(rbtEveryMonth, c);
        c.gridy++;
        add(rbtChoosenDays, c);
        c.gridy++;
        add(pnlWeekDays, c);
        rbtEveryDay.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                fireEditing();
            }
        });
        rbtEveryWeek.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                fireEditing();
            }
        });
        rbtEveryMonth.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                fireEditing();
            }
        });
        rbtChoosenDays.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                tbnMonday.setEnabled(rbtChoosenDays.isSelected());
                tbnTuesday.setEnabled(rbtChoosenDays.isSelected());
                tbnWednesday.setEnabled(rbtChoosenDays.isSelected());
                tbnThursday.setEnabled(rbtChoosenDays.isSelected());
                tbnFriday.setEnabled(rbtChoosenDays.isSelected());
                tbnSaturday.setEnabled(rbtChoosenDays.isSelected());
                tbnSunday.setEnabled(rbtChoosenDays.isSelected());
                fireEditing();
            }
        });
        dchStartTime.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                dchEndTime.setMinSelectableDate(dchStartTime.getDate());
                if (dchEndTime.getCalendar() == null || !dchEndTime.getCalendar().after(dchStartTime.getCalendar())) {
                    dchEndTime.setCalendar(dchStartTime.getCalendar());
                }
                fireEditing();
            }
        });
        dchEndTime.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fireEditing();
            }
        });
        tbnMonday.setEnabled(rbtChoosenDays.isSelected());
        tbnTuesday.setEnabled(rbtChoosenDays.isSelected());
        tbnWednesday.setEnabled(rbtChoosenDays.isSelected());
        tbnThursday.setEnabled(rbtChoosenDays.isSelected());
        tbnFriday.setEnabled(rbtChoosenDays.isSelected());
        tbnSaturday.setEnabled(rbtChoosenDays.isSelected());
        tbnSunday.setEnabled(rbtChoosenDays.isSelected());
        rbtEveryDay.setSelected(true);
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fireEditing();
            }
        };
        tbnMonday.addActionListener(actionListener);
        tbnTuesday.addActionListener(actionListener);
        tbnWednesday.addActionListener(actionListener);
        tbnThursday.addActionListener(actionListener);
        tbnFriday.addActionListener(actionListener);
        tbnSaturday.addActionListener(actionListener);
        tbnSunday.addActionListener(actionListener);
    }

    protected void fireEditing() {
        ChangeEvent changeEvent = new ChangeEvent(this);
        for (ChangeListener changeListener : changeListeners) {
            changeListener.stateChanged(changeEvent);
        }
    }

    public void addChangeListener(ChangeListener listener) {
        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }

    private void setEnabled(Component component, boolean enabled) {
        if (component instanceof Container) {
            for (int i = 0; i < ((Container) component).getComponentCount(); i++) {
                Component c = ((Container) component).getComponent(i);
                setEnabled(c, enabled);
            }
        }
        if (component != this) {
            component.setEnabled(enabled);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        setEnabled(this, enabled);
        tbnMonday.setEnabled(rbtChoosenDays.isSelected());
        tbnTuesday.setEnabled(rbtChoosenDays.isSelected());
        tbnWednesday.setEnabled(rbtChoosenDays.isSelected());
        tbnThursday.setEnabled(rbtChoosenDays.isSelected());
        tbnFriday.setEnabled(rbtChoosenDays.isSelected());
        tbnSaturday.setEnabled(rbtChoosenDays.isSelected());
        tbnSunday.setEnabled(rbtChoosenDays.isSelected());
    }

    public void setupObject(Object object) {
        if (!(object instanceof Event)) {
            return;
        }
        Calendar repeatStart = (Calendar) dchStartTime.getCalendar().clone();
        Calendar repeatEnd = (Calendar) dchEndTime.getCalendar().clone();
        Event event = (Event) object;

        repeatStart.setFirstDayOfWeek(Calendar.MONDAY);
        repeatStart.set(Calendar.HOUR_OF_DAY, 0);
        repeatStart.set(Calendar.MINUTE, 0);
        repeatStart.set(Calendar.SECOND, 0);
        repeatStart.set(Calendar.MILLISECOND, 0);
        repeatEnd.set(Calendar.HOUR_OF_DAY, 0);
        repeatEnd.set(Calendar.MINUTE, 0);
        repeatEnd.set(Calendar.SECOND, 0);
        repeatEnd.set(Calendar.MILLISECOND, 0);
        repeatEnd.add(Calendar.DATE, 1);
        repeatEnd.add(Calendar.MILLISECOND, -1);
        event.setRepeatStart(repeatStart);
        event.setRepeatEnd(repeatEnd);
        event.setRepeatMask(getRepeatMask());
    }

    public void setupPanel(Object object) {
        if (!(object instanceof Event)) {
            return;
        }
        Event event = (Event) object;

        dchStartTime.setCalendar(event.getRepeatStart());
        dchEndTime.setCalendar(event.getRepeatEnd());
        setRepatMask(event.getRepeatMask());
    }

    private void setRepatMask(int repeatMask) {
        if (repeatMask == Event.REPEAT_NONE) {
            return;
        }
        if (repeatMask == Event.REPEAT_EVERYDAY) {
            rbtEveryDay.setSelected(true);
            return;
        }
        if (repeatMask == Event.REPEAT_EVERYWEEK) {
            rbtEveryWeek.setSelected(true);
            return;
        }
        if (repeatMask == Event.REPEAT_EVERYMONTH) {
            rbtEveryMonth.setSelected(true);
            return;
        }
        rbtChoosenDays.setSelected(true);
        tbnMonday.setSelected((repeatMask & Event.REPEAT_MONDAY) == Event.REPEAT_MONDAY);
        tbnTuesday.setSelected((repeatMask & Event.REPEAT_TUESDAY) == Event.REPEAT_TUESDAY);
        tbnWednesday.setSelected((repeatMask & Event.REPEAT_WEDNESDAY) == Event.REPEAT_WEDNESDAY);
        tbnThursday.setSelected((repeatMask & Event.REPEAT_THURSDAY) == Event.REPEAT_THURSDAY);
        tbnFriday.setSelected((repeatMask & Event.REPEAT_FRIDAY) == Event.REPEAT_FRIDAY);
        tbnSaturday.setSelected((repeatMask & Event.REPEAT_SATURDAY) == Event.REPEAT_SATURDAY);
        tbnSunday.setSelected((repeatMask & Event.REPEAT_SUNDAY) == Event.REPEAT_SUNDAY);
    }

    private int getRepeatMask() {
        int repeatMask = 0;

        if (rbtEveryDay.isSelected()) {
            return Event.REPEAT_EVERYDAY;
        }
        if (rbtEveryWeek.isSelected()) {
            return Event.REPEAT_EVERYWEEK;
        }
        if (rbtEveryMonth.isSelected()) {
            return Event.REPEAT_EVERYMONTH;
        }
        if (rbtChoosenDays.isSelected()) {
            repeatMask += tbnMonday.isSelected() ? Event.REPEAT_MONDAY : 0;
            repeatMask += tbnTuesday.isSelected() ? Event.REPEAT_TUESDAY : 0;
            repeatMask += tbnWednesday.isSelected() ? Event.REPEAT_WEDNESDAY : 0;
            repeatMask += tbnThursday.isSelected() ? Event.REPEAT_THURSDAY : 0;
            repeatMask += tbnFriday.isSelected() ? Event.REPEAT_FRIDAY : 0;
            repeatMask += tbnSaturday.isSelected() ? Event.REPEAT_SATURDAY : 0;
            repeatMask += tbnSunday.isSelected() ? Event.REPEAT_SUNDAY : 0;
        }
        return repeatMask;
    }
}
