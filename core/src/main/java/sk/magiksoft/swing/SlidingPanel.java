package sk.magiksoft.swing;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

/**
 * @author wladimiiir
 */
public class SlidingPanel extends JPanel {

    public static final int ORIENTATION_LEFT = 1;
    public static final int ORIENTATION_RIGHT = 2;
    public static final int ORIENTATION_TOP = 3;
    public static final int ORIENTATION_BOTTOM = 4;
    public static final int STATE_HIDDEN = 1;
    public static final int STATE_SHOWN = 2;
    private static final int STATE_HIDING = 3;
    private static final int STATE_SHOWING = 4;
    private static final int DELAY = 200;
    private int orientation;
    private int maximumSize;
    private int currentPosition;
    private int state = STATE_SHOWN;
    private String title = "";
    private boolean vertical;
    private Color firstColor = new Color(20, 200, 244);
    private Color secondColor = new Color(100, 49, 244);
    private MouseAdapter mouseFocus = new MouseFocusImpl();
    private FocusAdapter focusAdapter = new FocusAdapter() {

        @Override
        public void focusLost(FocusEvent e) {
            updateUI();
        }
    };
    private Thread currentThread = null;

    public SlidingPanel(JPanel mainPanel, int orientation, int maximumSize) {
        this(mainPanel, orientation, maximumSize, "");
    }

    public SlidingPanel(JPanel mainPanel, int orientation, int maximumSize, String title) {
        super(new BorderLayout(), true);
        this.orientation = orientation;
        this.maximumSize = maximumSize;
        this.title = title;
        this.add(mainPanel, BorderLayout.CENTER);
        vertical = orientation == ORIENTATION_LEFT || orientation == ORIENTATION_RIGHT;
        initMouseListeners(mainPanel);
        init();
    }

    private void init() {
        currentPosition = 0;
        addMouseListener(mouseFocus);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                synchronized (SlidingPanel.this) {
                    if (state == STATE_HIDDEN) {
                        currentPosition = maximumSize;
                        updateUI();
                        state = STATE_SHOWN;
                    }
                }
            }
        });
    }

    private void initMouseListeners(Container container) {
        container.addMouseListener(mouseFocus);
        container.addFocusListener(focusAdapter);
        for (int i = 0; i < container.getComponents().length; i++) {
            Component component = container.getComponents()[i];
            if (component instanceof Container) {
                initMouseListeners((Container) component);
            } else {
                component.addMouseListener(mouseFocus);
                component.addFocusListener(focusAdapter);
            }
        }
    }

    public Color getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(Color firstColor) {
        this.firstColor = firstColor;
    }

    public Color getSecondColor() {
        return secondColor;
    }

    public void setSecondColor(Color secondColor) {
        this.secondColor = secondColor;
    }

    public void setState(int state) {
        if (state == STATE_HIDDEN) {
            hiding();
        } else if (state == STATE_SHOWN) {
            showing();
        }
    }

    private synchronized void hiding() {
        if (state == STATE_HIDDEN || state == STATE_HIDING) {
            return;
        }
        currentThread = new Thread(new Runnable() {

            public void run() {
                int location = -maximumSize + getLocation().x + 25;

                currentPosition = getLocation().x;
                state = STATE_HIDING;
                while (currentPosition - 15 > location) {
                    currentPosition -= 15;
                    setLocation(currentPosition, getLocation().y);
                    repaint();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
                currentPosition = location;
                setLocation(currentPosition, getLocation().y);
                state = STATE_HIDDEN;
                repaint();
            }
        });
        currentThread.setPriority(Thread.MAX_PRIORITY);
        currentThread.start();
    }

    private synchronized void showing() {
        if (state == STATE_SHOWN || state == STATE_SHOWING) {
            return;
        }
        currentThread = new Thread(new Runnable() {

            public void run() {
                int location = 0;


                state = STATE_SHOWING;
                while (currentPosition + 15 < location) {
                    currentPosition += 15;
                    setLocation(currentPosition, getLocation().y);
                    repaint();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
                currentPosition = location;
                setLocation(currentPosition, getLocation().y);
                state = STATE_SHOWN;
                repaint();
            }
        });
        currentThread.setPriority(Thread.MAX_PRIORITY);
        currentThread.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return vertical ? new Dimension(maximumSize, getHeight()) : new Dimension(getWidth(), maximumSize);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel();
        mainPanel.add(new JList(new Object[]{"Vajco", "Klobasa"}), BorderLayout.CENTER);
        JPanel panel = new SlidingPanel(mainPanel, ORIENTATION_LEFT, 200, "Kontexty");
        panel.setPreferredSize(new Dimension(200, 200));
        panel.setBorder(new LineBorder(Color.black));
        JTable table = new JTable(4, 10);
        frame.add(table, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.WEST);

        frame.setVisible(true);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        if (vertical) {
            super.setBounds(currentPosition, y, width, height);
        } else {
            super.setBounds(x, currentPosition, width, height);
        }
    }

    @Override
    public void paint(Graphics g) {
        if (state == STATE_HIDDEN) {
            Graphics2D g2d = (Graphics2D) g;
            Paint paint = g2d.getPaint();
            g2d.setPaint(new LinearGradientPaint(getBounds().x, getBounds().y, getBounds().width, getBounds().height, new float[]{0f, 0.5f, 1f},
                    new Color[]{firstColor, secondColor, firstColor}));
//            g2d.setPaint(new GradientPaint(0, 0, firstColor, g.getClipBounds().width, g.getClipBounds().height, secondColor));
            g.fillRect(-currentPosition, 0, getBounds().width, getBounds().height);
            if (!title.trim().isEmpty()) {
                if (orientation == ORIENTATION_LEFT) {
                    g2d.setPaint(paint);
                    g2d.setColor(Color.BLACK);
                    AffineTransform tr = g2d.getTransform();
                    g2d.rotate(Math.PI / 2);
                    g2d.translate(5, 17);
                    g2d.setFont(new Font(g2d.getFont().getFontName(), Font.BOLD, 15));
                    g2d.drawString(title, 0, -maximumSize);
                    g2d.setTransform(tr);
                }
            }
            paintBorder(g);
            return;
        }
        super.paint(g);
    }

    private class MouseFocusImpl extends MouseAdapter {


        private Timer timer;

        @Override
        public void mouseEntered(MouseEvent e) {
            if (timer != null) {
                timer.stop();
            }
            timer = new Timer(3000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (SlidingPanel.this.state == STATE_HIDING) {
                        if (currentThread != null) {
                            currentThread.interrupt();
                        }
                        showing();
                        return;
                    }
                    if (SlidingPanel.this.state == STATE_SHOWING || SlidingPanel.this.state == STATE_SHOWN) {
                        return;
                    }
                    showing();
                }
            });
            timer.start();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (timer != null) {
                timer.stop();
            }
            timer = new Timer(20, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (SlidingPanel.this.state == STATE_HIDDEN || SlidingPanel.this.state == STATE_HIDING) {
                        return;
                    }
                    hiding();
                }
            });
            timer.start();
        }
    }
}
