
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.license;

import java.io.Serializable;

/**
 * @author wladimiiir
 */
public class FileProperty implements Serializable {
    private String filePath;
    private String propertyName;

    public FileProperty(String filePath, String propertyName) {
        this.filePath = filePath;
        this.propertyName = propertyName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getPropertyName() {
        return propertyName;
    }
}