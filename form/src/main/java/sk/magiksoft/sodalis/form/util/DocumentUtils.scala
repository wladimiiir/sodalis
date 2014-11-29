package sk.magiksoft.sodalis.form.util

/**
 * @author wladimiiir
 * @since 2010/8/27
 */

object DocumentUtils {
  def toPix(mm: Double, dpi: Int): Int = math.round(mm * dpi / 25.4).toInt

  def toPix(mm: Double): Int = toPix(mm, 72)

  def toMM(pix: Int, dpi: Int): Double = pix.toDouble / (dpi / 25.4)

  def toMM(pix: Int): Double = toMM(pix, 72)
}
