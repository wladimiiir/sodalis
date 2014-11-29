package sk.magiksoft.sodalis.event.ui;

import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.factory.EntityFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.event.data.EventDataManager;
import sk.magiksoft.sodalis.event.entity.Event;
import sk.magiksoft.sodalis.event.entity.EventType;
import sk.magiksoft.sodalis.event.settings.EventSettings;
import sk.magiksoft.sodalis.event.ui.event.EventListener;
import sk.magiksoft.sodalis.event.ui.event.TimePanelListener;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class TimePanel extends JPanel implements Pageable, Printable {

    private static final int TOP_INSET = 10;
    private static final int BOTTOM_INSET = 10;
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private Comparator eventPanelComparator = new Comparator() {

        @Override
        public int compare(Object o1, Object o2) {
            EventPanel ep1 = (EventPanel) o1;
            EventPanel ep2 = (EventPanel) o2;
            return ep2.getEvent().getStartTime().compareTo(ep1.getEvent().getStartTime());
        }
    };
    private Calendar startHour = Calendar.getInstance();
    private Calendar endHour = Calendar.getInstance();
    private Calendar currentDay = Calendar.getInstance();
    private List<EventPanel> eventPanels = new ArrayList<EventPanel>();
    private EventPopupMenu popupMenu = new EventPopupMenu();
    private Calendar mouseTime = null;
    private Point mousePoint = null;
    private ScaleControler scaleControler = new ScaleControler();
    private DayCountControler dayCountControler = new DayCountControler();
    private JPanel timeLinePanel = new TimeLinePanel();
    private JPanel timePanelHeader = new TimePanelHeader();
    private JScrollBar timePanelFooter = new TimePanelFooter();
    private TopLeftCorner topLeftCorner = new TopLeftCorner();
    private boolean printing = false;
    private boolean adjusted = false;

    public TimePanel() {
        super(true);
        startHour.set(Calendar.MINUTE, 0);
        startHour.set(Calendar.SECOND, 0);
        startHour.set(Calendar.MILLISECOND, 0);
        setStartEndHour(EventSettings.getInstance().getInt(EventSettings.I_START_HOUR),
                EventSettings.getInstance().getInt(EventSettings.I_END_HOUR));
        initListeners();
    }

    public JPanel getTimeLinePanel() {
        return timeLinePanel;
    }

    public JPanel getTopLeftCorner() {
        return topLeftCorner;
    }

    public boolean containsEvent(Event event) {
        for (EventPanel eventPanel : eventPanels) {
            if (event.getId().equals(eventPanel.getEvent().getId())) {
                return true;
            }
        }
        return false;
    }

    private void fireEventAdded(Event event) {
        final TimePanelListener[] listeners = listenerList.getListeners(TimePanelListener.class);
        for (TimePanelListener timePanelListener : listeners) {
            timePanelListener.eventAdded(event);
        }
    }

    private void fireEventUpdated(Event event) {
        final TimePanelListener[] listeners = listenerList.getListeners(TimePanelListener.class);
        for (TimePanelListener listener : listeners) {
            listener.eventUpdated(event);
        }
    }

    private void fireEventSelected(Event event) {
        final TimePanelListener[] listeners = listenerList.getListeners(TimePanelListener.class);
        for (TimePanelListener timePanelListener : listeners) {
            timePanelListener.eventSelected(event);
        }
    }

    private boolean fireEventWillBeSelected(Event event) {
        final TimePanelListener[] listeners = listenerList.getListeners(TimePanelListener.class);
        for (TimePanelListener timePanelListener : listeners) {
            if (!timePanelListener.eventWillBeSelected(event)) {
                return false;
            }
        }

        return true;
    }

    private void fireEventRemoved(Event event) {
        final TimePanelListener[] listeners = listenerList.getListeners(TimePanelListener.class);
        for (TimePanelListener timePanelListener : listeners) {
            timePanelListener.eventRemoved(event);
        }
    }

    private void fireEventDoubleClicked(Event event) {
        TimePanelListener[] listeners = listenerList.getListeners(TimePanelListener.class);
        for (TimePanelListener timePanelListener : listeners) {
            timePanelListener.eventDoubleClicked(event);
        }
    }

    private int getDayCount(Calendar eventStart, Calendar currentDay) {
        return (int) ((eventStart.getTimeInMillis() - currentDay.getTimeInMillis()) / 1000 / 60 / 60 / 24);
    }

    private void initListeners() {
        final DragAndDropControler dragAndDropControler = new DragAndDropControler();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() != 1) {
                    adjusted = false;
                    return;
                }
                if (SwingUtilities.isLeftMouseButton(e) && getSelectedEventPanel() == null) {
                    EventPanel selectedEventPanel = getEventPanelOnPoint(e.getPoint(), true);
                    if (fireEventWillBeSelected(selectedEventPanel == null ? null : selectedEventPanel.getEvent())) {
                        if (selectedEventPanel != null) {
                            selectedEventPanel.setSelected(true);
                        }
                        fireEventSelected(selectedEventPanel == null ? null : selectedEventPanel.getEvent());
                        repaint();
                    }
                    adjusted = true;
                } else {
                    adjusted = false;
                }

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (adjusted) {
                    return;
                }

                EventPanel selectedEventPanel = getEventPanelOnPoint(e.getPoint(), true);
                if (fireEventWillBeSelected(selectedEventPanel == null ? null : selectedEventPanel.getEvent())) {
                    for (EventPanel eventPanel : eventPanels) {
                        eventPanel.setSelected(eventPanel == selectedEventPanel);
                    }
                    fireEventSelected(selectedEventPanel == null ? null : selectedEventPanel.getEvent());
                    repaint();
                    if (SwingUtilities.isRightMouseButton(e)) {
                        popupMenu.setEventPanel(selectedEventPanel);
                        popupMenu.show(TimePanel.this, e.getX(), e.getY());
                    } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                        if (selectedEventPanel != null) {
                            fireEventDoubleClicked(selectedEventPanel.getEvent());
                        } else {
                            EventType eventType = EventDataManager.getInstance().getDatabaseEntity(EventType.class, "id=" + EventSettings.getInstance().getLong(EventSettings.L_DEFAULT_EVENT_TYPE_ID));

                            if (eventType != null) {
                                addEvent(eventType.createEvent(), e.getPoint());
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseTime = null;
                if (timeLinePanel != null) {
                    timeLinePanel.paintImmediately(0, 0,
                            timeLinePanel.getPreferredSize().width, timeLinePanel.getPreferredSize().height);
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseTime = pointToCalendar(e.getPoint(), false);
                mousePoint = e.getPoint();
                if (timeLinePanel != null) {
                    timeLinePanel.paintImmediately(0, 0,
                            timeLinePanel.getPreferredSize().width, timeLinePanel.getPreferredSize().height);
                }
            }
        });
        addMouseListener(dragAndDropControler);
        addMouseMotionListener(dragAndDropControler);
        EventListener eventListener = new EventListener() {

            @Override
            public void addEvent(Event event, Point point) {
                TimePanel.this.addEvent(event, point);
            }

            @Override
            public void removeEvent(Point point) {
                EventPanel eventPanel = getEventPanelOnPoint(point, false);
                if (eventPanel == null) {
                    return;
                }
                fireEventRemoved(eventPanel.getEvent());
            }

            @Override
            public void removeFromRepeating(Point point) {
                final EventPanel eventPanel = getEventPanelOnPoint(point, false);
                if (eventPanel == null) {
                    return;
                }
                final Calendar onPoint = pointToCalendar(point, false);
                final Event eventPanelEvent = eventPanel.getEvent();
                final Calendar date = (Calendar) eventPanelEvent.getStartTime().clone();
                date.set(Calendar.DATE, onPoint.get(Calendar.DATE));
                date.set(Calendar.MONTH, onPoint.get(Calendar.MONTH));
                date.set(Calendar.YEAR, onPoint.get(Calendar.YEAR));
                final Event event = EntityFactory.getInstance().createEntity(Event.class);
                final long diff = eventPanelEvent.getEndTime().getTimeInMillis() - eventPanelEvent.getStartTime().getTimeInMillis();
                event.updateFrom(eventPanelEvent);
                event.clearIDs();
                event.setRepeatMask(Event.REPEAT_NONE);
                event.setStartTime((Calendar) date.clone());
                event.getEndTime().setTimeInMillis(event.getStartTime().getTimeInMillis() + diff);
                fireEventAdded(event);
                eventPanelEvent.removeFromRepeating(date);
                fireEventUpdated(eventPanelEvent);
            }
        };

        popupMenu.addEventListener(eventListener);

        EventSettings.getInstance().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(EventSettings.I_START_HOUR)) {
                    setStartEndHour((Integer) evt.getNewValue(),
                            EventSettings.getInstance().getInt(EventSettings.I_END_HOUR));
                    repaint();

                } else if (evt.getPropertyName().equals(EventSettings.I_END_HOUR)) {
                    setStartEndHour(EventSettings.getInstance().getInt(EventSettings.I_START_HOUR),
                            (Integer) evt.getNewValue());
                    repaint();
                }
            }
        });
    }


    private void setStartEndHour(int startHour, int endHour) {
        if (startHour > endHour) {
            int temp = endHour;
            endHour = startHour;
            startHour = temp;
        }

        this.startHour.set(Calendar.HOUR_OF_DAY, startHour);
        this.endHour = (Calendar) this.startHour.clone();
        if (startHour == endHour) {
            endHour = startHour + 1;
        }
        this.endHour.set(Calendar.HOUR_OF_DAY, endHour);
    }

    private void addEvent(Event event, Point point) {
        Calendar eventStart = pointToCalendar(point, false);
        Calendar eventEnd = (Calendar) eventStart.clone();

        eventEnd.add(Calendar.MINUTE, EventSettings.getInstance().getInt(EventSettings.I_EVENT_DURATION));
        event.setColor((Color) EventSettings.getInstance().getValue(EventSettings.O_EVENT_COLOR));
        event.setStartTime(eventStart);
        event.setEndTime(eventEnd);
        fireEventAdded(event);
    }

    public void addTimePanelListener(TimePanelListener listener) {
        listenerList.add(TimePanelListener.class, listener);
    }

    public void removeTimePanelListener(TimePanelListener listener) {
        listenerList.remove(TimePanelListener.class, listener);
    }

    private double getHourStep() {
        return (getPreferredSize().height - TOP_INSET - BOTTOM_INSET) / ((endHour.getTimeInMillis() - startHour.getTimeInMillis()) / 1000 / 60 / 60);
    }

    private Calendar pointToCalendar(Point p, boolean snap) {
        Calendar calendar = (Calendar) currentDay.clone();
        double hour = (p.y - TOP_INSET) / getHourStep() + startHour.get(Calendar.HOUR_OF_DAY);
        int snapMinutes = EventSettings.getInstance().getInt(EventSettings.I_SNAP_MINUTES);
        int dayCount = EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT);

        calendar.set(Calendar.HOUR_OF_DAY, (int) Math.floor(hour));
        hour = hour - Math.floor(hour);
        calendar.set(Calendar.MINUTE, (int) (hour * 60));
        calendar.add(Calendar.DATE, p.x / (getSize().width / dayCount));
        if (snap) {
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - calendar.get(Calendar.MINUTE) % snapMinutes);
        }

        return calendar;
    }

    private EventPanel getEventPanelOnPoint(Point point, boolean checkBellow) {
        Calendar clickedTime = pointToCalendar(point, false);
        boolean foundSelected = !checkBellow;

        for (int i = eventPanels.size() - 1; i >= 0; i--) {
            EventPanel eventPanel = eventPanels.get(i);
            if (eventPanel.getEvent().acceptTime(clickedTime)) {
                if (foundSelected) {
                    return eventPanel;
                } else {
                    foundSelected = eventPanel.isSelected();
                }
            }
        }
        return checkBellow ? getEventPanelOnPoint(point, false) : null;
    }

    private EventPanel getSelectedEventPanel() {
        for (EventPanel eventPanel : eventPanels) {
            if (eventPanel.isSelected()) {
                return eventPanel;
            }
        }
        return null;
    }

    @Override
    public void paint(Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(new GradientPaint(-200, -200, ColorList.LIGHTER_BLUE,
                getWidth() + 400, getHeight() + 400,
                Color.BLUE.brighter()));
        g.fillRect(0, 0, getWidth(), getHeight());
        paintEventPanels(g);
    }

    private void paintEventPanels(Graphics g) {
        EventPanel selectedEventPanel;
        Calendar day = (Calendar) currentDay.clone();
        int dayCount = EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT);

        paintTimeLines(g);

        for (int i = 0; i < dayCount; i++) {
            selectedEventPanel = null;
            for (EventPanel eventPanel : eventPanels) {
                if (eventPanel.isSelected()) {
                    selectedEventPanel = eventPanel;
                    continue;
                }
                if (!eventPanel.getEvent().acceptDay(day)) {
                    continue;
                }
                paintEventPanel(g, eventPanel, day);
            }
            //drawing selected panel as the last one to be on the top of the others
            if (selectedEventPanel != null && selectedEventPanel.getEvent().acceptDay(day)) {
                paintEventPanel(g, selectedEventPanel, day);
            }
            day.add(Calendar.DATE, 1);
        }
    }

    private void paintTimeLines(Graphics g) {
        int dayCount = EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT);
        int height = getPreferredSize().height - TOP_INSET - BOTTOM_INSET;
        int currentLine = 0;
        Calendar currentTime = (Calendar) startHour.clone();

        currentTime.set(Calendar.MINUTE, 0);
        //drawing hour lines
        while (currentLine <= height) {
            g.setColor(getForeground());
            g.drawLine(0, currentLine + TOP_INSET, getWidth(), currentLine + TOP_INSET);
//            g.drawLine(0, currentLine + TOP_INSET + 1, getWidth(), currentLine + TOP_INSET + 1);
            if (currentLine + TOP_INSET + getHourStep() / 2 <= height) {
                g.setColor(Color.GRAY);
                g.drawLine(1, currentLine + TOP_INSET + (int) getHourStep() / 2, getWidth() - 2, currentLine + TOP_INSET + (int) getHourStep() / 2);
            }
            currentLine += getHourStep();
            currentTime.add(Calendar.HOUR, 1);
        }
        g.setColor(getForeground());
        for (int i = 1; i < dayCount; i++) {
            g.drawLine(i * getSize().width / dayCount, 0, i * getSize().width / dayCount, getPreferredSize().height);
        }
    }

    private void paintEventPanel(Graphics g, EventPanel eventPanel, Calendar day) {
        Calendar eventStart = eventPanel.getEvent().getStartTime();
        Calendar eventEnd = eventPanel.getEvent().getEndTime();
        Calendar startDate = (Calendar) startHour.clone();
        double hours;
        double startTime;
        double oldStartTime;
        int dayCount = EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT);

        eventStart.set(Calendar.SECOND, 0);
        eventStart.set(Calendar.MILLISECOND, 0);
        eventEnd.set(Calendar.SECOND, 0);
        eventEnd.set(Calendar.MILLISECOND, 0);
        hours = (eventEnd.getTimeInMillis() - eventStart.getTimeInMillis()) / 1000d / 60d / 60d;
        hours *= getHourStep();
        startDate.set(Calendar.DATE, day.get(Calendar.DATE));
        startDate.set(Calendar.MONTH, day.get(Calendar.MONTH));
        startDate.set(Calendar.YEAR, day.get(Calendar.YEAR));
        startTime = (eventStart.getTimeInMillis() - startDate.getTimeInMillis()) / 1000d / 60d / 60d;
        startTime = startTime * getHourStep() + TOP_INSET;
        boolean selected = eventPanel.isSelected();
        if (printing) {
            eventPanel.setSelected(false);
        }
        eventPanel.paint(g, getDayCount(day, currentDay) * getSize().width / dayCount + 1, (int) startTime, getSize().width / dayCount - 5, (int) hours - 3);
        if (printing && selected) {
            eventPanel.setSelected(true);
        }
        if (eventPanel.getEvent().isRepeating()) {
            startDate.set(Calendar.DATE, eventStart.get(Calendar.DATE));
            startDate.set(Calendar.MONTH, eventStart.get(Calendar.MONTH));
            startDate.set(Calendar.YEAR, eventStart.get(Calendar.YEAR));
            oldStartTime = startTime;
            startTime = (eventStart.getTimeInMillis() - startDate.getTimeInMillis()) / 1000d / 60d / 60d;
            startTime = startTime * getHourStep() + TOP_INSET;
            if (oldStartTime != startTime) {
                eventPanel.paint(g, getDayCount(day, currentDay) * getSize().width / dayCount + 1, (int) startTime, getSize().width / dayCount - 5, (int) hours - 3);
            }
        }
    }

    public void addEventPanel(EventPanel eventPanel) {
        eventPanels.add(eventPanel);
        Collections.sort(eventPanels, eventPanelComparator);
        repaint();
    }

    public void removeEventPanel(EventPanel eventPanel) {
        eventPanels.remove(eventPanel);
        repaint();
    }

    public void removeEvent(Long id) {
        for (EventPanel eventPanel : eventPanels) {
            if (eventPanel.getEvent().getId() != null && eventPanel.getEvent().getId().equals(id)) {
                eventPanels.remove(eventPanel);
                repaint();
                return;
            }
        }
    }

    public void setSelectedEvent(Event event) {
        for (EventPanel eventPanel : eventPanels) {
            if (event != null && eventPanel.getEvent().getId().equals(event.getId())) {
                eventPanel.setSelected(true);
            } else {
                eventPanel.setSelected(false);
            }
        }
        repaint();
    }

    public void removeAllEventPanels() {
        eventPanels.clear();
        repaint();
    }

    public List<EventPanel> getEventPanels() {
        return eventPanels;
    }

    public Calendar getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Calendar currentDay) {
        this.currentDay = currentDay;
        repaint();
    }

    public ScaleControler getScaleControler() {
        return scaleControler;
    }

    public DayCountControler getDayCountControler() {
        return dayCountControler;
    }

    public JPanel getTimePanelHeader() {
        return timePanelHeader;
    }

    public JScrollBar getTimePanelFooter() {
        return timePanelFooter;
    }

    @Override
    public void repaint() {
        super.repaint();
        if (timeLinePanel != null) {
            timeLinePanel.repaint();
        }
        if (timePanelHeader != null) {
            timePanelHeader.repaint();
        }
    }

    @Override
    public int getNumberOfPages() {
        int page = 0;
        final Graphics graphics = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB).createGraphics();

        try {
            while (print(graphics, getPageFormat(page), page) != Printable.NO_SUCH_PAGE) {
                page++;
            }
        } catch (PrinterException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }

        return page;
    }

    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return PrinterJob.getPrinterJob().defaultPage();
    }

    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return this;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        final Graphics2D g2d = (Graphics2D) graphics;
        int width = getWidth() + timeLinePanel.getWidth();
        double ratio = pageFormat.getImageableWidth() / width;

        printing = true;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, (int) pageFormat.getWidth(), (int) pageFormat.getHeight());
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        if (pageIndex == 0) {
            final Font font = g2d.getFont();
            String printString = LocaleManager.getString("eventReview");
            g2d.setColor(Color.BLACK);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 16f));
            g2d.drawString(printString, (int) ((pageFormat.getImageableWidth() - g2d.getFontMetrics().stringWidth(printString)) / 2),
                    g2d.getFontMetrics().getHeight());
            final Calendar endDate = (Calendar) startHour.clone();
            endDate.add(Calendar.DATE, EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT));
            printString = DateFormat.getDateInstance().format(startHour.getTime())
                    + (EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT) > 1
                    ? " - " + DateFormat.getDateInstance().format(endDate.getTime()) : "");
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 10f));
            g2d.drawString(printString, (int) ((pageFormat.getImageableWidth() - g2d.getFontMetrics().stringWidth(printString)) / 2),
                    g2d.getFontMetrics().getHeight() + 20);
            g2d.setFont(font);
            g2d.translate(0, 50);
        }

        int remaining = (int) (getHeight() * ratio);
        int index = 0;
        while (remaining > 0) {
            if (index == pageIndex) {
                Rectangle bounds = g2d.getClipBounds();
                if (bounds == null) {
                    bounds = new Rectangle(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
                }
                g2d.setClip(new Rectangle(0, 0, (int) pageFormat.getImageableWidth(), (int) (pageFormat.getImageableHeight() - (index == 0 ? 50 : 0))).intersection(bounds));
                g2d.transform(AffineTransform.getScaleInstance(ratio, ratio));
                g2d.translate(0, (index == 0 ? 1 : 2) * timePanelHeader.getHeight() - (index * pageFormat.getImageableHeight() - (index == 0 ? 0 : 50)) / ratio);
                timeLinePanel.paint(g2d);
                g2d.translate(timeLinePanel.getWidth(), (index == 0 ? 1 : 2) * -timePanelHeader.getHeight() + (index * pageFormat.getImageableHeight() - (index == 0 ? 0 : 50)) / ratio);
                timePanelHeader.paint(g2d);
                g2d.transform(AffineTransform.getScaleInstance(1 / ratio, 1 / ratio));
                g2d.setClip(new Rectangle(0, index == 0 ? 0 : (int) (timePanelHeader.getHeight() * ratio), (int) pageFormat.getImageableWidth(), (int) (pageFormat.getImageableHeight() - (index == 0 ? 50 : 0))).intersection(bounds));
                g2d.transform(AffineTransform.getScaleInstance(ratio, ratio));
                g2d.translate(0, (index == 0 ? 1 : 2) * timePanelHeader.getHeight() - (index * pageFormat.getImageableHeight() - (index == 0 ? 0 : 50)) / ratio);
                paintTimeLines(g2d);
                paintEventPanels(g2d);
                printing = false;

                return Printable.PAGE_EXISTS;
            }
            index++;
            remaining -= pageFormat.getImageableHeight();
        }
        printing = false;

        return Printable.NO_SUCH_PAGE;
    }

    public void scrollToEvent(Event event) {
        final Rectangle rectangle = new Rectangle(
                0,
                (int) ((event.getStartTime().get(Calendar.HOUR_OF_DAY) - startHour.get(Calendar.HOUR_OF_DAY)) * getHourStep()) + TOP_INSET,
                getWidth(),
                event.getStartTime().get(Calendar.HOUR_OF_DAY) < event.getEndTime().get(Calendar.HOUR_OF_DAY)
                        ? (int) ((event.getEndTime().get(Calendar.HOUR_OF_DAY) - event.getStartTime().get(Calendar.HOUR_OF_DAY)) * getHourStep()) + BOTTOM_INSET + 5
                        : 300
        );
        if (getVisibleRect().contains(rectangle)) {
            return;
        }
        scrollRectToVisible(rectangle);
    }

    private class TimePanelHeader extends JPanel {

        private final DateFormat DAY_FORMAT = new SimpleDateFormat("d.");

        public TimePanelHeader() {
            super.setPreferredSize(new Dimension(100, 20));
        }

        @Override
        public void paint(Graphics g) {
            Calendar day = (Calendar) currentDay.clone();
            int dayCount = EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT);

            g.setColor(ColorList.BLUE_1);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK.brighter());
            g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            g.setColor(Color.BLACK);
            g.drawLine(0, getHeight() - 3, getWidth(), getHeight() - 3);
            for (int i = 0; i < dayCount; i++) {
                String weekDay = day.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
                String date = DAY_FORMAT.format(day.getTime());
                g.drawString(weekDay,
                        (i) * TimePanel.this.getSize().width / dayCount + 15,
                        getHeight() - 8);
                g.drawString(date,
                        (i + 1) * TimePanel.this.getSize().width / dayCount - 15 - g.getFontMetrics().stringWidth(date),
                        getHeight() - 8);
                day.add(Calendar.DATE, 1);
            }
        }

        @Override
        public void setPreferredSize(Dimension preferredSize) {
        }

        @Override
        public Color getBackground() {
            return TimePanel.this.getBackground().darker();
        }
    }

    private class TimeLinePanel extends JPanel {

        public TimeLinePanel() {
            super.setPreferredSize(new Dimension(45, TimePanel.this.getPreferredSize().height));
        }

        @Override
        public void paint(Graphics g) {
            int height = TimePanel.this.getPreferredSize().height - TOP_INSET - BOTTOM_INSET;
            int currentLine = 0;
            Calendar currentTime = (Calendar) startHour.clone();

            g.setColor(ColorList.BLUE_1);
            g.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);
            currentTime.set(Calendar.MINUTE, 0);
            g.setColor(TimePanel.this.getForeground());
            while (currentLine <= height) {
                g.drawString(TIME_FORMAT.format(currentTime.getTime()), 40 - g.getFontMetrics().stringWidth(TIME_FORMAT.format(currentTime.getTime())),
                        currentLine + TOP_INSET + (g.getFont().getSize() / 2) - 1);
                currentLine += getHourStep();
                currentTime.add(Calendar.HOUR, 1);
            }
            if (mouseTime != null && !printing) {
                g.setColor(Color.DARK_GRAY);
                g.drawString(TIME_FORMAT.format(mouseTime.getTime()), 40 - g.getFontMetrics().stringWidth(TIME_FORMAT.format(mouseTime.getTime())),
                        mousePoint.y + (g.getFont().getSize() / 2) - 1);
            }
        }

        @Override
        public void setPreferredSize(Dimension preferredSize) {
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, TimePanel.this.getPreferredSize().height);
        }

        @Override
        public Color getBackground() {
            return TimePanel.this.getBackground().darker();
        }
    }

    public class ScaleControler extends JPanel {

        private JButton btnPlus = new JButton("+");
        private JButton btnMinus = new JButton("-");

        public ScaleControler() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new GridLayout(2, 1));

            btnPlus.setMargin(new Insets(0, 0, 0, 0));
            btnPlus.setBorder(new MatteBorder(0, 1, 1, 0, Color.GRAY));
            btnPlus.setFont(new Font("Monospaced", Font.PLAIN, 8));
            btnPlus.setFocusPainted(false);
            btnMinus.setMargin(new Insets(0, 0, 0, 0));
            btnMinus.setBorder(new MatteBorder(0, 1, 1, 0, Color.GRAY));
            btnMinus.setFont(new Font("Monospaced", Font.PLAIN, 8));
            btnMinus.setFocusPainted(false);
            add(btnPlus);
            add(btnMinus);
            btnPlus.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    TimePanel.this.setPreferredSize(
                            new Dimension(TimePanel.this.getPreferredSize().width,
                                    TimePanel.this.getPreferredSize().height + 75));
                    timeLinePanel.revalidate();
                    TimePanel.this.revalidate();
                    timeLinePanel.getRootPane().updateUI();
                    TimePanel.this.getRootPane().updateUI();
                }
            });
            btnMinus.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    TimePanel.this.setPreferredSize(
                            new Dimension(TimePanel.this.getPreferredSize().width,
                                    (TimePanel.this.getPreferredSize().height - 75) > 300 ? (TimePanel.this.getPreferredSize().height - 75) : 300));
                    timeLinePanel.revalidate();
                    TimePanel.this.revalidate();
                    timeLinePanel.getRootPane().updateUI();
                    TimePanel.this.getRootPane().updateUI();
                }
            });
        }
    }

    public class DayCountControler extends JPanel {

        private JButton btnPlus = new JButton("+");
        private JButton btnMinus = new JButton("-");

        public DayCountControler() {
            initComponents();
        }

        private void initComponents() {
            setLayout(new GridLayout(1, 2));

            btnPlus.setMargin(new Insets(0, 0, 0, 0));
            btnPlus.setBorder(new MatteBorder(0, 1, 1, 0, Color.GRAY));
            btnPlus.setFont(new Font("Monospaced", Font.PLAIN, 8));
            btnPlus.setFocusPainted(false);
            btnMinus.setMargin(new Insets(0, 0, 0, 0));
            btnMinus.setBorder(new MatteBorder(0, 1, 1, 0, Color.GRAY));
            btnMinus.setFont(new Font("Monospaced", Font.PLAIN, 8));
            btnMinus.setFocusPainted(false);
            add(btnMinus);
            add(btnPlus);
            btnPlus.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int dayCount = EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT);

                    if (dayCount >= 7) {
                        return;
                    }
                    EventSettings.getInstance().setValue(EventSettings.I_DAY_COUNT, ++dayCount);
                    TimePanel.this.updateUI();
                }
            });
            btnMinus.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int dayCount = EventSettings.getInstance().getInt(EventSettings.I_DAY_COUNT);

                    if (dayCount <= 1) {
                        return;
                    }
                    EventSettings.getInstance().setValue(EventSettings.I_DAY_COUNT, --dayCount);
                    TimePanel.this.updateUI();
                }
            });
        }
    }

    private class DragAndDropControler extends MouseAdapter {

        private static final int CONTROL_NONE = 0;
        private static final int CONTROL_RESIZING_UP = 1;
        private static final int CONTROL_RESISING_DOWN = 2;
        private static final int CONTROL_MOVING = 3;
        private EventPanel draggedEventPanel = null;
        private Calendar draggedTime;
        private int control = CONTROL_MOVING;
        private boolean snapEnabled;
        private int snapMinutes;

//        @Override
//        public void mousePressed(MouseEvent e) {
//            if (control == CONTROL_NONE) {
//                return;
//            }
//
//
//            if (draggedEventPanel != null) {
//                draggedTime = pointToCalendar(e.getPoint(), snapEnabled);
//            }
//        }

        @Override
        public void mouseDragged(MouseEvent e) {
            long timeDiff;
            long newStartTime;
            long newEndTime;

            if (draggedEventPanel == null) {
                draggedEventPanel = getSelectedEventPanel();
                if (draggedEventPanel == null) {
                    return;
                }
                draggedTime = pointToCalendar(e.getPoint(), snapEnabled);
                snapEnabled = EventSettings.getInstance().getBoolean(EventSettings.B_SNAP_ENABLED);
                snapMinutes = EventSettings.getInstance().getInt(EventSettings.I_SNAP_MINUTES);
            }

            newStartTime = draggedEventPanel.getEvent().getStartTime().getTimeInMillis();
            newEndTime = draggedEventPanel.getEvent().getEndTime().getTimeInMillis();
            timeDiff = pointToCalendar(e.getPoint(), snapEnabled).getTimeInMillis() - draggedTime.getTimeInMillis();

            if (control == CONTROL_MOVING || control == CONTROL_RESIZING_UP) {
                newStartTime += timeDiff;
            }
            if (control == CONTROL_MOVING || control == CONTROL_RESISING_DOWN) {
                newEndTime += timeDiff;
            }

            if (newStartTime < newEndTime) {
                newStartTime = newStartTime - (control != CONTROL_RESISING_DOWN && snapEnabled ? (newStartTime % (60000 * snapMinutes)) : 0);
                newEndTime = newEndTime - (control != CONTROL_RESIZING_UP && snapEnabled ? (newEndTime % (60000 * snapMinutes)) : 0);
                draggedEventPanel.getEvent().getStartTime().setTimeInMillis(newStartTime);
                draggedEventPanel.getEvent().getEndTime().setTimeInMillis(newEndTime);
                draggedTime = pointToCalendar(e.getPoint(), snapEnabled);
                if (snapEnabled) {
                    draggedTime.set(Calendar.MINUTE, draggedTime.get(Calendar.MINUTE) - draggedTime.get(Calendar.MINUTE) % snapMinutes);
                }
            }
            TimePanel.this.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            EventPanel selectedEventPanel = getSelectedEventPanel();
            final EventPanel eventPanelOnPoint = getEventPanelOnPoint(e.getPoint(), false);

//            if (selectedEventPanel == null) {
//                selectedEventPanel = eventPanelOnPoint;
//            }
            setCursorForEvent(selectedEventPanel, e.getPoint());
            if (control == CONTROL_NONE && eventPanelOnPoint != null) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }

        private void setCursorForEvent(EventPanel eventPanel, Point p) {
            if (eventPanel != null && eventPanel.getEvent().acceptTime(pointToCalendar(p, false)) && !eventPanel.getEvent().acceptTime(pointToCalendar(new Point(p.x, p.y - 4), false))) {
                control = CONTROL_RESIZING_UP;
                setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            } else if (eventPanel != null && eventPanel.getEvent().acceptTime(pointToCalendar(p, false)) && !eventPanel.getEvent().acceptTime(pointToCalendar(new Point(p.x, p.y + 4), false))) {
                control = CONTROL_RESISING_DOWN;
                setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
            } else if (eventPanel != null && eventPanel.getEvent().acceptTime(pointToCalendar(p, false))) {
                control = CONTROL_MOVING;
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            } else {
                control = CONTROL_NONE;
                setCursor(Cursor.getDefaultCursor());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (draggedEventPanel == null) {
                return;
            }
            final Event event = draggedEventPanel.getEvent();


            new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    EventDataManager.getInstance().updateDatabaseEntity(event);
                    //TODO: presunut to recordsUpdated resp. added...
                    Collections.sort(eventPanels, eventPanelComparator);

                    return null;
                }

            }.execute();
            setCursorForEvent(draggedEventPanel, e.getPoint());
            draggedEventPanel = null;
        }
    }

    private class TimePanelFooter extends JScrollBar {

        public TimePanelFooter() {
            super(HORIZONTAL);
            removeAll();
        }

        @Override
        public void paint(Graphics g) {
//            Graphics2D g2d = (Graphics2D) g;
//
//            g2d.setPaint(new GradientPaint(-200, -200, ColorFactory.COLOR_LIGHTER_BLUE,
//                    getWidth() + 400, getHeight() + 400,
//                    Color.BLUE.brighter()));
            g.setColor(ColorList.BLUE_1);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private class TopLeftCorner extends JPanel {

        @Override
        public void paint(Graphics g) {
            g.setColor(ColorList.BLUE_1);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.BLACK);
            g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            g.drawLine(0, getHeight() - 3, getWidth(), getHeight() - 3);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        TimePanel timePanel = new TimePanel();
        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();
        fromCal.set(2008, 10, 20, 7, 20);
        toCal.set(2008, 10, 20, 8, 30);
        EventPanel panel = new EventPanel(new Event("dsd", fromCal, toCal, Color.green));
        timePanel.addEventPanel(panel);
        fromCal.set(2008, 10, 20, 5, 40);
        toCal.set(2008, 10, 20, 19, 59);
        panel = new EventPanel(new Event("dsffds", fromCal, toCal, Color.red));
        timePanel.addEventPanel(panel);
        frame.add(timePanel);
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
