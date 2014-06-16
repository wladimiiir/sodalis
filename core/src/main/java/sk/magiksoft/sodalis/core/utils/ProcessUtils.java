
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
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * @author wladimiiir
 */
public class ProcessUtils {

    public static int runCommand(boolean waitFor, String... command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command).start();
        byte[] bytes = new byte[2048];
        int count;

        if (!waitFor) {
            return 0;
        }
        BufferedInputStream bis = new BufferedInputStream(process.getErrorStream());
        BufferedOutputStream bos = new BufferedOutputStream(System.err);

        while ((count = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, count);
        }
        bos.flush();
        bis.close();

        return process.waitFor();
    }
}