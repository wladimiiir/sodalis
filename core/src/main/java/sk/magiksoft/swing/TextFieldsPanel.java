
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

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

/**
 *
 * @author wladimiiir
 */
public class TextFieldsPanel extends ComponentsPanel{

    @Override
    protected Component createPanelComponent(Object value){
        JTextField textField = new JTextField();

        if(value!=null){
            textField.setText(getText(value));
        }
        textField.addActionListener(getActionListener());
        return textField;
    }

    protected String getText(Object value){
        return value.toString();
    }

    @Override
    public List getValues(){
        List values = new ArrayList();

        for (int i = 0; i < getPanelComponents().length-1; i++) {
            JTextField textField = (JTextField) getPanelComponents()[i];
            values.add(textField.getText());
        }

        return values;
    }

    @Override
    protected boolean isEmpty(Component component) {
        return ((JTextField)component).getText().trim().isEmpty();
    }

}