
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.swing;

import sk.magiksoft.swing.calendar.DayChangedEvent;
import sk.magiksoft.swing.calendar.DayChangedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author wladimiiir
 */
public class CalendarPanel extends JPanel {

    private JButton[] dayInfoButtons = new JButton[7];
    private DayButton[] dayButtons = new DayButton[42];
    private JButton[] weekNumButtons = new JButton[7];
    private Color dayBackground = new Color(250, 249, 234);
    private Color dayForeground = Color.BLACK;
    private Color sundayAndHolidayForeground = new Color(255, 100, 56);
    private Color selectedDayColorMask = new Color(165, 159, 243);
    private Calendar currentDay;
    private Calendar referenceCalendar = Calendar.getInstance();
    private Calendar selected = null;
    private Map<Calendar, String> tooltipTextMap = new HashMap<Calendar, String>();
    private Map<Calendar, Color> dayColorMap = new HashMap<Calendar, Color>();
    private Vector<DayChangedListener> dayChangedListeners = new Vector<DayChangedListener>();
    private JLabel currentMonthLabel = new JLabel();
    private JTextField currentYearTextField = new CheckedTextField("\\d*");

    public CalendarPanel(Calendar currentDay) {
        this.currentDay = currentDay;
        initCalendarPanel();
    }

    public void setSelectedDay(Calendar selectedDay) {
        if(selected!=null
                && selected.get(Calendar.YEAR)==selectedDay.get(Calendar.YEAR)
                && selected.get(Calendar.MONTH) == selectedDay.get(Calendar.MONTH)
                && selected.get(Calendar.DATE) == selectedDay.get(Calendar.DATE)){
            return;
        }
        currentDay = (Calendar) selectedDay.clone();
        refreshCalendar();
        fireSelectedDayChanged(new DayChangedEvent(this, selected, selectedDay));
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setOpaque(false);

        JPanel monthChooser = new JPanel(new BorderLayout());
        monthChooser.setOpaque(false);

        JPopupMenu monthPopupMenu = new JPopupMenu();

        int maxWidth = 0;
        for (int i = 0; i < DateFormatSymbols.getInstance().getMonths().length; i++) {
            String month = DateFormatSymbols.getInstance().getMonths()[i];
            int monthWidth = currentMonthLabel.getFontMetrics(currentMonthLabel.getFont()).stringWidth(month);
            if (maxWidth < monthWidth) {
                maxWidth = monthWidth;
            }

            if (month.trim().isEmpty()) {
                continue;
            }

            JMenuItem item = new JMenuItem(month);
            final int monthNum = i;
            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    currentDay.set(Calendar.MONTH, monthNum);
                    currentMonthLabel.setText(DateFormatSymbols.getInstance().getMonths()[currentDay.get(Calendar.MONTH)]);
                    currentYearTextField.setText(String.valueOf(currentDay.get(Calendar.YEAR)));
                    setupDays();
                    setupWeeks();
                }
            });
            monthPopupMenu.add(item);
        }

        currentMonthLabel.setPreferredSize(new Dimension(maxWidth + 10, currentMonthLabel.getHeight()));
        currentMonthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentMonthLabel.setText(DateFormatSymbols.getInstance().getMonths()[currentDay.get(Calendar.MONTH)]);
        final JPopupMenu pum = monthPopupMenu;
        currentMonthLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                pum.show(currentMonthLabel, 0, currentMonthLabel.getHeight());
            }
        });
        JButton previousMonth = new JButton("<");
        previousMonth.setOpaque(false);
        previousMonth.setBackground(Color.white);
        previousMonth.setMargin(new Insets(0, 0, 0, 0));
        previousMonth.setFocusPainted(false);
        previousMonth.setFont(Font.decode("Dialog Plain 11"));
        previousMonth.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentDay.add(Calendar.MONTH, -1);
                currentMonthLabel.setText(DateFormatSymbols.getInstance().getMonths()[currentDay.get(Calendar.MONTH)]);
                currentYearTextField.setText(String.valueOf(currentDay.get(Calendar.YEAR)));
                setupDays();
                setupWeeks();
            }
        });

        JButton nextMonth = new JButton(">");
        nextMonth.setOpaque(false);
        nextMonth.setBackground(Color.white);
        nextMonth.setMargin(new Insets(0, 0, 0, 0));
        nextMonth.setFocusPainted(false);
        nextMonth.setFont(Font.decode("Dialog Plain 11"));
        nextMonth.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentDay.add(Calendar.MONTH, 1);
                currentMonthLabel.setText(DateFormatSymbols.getInstance().getMonths()[currentDay.get(Calendar.MONTH)]);
                currentYearTextField.setText(String.valueOf(currentDay.get(Calendar.YEAR)));
                setupDays();
                setupWeeks();
            }
        });
        monthChooser.add(previousMonth, BorderLayout.WEST);
        monthChooser.add(currentMonthLabel, BorderLayout.CENTER);
        monthChooser.add(nextMonth, BorderLayout.EAST);

        JPanel yearChooser = new JPanel(new BorderLayout());
        yearChooser.setOpaque(false);

        maxWidth = currentYearTextField.getFontMetrics(currentMonthLabel.getFont()).stringWidth(String.valueOf(currentDay.get(Calendar.YEAR)));
        currentYearTextField.setPreferredSize(new Dimension(maxWidth + 10, currentMonthLabel.getHeight()));
        currentYearTextField.setHorizontalAlignment(SwingConstants.CENTER);
        currentYearTextField.setText(String.valueOf(currentDay.get(Calendar.YEAR)));
        JButton previousYear = new JButton("<");
        previousYear.setOpaque(false);
        previousYear.setBackground(Color.white);
        previousYear.setMargin(new Insets(0, 0, 0, 0));
        previousYear.setFocusPainted(false);
        previousYear.setFont(Font.decode("Dialog Plain 11"));
        previousYear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                currentDay.add(Calendar.YEAR, -1);
                currentYearTextField.setText(String.valueOf(currentDay.get(Calendar.YEAR)));
                setupDays();
                setupWeeks();
            }
        });

        JButton nextYear = new JButton(">");
        nextYear.setOpaque(false);
        nextYear.setBackground(Color.white);
        nextYear.setMargin(new Insets(0, 0, 0, 0));
        nextYear.setFocusPainted(false);
        nextYear.setFont(Font.decode("Dialog Plain 11"));
        nextYear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                currentDay.add(Calendar.YEAR, 1);
                currentYearTextField.setText(String.valueOf(currentDay.get(Calendar.YEAR)));
                setupDays();
                setupWeeks();
            }
        });

        currentYearTextField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                    return;
                }
                try {
                    currentDay.set(Calendar.YEAR, Integer.valueOf(currentYearTextField.getText()));
                    setupDays();
                    setupWeeks();
                } catch (NumberFormatException ex) {
                    currentYearTextField.setText(String.valueOf(currentDay.get(Calendar.YEAR)));
                }
            }
        });

        yearChooser.add(previousYear, BorderLayout.WEST);
        yearChooser.add(currentYearTextField, BorderLayout.CENTER);
        yearChooser.add(nextYear, BorderLayout.EAST);

        controlPanel.add(monthChooser, BorderLayout.WEST);
        controlPanel.add(yearChooser, BorderLayout.EAST);

        return controlPanel;
    }

    private JPanel createWeekPanel() {
        JPanel weekPanel = new JPanel(new GridLayout(7, 1));
        weekPanel.setOpaque(false);
        for (int i = 0; i < weekNumButtons.length; i++) {
            weekNumButtons[i] = new JButton() {

                @Override
                public boolean isFocusable() {
                    return false;
                }
            };
            weekNumButtons[i].setBackground(Color.lightGray);
            weekNumButtons[i].setFocusPainted(false);
            weekNumButtons[i].setContentAreaFilled(false);
            weekNumButtons[i].setBorderPainted(false);
            weekNumButtons[i].setMargin(new Insets(0, 0, 0, 0));
            weekNumButtons[i].setFont(Font.decode("Dialog Plain 11"));
            weekPanel.add(weekNumButtons[i]);
        }
        weekPanel.setPreferredSize(new Dimension(20, 0));
        return weekPanel;
    }

    private JPanel createDayPanel() {
        JPanel dayPanel = new JPanel(new GridLayout(7, 7));
        dayPanel.setOpaque(false);

        String[] dayNames = new DateFormatSymbols().getShortWeekdays();
        int dayOfWeek = currentDay.getFirstDayOfWeek();

        for (int i = 0; i < 49; i++) {
            if (i < 7) {
                dayInfoButtons[i] = new JButton(dayNames[dayOfWeek]) {

                    @Override
                    public boolean isFocusable() {
                        return false;
                    }
                };
                dayInfoButtons[i].setBackground(Color.lightGray);
                dayInfoButtons[i].setFocusPainted(false);
                dayInfoButtons[i].setContentAreaFilled(false);
                dayInfoButtons[i].setBorderPainted(false);
                dayInfoButtons[i].setMargin(new Insets(0, 0, 0, 0));
                dayInfoButtons[i].setFont(Font.decode("Dialog Plain 11"));
                dayPanel.add(dayInfoButtons[i]);
                if (++dayOfWeek > 7) {
                    dayOfWeek -= 7;
                }
            } else {
                dayButtons[i - 7] = new DayButton();
                dayButtons[i - 7].setMargin(new Insets(0, 0, 0, 0));
                dayPanel.add(dayButtons[i - 7]);
            }
        }

        return dayPanel;
    }

    private void initCalendarPanel() {
        this.setLayout(new BorderLayout());
        this.add(createDayPanel(), BorderLayout.CENTER);
        this.add(createWeekPanel(), BorderLayout.WEST);
        this.add(createControlPanel(), BorderLayout.NORTH);

        setupDays();
        setupWeeks();
    }

    private void refreshCalendar() {
        currentMonthLabel.setText(DateFormatSymbols.getInstance().getMonths()[currentDay.get(Calendar.MONTH)]);
        currentYearTextField.setText(String.valueOf(currentDay.get(Calendar.YEAR)));
        setupDays();
        setupWeeks();
    }

    private void setupDays() {
        Calendar thisMonth = (Calendar) currentDay.clone();
        thisMonth.set(currentDay.get(Calendar.YEAR), currentDay.get(Calendar.MONTH), 1, 0, 0, 0);
        Calendar nextMonth = (Calendar) thisMonth.clone();
        nextMonth.add(Calendar.MONTH, 1);
        Calendar today = Calendar.getInstance();

        int firstDayOfWeek = currentDay.getFirstDayOfWeek();
        boolean started = false;

        for (int i = 0; i < dayButtons.length; i++) {
            DayButton dayButton = dayButtons[i];
            if (!started) {
                int day = (firstDayOfWeek + i) > 7 ? firstDayOfWeek + i - 7 : firstDayOfWeek + i;
                if (thisMonth.get(Calendar.DAY_OF_WEEK) == day) {
                    started = true;
                } else {
                    dayButton.setDay(null);
                    dayButton.setVisible(false);
                    continue;
                }
            }

            if (!thisMonth.before(nextMonth)) {
                dayButton.setDay(null);
                dayButton.setVisible(false);
            } else {
                dayButton.setVisible(true);
                dayButton.setDay((Calendar) thisMonth.clone());
                if (thisMonth.get(Calendar.DATE) == today.get(Calendar.DATE) && thisMonth.get(Calendar.MONTH) == today.get(Calendar.MONTH) && thisMonth.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
                    dayButton.setFont(Font.decode("Dialog Bold 12"));
                    if (selected == null) {
                        referenceCalendar.set(dayButton.getDay().get(Calendar.YEAR), dayButton.getDay().get(Calendar.MONTH), dayButton.getDay().get(Calendar.DATE));
                        selected = (Calendar) referenceCalendar.clone();
                    }
                } else {
                    dayButton.setFont(Font.decode("Dialog Plain 11"));
                }
                if (thisMonth.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    dayButton.setForeground(sundayAndHolidayForeground);
                } else {
                    dayButton.setForeground(dayForeground);
                }
            }
            thisMonth.add(Calendar.DATE, 1);
        }

        refreshTooltipTexts();
        refreshDaysColors();
    }

    private void setupWeeks() {
        Calendar thisMonth = (Calendar) currentDay.clone();
        thisMonth.set(currentDay.get(Calendar.YEAR), currentDay.get(Calendar.MONTH), 1, 0, 0, 0);

        for (int i = 0; i < weekNumButtons.length; i++) {
            if (i == 0) {
                continue;
            }
            JButton weekNumButton = weekNumButtons[i];
            if (!dayButtons[(i - 1) * 7].isVisible() && !dayButtons[i * 7 - 1].isVisible()) {
                weekNumButton.setText("");
            } else {
                weekNumButton.setText(String.valueOf(thisMonth.get(Calendar.WEEK_OF_YEAR)));
            }
            thisMonth.add(Calendar.DATE, 7);
        }

    }

    private void refreshTooltipTexts() {
        for (int i = 0; i < dayButtons.length; i++) {
            DayButton dayButton = dayButtons[i];
            if (dayButton.getDay() == null) {
                continue;
            }
            boolean found = false;

            for (Map.Entry<Calendar, String> entry : tooltipTextMap.entrySet()) {
                if (entry.getKey().get(Calendar.DATE) == dayButton.getDay().get(Calendar.DATE) && entry.getKey().get(Calendar.MONTH) == dayButton.getDay().get(Calendar.MONTH) && entry.getKey().get(Calendar.YEAR) == dayButton.getDay().get(Calendar.YEAR)) {
                    found = true;
                    dayButton.setToolTipText(entry.getValue());
                    break;
                }
            }

            if (!found) {
                dayButton.setToolTipText(null);
            }
        }
    }

    private void refreshDaysColors() {
        for (int i = 0; i < dayButtons.length; i++) {
            DayButton dayButton = dayButtons[i];
            if (dayButton.getDay() == null) {
                continue;
            }
            boolean found = false;

            for (Map.Entry<Calendar, Color> entry : dayColorMap.entrySet()) {
                if (entry.getKey().get(Calendar.DATE) == dayButton.getDay().get(Calendar.DATE) && entry.getKey().get(Calendar.MONTH) == dayButton.getDay().get(Calendar.MONTH) && entry.getKey().get(Calendar.YEAR) == dayButton.getDay().get(Calendar.YEAR)) {
                    found = true;
                    if (selected.get(Calendar.DATE) == dayButton.getDay().get(Calendar.DATE) && selected.get(Calendar.MONTH) == dayButton.getDay().get(Calendar.MONTH) && selected.get(Calendar.YEAR) == dayButton.getDay().get(Calendar.YEAR)) {
                        dayButton.setBackground(new Color(entry.getValue().getRGB() & selectedDayColorMask.getRGB()));
                    } else {
                        dayButton.setBackground(entry.getValue());
                    }
                    break;
                }
            }

            if (!found) {
                if (selected.get(Calendar.DATE) == dayButton.getDay().get(Calendar.DATE) && selected.get(Calendar.MONTH) == dayButton.getDay().get(Calendar.MONTH) && selected.get(Calendar.YEAR) == dayButton.getDay().get(Calendar.YEAR)) {
                    dayButton.setBackground(new Color(dayBackground.getRGB() & selectedDayColorMask.getRGB()));
                } else {
                    dayButton.setBackground(dayBackground);
                }
            }
        }
    }

    public void setColorForDay(Calendar day, Color color) {
        referenceCalendar.set(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DATE));
        day = ((Calendar) referenceCalendar.clone());
        if (color == null) {
            if (dayColorMap.remove(day) != null) {
                for (int i = 0; i < dayButtons.length; i++) {
                    DayButton dayButton = dayButtons[i];
                    if (dayButton.getDay() != null && dayButton.getDay().get(Calendar.DATE) == day.get(Calendar.DATE) && dayButton.getDay().get(Calendar.MONTH) == day.get(Calendar.MONTH) && dayButton.getDay().get(Calendar.YEAR) == day.get(Calendar.YEAR)) {
                        dayButton.setBackground(dayBackground);
                        break;
                    }
                }
            }
        } else {
            dayColorMap.put(day, color);
            for (int i = 0; i < dayButtons.length; i++) {
                DayButton dayButton = dayButtons[i];
                if (dayButton.getDay() != null && dayButton.getDay().get(Calendar.DATE) == day.get(Calendar.DATE) && dayButton.getDay().get(Calendar.MONTH) == day.get(Calendar.MONTH) && dayButton.getDay().get(Calendar.YEAR) == day.get(Calendar.YEAR)) {
                    dayButton.setBackground((selected.get(Calendar.DATE) == dayButton.getDay().get(Calendar.DATE) && selected.get(Calendar.MONTH) == dayButton.getDay().get(Calendar.MONTH) && selected.get(Calendar.YEAR) == dayButton.getDay().get(Calendar.YEAR)) ? new Color(color.getRGB() & selectedDayColorMask.getRGB()) : color);
                    break;
                }
            }
        }
    }

    public void addTooltipTextForDay(Calendar day, String tooltipText) {
        referenceCalendar.set(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DATE));
        day = ((Calendar) referenceCalendar.clone());
        tooltipTextMap.put(day, tooltipText);

        for (int i = 0; i < dayButtons.length; i++) {
            DayButton dayButton = dayButtons[i];
            if (dayButton.getDay() != null && dayButton.getDay().get(Calendar.DATE) == day.get(Calendar.DATE) && dayButton.getDay().get(Calendar.MONTH) == day.get(Calendar.MONTH) && dayButton.getDay().get(Calendar.YEAR) == day.get(Calendar.YEAR)) {
                dayButton.setToolTipText(tooltipText);
                break;
            }
        }

    }

    public String removeTooltipTextForDay(Calendar day) {
        referenceCalendar.set(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DATE));
        day = ((Calendar) referenceCalendar.clone());
        String result = tooltipTextMap.remove(day);

        if (result != null) {
            for (int i = 0; i < dayButtons.length; i++) {
                DayButton dayButton = dayButtons[i];
                if (dayButton.getDay() != null && dayButton.getDay().get(Calendar.DATE) == day.get(Calendar.DATE) && dayButton.getDay().get(Calendar.MONTH) == day.get(Calendar.MONTH) && dayButton.getDay().get(Calendar.YEAR) == day.get(Calendar.YEAR)) {
                    dayButton.setToolTipText(null);
                    break;
                }
            }
        }

        return result;
    }

    public void addDayChangedListener(DayChangedListener listener) {
        dayChangedListeners.add(listener);
    }

    public boolean removeDayChangedListener(DayChangedListener listener) {
        return dayChangedListeners.remove(listener);
    }

    public Vector<DayChangedListener> getDayChangedListeners() {
        return dayChangedListeners;
    }

    private void fireSelectedDayChanged(DayChangedEvent e) {
        referenceCalendar.set(e.getNewDay().get(Calendar.YEAR), e.getNewDay().get(Calendar.MONTH), e.getNewDay().get(Calendar.DATE));
        selected = (Calendar) referenceCalendar.clone();
        refreshDaysColors();

        for (DayChangedListener dayChangedListener : dayChangedListeners) {
            dayChangedListener.dayChanged(e);
        }
    }

    private class DayButton extends JButton {

        private Calendar day;

        public DayButton() {
            initListener();
        }

        private void initListener() {
            addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    fireSelectedDayChanged(new DayChangedEvent(DayButton.this, selected, day));
                }
            });
        }

        public Calendar getDay() {
            return day;
        }

        public void setDay(Calendar day) {
            this.day = day;
            if (day != null) {
                this.setText(String.valueOf(day.get(Calendar.DATE)));
            } else {
                this.setText("");
                this.setToolTipText(null);
            }

        }

        @Override
        public boolean isFocusPainted() {
            return false;
        }
    }
    static CalendarPanel calPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        calPanel = new CalendarPanel(cal);
        calPanel.setColorForDay((Calendar) cal.clone(), Color.red);
        frame.add(calPanel, BorderLayout.CENTER);

        calPanel.addDayChangedListener(new DayChangedListener() {

            @Override
            public void dayChanged(DayChangedEvent e) {
                if (e.getNewDay().after(Calendar.getInstance())) {
                    calPanel.setColorForDay(Calendar.getInstance(), null);
                }
            }
        });
        cal = (Calendar) cal.clone();
        cal.set(2007, 2, 2);
        calPanel.setSelectedDay(cal);

        frame.setVisible(true);
    }
}