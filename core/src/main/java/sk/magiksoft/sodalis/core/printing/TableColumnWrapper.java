
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

import sk.magiksoft.sodalis.core.locale.LocaleManager;

import java.io.Serializable;

/**
 * @author wladimiiir
 */
public class TableColumnWrapper implements Serializable {
    private static final long serialVersionUID = 6408905948615185805l;

    public enum Alignment {
        LEFT, CENTER, RIGHT;


        @Override
        public String toString() {
            switch (this) {
                case LEFT:
                    return LocaleManager.getString("leftAlignment");
                case CENTER:
                    return LocaleManager.getString("centerAlignment");
                case RIGHT:
                    return LocaleManager.getString("rightAlignment");
                default:
                    return super.toString();
            }
        }
    }

    private String key;
    private String headerValue;
    private boolean sum;
    private int width;
    private Class valueClass = String.class;
    private Alignment alignment = Alignment.LEFT;

    public TableColumnWrapper(String key, String headerValue, int width) {
        this.key = key;
        this.headerValue = headerValue;
        this.width = width;
    }

    public TableColumnWrapper(String key, String headerValue, int width, Alignment alignment, boolean sum) {
        this.key = key;
        this.headerValue = headerValue;
        this.width = width;
        this.alignment = alignment;
        this.sum = sum;
    }

    public TableColumnWrapper(String key, String headerValue, int width, Class valueClass, Alignment alignment, boolean sum) {
        this.key = key;
        this.headerValue = headerValue;
        this.width = width;
        this.valueClass = valueClass;
        this.alignment = alignment;
        this.sum = sum;
    }

    public Class getValueClass() {
        return valueClass == null ? String.class : valueClass;
    }

    public void setValueClass(Class valueClass) {
        this.valueClass = valueClass;
    }

    public String getKey() {
        return key;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public boolean isSum() {
        return sum;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public int getWidth() {
        return width;
    }
}