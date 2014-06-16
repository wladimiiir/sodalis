
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

import sk.magiksoft.sodalis.core.filter.action.Filter;
import sk.magiksoft.sodalis.core.filter.action.FilterObject;
import sk.magiksoft.sodalis.core.filter.action.FilterObjectListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.text.Collator;
import java.util.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class ListTextField<T> extends JTextField {

    private final ObjectComparator OBJECT_COMPARATOR = new ObjectComparator();
    private List<T> objectList;
    private List<T> sortedObjectList;
    private Filter<T> filter;
    private final Map<String, Integer> characterMap = new HashMap<String, Integer>();
    private boolean adjusting = false;
    private T currentObject;
    private JPopupMenu popupMenu;

    public ListTextField(List<T> objectList) {
        this(objectList, null);
    }

    public ListTextField(List<T> objectList, Filter<T> filter) {
        super(10);
        this.objectList = new ArrayList<T>(objectList);
        this.filter = filter;
        init();
    }

    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    private void fireStateChanged() {
        final ChangeEvent event = new ChangeEvent(this);
        final ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
        for (ChangeListener listener : listeners) {
            listener.stateChanged(event);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!popupMenu.isShowing()) {
                    popupMenu.show(ListTextField.this, 0, getHeight() + 1);
                } else {
                    popupMenu.repaint();
                }
            }
        });
    }

    private void initCharacterMap() {
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                synchronized (characterMap) {
                    characterMap.clear();
                    sortedObjectList = new ArrayList<T>(filter != null ? filter.filter(objectList) : objectList);
                    Collections.sort(sortedObjectList, OBJECT_COMPARATOR);
                    for (int i = 0; i < sortedObjectList.size(); i++) {
                        Object object = sortedObjectList.get(i);
                        if (object.toString().isEmpty() || characterMap.containsKey(String.valueOf(object.toString().charAt(0)).toUpperCase())) {
                            continue;
                        }

                        characterMap.put(String.valueOf(object.toString().charAt(0)).toUpperCase(), i);
                    }
                }
                return null;
            }
        }.execute();

    }

    private void init() {
        if (filter != null) {
            filter.addFilterObjectListener(new FilterObjectListener() {
                @Override
                public void filterObjectChanged(FilterObject filterObject) {
                    initCharacterMap();
                }
            });
        }

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                selectAll();
                initCharacterMap();
            }
        });

        getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (adjusting) {
                    return;
                }
                new SwingWorker<Void, String>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        String text = getText().toUpperCase();
                        int i;

                        if (text.isEmpty()) {
                            return null;
                        }
                        synchronized (characterMap) {
                            i = !characterMap.containsKey(String.valueOf(text.charAt(0)).toUpperCase())
                                    ? 0
                                    : characterMap.get(String.valueOf(text.charAt(0)).toUpperCase());

                            for (; i < sortedObjectList.size(); i++) {
                                T object = sortedObjectList.get(i);
                                if (object.toString().toUpperCase().startsWith(text)) {
                                    currentObject = object;
                                    process(Arrays.asList(object.toString()));
                                    fireStateChanged();
                                    return null;
                                }
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void process(List<String> chunks) {
                        synchronized (getDocument()) {
                            adjusting = true;

                            final String text = getText();
                            setText(text + chunks.get(0).substring(text.length()));
                            select(text.length(), chunks.get(0).length());
                            adjusting = false;
                        }
                    }
                }.execute();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        popupMenu = new JPopupMenu() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(ListTextField.this.getWidth(), 60);
            }
        };
        popupMenu.setLayout(new BorderLayout());
        popupMenu.setFocusable(false);
        popupMenu.add(new ObjectListPanel(), BorderLayout.CENTER);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (popupMenu.isShowing()) {
                    popupMenu.setVisible(false);
                }
            }
        });
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        if (e.getID() != KeyEvent.KEY_PRESSED) {
            super.processKeyEvent(e);
        } else if (getSelectionEnd() != getSelectionStart() && e.getKeyCode() == KeyEvent.VK_ENTER) {
            setSelectionStart(getSelectionEnd());
            popupMenu.setVisible(false);
            super.processKeyEvent(e);
        } else if (currentObject == null) {
            super.processKeyEvent(e);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            int index = sortedObjectList.indexOf(currentObject);

            if (index == -1 || ++index > sortedObjectList.size() - 1) {
                return;
            }
            currentObject = sortedObjectList.get(index);
            fireStateChanged();
            synchronized (getDocument()) {
                int selectionStart = getSelectionStart();
                setText(currentObject.toString());
                select(selectionStart, currentObject.toString().length());
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            int index = sortedObjectList.indexOf(currentObject);

            if (index == -1 || --index < 0) {
                return;
            }
            currentObject = sortedObjectList.get(index);
            fireStateChanged();
            synchronized (getDocument()) {
                int selectionStart = getSelectionStart();
                setText(currentObject.toString());
                select(selectionStart, currentObject.toString().length());
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            popupMenu.setVisible(false);
            super.processKeyEvent(e);
        } else {
            super.processKeyEvent(e);
        }
    }

    public void addItem(T item) {
        objectList.add(item);
        initCharacterMap();
    }

    public void setItems(List<T> items) {
        objectList = new ArrayList<T>(items);
        initCharacterMap();
    }

    public T getCurrentObject() {
        return currentObject;
    }

    private class ObjectComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            return Collator.getInstance().compare(o1.toString(), o2.toString());
        }
    }

    private class ObjectListPanel extends JComponent {
        private ObjectListPanel() {
            setFocusable(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (currentObject == null) {
                return;
            }

            int currentIndex = sortedObjectList.indexOf(currentObject);

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(172, 200, 255));
            g.fillRect(0, getHeight() / 3, getWidth(), getHeight() / 3);
            g.setColor(Color.BLACK);
            if (currentIndex > 0) {
                g.drawString(sortedObjectList.get(currentIndex - 1).toString(), 3, getHeight() / 3 - 3);
            }
            g.drawString(sortedObjectList.get(currentIndex).toString(), 3, getHeight() / 3 * 2 - 3);
            if (currentIndex < sortedObjectList.size() - 1) {
                g.drawString(sortedObjectList.get(currentIndex + 1).toString(), 3, getHeight() - 3);
            }
        }
    }

    @Override
    public void setText(String t) {
        adjusting = true;
        super.setText(t);
        adjusting = false;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        List<String> oList = new ArrayList<String>();
        oList.add("Bicykel");
        oList.add("Motorka");
        oList.add("Motorovy cln");
        oList.add("Auto");
        oList.add("Fretka");

        frame.setLayout(new FlowLayout());
        frame.add(new ListTextField(oList));
        frame.add(new JTextField());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}