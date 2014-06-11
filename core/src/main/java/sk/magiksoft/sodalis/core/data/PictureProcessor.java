
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import javax.imageio.ImageIO;

/**
 *
 * @author wladimiiir
 */
public class PictureProcessor {

    public static String getPathForImage(BufferedImage image) {
        if (image == null) {
            return "";
        }
        int[] imageArray = new int[image.getHeight() * image.getWidth()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), imageArray, 0, 1);
        //TODO: check hashcode in database and make a result

        return "";
    }

    public static BufferedImage getImageForPath(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException ex) {
            return null;
        }
    }
}