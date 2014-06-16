
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.update;

import java.io.File;

/**
 * @author wladimiiir
 */
public class DifferenceUpdater {
    public static void main(String[] args) {
        if (args.length != 4) {
            return;
        }

        File diffFile = new File(args[0]);
        File updateFile = new File(args[1]);
        String version = args[2];
        String moduleJar = args[3];

        UpdateManager.getInstance().generateUpdate(updateFile, diffFile, version, moduleJar);
    }
}