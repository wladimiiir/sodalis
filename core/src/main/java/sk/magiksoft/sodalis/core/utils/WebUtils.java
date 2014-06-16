
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.utils;

import java.io.*;
import java.net.URL;

/**
 * @author wladimiiir
 */
public class WebUtils {

    public static void downloadFile(URL fileUrl, File localFile) throws IOException {
        if (!localFile.getParentFile().exists()) {
            localFile.getParentFile().mkdirs();
        }

        BufferedInputStream bis = new BufferedInputStream(fileUrl.openStream());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFile));
        byte[] bytes = new byte[2048];
        int count;

        while ((count = bis.read(bytes, 0, bytes.length)) != -1) {
            bos.write(bytes, 0, count);
        }

        bis.close();
        bos.close();
    }
}