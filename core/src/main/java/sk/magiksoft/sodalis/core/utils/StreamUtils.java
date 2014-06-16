
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wladimiiir
 */
public class StreamUtils {
    public static String getInputStreamString(InputStream inputStream) throws IOException {
        final StringBuilder streamString = new StringBuilder();
        final BufferedInputStream bis = new BufferedInputStream(inputStream);
        byte[] bytes = new byte[256];

        while (bis.read(bytes) != -1) {
            streamString.append(new String(bytes));
        }

        return streamString.toString();
    }
}