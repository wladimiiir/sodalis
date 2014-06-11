
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

import net.sf.jasperreports.engine.JRRewindableDataSource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

/**
 * @author wladimiiir
 */
public class TablePrintDataSource implements Serializable {

    private Component pageHeaderComponent;
    private BufferedImage pageHeaderImage;
    private List<Object> columnObjects;
    private List<TableColumnWrapper> columns;
    private transient JRRewindableDataSource dataSource;
    private boolean showPageNumbers;
    private String totalCountLabel;

    public List<TableColumnWrapper> getColumns() {
        return columns;
    }

    public void setColumns(List<TableColumnWrapper> columns) {
        this.columns = columns;
    }

    public JRRewindableDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(JRRewindableDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Object> getColumnObjects() {
        return columnObjects;
    }

    public void setColumnObjects(List<Object> columnObjects) {
        this.columnObjects = columnObjects;
    }

    public Component getPageHeaderComponent() {
        return pageHeaderComponent;
    }

    public void setPageHeaderComponent(Component pageHeaderComponent) {
        this.pageHeaderComponent = pageHeaderComponent;
    }

    public boolean isShowPageNumbers() {
        return showPageNumbers;
    }

    public void setShowPageNumbers(boolean showPageNumbers) {
        this.showPageNumbers = showPageNumbers;
    }

    public BufferedImage getPageHeaderImage() {
        return pageHeaderImage;
    }

    public void setPageHeaderImage(BufferedImage pageHeaderImage) {
        this.pageHeaderImage = pageHeaderImage;
    }

    public Format getDoubleFormatter() {
        return new DecimalFormat("0.00");
    }

    public String getTotalCountLabel() {
        return totalCountLabel;
    }

    public void setTotalCountLabel(String totalCountLabel) {
        this.totalCountLabel = totalCountLabel;
    }
}