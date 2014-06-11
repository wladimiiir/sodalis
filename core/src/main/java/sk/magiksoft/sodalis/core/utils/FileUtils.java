
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author wladimiiir
 */
public class FileUtils {

    public static void copyFile(File srcFile, File destFile) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
        byte[] bytes = new byte[2048];
        int count;

        while ((count = bis.read(bytes, 0, bytes.length)) != -1) {
            bos.write(bytes, 0, count);
        }

        bis.close();
        bos.close();
    }

    public static void copyDirectory(File srcDir, File destDir, boolean onlyContent) throws IOException {
        if (!onlyContent) {
            destDir = new File(destDir, srcDir.getName());
        }
        destDir.mkdirs();
        for (File file : srcDir.listFiles()) {
            if (file.isDirectory()) {
                copyDirectory(file, destDir, false);
            } else {
                copyFile(file, new File(destDir, file.getName()));
            }
        }
    }

    public static void decodeFile(File file, File decodedFile, byte shift) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(decodedFile);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[1];

        while (bis.read(buf) != -1) {
            fos.write(shift - buf[0]);
        }

        bis.close();
        fos.close();
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static void unpackZipFile(File zipFile, File destDir) throws FileNotFoundException, IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        ZipEntry zipEntry;
        File zipEntryFile;
        BufferedOutputStream bos;
        byte[] bytes = new byte[2048];
        int count;

        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        while ((zipEntry = zis.getNextEntry()) != null) {
            zipEntryFile = new File(destDir, zipEntry.getName());
            if (zipEntry.isDirectory()) {
                zipEntryFile.mkdirs();
                continue;
            } else {
                zipEntryFile.getParentFile().mkdirs();
            }
            if (!zipEntryFile.exists()) {
                zipEntryFile.createNewFile();
            }
            bos = new BufferedOutputStream(new FileOutputStream(zipEntryFile));
            while ((count = zis.read(bytes, 0, bytes.length)) != -1) {
                bos.write(bytes, 0, count);
            }
            bos.close();
        }
        zis.close();
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024]; // read up to 1k at a time
        int amtRead;

        try {
            while ((amtRead = fis.read(buf)) != -1) {
                baos.write(buf, 0, amtRead);
            }

            return baos.toByteArray();
        } finally {
            fis.close();
            baos.close();
        }
    }

    public static byte[] getBytesFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024]; // read up to 1k at a time
        int amtRead;

        try {
            while ((amtRead = inputStream.read(buf)) != -1) {
                baos.write(buf, 0, amtRead);
            }

            return baos.toByteArray();
        } finally {
            inputStream.close();
            baos.close();
        }
    }

    public static void saveBytesToFile(File file, byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);

        try {
            fos.write(bytes);
        } finally {
            fos.close();
        }
    }

}