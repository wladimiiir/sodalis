package sk.magiksoft.swing;

import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wladimiiir
 */
public class OSDDialog extends JDialog {

    private Timer timer;
    protected String text = "";
    private Image background = null;

    public OSDDialog(Frame owner) {
        super(owner, false);
        initDialog();
    }

    private void initDialog() {
        setUndecorated(true);
        setSize(300, 200);
        getContentPane().add(new CapturePanel());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void show(final int seconds) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                timer = new Timer();
                setLocationRelativeTo(null);
                updateBackground();
                setVisible(true);
                timer.schedule(new HideDialogTask(), seconds * 1000);
            }
        });
    }

    private void updateBackground() {
        try {
            Robot robot = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension dim = tk.getScreenSize();

            background = robot.createScreenCapture(new Rectangle(0, 0, (int) dim.getWidth(), (int) dim.getHeight()));
        } catch (AWTException ex) {
            LoggerManager.getInstance().error(getClass(), ex);
        }

    }

    private class CapturePanel extends JPanel {

        @Override
        public void paint(Graphics g) {
            if (background == null) {
                return;
            }
            Point pos = this.getLocationOnScreen();
            Point offset = new Point(-pos.x, -pos.y);
            g.drawImage(background, offset.x, offset.y, null);
            ((Graphics2D) g).setPaint(
                    new GradientPaint(getWidth() / 2, 0, Color.BLUE, getWidth() / 2, getHeight(), Color.BLUE.darker()));
            Composite composite = ((Graphics2D) g).getComposite();
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.3));
            g.fillRect(0, 0, getWidth(), getHeight());
            ((Graphics2D) g).setComposite(composite);
        }
    }

    private class HideDialogTask extends TimerTask {

        @Override
        public void run() {
            setVisible(false);
        }
    }
}
