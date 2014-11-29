package sk.magiksoft.sodalis.form.ui.figure

import org.jhotdraw.draw.TextHolderFigure


/**
 * @author wladimiiir
 * @since 2010/4/28
 */

trait ItemsHolderFigure extends TextHolderFigure {
  var items: List[String] = List[String]("abc", "fdfsd", "fsdfs", "rewrwer")
  var selectedItem: String = null
}
