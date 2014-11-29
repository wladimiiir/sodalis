package sk.magiksoft.swing;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author wladimiiir
 * @since 2010/5/18
 */
public abstract class LazyPanel extends JPanel {
    private JComponent lazyComponent;

    protected LazyPanel() {
        super(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JLabel loadingLabel = new JLabel(LocaleManager.getString("loading"));

        loadingLabel.setFont(loadingLabel.getFont().deriveFont(20f));
        setLayout(new GridBagLayout());
        add(loadingLabel, new GridBagConstraints());
    }

    protected abstract JComponent initLazyComponent();

    protected abstract void fireInitializationFinished();

    public void loadLazyComponent() {
        if (lazyComponent != null) {
            return;
        }

        final SwingWorker swingWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    lazyComponent = initLazyComponent();
                } catch (Exception e) {
                    LoggerManager.getInstance().error(LazyPanel.class, e);
                    throw e;
                }

                return null;
            }

            @Override
            protected void done() {
                if (lazyComponent == null) {
                    System.out.println("something went wrong!!!");
                    return;
                }
                removeAll();
                setLayout(new BorderLayout());
                add(lazyComponent, BorderLayout.CENTER);
                revalidate();
                repaint();
                fireInitializationFinished();
            }
        };
        swingWorker.execute();
    }
}
