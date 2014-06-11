
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

import org.jdesktop.swingx.border.DropShadowBorder;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.swing.gradient.GradientPanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 *
 * @author wladimiiir
 */
public class ProgressDialog extends JDialog{

    private JLabel progressMessage;
    private JProgressBar progressBar;

    public ProgressDialog() {
        this(SodalisApplication.get().getMainFrame());
    }

    public ProgressDialog(Frame owner) {
        super(owner, true);
        initDialog();
    }

    private void initDialog() {
        JPanel dialogPanel = new JPanel();
        JPanel messagePanel = new GradientPanel(new FlowLayout(FlowLayout.CENTER));

        progressMessage = new JLabel();
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setBackground(ColorList.LIGHT_BLUE);
        progressBar.setForeground(ColorList.SPLASH_FOREGROUND);
        progressBar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, ColorList.BORDER));

        dialogPanel.setLayout(new GridLayout(2, 1));
        setLayout(new BorderLayout());
        messagePanel.add(progressMessage);
        dialogPanel.add(messagePanel);
        dialogPanel.add(progressBar);
        dialogPanel.setBorder(new CompoundBorder(new LineBorder(ColorList.BORDER), new DropShadowBorder()));
        messagePanel.setBackground(ColorList.LIGHT_BLUE);

        add(dialogPanel, BorderLayout.CENTER);
        getGlassPane().setBackground(ColorList.LIGHT_BLUE);
//        dialogPanel.showOnGlassPane((Container) getGlassPane(), getContentPane(), 0, 0, 0);
        setUndecorated(true);
        setSize(500, 50);
        setLocationRelativeTo(null);
    }

    public void startProgress() {
        setVisible(true);
    }

    public void setProgressMessage(String message){
        progressMessage.setText(message);
    }

    public void stopProgress() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setVisible(false);
            }
        });
    }
}