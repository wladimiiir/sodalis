
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

import java.util.List;

/**
 *
 * @author wladimiiir
 */
public interface JRExtendedDataSource extends JRRewindableDataSource {
    void setData(List data);
}