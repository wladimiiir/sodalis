package sk.magiksoft.sodalis.form.ui.figure

/**
 * @author wladimiiir
 * @since 2010/4/28
 */

object Test {
  private var figure: ItemsHolderFigure = null

  def main(args: Array[String]) = {
    figure = new ComboBoxFigure
    update
  }

  def update = {
    for (item <- figure.items) {
      println(item)
    }

    println(figure.selectedItem)
  }
}
