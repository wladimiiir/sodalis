
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.ui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author wladimiiir
 */
public class WelcomePanel extends SlideshowPanel {

    public WelcomePanel() throws MalformedURLException {
        super(new URL[]{
                new File("data/welcomepage/welcome.html").toURI().toURL()
        });
    }
}