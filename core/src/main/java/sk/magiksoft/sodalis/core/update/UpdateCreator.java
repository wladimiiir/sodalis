
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
public class UpdateCreator {
    public static void main(String[] args) {
        if (args.length != 3) {
            return;
        }

        File updatesFile = new File(args[0]);
        String forVersion = args[1];
        File updateZipFile = new File(args[2]);

        UpdateManager.getInstance().generateUpdateZipFile(updatesFile, forVersion, updateZipFile);
    }
}