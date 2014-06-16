
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 1/5/11
 * Time: 9:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceEnumerationEntry extends EnumerationEntry {
    private static final long serialVersionUID = -1l;

    private String reference;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}