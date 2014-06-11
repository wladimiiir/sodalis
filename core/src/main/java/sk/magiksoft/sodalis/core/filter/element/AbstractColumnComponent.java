
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

import java.text.MessageFormat;

/**
 * @author wladimiiir
 */
public abstract class AbstractColumnComponent implements ColumnComponent {
    protected String labelText;
    protected String select;
    protected String from;
    protected String where;

    public AbstractColumnComponent() {
        labelText = "";
        select = "";
        from = "";
        where = "";
    }

    protected abstract String getWhereText();

    protected abstract String getComparator();

    @Override
    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    @Override
    public void setWhere(String where) {
        this.where = where;
    }

    @Override
    public void addItem(Object object) {
    }

    @Override
    public String getFromQuery() {
        return from;
    }

    @Override
    public String getLabelText() {
        return labelText;
    }

    @Override
    public String getSelectQuery() {
        return select;
    }

    @Override
    public void setSelect(String select) {
        this.select = select;
    }

    @Override
    public String getWhereQuery() {
        final StringBuilder whereQuery = new StringBuilder();
        final String whereText = getWhereText();

        if(whereText==null || whereText.trim().isEmpty()){
            return whereQuery.toString();
        }

        final String comparator = getComparator();
        if(comparator!=null){
            whereQuery.append(MessageFormat.format(where, comparator, whereText));
        }else{
            whereQuery.append(MessageFormat.format(where, whereText));
        }

        return whereQuery.toString();
    }
}