package sk.magiksoft.sodalis.core.utils

import java.awt.Image
import java.awt.image.BufferedImage
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * @author wladimiiir
 * @since 2011/2/20
 */
object ImageUtils {
  def resizeImage(image: Image, scale: Double) = {
    val resized = new BufferedImage(image.getWidth(null) * scale, image.getHeight(null) * scale, BufferedImage.TYPE_INT_ARGB);
    resized.getGraphics.drawImage(image, 0, 0, resized.getWidth(), resized.getHeight(), null)
    resized
  }
}
