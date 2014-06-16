
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core;

/**
 * @author wladimiiir
 */
public class Version {
    public static String getVersion() {
        return Version.class.getPackage().getImplementationVersion();
    }

    public static void main(String[] args) {
        System.out.println(getVersion());
    }
}