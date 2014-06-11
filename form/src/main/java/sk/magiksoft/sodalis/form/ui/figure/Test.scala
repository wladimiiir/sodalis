
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form.ui.figure

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 28, 2010
 * Time: 8:32:43 PM
 * To change this template use File | Settings | File Templates.
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