
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.settings.valuecomponent;

import javax.swing.*;

/**
 * @author wladimiiir
 */
public abstract class ValueComponent {
    protected JComponent component;

    public ValueComponent(JComponent component) {
        this.component = component;
    }

    public JComponent getComponent() {
        return component;
    }

    public abstract void setValue(Object value);

    public abstract Object getValue();

    public abstract void checkValue() throws WrongValueException;
}