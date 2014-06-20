
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.app;

import org.jdesktop.application.Application;
import sk.magiksoft.sodalis.core.EnvironmentSetup;
import sk.magiksoft.sodalis.core.SodalisApplication;

/**
 * @author wladimiiir
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new EnvironmentSetup().setup();

        Application.launch(SodalisApplication.class, new String[0]);
    }
}