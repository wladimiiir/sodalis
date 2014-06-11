
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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author wladimiiir
 */
public abstract class Validator {
    private static final Color INVALID_BACKGROUD = new Color(255, 208, 175);

    private Component component;
    private Color background;

    public Validator(final JTextField component) {
        this.component = component;
        this.background = component.getBackground();
        component.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                validate(component.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validate(component.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validate(component.getText());
            }
        });
    }

    protected Color getInvalidBackground(){
        return INVALID_BACKGROUD;
    }

    public void validate(Object object) {
        component.setBackground(isValid(object) ? background : getInvalidBackground());
    }

    public abstract boolean isValid(Object object);
}