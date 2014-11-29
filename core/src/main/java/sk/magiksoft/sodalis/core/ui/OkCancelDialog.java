package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.Checker;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.utils.UIUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author wladimiiir
 */
public class OkCancelDialog extends JDialog {

    public static final int ACTION_CANCEL = 0;
    public static final int ACTION_OK = 1;
    private Action okAction = new AbstractAction(
            LocaleManager.getString("okAction"), IconFactory.getInstance().getIcon("ok")) {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!checkOkAction()) {
                return;
            }
            resultAction = ACTION_OK;
            setVisible(false);
        }
    };
    private Action cancelAction = new AbstractAction(
            LocaleManager.getString("cancelAction"), IconFactory.getInstance().getIcon("cancel")) {

        @Override
        public void actionPerformed(ActionEvent e) {
            resultAction = ACTION_CANCEL;
            if (closeOnCancel) {
                setVisible(false);
            }
        }
    };
    protected int resultAction = ACTION_CANCEL;
    private JButton okButton = new JButton(okAction);
    private JButton cancelButton = new JButton(cancelAction);
    private Component mainPanel = new JPanel();
    private JPanel additionalButtonPanel;
    private JPanel dialogPanel;
    private boolean closeOnCancel = true;
    private EventListenerList listenerList = new EventListenerList();

    public OkCancelDialog() {
        this(SodalisApplication.get().getMainFrame());
    }

    public OkCancelDialog(String title) {
        this(SodalisApplication.get().getMainFrame(), title);
    }

    public OkCancelDialog(Window owner) {
        super(owner);
        initLayout();
    }

    public OkCancelDialog(Window owner, String title) {
        this(owner);
        setTitle(title);
        UIUtils.makeISDialog(this);
    }

    public OkCancelDialog(Action okAction, Action cancelAction) {
        this();
        this.okAction = okAction;
        this.cancelAction = cancelAction;
    }

    private void initLayout() {
        final JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        dialogPanel = new JPanel(new BorderLayout());
        additionalButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(3, 3, 3, 3);
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(additionalButtonPanel, c);

        c.gridx++;
        c.weightx = 0;
        buttonPanel.add(okButton, c);

        c.gridx++;
        buttonPanel.add(cancelButton, c);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));
        setLayout(new BorderLayout());
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialogPanel.add(mainPanel, BorderLayout.CENTER);
        add(dialogPanel, BorderLayout.CENTER);

        ((JComponent) getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_MASK), "OK");
        ((JComponent) getContentPane()).getActionMap().put("OK", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getOkButton().doClick();
            }
        });
        ((JComponent) getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE");
        ((JComponent) getContentPane()).getActionMap().put("ESCAPE", cancelAction);

    }

    public void setCloseOnCancel(boolean closeOnCancel) {
        this.closeOnCancel = closeOnCancel;
    }

    public void setCloseDialog() {
        okButton.setVisible(false);
        cancelButton.setText(LocaleManager.getString("closeAction"));
    }

    public void addAdditionalActions(Action... actions) {
        for (Action action : actions) {
            additionalButtonPanel.add(new JButton(action));
        }
    }

    public void addOkActionChecker(Checker checker) {
        listenerList.add(Checker.class, checker);
    }

    public void setBorder(Border border) {
        dialogPanel.setBorder(border);
    }

    public Action getCancelAction() {
        return cancelAction;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    protected boolean checkOkAction() {
        final Checker[] checkers = listenerList.getListeners(Checker.class);
        for (Checker checker : checkers) {
            if (!checker.check()) {
                return false;
            }
        }
        return true;
    }

    public void setCancelAction(Action cancelAction) {
        if (cancelAction.getValue(Action.NAME) == null) {
            cancelAction.putValue(Action.NAME, LocaleManager.getString("cancelAction"));
            cancelAction.putValue(Action.SMALL_ICON, IconFactory.getInstance().getIcon("cancel"));
        }
        cancelButton.setAction(cancelAction);
    }

    public Action getOkAction() {
        return okAction;
    }

    public void setOkAction(Action okAction) {
        if (okAction.getValue(Action.NAME) == null) {
            okAction.putValue(Action.NAME, LocaleManager.getString("okAction"));
            okAction.putValue(Action.SMALL_ICON, IconFactory.getInstance().getIcon("ok"));
        }
        okButton.setAction(okAction);
    }

    public Component getMainPanel() {
        return mainPanel;
    }

    public int getResultAction() {
        return resultAction;
    }

    public void setMainPanel(Component component) {
        dialogPanel.remove(this.mainPanel);
        this.mainPanel = component;
        dialogPanel.add(component, BorderLayout.CENTER);
        validate();
        repaint();
    }
}
