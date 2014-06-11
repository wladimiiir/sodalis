
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

import sk.magiksoft.sodalis.core.settings.storage.Storage;

import java.io.Serializable;
import java.util.List;

/**
 * @author wladimiiir
 */
public class TablePrintSettings implements Serializable {

    private static final long serialVersionUID = -6510164351281244928L;

    private String name;
    private String headerText = "";
    private Storage tableColumnSettings;
    private List<TableColumnWrapper> tableColumnWrappers;
    private boolean showPageHeader;
    private boolean showPageNumbers;
    private String totalCountLabel;
    private Long formID;

    public TablePrintSettings(String name) {
        this.name = name;
    }

    public String getTotalCountLabel() {
        return totalCountLabel;
    }

    public void setTotalCountLabel(String totalCountLabel) {
        this.totalCountLabel = totalCountLabel;
    }

    public Long getFormID() {
        return formID;
    }

    public void setFormID(Long formID) {
        this.formID = formID;
    }

    public Storage getTableColumnSettings() {
        return tableColumnSettings;
    }

    public void setTableColumnSettings(Storage tableColumnSettings) {
        this.tableColumnSettings = tableColumnSettings;
    }

    public List<TableColumnWrapper> getTableColumnWrappers() {
        return tableColumnWrappers;
    }

    public void setTableColumnWrappers(List<TableColumnWrapper> tableColumnWrappers) {
        this.tableColumnWrappers = tableColumnWrappers;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getHeaderText() {
        return headerText;
    }

    public boolean isShowPageHeader() {
        return showPageHeader;
    }

    public void setShowPageHeader(boolean showPageHeader) {
        this.showPageHeader = showPageHeader;
    }

    public String getName() {
        return name;
    }

    public boolean isShowPageNumbers() {
        return showPageNumbers;
    }

    public void setShowPageNumbers(boolean showPageNumbers) {
        this.showPageNumbers = showPageNumbers;
    }

    @Override
    public String toString() {
        return name;
    }
}