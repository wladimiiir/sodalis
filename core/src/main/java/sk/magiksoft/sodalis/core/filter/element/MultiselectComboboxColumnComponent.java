
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.filter.element;

import sk.magiksoft.swing.MultiSelectComboBox;

import javax.swing.*;
import java.util.List;

/**
 *
 * @author wladimiiir
 */
public class MultiselectComboboxColumnComponent extends AbstractColumnComponent {

    protected MultiSelectComboBox component;

    public MultiselectComboboxColumnComponent() {
        component = new MultiSelectComboBox();
    }

    public MultiselectComboboxColumnComponent(List<? extends Object> items){
        component = new MultiSelectComboBox(items);
    }

    @Override
    protected String getWhereText() {
        StringBuilder whereText = new StringBuilder();

        for (Object object : component.getSelectedObjects()) {
            if (whereText.length() == 0) {
                whereText.append("(");
            } else {
                whereText.append(", ");
            }
            translateItem(whereText, object);
        }
        if (whereText.length() > 0) {
            whereText.append(")");
        }
        return whereText.toString();
    }

    protected void translateItem(StringBuilder where, Object item){
        if (item instanceof Enum) {
            where.append(((Enum) item).ordinal());
        } else {
            where.append("'").append(item).append("'");
        }
    }

    @Override
    public void addItem(Object item) {
        component.addItem(item);
    }

    @Override
    protected String getComparator() {
        return "IN";
    }

    @Override
    public JComponent getComponent() {
        return component;
    }

    @Override
    public boolean isIncluded() {
        for (Object object : component.getSelectedObjects()) {
            if (!object.toString().trim().isEmpty()) {
                return true;
            }

        }
        return false;
    }
}