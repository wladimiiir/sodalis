
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.splash;

import org.jdesktop.swingx.border.DropShadowBorder;
import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author wladimiiir
 */
public class SplashScreen extends SwingWorker<Void, Void> {

    private JDialog splashDialog;

    private JPanel splashPanel;
    private JProgressBar progressBar;
    private SplashLoader loader;
    private Image currentImage;

    public SplashScreen(SplashLoader loader, int width, int height) {
        this.loader = loader;
        initComponents();
        splashDialog.setSize(width, height);
    }

    private void initComponents() {
        splashDialog = new JDialog();

        splashPanel = new JPanel(new BorderLayout()) {

            @Override
            public void paint(Graphics g) {
                if (currentImage == null) {
                    super.paint(g);
                    return;
                }
                ((Graphics2D) g).setPaint(new LinearGradientPaint(getBounds().x, getBounds().y, getBounds().width, getBounds().height, new float[]{0f, 0.5f, 1f},
                        new Color[]{getBackground(), getForeground(), getBackground()}));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), null);
                paintBorder(g);
            }
        };
        ((JComponent) splashDialog.getContentPane()).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorList.BORDER, 2),
                new DropShadowBorder()));
        progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setFont(progressBar.getFont().deriveFont(Font.BOLD));

        splashDialog.setLayout(new BorderLayout());
        splashDialog.add(splashPanel, BorderLayout.CENTER);
        splashDialog.add(progressBar, BorderLayout.SOUTH);
        splashDialog.setUndecorated(true);
        splashDialog.setTitle(loader.getTitle());
        splashDialog.setIconImage(loader.getIconImage());
    }

    public void setForeground(Color c) {
        splashDialog.setForeground(c);
        if (progressBar != null) {
            progressBar.setForeground(c);
        }
        if (splashPanel != null) {
            splashPanel.setForeground(c);
        }
    }

    public void setBackground(Color c) {
        splashDialog.setBackground(c);
        if (progressBar != null) {
            progressBar.setBackground(c);
        }
        if (splashPanel != null) {
            splashPanel.setBackground(c);
        }
    }

    public void start() {
//        try {
//            doInBackground();
//        } catch (Exception ex) {
//            LoggerManager.getInstance().error(getClass(), ex);
//        }
        execute();
    }

    @Override
    protected void done() {
        try {
            get();

            splashDialog.setVisible(false);
            splashDialog.dispose();
            loader.loaderFinished();

            return;
        } catch (InterruptedException e) {
            LoggerManager.getInstance().error(SplashScreen.class, e);
        } catch (ExecutionException e) {
            LoggerManager.getInstance().error(SplashScreen.class, e);
            loader.loaderCancelled(e.getCause());
        }
        loader.loaderCancelled(null);
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            final List<SplashAction> splashActions = loader.getSplashActions();

            splashDialog.setLocationRelativeTo(null);
            splashDialog.setVisible(true);
            for (final SplashAction splashAction : splashActions) {
                currentImage = splashAction.getSplashImage();
                progressBar.setString(splashAction.getActionName());
                splashPanel.repaint();
                try {
                    splashAction.run();
                } catch (VetoException e) {
                    LoggerManager.getInstance().info(SplashScreen.class, e.getMessage());
                    splashDialog.setVisible(false);
                    return null;
                } catch (Exception e) {
                    LoggerManager.getInstance().error(getClass(), e);
                    loader.loaderCancelled(e);
                }
            }
        } catch (Exception ex) {
            LoggerManager.getInstance().error(SplashScreen.class, ex);
            throw ex;
        }

        return null;
    }
}