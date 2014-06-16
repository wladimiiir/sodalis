
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

import sk.magiksoft.swing.event.PopupTextFieldListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author wladimiiir
 */
public class PopupTextField<T> extends JTextField {
    private static final Comparator COMPARATOR = new Comparator() {

        @Override
        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };

    protected JPopupMenu popupMenu = new JPopupMenu();
    private List<T> popupItems = new ArrayList<T>();
    private DefaultListModel model = new DefaultListModel();
    private JList lstObjects = new JList();
    private List<PopupTextFieldListener> listeners = new ArrayList<PopupTextFieldListener>();
    private boolean adjusting = false;
    private boolean whitespaceCheck = true;
    private boolean caseSensitive = false;
    private T selectedItem;

    public PopupTextField() {
        super(15);
        initPopupMenu();
        initListeners();
    }

    public PopupTextField(List<T> popupItems) {
        super(15);
        Collections.sort(this.popupItems = popupItems, COMPARATOR);
        initPopupMenu();
        initListeners();
    }

    private void fireItemChanged(T item) {
        for (PopupTextFieldListener listener : listeners) {
            listener.itemChanged(item);
        }
    }

    private void initListeners() {
        this.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                if (adjusting) {
                    return;
                }
                processDocumentUpdate(e);
            }

            public void removeUpdate(DocumentEvent e) {
                if (adjusting) {
                    return;
                }
                processDocumentUpdate(e);

            }

            public void changedUpdate(DocumentEvent e) {
                if (adjusting) {
                    return;
                }
                processDocumentUpdate(e);
            }
        });

        lstObjects.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                enterAction();
            }
        });
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upAction();
                return;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downAction();
                return;
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                enterAction();
                return;
            }
        }
        super.processKeyEvent(e);
    }

    private void processDocumentUpdate(DocumentEvent e) {
        Object obj = lstObjects.getSelectedValue();
        int row = lstObjects.getSelectedIndex();
        initShownObjects();
        if (model.getSize() == 0 && popupMenu.isShowing()) {
            popupMenu.setVisible(false);
            return;
        }
        selectedItem = null;
        if (row > (model.getSize() - 1)) {
            lstObjects.setSelectedIndex(model.getSize() - 1);
        } else if (row < 0 || model.indexOf(obj) == -1) {
            lstObjects.setSelectedIndex(0);
        } else {
            lstObjects.setSelectedValue(obj, true);
        }
        if (e.getDocument().getLength() > 1 && !popupMenu.isShowing()) {
            showPopupMenu();
            lstObjects.setSelectedIndex(0);
            requestFocus();
        }
    }

    private void enterAction() {
        if (popupMenu.isShowing()) {
            int row = lstObjects.getSelectedIndex();
            if (row > -1) {
                selectedItem = (T) model.get(row);
                setText(selectedItem.toString());
                fireItemChanged(selectedItem);
            }
            popupMenu.setVisible(false);
        }
    }

    private void upAction() {
        if (!popupMenu.isShowing()) {
            showPopupMenu();
        }
        int row = lstObjects.getSelectedIndex();
        lstObjects.setSelectedIndex((row - 1) < 0 ? 0 : (row - 1));
        requestFocus();
    }

    private void downAction() {
        if (!popupMenu.isShowing()) {
            showPopupMenu();
        }
        int row = lstObjects.getSelectedIndex();
        lstObjects.setSelectedIndex(row + 1);
        requestFocus();
    }

    private void initPopupMenu() {
        JScrollPane spnObjects = new JScrollPane(lstObjects);

        spnObjects.setBorder(null);
        lstObjects.setFocusable(false);
        lstObjects.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        lstObjects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        popupMenu.setBorder(null);
        lstObjects.setModel(model);
        popupMenu.setLayout(new BorderLayout());
        popupMenu.add(spnObjects, BorderLayout.CENTER);
    }

    private void showPopupMenu() {
        initShownObjects();
        if (model.getSize() == 0) {
            return;
        }
        popupMenu.show(this, 0, getHeight());
        popupMenu.setPopupSize(new Dimension(getWidth(), 150));
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    private void initShownObjects() {
        String regex = getText()
                .replaceAll("[(]", "[(]").replaceAll("[)]", "[)]")
                .replaceAll("\\*", "(.)*").replaceAll("\\.", "\\.");
        String itemString;

        if (!caseSensitive) {
            regex = regex.toLowerCase();
        }
        model.removeAllElements();
        regex = regex.trim() + "(.)*";
        for (T item : popupItems) {
            itemString = item.toString();
            if (!caseSensitive) {
                itemString = itemString.toLowerCase();
            }
            if (Pattern.matches(regex, itemString)
                    || (whitespaceCheck && Pattern.matches("(.)*\\s" + regex, itemString))) {
                model.addElement(item);
            }
        }
    }

    public void addItem(T item) {
        if (popupItems.contains(item)) {
            return;
        }
        popupItems.add(item);
    }

    public T getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem = selectedItem;
        setText(selectedItem == null ? "" : selectedItem.toString());
        fireItemChanged(selectedItem);
    }

    public void setPopupObjects(List<T> popupItems) {
        Collections.sort(this.popupItems = popupItems, COMPARATOR);
    }

    public List<T> getPopupObjects() {
        return popupItems;
    }

    public void addPopupTextFieldListener(PopupTextFieldListener listener) {
        listeners.add(listener);
    }

    public void removePopupTextFieldListener(PopupTextFieldListener listener) {
        listeners.remove(listener);
    }

    public boolean isWhitespaceCheck() {
        return whitespaceCheck;
    }

    /**
     * Defines if while finding a matching entity check is made after each whitespace.
     * <p/>
     * Example: if value is true entity is "John Doe" and string "Doe" is entered entity "John Doe" will be
     * seen as a result of search.
     *
     * @param whitespaceCheck
     */
    public void setWhitespaceCheck(boolean whitespaceCheck) {
        this.whitespaceCheck = whitespaceCheck;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }


    @Override
    public void setText(String t) {
        adjusting = true;
        super.setText(t);
        adjusting = false;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("sodalis");
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.add(new PopupTextField(Arrays.asList("jojo", "jeje", "joje manko", "joejje")));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}