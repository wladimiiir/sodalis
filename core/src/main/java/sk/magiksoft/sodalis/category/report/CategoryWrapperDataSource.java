
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.category.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import sk.magiksoft.sodalis.core.printing.JRExtendedDataSource;
import sk.magiksoft.sodalis.core.printing.PrintingManager;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author wladimiiir
 */
public class CategoryWrapperDataSource implements JRRewindableDataSource {

    private Iterator<CategoryPathWrapper> iterator;
    private CategoryPathWrapper wrapper;
    private JRExtendedDataSource delegateDataSource;
    private List<CategoryPathWrapper> categoryPathWrappers;

    public CategoryWrapperDataSource(List<CategoryPathWrapper> categoryPathWrappers, JRExtendedDataSource delegateDataSource) {
        this.categoryPathWrappers = categoryPathWrappers;
        this.iterator = categoryPathWrappers.iterator();
        this.delegateDataSource = delegateDataSource;
    }

    @Override
    public boolean next() throws JRException {
        if (iterator.hasNext()) {
            wrapper = iterator.next();
            return true;
        }
        wrapper = null;
        return false;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        String fieldName = jrField.getName();

        if (wrapper == null) {
            return null;
        }
        if (fieldName.startsWith(PrintingManager.GROUP_FIELD_NAME_PREFIX)) {
            int index = getCategoryPathIndex(fieldName);
            return wrapper.getPath()[index];
        } else {
            delegateDataSource.setData(Arrays.asList(wrapper.getCategorized()));
            delegateDataSource.next();
            return delegateDataSource.getFieldValue(jrField);
        }
    }

    private int getCategoryPathIndex(String fieldName) {
        return Integer.valueOf(fieldName.substring(PrintingManager.GROUP_FIELD_NAME_PREFIX.length()));
    }

    @Override
    public void moveFirst() throws JRException {
        iterator = categoryPathWrappers.iterator();
    }
}