
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.splash;

import java.awt.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public interface SplashLoader {
    String getTitle();

    Image getIconImage();

    List<SplashAction> getSplashActions();

    void loaderFinished();

    void loaderCancelled(Throwable e);
}