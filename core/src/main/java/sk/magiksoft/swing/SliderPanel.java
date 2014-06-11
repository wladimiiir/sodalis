
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

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

/**
 * @author wladimiiir
 */
public class SliderPanel extends JPanel implements TimingTarget {

    public static final int POSITION_TOP = 1;
    public static final int POSITION_BOTTOM = 2;
    public static final int POSITION_LEFT = 3;
    public static final int POSITION_RIGHT = 4;
    private static final int SLIDE_DURATION = 150;
    public static final int STATE_HIDDEN = 1;
    public static final int STATE_SHOWN = 2;
    private static final int STATE_HIDING = 3;
    private static final int STATE_SHOWING = 4;
    private static final int DELAY = 3000;
    private static final int DURATION = 300;
    private Animator animator;
    private int position;
    private int openedSize;
    private String title;
    private int state = STATE_HIDDEN;
    private Color firstColor = new Color(20, 200, 244);
    private Color secondColor = new Color(100, 49, 244);
    private int currentLocation = 0;


    public SliderPanel() {
        this(null);
    }

    public SliderPanel(JPanel mainPanel) {
        this(mainPanel, POSITION_LEFT);
    }

    public SliderPanel(JPanel mainPanel, int position) {
        this(mainPanel, position, 100);
    }

    public SliderPanel(JPanel mainPanel, int position, int openedSize) {
        this.position = position;
        this.openedSize = openedSize;
        initialize();
        setMainPanel(mainPanel);
        initMouseListener();
    }

    private void mouseClicked(MouseEvent e) {
        animator.cancel();
        setState(STATE_SHOWN);
        e.consume();
    }

    private void mouseEntered(MouseEvent e) {
        if (state == STATE_HIDDEN) {
            animator.stop();
            animator.setStartDelay(DELAY);
            animator.setStartFraction(0f);
            animator.setStartDirection(Animator.Direction.FORWARD);
            state = STATE_SHOWING;
            animator.start();
        } else if (state == STATE_HIDING) {
            animator.pause();
            animator.setStartDelay(50);
            animator.setStartDirection(Animator.Direction.FORWARD);
            state = STATE_SHOWING;
            animator.resume();
        }
    }

    private void mouseExited(MouseEvent e) {
//        if (e.getX() < 0 && e.getX() > -5 || e.getY() < 0 && e.getY() > -5) {
//            return;
//        }
        if (state == STATE_SHOWING) {
            animator.pause();
            animator.setStartFraction(animator.getTimingFraction());
            animator.cancel();
            animator.setStartDirection(Animator.Direction.BACKWARD);
            state = STATE_HIDING;
            animator.start();
        } else if (state == STATE_HIDDEN) {
            animator.cancel();
            revalidate();
            repaint();
        } else if (state == STATE_SHOWN) {
            animator.stop();
            animator.setStartDelay(50);
            animator.setStartFraction(1f);
            animator.setStartDirection(Animator.Direction.BACKWARD);
            animator.start();
        }
    }

    public Color getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(Color firstColor) {
        this.firstColor = firstColor;
    }

    public int getOpenedSize() {
        return openedSize;
    }

    public void setOpenedSize(int openedSize) {
        this.openedSize = openedSize;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Color getSecondColor() {
        return secondColor;
    }

    public void setSecondColor(Color secondColor) {
        this.secondColor = secondColor;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        if (state == STATE_SHOWN) {
            setCurrentLocation(0);
        } else {
            setCurrentLocation(-openedSize + 25);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void initialize() {
        setLayout(new BorderLayout());
        animator = new Animator(SLIDE_DURATION, this);
        animator.setStartDelay(DURATION);
        setState(STATE_HIDDEN);
    }

    private void initMouseListener() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

            @Override
            public void eventDispatched(AWTEvent event) {
                MouseEvent e = (MouseEvent) event;

                if (e.getID() == MouseEvent.MOUSE_MOVED && isShowing()) {
                    Point panelPoint = getLocationOnScreen();
                    Point mousePoint = e.getLocationOnScreen();
                    if ((mousePoint.x >= panelPoint.x) && (mousePoint.x < getWidth() + panelPoint.x)
                            && (mousePoint.y >= panelPoint.y) && (mousePoint.y < getHeight() + panelPoint.y)) {
                        mouseEntered(e);
                    } else {
                        mouseExited(e);
                    }
                }
            }
        }, AWTEvent.MOUSE_MOTION_EVENT_MASK);
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

            @Override
            public void eventDispatched(AWTEvent event) {
                MouseEvent e = (MouseEvent) event;

                if (e.getID() == MouseEvent.MOUSE_CLICKED && isShowing()) {
                    Point panelPoint = getLocationOnScreen();
                    Point mousePoint = e.getLocationOnScreen();

                    if ((mousePoint.x >= panelPoint.x) && (mousePoint.x < getWidth() + panelPoint.x)
                            && (mousePoint.y >= panelPoint.y) && (mousePoint.y < getHeight() + panelPoint.y)) {
                        animator.cancel();
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }

    private void setCurrentLocation(int location) {
        this.currentLocation = location;
        switch (position) {
            case POSITION_LEFT:
            case POSITION_RIGHT:
                setLocation(location, getY());
                break;
            case POSITION_BOTTOM:
            case POSITION_TOP:
                setLocation(getX(), location);
                break;
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        switch (position) {
            case POSITION_LEFT:
            case POSITION_RIGHT:
                return new Dimension(openedSize, getHeight());
            case POSITION_BOTTOM:
            case POSITION_TOP:
                return new Dimension(getWidth(), openedSize);
            default:
                return null;
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        switch (position) {
            case POSITION_LEFT:
            case POSITION_RIGHT:
                super.setBounds(currentLocation, y, width, height);
                break;
            case POSITION_BOTTOM:
            case POSITION_TOP:
                super.setBounds(x, currentLocation, width, height);
                break;
        }
    }

    public void setMainPanel(JPanel mainPanel) {
        removeAll();
        if (mainPanel != null) {
            add(mainPanel, BorderLayout.CENTER);
        }
        revalidate();
    }

    @Override
    public void timingEvent(float fraction) {
        setCurrentLocation(-openedSize + (int) (openedSize * fraction) + (int) (25 - 25 * fraction));
    }

    @Override
    public void begin() {
        if (state == STATE_HIDDEN) {
            state = STATE_SHOWING;
        } else if (state == STATE_SHOWN) {
            state = STATE_HIDING;
        }
    }

    @Override
    public void end() {
        if (state == STATE_HIDING) {
            setState(STATE_HIDDEN);
        } else if (state == STATE_SHOWING) {
            setState(STATE_SHOWN);
        }
    }

    @Override
    public void repeat() {
    }

    @Override
    public void paint(Graphics g) {
        if (state != STATE_HIDDEN || title == null) {
            super.paint(g);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        Paint paint = g2d.getPaint();
        g2d.setPaint(new LinearGradientPaint(getBounds().x, getBounds().y, getBounds().width,
                getBounds().height, new float[]{0f, 0.5f, 1f},
                new Color[]{firstColor, secondColor, firstColor}));
        g.fillRect(-currentLocation, 0, getBounds().width, getBounds().height);

        paintBorder(g);

        if (title.trim().isEmpty()) {
            return;
        }

        g2d.setPaint(paint);
        switch (position) {

            case POSITION_LEFT:
                paintLeftSideTitle(g2d);
                break;
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private void paintLeftSideTitle(Graphics2D g2d) {
        AffineTransform tr = g2d.getTransform();

        g2d.setColor(Color.BLACK);
        g2d.rotate(Math.PI / 2);
        g2d.translate(5, 17);
        g2d.setFont(new Font(g2d.getFont().getFontName(), Font.BOLD, 15));
        g2d.drawString(title, 0, -openedSize);
        g2d.setTransform(tr);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel();
        mainPanel.add(new JList(new Object[]{"Vajco", "Klobasa"}), BorderLayout.CENTER);
        SliderPanel panel = new SliderPanel(mainPanel, POSITION_LEFT, 200);
        panel.setTitle("Kontexty");
        panel.setPreferredSize(new Dimension(200, 200));
        panel.setBorder(new LineBorder(Color.black));
        JTable table = new JTable(4, 10);
        frame.add(table, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.WEST);

        frame.setVisible(true);
    }
}