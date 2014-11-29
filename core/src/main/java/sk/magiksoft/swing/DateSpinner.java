package sk.magiksoft.swing;

import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.swing.calendar.DayChangedEvent;
import sk.magiksoft.swing.calendar.DayChangedListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wladimiiir
 */
public class DateSpinner extends JPanel {

    private JSpinner spnDate;
    private JButton btnChooseDate;
    private JDialog dlgCalendar;
    private CalendarPanel calendarPanel;
    private String pattern;

    public DateSpinner() {
        this(null);
    }

    public DateSpinner(String pattern) {
        this.pattern = pattern;
        initComponent();
    }

    private void initComponent() {
        GridBagConstraints c = new GridBagConstraints();

        spnDate = new JSpinner(new SpinnerDateModel()) {

            @Override
            public Object getNextValue() {
                return DateSpinner.this.getNextValue();
            }

            @Override
            public Object getPreviousValue() {
                return DateSpinner.this.getPreviousValue();
            }
        };
        spnDate.setEditor(pattern == null ? new JSpinner.DateEditor(spnDate) : new JSpinner.DateEditor(spnDate, pattern));
        ((JSpinner.DateEditor) spnDate.getEditor()).getTextField().setHorizontalAlignment(JFormattedTextField.RIGHT);
        btnChooseDate = new JButton(IconFactory.getInstance().getIcon("calendar"));
        btnChooseDate.setMargin(new Insets(0, 0, 0, 0));
        btnChooseDate.setFocusPainted(false);
        btnChooseDate.setOpaque(false);
        setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        add(spnDate, c);
        c.gridx++;
        c.weightx = 0.0;
        add(btnChooseDate, c);
        btnChooseDate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                chooseDate();
            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendarPanel = new CalendarPanel(calendar);
        calendarPanel.setOpaque(false);
        calendarPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        calendarPanel.addDayChangedListener(new DayChangedListener() {

            @Override
            public void dayChanged(DayChangedEvent e) {
                Calendar newDay = Calendar.getInstance();

                newDay.setTime((Date) spnDate.getValue());
                newDay.set(Calendar.YEAR, e.getNewDay().get(Calendar.YEAR));
                newDay.set(Calendar.MONTH, e.getNewDay().get(Calendar.MONTH));
                newDay.set(Calendar.DATE, e.getNewDay().get(Calendar.DATE));
                spnDate.setValue(newDay.getTime());
                dlgCalendar.setVisible(false);
            }
        });
        dlgCalendar = new JDialog();
        dlgCalendar.getContentPane().setLayout(new BorderLayout());
        dlgCalendar.getContentPane().add(calendarPanel, BorderLayout.CENTER);
        dlgCalendar.setUndecorated(true);
        dlgCalendar.setAlwaysOnTop(true);
        dlgCalendar.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        dlgCalendar.setSize(200, 150);
        dlgCalendar.setResizable(false);
        dlgCalendar.setBackground(Color.WHITE);
        dlgCalendar.addWindowFocusListener(new WindowAdapter() {

            @Override
            public void windowLostFocus(WindowEvent e) {
                dlgCalendar.setVisible(false);
            }
        });
        calendarPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE_DIALOG");
        calendarPanel.getActionMap().put("ESCAPE_DIALOG", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dlgCalendar.setVisible(false);
            }
        });
    }

    private void chooseDate() {
        dlgCalendar.setLocationRelativeTo(btnChooseDate);
        dlgCalendar.setVisible(true);
    }

    public void setValue(Calendar date) {
        Calendar c = (Calendar) date.clone();

        calendarPanel.setSelectedDay(c);
        spnDate.setValue(date.getTime());
    }

    public Calendar getValue() {
        Calendar c = Calendar.getInstance();

        c.setTime((Date) spnDate.getValue());

        return c;
    }

    public Date getNextValue() {
        final SpinnerDateModel model = (SpinnerDateModel) spnDate.getModel();
        Calendar cal = Calendar.getInstance();
        cal.setTime(model.getDate());
        cal.add(model.getCalendarField(), 1);
        Date next = cal.getTime();
        return ((model.getEnd() == null) || (model.getEnd().compareTo(next) >= 0)) ? next : null;
    }

    public Date getPreviousValue() {
        final SpinnerDateModel model = (SpinnerDateModel) spnDate.getModel();
        Calendar cal = Calendar.getInstance();
        cal.setTime(model.getDate());
        cal.add(model.getCalendarField(), -1);
        Date prev = cal.getTime();
        return ((model.getStart() == null) || (model.getStart().compareTo(prev) <= 0)) ? prev : null;
    }

    public void addChangeListener(ChangeListener listener) {
        spnDate.addChangeListener(listener);
    }

    public void setStart(Comparable date) {
        ((SpinnerDateModel) spnDate.getModel()).setStart(date);
    }

    public void setEnd(Comparable date) {
        ((SpinnerDateModel) spnDate.getModel()).setEnd(date);
    }

    public void setEditorHorizontalAlignment(int align) {
        ((JSpinner.DateEditor) spnDate.getEditor()).getTextField().setHorizontalAlignment(align);
    }

    @Override
    public void setEnabled(boolean enabled) {
        spnDate.setEnabled(enabled);
        btnChooseDate.setEnabled(enabled);
    }
}
