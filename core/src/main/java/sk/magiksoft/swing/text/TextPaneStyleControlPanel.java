
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.swing.text;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author wladimiiir
 */
public class TextPaneStyleControlPanel extends JPanel implements DocumentListener {

    private static final Integer[] FONT_SIZES = {
            8, 10, 11, 12, 13, 14, 16, 18, 20, 22, 24, 26, 30, 35, 40, 45, 50
    };
    private JComboBox fontNameComboBox;
    private JComboBox fontSizeComboBox;
    private JToggleButton boldButton;
    private JToggleButton italicButton;
    private JToggleButton underlineButton;
    private JToggleButton leftAlignmentButton;
    private JToggleButton rightAlignmentButton;
    private JToggleButton centerAlignmentButton;
    private JToggleButton justifiedAlignmentButton;
    private JTextPane textPane;
    private DefaultStyledDocument doc;
    private MutableAttributeSet currentAttributes;
    private boolean adjusting = false;

    public TextPaneStyleControlPanel(JTextPane textPane) {
        this.textPane = textPane;
        if (textPane.getDocument() instanceof StyledDocument) {
            this.doc = (DefaultStyledDocument) textPane.getDocument();
        } else {
            textPane.setDocument(doc = new DefaultStyledDocument());
        }
        initComponents();
        initListeners();
    }

    private void initComponents() {
        GridBagConstraints c = new GridBagConstraints();
        ButtonGroup group = new ButtonGroup();

        boldButton = new JToggleButton("B");
        boldButton.setPreferredSize(new Dimension(22, 22));
        boldButton.setFocusPainted(false);
        boldButton.setFocusable(false);
        italicButton = new JToggleButton("I");
        italicButton.setPreferredSize(new Dimension(22, 22));
        italicButton.setFocusPainted(false);
        italicButton.setFocusable(false);
        underlineButton = new JToggleButton("U");
        underlineButton.setPreferredSize(new Dimension(22, 22));
        underlineButton.setFocusPainted(false);
        underlineButton.setFocusable(false);
        fontNameComboBox = new JComboBox(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontNameComboBox.setPreferredSize(new Dimension(150, 22));
        fontNameComboBox.setFocusable(false);
        fontSizeComboBox = new JComboBox(FONT_SIZES);
        fontSizeComboBox.setFocusable(false);
        fontSizeComboBox.setEditable(true);
        fontSizeComboBox.getEditor().getEditorComponent().setFocusable(true);
        fontSizeComboBox.setPreferredSize(new Dimension(40, 22));
        leftAlignmentButton = new JToggleButton(IconFactory.getInstance().getIcon("leftAlign"));
        leftAlignmentButton.setFocusable(false);
        leftAlignmentButton.setPreferredSize(new Dimension(22, 22));
        rightAlignmentButton = new JToggleButton(IconFactory.getInstance().getIcon("rightAlign"));
        rightAlignmentButton.setFocusable(false);
        rightAlignmentButton.setPreferredSize(new Dimension(22, 22));
        centerAlignmentButton = new JToggleButton(IconFactory.getInstance().getIcon("centerAlign"));
        centerAlignmentButton.setFocusable(false);
        centerAlignmentButton.setPreferredSize(new Dimension(22, 22));
        justifiedAlignmentButton = new JToggleButton("J");
        justifiedAlignmentButton.setFocusable(false);
        justifiedAlignmentButton.setPreferredSize(new Dimension(22, 22));
        group.add(leftAlignmentButton);
        group.add(rightAlignmentButton);
        group.add(centerAlignmentButton);
        group.add(justifiedAlignmentButton);

        setLayout(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        add(fontNameComboBox, c);
        c.gridx++;
        add(fontSizeComboBox, c);
        c.gridx++;
        add(boldButton, c);
        c.gridx++;
        add(italicButton, c);
        c.gridx++;
        add(underlineButton, c);
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(10, 22));

        c.gridx++;
        add(panel, c);
        c.gridx++;
        add(leftAlignmentButton, c);
        c.gridx++;
        add(centerAlignmentButton, c);
        c.gridx++;
        add(rightAlignmentButton, c);
//        c.gridx++;
//        add(justifiedAlignmentButton, c);
        c.gridx++;
        c.weightx = 1.0;
        add(new JPanel(), c);

        setCurrentAttributes(doc.getCharacterElement(0).getAttributes());
    }

    private void initListeners() {
        textPane.addCaretListener(new CaretListener() {

            @Override
            public void caretUpdate(CaretEvent e) {
                if (adjusting) {
                    return;
                }
                int offset = e.getMark();

                if (--offset < 0) {
                    offset++;
                }
                setCurrentAttributes(doc.getCharacterElement(offset).getAttributes());
            }
        });

        doc.addDocumentListener(this);
        textPane.addPropertyChangeListener("document", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            }
        });
        boldButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (adjusting) {
                    return;
                }
                setBold(boldButton.isSelected(),
                        textPane.getSelectionStart(),
                        textPane.getSelectionEnd() - textPane.getSelectionStart());
                StyleConstants.setBold(currentAttributes, boldButton.isSelected());
                doc.setCharacterAttributes(doc.getLength(), 1, currentAttributes, true);
            }
        });
        italicButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (adjusting) {
                    return;
                }
                setItalic(italicButton.isSelected(),
                        textPane.getSelectionStart(),
                        textPane.getSelectionEnd() - textPane.getSelectionStart());
                StyleConstants.setItalic(currentAttributes, italicButton.isSelected());
                doc.setCharacterAttributes(doc.getLength(), 1, currentAttributes, true);
            }
        });
        underlineButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (adjusting) {
                    return;
                }
                setUnderline(underlineButton.isSelected(),
                        textPane.getSelectionStart(),
                        textPane.getSelectionEnd() - textPane.getSelectionStart());
                StyleConstants.setUnderline(currentAttributes, underlineButton.isSelected());
                doc.setCharacterAttributes(doc.getLength(), 1, currentAttributes, true);
            }
        });
        fontNameComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (adjusting) {
                    return;
                }
                setFontName(fontNameComboBox.getSelectedItem().toString(),
                        textPane.getSelectionStart(),
                        textPane.getSelectionEnd() - textPane.getSelectionStart());
                StyleConstants.setFontFamily(currentAttributes, fontNameComboBox.getSelectedItem().toString());
                doc.setCharacterAttributes(doc.getLength(), 1, currentAttributes, true);
            }
        });
        fontSizeComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                try {
                    if (adjusting) {
                        return;
                    }
                    setFontSize(Integer.valueOf(fontSizeComboBox.getSelectedItem().toString()),
                            textPane.getSelectionStart(),
                            textPane.getSelectionEnd() - textPane.getSelectionStart());
                    StyleConstants.setFontSize(currentAttributes, Integer.valueOf(fontSizeComboBox.getSelectedItem().toString()));
                    doc.setCharacterAttributes(doc.getLength(), 1, currentAttributes, true);
                } catch (Exception ex) {
                }
            }
        });
        leftAlignmentButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (adjusting) {
                    return;
                }
                setAlignment(StyleConstants.ALIGN_LEFT, textPane.getSelectionStart());
            }
        });
        rightAlignmentButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (adjusting) {
                    return;
                }
                setAlignment(StyleConstants.ALIGN_RIGHT, textPane.getSelectionStart());
            }
        });
        centerAlignmentButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (adjusting) {
                    return;
                }
                setAlignment(StyleConstants.ALIGN_CENTER, textPane.getSelectionStart());

            }
        });
        justifiedAlignmentButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (adjusting) {
                    return;
                }
                setAlignment(StyleConstants.ALIGN_JUSTIFIED, textPane.getSelectionStart());
            }
        });
    }

    private void setAlignment(int alignment, int pos) {
        MutableAttributeSet attrs = new SimpleAttributeSet(doc.getParagraphElement(pos).getAttributes());

        StyleConstants.setAlignment(attrs, alignment);
        doc.setParagraphAttributes(pos, 1, attrs, true);
    }

    private void setCurrentAttributes(AttributeSet currentAttributes) {
        adjusting = true;
        this.currentAttributes = new SimpleAttributeSet(currentAttributes);
        fontNameComboBox.setSelectedItem(StyleConstants.getFontFamily(currentAttributes));
        fontSizeComboBox.setSelectedItem(StyleConstants.getFontSize(currentAttributes));
        boldButton.setSelected(StyleConstants.isBold(currentAttributes));
        italicButton.setSelected(StyleConstants.isItalic(currentAttributes));
        underlineButton.setSelected(StyleConstants.isUnderline(currentAttributes));
        switch (StyleConstants.getAlignment(currentAttributes)) {
            case StyleConstants.ALIGN_LEFT:
                leftAlignmentButton.setSelected(true);
                break;
            case StyleConstants.ALIGN_RIGHT:
                rightAlignmentButton.setSelected(true);
                break;
            case StyleConstants.ALIGN_CENTER:
                centerAlignmentButton.setSelected(true);
                break;
            case StyleConstants.ALIGN_JUSTIFIED:
                justifiedAlignmentButton.setSelected(true);
                break;
        }
        adjusting = false;
    }

    public void setBold(boolean isBold, int offset, int length) {
        MutableAttributeSet attrs;

        for (int i = 0; i < length; i++) {
            attrs = new SimpleAttributeSet(doc.getCharacterElement(offset + i).getAttributes());
            StyleConstants.setBold(attrs, isBold);
            doc.setCharacterAttributes(offset + i, 1, attrs, true);
        }
    }

    public void setItalic(boolean isItalic, int offset, int length) {
        MutableAttributeSet attrs;

        for (int i = 0; i < length; i++) {
            attrs = new SimpleAttributeSet(doc.getCharacterElement(offset + i).getAttributes());
            StyleConstants.setItalic(attrs, isItalic);
            doc.setCharacterAttributes(offset + i, 1, attrs, true);
        }
    }

    public void setUnderline(boolean isUnderline, int offset, int length) {
        MutableAttributeSet attrs = (MutableAttributeSet) currentAttributes.copyAttributes();

//        for (int i = 0; i < length; i++) {
//            attrs = new SimpleAttributeSet(doc.getCharacterElement(offset + i).getAttributes());
//            StyleConstants.setUnderline(attrs, isUnderline);
//            doc.setCharacterAttributes(offset + i, 1, attrs, true);
//        }

        StyleConstants.setUnderline(attrs, isUnderline);
        String text;
        try {
            text = doc.getText(offset - 1, length + 1);
            doc.remove(offset - 1, length + 1);
            doc.insertString(offset - 1, text, attrs);
        } catch (BadLocationException ex) {
            LoggerManager.getInstance().error(getClass(), ex);
        }
    }

    public void setFontName(String fontName, int offset, int length) {
        MutableAttributeSet attrs;

        for (int i = 0; i < length; i++) {
            attrs = new SimpleAttributeSet(doc.getCharacterElement(offset + i).getAttributes());
            StyleConstants.setFontFamily(attrs, fontName);
            doc.setCharacterAttributes(offset + i, 1, attrs, true);
        }
    }

    public void setFontSize(int size, int offset, int length) {
        MutableAttributeSet attrs;

        for (int i = 0; i < length; i++) {
            attrs = new SimpleAttributeSet(doc.getCharacterElement(offset + i).getAttributes());
            StyleConstants.setFontSize(attrs, size);
            doc.setCharacterAttributes(offset + i, 1, attrs, true);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
            JFrame frame = new JFrame();
            JTextPane textPane = new JTextPane();
//            frame.setIconImage(ImageIO.read(new File("resources/icons/people.png")));
            frame.setTitle("sodalis");
            frame.setLayout(new BorderLayout());
            frame.add(textPane, BorderLayout.CENTER);
            frame.add(new TextPaneStyleControlPanel(textPane), BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
//        } catch (IOException ex) {
//            LoggerManager.getInstance().error(getClass(), ex);
        } catch (UnsupportedLookAndFeelException ex) {
            LoggerManager.getInstance().error(TextPaneStyleControlPanel.class, ex);
        }
    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
        adjusting = true;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                doc.setCharacterAttributes(e.getOffset(), 1, currentAttributes, true);
                adjusting = false;
            }
        });
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}