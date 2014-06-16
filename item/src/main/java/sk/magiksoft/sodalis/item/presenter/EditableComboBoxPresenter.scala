
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.presenter

import sk.magiksoft.sodalis.item.entity.ItemProperty
import java.io.Serializable

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 28, 2010
 * Time: 9:20:22 PM
 * To change this template use File | Settings | File Templates.
 */

class EditableComboBoxPresenter extends ComboBoxPresenter {
  override def getComponent(itemProperty: ItemProperty, value: Serializable) = {
    val comboBox = super.getComponent(itemProperty, value)

    comboBox.peer.setEditable(true)
    comboBox
  }
}