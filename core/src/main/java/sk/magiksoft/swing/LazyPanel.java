
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.swing;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 18, 2010
 * Time: 8:09:53 PM
 * To change this template use File | Settings | File Templates.
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