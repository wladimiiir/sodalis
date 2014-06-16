
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

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author wladimiiir
 */
public class DateColumnComponent extends AbstractColumnComponent {

    private JPanel component;
    protected JComboBox comparatorComboBox;
    protected JDateChooser dateChooser;

    public DateColumnComponent() {
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints c = new GridBagConstraints();

        comparatorComboBox = new JComboBox(new Object[]{"", "<", ">", "=", "<=", ">="});
        dateChooser = new JDateChooser("dd.MM.yyyy", "##.##.####", '-');
        component = new JPanel(new GridBagLayout());
        c.gridx++;
        c.gridy++;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        component.add(comparatorComboBox, c);
        c.gridx++;
        c.weightx = 1.0;
        component.add(dateChooser, c);
    }

    @Override
    protected String getWhereText() {
        if (dateChooser.getCalendar() == null) {
            return null;
        }

        final Calendar date = (Calendar) dateChooser.getCalendar().clone();

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return "'" + new Timestamp(date.getTimeInMillis()).toString() + "'";
    }

    @Override
    public JComponent getComponent() {
        return component;
    }

    @Override
    protected String getComparator() {
        return comparatorComboBox.getSelectedItem().toString();
    }

    @Override
    public boolean isIncluded() {
        return dateChooser.getCalendar() != null && !getComparator().trim().isEmpty();
    }


}