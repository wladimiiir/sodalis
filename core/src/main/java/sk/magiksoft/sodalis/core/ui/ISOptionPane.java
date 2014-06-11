
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author wladimiiir
 */
public class ISOptionPane extends JOptionPane {

    /**
     * Shows a question-message dialog requesting input from the user. The
     * dialog uses the default frame, which usually means it is centered on
     * the screen.
     *
     * @param message the <code>Object</code> to display
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static String showInputDialog(Object message)
            throws HeadlessException {
        return showInputDialog(null, message);
    }

    /**
     * Shows a question-message dialog requesting input from the user, with
     * the input value initialized to <code>initialSelectionValue</code>. The
     * dialog uses the default frame, which usually means it is centered on
     * the screen.
     *
     * @param message               the <code>Object</code> to display
     * @param initialSelectionValue the value used to initialize the input
     *                              field
     * @since 1.4
     */
    public static String showInputDialog(Object message, Object initialSelectionValue) {
        return showInputDialog(null, message, initialSelectionValue);
    }

    /**
     * Shows a question-message dialog requesting input from the user
     * parented to <code>parentComponent</code>.
     * The dialog is displayed on top of the <code>Component</code>'s
     * frame, and is usually positioned below the <code>Component</code>.
     *
     * @param parentComponent the parent <code>Component</code> for the
     *                        dialog
     * @param message         the <code>Object</code> to display
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static String showInputDialog(Component parentComponent,
                                         Object message) throws HeadlessException {
        return showInputDialog(parentComponent, message, UIManager.getString(
                "OptionPane.inputDialogTitle"), QUESTION_MESSAGE);
    }

    /**
     * Shows a question-message dialog requesting input from the user and
     * parented to <code>parentComponent</code>. The input value will be
     * initialized to <code>initialSelectionValue</code>.
     * The dialog is displayed on top of the <code>Component</code>'s
     * frame, and is usually positioned below the <code>Component</code>.
     *
     * @param parentComponent       the parent <code>Component</code> for the
     *                              dialog
     * @param message               the <code>Object</code> to display
     * @param initialSelectionValue the value used to initialize the input
     *                              field
     * @since 1.4
     */
    public static String showInputDialog(Component parentComponent, Object message,
                                         Object initialSelectionValue) {
        return (String) showInputDialog(parentComponent, message,
                UIManager.getString("OptionPane.inputDialogTitle"), QUESTION_MESSAGE, null, null,
                initialSelectionValue);
    }

    /**
     * Shows a dialog requesting input from the user parented to
     * <code>parentComponent</code> with the dialog having the title
     * <code>title</code> and message type <code>messageType</code>.
     *
     * @param parentComponent the parent <code>Component</code> for the
     *                        dialog
     * @param message         the <code>Object</code> to display
     * @param title           the <code>String</code> to display in the dialog
     *                        title bar
     * @param messageType     the type of message that is to be displayed:
     *                        <code>ERROR_MESSAGE</code>,
     *                        <code>INFORMATION_MESSAGE</code>,
     *                        <code>WARNING_MESSAGE</code>,
     *                        <code>QUESTION_MESSAGE</code>,
     *                        or <code>PLAIN_MESSAGE</code>
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static String showInputDialog(Component parentComponent,
                                         Object message, String title, int messageType)
            throws HeadlessException {
        return (String) showInputDialog(parentComponent, message, title,
                messageType, null, null, null);
    }

    /**
     * Prompts the user for input in a blocking dialog where the
     * initial selection, possible selections, and all other options can
     * be specified. The user will able to choose from
     * <code>selectionValues</code>, where <code>null</code> implies the
     * user can input
     * whatever they wish, usually by means of a <code>JTextField</code>.
     * <code>initialSelectionValue</code> is the initial value to prompt
     * the user with. It is up to the UI to decide how best to represent
     * the <code>selectionValues</code>, but usually a
     * <code>JComboBox</code>, <code>JList</code>, or
     * <code>JTextField</code> will be used.
     *
     * @param parentComponent       the parent <code>Component</code> for the
     *                              dialog
     * @param message               the <code>Object</code> to display
     * @param title                 the <code>String</code> to display in the
     *                              dialog title bar
     * @param messageType           the type of message to be displayed:
     *                              <code>ERROR_MESSAGE</code>,
     *                              <code>INFORMATION_MESSAGE</code>,
     *                              <code>WARNING_MESSAGE</code>,
     *                              <code>QUESTION_MESSAGE</code>,
     *                              or <code>PLAIN_MESSAGE</code>
     * @param icon                  the <code>Icon</code> image to display
     * @param selectionValues       an array of <code>Object</code>s that
     *                              gives the possible selections
     * @param initialSelectionValue the value used to initialize the input
     *                              field
     * @return user's input, or <code>null</code> meaning the user
     *         canceled the input
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static Object showInputDialog(Component parentComponent,
                                         Object message, String title, int messageType, Icon icon,
                                         Object[] selectionValues, Object initialSelectionValue)
            throws HeadlessException {
        ISOptionPane pane = new ISOptionPane(message, messageType,
                OK_CANCEL_OPTION, icon,
                null, null);

        pane.setWantsInput(true);
        pane.setSelectionValues(selectionValues);
        pane.setInitialSelectionValue(initialSelectionValue);
        pane.setComponentOrientation(((parentComponent == null) ?
                getRootFrame() : parentComponent).getComponentOrientation());

        JDialog dialog = pane.createDialog(parentComponent, title);

        pane.selectInitialValue();
        dialog.show();
        dialog.dispose();

        Object value = pane.getInputValue();

        if (value == UNINITIALIZED_VALUE) {
            return null;
        }
        return value;
    }

    /**
     * Brings up an information-message dialog titled "Message".
     *
     * @param parentComponent determines the <code>Frame</code> in
     *                        which the dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no
     *                        <code>Frame</code>, a default <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static void showMessageDialog(Component parentComponent,
                                         Object message) throws HeadlessException {
        showMessageDialog(parentComponent, message, UIManager.getString(
                "OptionPane.messageDialogTitle"),
                INFORMATION_MESSAGE);
    }

    public static void showMessageDialog(Object message) throws HeadlessException {
        showMessageDialog(SodalisApplication.get().getMainFrame(), message, UIManager.getString(
                "OptionPane.messageDialogTitle"),
                INFORMATION_MESSAGE);
    }

    /**
     * Brings up a dialog that displays a message using a default
     * icon determined by the <code>messageType</code> parameter.
     *
     * @param parentComponent determines the <code>Frame</code>
     *                        in which the dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no
     *                        <code>Frame</code>, a default <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     * @param title           the title string for the dialog
     * @param messageType     the type of message to be displayed:
     *                        <code>ERROR_MESSAGE</code>,
     *                        <code>INFORMATION_MESSAGE</code>,
     *                        <code>WARNING_MESSAGE</code>,
     *                        <code>QUESTION_MESSAGE</code>,
     *                        or <code>PLAIN_MESSAGE</code>
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static void showMessageDialog(Component parentComponent,
                                         Object message, String title, int messageType)
            throws HeadlessException {
        showMessageDialog(parentComponent, message, title, messageType, null);
    }

    /**
     * Brings up a dialog displaying a message, specifying all parameters.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     *                        dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no
     *                        <code>Frame</code>, a
     *                        default <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     * @param title           the title string for the dialog
     * @param messageType     the type of message to be displayed:
     *                        <code>ERROR_MESSAGE</code>,
     *                        <code>INFORMATION_MESSAGE</code>,
     *                        <code>WARNING_MESSAGE</code>,
     *                        <code>QUESTION_MESSAGE</code>,
     *                        or <code>PLAIN_MESSAGE</code>
     * @param icon            an icon to display in the dialog that helps the user
     *                        identify the kind of message that is being displayed
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static void showMessageDialog(Component parentComponent,
                                         Object message, String title, int messageType, Icon icon)
            throws HeadlessException {
        showOptionDialog(parentComponent, message, title, DEFAULT_OPTION,
                messageType, icon, null, null);
    }

    /**
     * Brings up a dialog with the options <i>Yes</i>,
     * <i>No</i> and <i>Cancel</i>; with the
     * title, <b>Select an Option</b>.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     *                        dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no
     *                        <code>Frame</code>, a
     *                        default <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     * @return an integer indicating the option selected by the user
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Component parentComponent,
                                        Object message) throws HeadlessException {
        return showConfirmDialog(parentComponent, message,
                UIManager.getString("OptionPane.titleText"),
                YES_NO_CANCEL_OPTION);
    }

    /**
     * Brings up a dialog where the number of choices is determined
     * by the <code>optionType</code> parameter.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     *                        dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no
     *                        <code>Frame</code>, a
     *                        default <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     * @param title           the title string for the dialog
     * @param optionType      an int designating the options available on the dialog:
     *                        <code>YES_NO_OPTION</code>,
     *                        <code>YES_NO_CANCEL_OPTION</code>,
     *                        or <code>OK_CANCEL_OPTION</code>
     * @return an int indicating the option selected by the user
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Component parentComponent,
                                        Object message, String title, int optionType)
            throws HeadlessException {
        return showConfirmDialog(parentComponent, message, title, optionType,
                QUESTION_MESSAGE);
    }

    /**
     * Brings up a dialog where the number of choices is determined
     * by the <code>optionType</code> parameter, where the
     * <code>messageType</code>
     * parameter determines the icon to display.
     * The <code>messageType</code> parameter is primarily used to supply
     * a default icon from the Look and Feel.
     *
     * @param parentComponent determines the <code>Frame</code> in
     *                        which the dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no
     *                        <code>Frame</code>, a
     *                        default <code>Frame</code> is used.
     * @param message         the <code>Object</code> to display
     * @param title           the title string for the dialog
     * @param optionType      an integer designating the options available
     *                        on the dialog: <code>YES_NO_OPTION</code>,
     *                        <code>YES_NO_CANCEL_OPTION</code>,
     *                        or <code>OK_CANCEL_OPTION</code>
     * @param messageType     an integer designating the kind of message this is;
     *                        primarily used to determine the icon from the pluggable
     *                        Look and Feel: <code>ERROR_MESSAGE</code>,
     *                        <code>INFORMATION_MESSAGE</code>,
     *                        <code>WARNING_MESSAGE</code>,
     *                        <code>QUESTION_MESSAGE</code>,
     *                        or <code>PLAIN_MESSAGE</code>
     * @return an integer indicating the option selected by the user
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Component parentComponent,
                                        Object message, String title, int optionType, int messageType)
            throws HeadlessException {
        return showConfirmDialog(parentComponent, message, title, optionType,
                messageType, null);
    }

    /**
     * Brings up a dialog with a specified icon, where the number of
     * choices is determined by the <code>optionType</code> parameter.
     * The <code>messageType</code> parameter is primarily used to supply
     * a default icon from the look and feel.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     *                        dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no
     *                        <code>Frame</code>, a
     *                        default <code>Frame</code> is used
     * @param message         the Object to display
     * @param title           the title string for the dialog
     * @param optionType      an int designating the options available on the dialog:
     *                        <code>YES_NO_OPTION</code>,
     *                        <code>YES_NO_CANCEL_OPTION</code>,
     *                        or <code>OK_CANCEL_OPTION</code>
     * @param messageType     an int designating the kind of message this is,
     *                        primarily used to determine the icon from the pluggable
     *                        Look and Feel: <code>ERROR_MESSAGE</code>,
     *                        <code>INFORMATION_MESSAGE</code>,
     *                        <code>WARNING_MESSAGE</code>,
     *                        <code>QUESTION_MESSAGE</code>,
     *                        or <code>PLAIN_MESSAGE</code>
     * @param icon            the icon to display in the dialog
     * @return an int indicating the option selected by the user
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Component parentComponent,
                                        Object message, String title, int optionType,
                                        int messageType, Icon icon) throws HeadlessException {
        return showOptionDialog(parentComponent, message, title, optionType,
                messageType, icon, null, null);
    }

    /**
     * Brings up a dialog with a specified icon, where the initial
     * choice is determined by the <code>initialValue</code> parameter and
     * the number of choices is determined by the <code>optionType</code>
     * parameter.
     * <p/>
     * If <code>optionType</code> is <code>YES_NO_OPTION</code>,
     * or <code>YES_NO_CANCEL_OPTION</code>
     * and the <code>options</code> parameter is <code>null</code>,
     * then the options are
     * supplied by the look and feel.
     * <p/>
     * The <code>messageType</code> parameter is primarily used to supply
     * a default icon from the look and feel.
     *
     * @param parentComponent determines the <code>Frame</code>
     *                        in which the dialog is displayed;  if
     *                        <code>null</code>, or if the
     *                        <code>parentComponent</code> has no
     *                        <code>Frame</code>, a
     *                        default <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     * @param title           the title string for the dialog
     * @param optionType      an integer designating the options available on the
     *                        dialog: <code>DEFAULT_OPTION</code>,
     *                        <code>YES_NO_OPTION</code>,
     *                        <code>YES_NO_CANCEL_OPTION</code>,
     *                        or <code>OK_CANCEL_OPTION</code>
     * @param messageType     an integer designating the kind of message this is,
     *                        primarily used to determine the icon from the
     *                        pluggable Look and Feel: <code>ERROR_MESSAGE</code>,
     *                        <code>INFORMATION_MESSAGE</code>,
     *                        <code>WARNING_MESSAGE</code>,
     *                        <code>QUESTION_MESSAGE</code>,
     *                        or <code>PLAIN_MESSAGE</code>
     * @param icon            the icon to display in the dialog
     * @param options         an array of objects indicating the possible choices
     *                        the user can make; if the objects are components, they
     *                        are rendered properly; non-<code>String</code>
     *                        objects are
     *                        rendered using their <code>toString</code> methods;
     *                        if this parameter is <code>null</code>,
     *                        the options are determined by the Look and Feel
     * @param initialValue    the entity that represents the default selection
     *                        for the dialog; only meaningful if <code>options</code>
     *                        is used; can be <code>null</code>
     * @return an integer indicating the option chosen by the user,
     *         or <code>CLOSED_OPTION</code> if the user closed
     *         the dialog
     * @throws HeadlessException if
     *                           <code>GraphicsEnvironment.isHeadless</code> returns
     *                           <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showOptionDialog(Component parentComponent,
                                       Object message, String title, int optionType, int messageType,
                                       Icon icon, Object[] options, Object initialValue)
            throws HeadlessException {
        ISOptionPane pane = new ISOptionPane(message, messageType,
                optionType, icon,
                options, initialValue);

        pane.setInitialValue(initialValue);
        pane.setComponentOrientation(((parentComponent == null) ?
                getRootFrame() : parentComponent).getComponentOrientation());

        JDialog dialog = pane.createDialog(parentComponent, title);

        pane.selectInitialValue();
        dialog.show();
        dialog.dispose();

        Object selectedValue = pane.getValue();

        if (selectedValue == null) {
            return CLOSED_OPTION;
        }
        if (options == null) {
            if (selectedValue instanceof Integer) {
                return ((Integer) selectedValue).intValue();
            }
            return CLOSED_OPTION;
        }
        for (int counter = 0, maxCounter = options.length;
             counter < maxCounter; counter++) {
            if (options[counter].equals(selectedValue)) {
                return counter;
            }
        }
        return CLOSED_OPTION;
    }

    public ISOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        super(message, messageType, optionType, icon, options, initialValue);
    }

    public ISOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options) {
        super(message, messageType, optionType, icon, options);
    }

    public ISOptionPane(Object message, int messageType, int optionType, Icon icon) {
        super(message, messageType, optionType, icon);
    }

    public ISOptionPane(Object message, int messageType, int optionType) {
        super(message, messageType, optionType);
    }

    public ISOptionPane(Object message, int messageType) {
        super(message, messageType);
    }

    public ISOptionPane(Object message) {
        super(message);
    }

    public ISOptionPane() {
    }


    @Override
    public JDialog createDialog(Component parentComponent, String title) throws HeadlessException {
        final JDialog dialog;

        dialog = new JDialog(SodalisApplication.get().getMainFrame(), title, true);
        initDialog(dialog, styleFromMessageType(getMessageType()), parentComponent);

        return dialog;
    }

    @Override
    public JDialog createDialog(String title) throws HeadlessException {
        JDialog dialog = super.createDialog(title);

        UIUtils.makeISDialog(dialog);
        return dialog;
    }

    private void initDialog(final JDialog dialog, int style, Component parentComponent) {
        dialog.setComponentOrientation(this.getComponentOrientation());
        Container contentPane = dialog.getContentPane();

        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
        dialog.setResizable(false);
        if (JDialog.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations =
                    UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                dialog.setUndecorated(true);
                getRootPane().setWindowDecorationStyle(style);
            }
        }
        UIUtils.makeISDialog(dialog);
        dialog.pack();
        dialog.setLocationRelativeTo(parentComponent);
        WindowAdapter adapter = new WindowAdapter() {
            private boolean gotFocus = false;

            @Override
            public void windowClosing(WindowEvent we) {
                setValue(null);
            }

            @Override
            public void windowGainedFocus(WindowEvent we) {
                // Once window gets focus, set initial focus
                if (!gotFocus) {
                    selectInitialValue();
                    gotFocus = true;
                }
            }
        };
        dialog.addWindowListener(adapter);
        dialog.addWindowFocusListener(adapter);
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                // reset value to ensure closing works properly
                setValue(ISOptionPane.UNINITIALIZED_VALUE);
            }
        });
        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                // Let the defaultCloseOperation handle the closing
                // if the user closed the window without selecting a button
                // (newValue = null in that case).  Otherwise, close the dialog.
                if (dialog.isVisible() && event.getSource() == ISOptionPane.this &&
                        (event.getPropertyName().equals(VALUE_PROPERTY)) &&
                        event.getNewValue() != null &&
                        event.getNewValue() != ISOptionPane.UNINITIALIZED_VALUE) {
                    dialog.setVisible(false);
                }
            }
        });
    }

    private static int styleFromMessageType(int messageType) {
        switch (messageType) {
            case ERROR_MESSAGE:
                return JRootPane.ERROR_DIALOG;
            case QUESTION_MESSAGE:
                return JRootPane.QUESTION_DIALOG;
            case WARNING_MESSAGE:
                return JRootPane.WARNING_DIALOG;
            case INFORMATION_MESSAGE:
                return JRootPane.INFORMATION_DIALOG;
            case PLAIN_MESSAGE:
            default:
                return JRootPane.PLAIN_DIALOG;
        }
    }

}