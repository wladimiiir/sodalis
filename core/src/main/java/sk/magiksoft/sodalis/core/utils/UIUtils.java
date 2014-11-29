package sk.magiksoft.sodalis.core.utils;

import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.swing.ProgressDialog;
import sk.magiksoft.swing.gradient.GradientToolBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author wladimiiir
 */
public class UIUtils {

    private UIUtils() {
    }

    public static Component createTitlePanel(String title) {
        final JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setIcon(sk.magiksoft.sodalis.core.factory.IconFactory.getInstance().getIcon("application", 16));
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ColorList.BORDER));
        makeMoveableComponent(titlePanel);

        return makeGradientComponent(titlePanel, ColorList.LIGHT_BLUE, ColorList.SPLASH_FOREGROUND);
    }

    private static void makeMoveableComponent(final JComponent component) {
        MovableComponentMouseListener listener = new MovableComponentMouseListener(component);

        component.addMouseListener(listener);
        component.addMouseMotionListener(listener);
    }

    public static JDialog makeISDialog(JDialog dialog) {
        JPanel centerPanel = new JPanel(new BorderLayout());
        Component titlePanel = createTitlePanel(dialog.getTitle());

        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(dialog.getContentPane(), BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createLineBorder(ColorList.BORDER, 2));

        dialog.setContentPane(centerPanel);
        dialog.setUndecorated(true);
        return dialog;
    }

    public static JComponent makeGradientComponent(JComponent component, final Color firstColor, final Color secondColor) {
        JPanel panel = new JPanel(new BorderLayout()) {

            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;

                g2.setPaint(new LinearGradientPaint(getBounds().x, getBounds().y, getBounds().width, getBounds().height, new float[]{0f, 0.5f, 1f},
                        new Color[]{firstColor, secondColor, firstColor}));
                g2.fillRect(0, 0, getBounds().width, getBounds().height);
                paintBorder(g);
                paintChildren(g);
            }
        };

        component.setOpaque(false);
        panel.add(component, BorderLayout.CENTER);

        return panel;
    }

    public static JToolBar createToolBar() {
        GradientToolBar toolBar = new GradientToolBar();

        toolBar.setBackground(ColorList.LIGHT_BLUE);
        toolBar.setForeground(ColorList.SPLASH_FOREGROUND);

        return toolBar;
    }

    public static void doInBackground(final Runnable runnable) {
        try {
            new SwingWorker() {

                @Override
                protected Object doInBackground() throws Exception {
                    runnable.run();
                    return null;
                }
            }.execute();
        } catch (Exception ex) {
            LoggerManager.getInstance().error(UIUtils.class, ex);
        }
    }

    public static void doWithProgress(final String progressMessage, final Runnable runnable) {
        final ProgressDialog progressDialog = new ProgressDialog();
        progressDialog.setProgressMessage(progressMessage);
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                runnable.run();
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.stopProgress();
                    }
                });

            }
        }.execute();
        progressDialog.startProgress();
    }

    private static class MovableComponentMouseListener extends MouseAdapter {
        private Window window;
        private Point mousePoint = new Point();
        private Point location;
        private JComponent component;

        public MovableComponentMouseListener(JComponent component) {
            this.component = component;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mousePoint.x = e.getXOnScreen();
            mousePoint.y = e.getYOnScreen();
            if (window == null) {
                window = SwingUtilities.getWindowAncestor(component);
            }
            if (window != null) {
                location = (Point) window.getLocationOnScreen().clone();
            }
        }

        @Override
        public void mouseDragged(final MouseEvent e) {


            new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    final double translationX = e.getLocationOnScreen().getX() - mousePoint.getX();
                    final double translationY = e.getLocationOnScreen().getY() - mousePoint.getY();

//                    System.out.printf("Location: %s\n", location.toString());
                    if (Math.abs(translationX) < 5 && Math.abs(translationY) < 5) {
                        cancel(true);
                        return null;
                    }

                    location.translate((int) Math.round(translationX), (int) Math.round(translationY));
                    mousePoint.x = e.getXOnScreen();
                    mousePoint.y = e.getYOnScreen();

                    return null;
                }

                @Override
                protected void done() {
                    if (isCancelled()) {
                        return;
                    }
//                    System.out.printf("Setting location to: %s\n", location.toString());
                    window.setLocation(location);
                }
            }.execute();
        }
    }
}
