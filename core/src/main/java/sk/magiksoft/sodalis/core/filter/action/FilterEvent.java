
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.filter.action;

/**
 * @author wladimiiir
 */
public class FilterEvent {
    public static final int ACTION_RESET = 0;
    public static final int ACTION_FILTER_ALL = 1;
    public static final int ACTION_FILTER_SELECTED = 2;
    private String select;
    private String from;
    private String where;
    private int action;

    public FilterEvent(String select, String from, String where, int action) {
        this.select = select;
        this.from = from;
        this.where = where;
        this.action = action;
    }

    public String getSelect() {
        return select;
    }

    public String getFrom() {
        return from;
    }

    public String getWhere() {
        return where;
    }

    public String getQuery() {
        StringBuilder query = new StringBuilder();

        if (select != null && !select.trim().isEmpty()) {
            query.append("select ").append(select).append(" ");
        }
        query.append("from ").append(from);
        if (where != null && !where.trim().isEmpty()) {
            query.append(" where ").append(where);
        }

        return query.toString();
    }

    public int getAction() {
        return action;
    }

    public boolean isFilterPerformed() {
        return action!=ACTION_RESET && !getQuery().trim().isEmpty() && !getQuery().trim().equals("from");
    }
}