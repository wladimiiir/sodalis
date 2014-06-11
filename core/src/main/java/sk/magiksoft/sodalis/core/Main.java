
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

import org.jdesktop.application.Application;

import java.util.Locale;

/**
 *
 * @author wladimiiir
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            Locale.setDefault(new Locale("sk", "SK"));
            new EnvironmentSetup().setup();

            Application.launch(SodalisApplication.class, new String[0]);
    }
}