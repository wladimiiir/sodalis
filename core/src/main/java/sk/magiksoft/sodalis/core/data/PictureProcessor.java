package sk.magiksoft.sodalis.core.data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
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
