
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.printing;

import net.sf.jasperreports.engine.JRException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author wladimiiir
 */
public abstract class ObjectDataSource<T> implements JRExtendedDataSource {

    protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("d.M.yyyy");
    private List<T> objects;
    protected T entity;
    protected int index = -1;

    public ObjectDataSource(List<T> objects) {
        this.objects = objects;
    }

    @Override
    public void moveFirst() throws JRException {
        index = -1;
    }

    @Override
    public boolean next() throws JRException {
        if (objects.size() > ++index) {
            entity = objects.get(index);
            return true;
        }
        entity = null;
        return false;
    }

    @Override
    public void setData(List data) {
        objects = data;
        index = -1;
    }
}