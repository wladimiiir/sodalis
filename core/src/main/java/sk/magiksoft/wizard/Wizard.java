
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.wizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

/**
 * @author wladimiiir
 */
public class Wizard extends JDialog {
    private static final Icon PREVIOUS_ICON = new ImageIcon(Wizard.class.getResource("previous.png"));
    private static final Icon NEXT_ICON = new ImageIcon(Wizard.class.getResource("next.png"));
    private static final Icon FINISH_ICON = new ImageIcon(Wizard.class.getResource("finish.png"));
    private static final Icon CANCEL_ICON = new ImageIcon(Wizard.class.getResource("cancel.png"));

    public static final int CANCEL_ACTION = 0;
    public static final int FINISH_ACTION = 1;
    private JButton btnNextOrFinish = new JButton();
    private JButton btnPrevious = new JButton(new PreviousAction());
    private JButton btnCancel = new JButton(new CancelAction());
    private CardLayout cardLayout = new CardLayout();
    private JPanel pagePanel;
    private int currentPage = -1;
    private Component[] pages;
    private AbstractAction nextAction = new NextAction();
    private AbstractAction finishAction = new FinishAction();
    private int resultAction = CANCEL_ACTION;

    private static ResourceBundle strings = ResourceBundle.getBundle("sk.magiksoft.wizard.locale.wizard");

    public Wizard(Frame owner, Component[] pages) {
        super(owner, true);
        this.pages = pages;
        initComponents();
    }

    private void initComponents() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnPrevious);
        buttonPanel.add(btnNextOrFinish);
        buttonPanel.add(new JPanel());
        buttonPanel.add(btnCancel);
        pagePanel = new JPanel(cardLayout);

        for (int i = 0; i < pages.length; i++) {
            Component component = pages[i];
            pagePanel.add(component, String.valueOf(i));
        }
        cardLayout.show(pagePanel, String.valueOf(0));
        currentPage = 0;

        this.setLayout(new BorderLayout());
        this.getContentPane().add(pagePanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.setSize(300, 200);

        setupButtons();

        ((JComponent) getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE");
        ((JComponent) getContentPane()).getActionMap().put("ESCAPE", new CancelAction());
    }

    public int showWizard() {
        currentPage = 0;
        cardLayout.show(pagePanel, String.valueOf(currentPage));
        setupButtons();
        this.setVisible(true);
        return resultAction;
    }

    private void setupButtons() {
        if (currentPage == 0) {
            btnPrevious.setEnabled(false);
        } else {
            btnPrevious.setEnabled(true);
        }

        if (currentPage == (pages.length - 1)) {
            btnNextOrFinish.setAction(finishAction);
        } else {
            btnNextOrFinish.setAction(nextAction);
        }
    }

    private class NextAction extends AbstractAction {

        public NextAction() {
            super(strings.getString("next"), NEXT_ICON);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(pagePanel, String.valueOf(++currentPage));
            setupButtons();
        }
    }

    private class PreviousAction extends AbstractAction {

        public PreviousAction() {
            super(strings.getString("previous"), PREVIOUS_ICON);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(pagePanel, String.valueOf(--currentPage));
            setupButtons();
        }
    }

    private class FinishAction extends AbstractAction {

        public FinishAction() {
            super(strings.getString("finish"), FINISH_ICON);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            resultAction = FINISH_ACTION;
            setVisible(false);
        }
    }

    private class CancelAction extends AbstractAction {

        public CancelAction() {
            super(strings.getString("cancel"), CANCEL_ICON);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            resultAction = CANCEL_ACTION;
            setVisible(false);
        }
    }
}