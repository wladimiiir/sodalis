
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.ui.controlpanel;

import java.util.List;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

/**
 *
 * @author wladimiiir
 */
public class AcceptProperty {
    private String propertyName;
    private List<String> values;

    public AcceptProperty(String propertyName, List<String> values) {
        this.propertyName = propertyName;
        this.values = values;
    }

    public boolean accept(Object object){
        try{
            String value = object.getClass().getDeclaredField(propertyName).get(object).toString();

            for (String acceptValue : values) {
                if(acceptValue.equals(value)){
                    return true;
                }
            }
            //not found
            return false;
        }catch(Exception e){
            LoggerManager.getInstance().info(getClass(), e);
        }

        //exception probably
        return true;
    }
}