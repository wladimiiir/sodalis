
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

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicBorders.MarginBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wladimiiir
 */
public class MultiSelectComboBox extends JComponent {

    private JTextField textField = new JTextField(10) {

        @Override
        public boolean isEditable() {
            return MultiSelectComboBox.this.isEditable();
        }
    };
    private JPopupMenu popupMenu;
    private JButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE) {

        @Override
        public void paint(Graphics g) {
            int w, h, size;

            w = getSize().width;
            h = getSize().height;

            g.setColor(getBackground());
            g.fillRect(1, 1, w - 2, h - 2);
            size = Math.min((h - 4) / 3, (w - 4) / 3);
            size = Math.max(size, 2);
            paintTriangle(g, (w - size) / 2, (h - size) / 2,
                    size, direction, isEnabled());

            paintBorder(g);
        }
    };
    private ListModel model;
    private JList list;
    private JScrollPane scrollPane;
    private int[] selected = new int[0];
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean editable = false;

    public MultiSelectComboBox() {
        this(new DefaultListModel());
    }

    public MultiSelectComboBox(ListModel model) {
        this.model = model;
        initComponents();
        initListeners();
    }

    public MultiSelectComboBox(List items) {
        this();
        for (Object object : items) {
            addItem(object);
        }
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    private void fireChangeEvent() {
        ChangeEvent event = new ChangeEvent(this);

        for (ChangeListener changeListener : listeners) {
            changeListener.stateChanged(event);
        }
    }

    private void initComponents() {
        final GridBagConstraints c = new GridBagConstraints();

        setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add(textField, c);
        c.gridx++;
        c.weightx = 0.0;
        add(arrowButton, c);


        textField.setBackground(Color.WHITE);
        setBackground(Color.WHITE);
        arrowButton.setFocusPainted(false);
        arrowButton.setOpaque(false);
        textField.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 0, Color.GRAY), new MarginBorder()));
        arrowButton.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.GRAY));
        list = new JList(model);
        list.setSelectionModel(new DefaultListSelectionModel(){
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                } else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });
        list.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, value, index, isSelected, false);
            }
        });
        scrollPane = new JScrollPane(list);
        list.setFocusable(false);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        popupMenu = new JPopupMenu(){
            @Override public Dimension getPreferredSize() {
                int height = model.getSize() * (int) ((list.getCellBounds(0, 0) == null) ? 0 : list.getCellBounds(0, 0).getHeight()) + 6;
                if (height > 150) {
                    height = 155;
                }
                return new Dimension(MultiSelectComboBox.this.getWidth(), height);
            }
        };
        list.setBorder(null);
        scrollPane.setBorder(null);
        popupMenu.setLayout(new BorderLayout());
        popupMenu.add(scrollPane, BorderLayout.CENTER);
        popupMenu.setBorder(null);
    }

    private void initListeners() {
        arrowButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isEnabled()) {
                    return;
                }
                if (popupMenu.isShowing()) {
                    hidePopup();
                } else {
                    showPopup();
                }
            }
        });
        textField.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isEnabled() || isEditable()) {
                    return;
                }
                if (popupMenu.isShowing()) {
                    hidePopup();
                } else {
                    showPopup();
                }
            }
        });
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override public void valueChanged(ListSelectionEvent e) {
                refreshTextFieldText();
                fireChangeEvent();
            }
        });
        textField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) && popupMenu.isShowing()) {
                    hidePopup();
                }
            }
        });
    }

    private void refreshTextFieldText() {
        final StringBuilder text = new StringBuilder();
        final StringBuilder tooltipText = new StringBuilder();
        int index = 0;

        for (int i = 0; i < model.getSize(); i++) {
            if (!list.isSelectedIndex(i)) {
                continue;
            }
            if (text.length() > 0) {
                text.append(", ");
                tooltipText.append(", ");
            }
            if (index > 0 && index % 5 == 0) {
                tooltipText.append("<br/>");
            }
            text.append(model.getElementAt(i).toString());
            tooltipText.append(model.getElementAt(i).toString());
            index++;
        }
        tooltipText.insert(0, "<html>").append("</html>");
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                textField.setText(text.toString());
                textField.setCaretPosition(0);
                textField.setToolTipText(tooltipText.toString());
            }
        });
    }

    protected void showPopup() {
        popupMenu.show(MultiSelectComboBox.this, 0, getHeight()-1);
    }

    protected void hidePopup() {
        popupMenu.setVisible(false);
    }

    public void addItem(Object item) {
        if(model instanceof DefaultListModel){
            ((DefaultListModel) model).addElement(item);
        }
    }

    public void removeItem(Object item) {
        if(model instanceof DefaultListModel){
            ((DefaultListModel) model).removeElement(item);
        }
    }

    public void removeAllItems() {
        if(model instanceof DefaultListModel){
            ((DefaultListModel) model).removeAllElements();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textField.setEnabled(enabled);
        arrowButton.setEnabled(enabled);
    }

    public List getSelectedObjects() {
        return Arrays.asList(list.getSelectedValues());
    }

    public String getEditorText() {
        return textField.getText();
    }

    public void setSelectedObjects(List objects) {
        list.clearSelection();
        for (Object object : objects) {
            int index = indexOf(object);
            if (index < 0) {
                continue;
            }
            list.addSelectionInterval(index, index);
        }
        selected = list.getSelectedIndices();
        refreshTextFieldText();
    }

    private int indexOf(Object object){
        for (int index = 0; index < model.getSize(); index++) {
            if(model.getElementAt(index).equals(object)){
                return index;
            }
        }

        return -1;
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
        final MultiSelectComboBox comp = new MultiSelectComboBox(oList);

        comp.setSelectedObjects(Arrays.asList("Bicykel", "Fretka"));
        frame.add(comp);
        frame.add(new JTextField(10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

