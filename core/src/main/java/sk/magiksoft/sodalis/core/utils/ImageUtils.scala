
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/20/11
 * Time: 7:40 PM
 */
package sk.magiksoft.sodalis.core.utils

import java.awt.Image
import java.awt.image.BufferedImage
import sk.magiksoft.sodalis.core.utils.Conversions._

object ImageUtils {
  def resizeImage(image: Image, scale: Double) = {
    val resized = new BufferedImage(image.getWidth(null) * scale, image.getHeight(null) * scale, BufferedImage.TYPE_INT_ARGB);
    resized.getGraphics.drawImage(image, 0, 0, resized.getWidth(), resized.getHeight(), null)
    resized
  }
}