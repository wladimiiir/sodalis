
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.swing;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 1/26/11
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiActionButton extends JButton {
    private Action[] actions;
    private JMenuItem[] menuItems;
    private JPopupMenu actionPopup;

    public MultiActionButton(Action[] actions) {
        this.actions = actions;
        initComponent();
    }

    public MultiActionButton(Action[] actions, Icon icon) {
        super(icon);
        this.actions = actions;
        initComponent();
    }

    public MultiActionButton(Action[] actions, String text) {
        super(text);
        this.actions = actions;
        initComponent();
    }

    public MultiActionButton(Action[] actions, String text, Icon icon) {
        super(text, icon);
        this.actions = actions;
        initComponent();
    }

    @Override
    public void addActionListener(ActionListener l) {
        for (JMenuItem menuItem : menuItems) {
            menuItem.addActionListener(l);
        }
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
        initPopupMenu();
    }

    public Action[] getActions() {
        return actions;
    }

    private void initComponent() {
        initPopupMenu();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e) || e.getClickCount() != 1) {
                    return;
                }
                if (actionPopup.isShowing()) {
                    hideActionPopup();
                } else {
                    showActionPopup();
                }
            }
        });
    }

    private void initPopupMenu() {
        actionPopup = new JPopupMenu();
        menuItems = new JMenuItem[actions.length];
        for (int i = 0; i < actions.length; i++) {
            final Action action = actions[i];
            actionPopup.add(menuItems[i] = new JMenuItem(action));
        }
    }

    private void showActionPopup() {
        actionPopup.show(this, 0, getHeight());
    }

    private void hideActionPopup() {
        actionPopup.setVisible(false);
    }
}