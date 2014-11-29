package sk.magiksoft.sodalis.item.presenter

import sk.magiksoft.sodalis.item.entity.ItemProperty
import java.io.Serializable

/**
 * @author wladimiiir
 * @since 2010/7/28
 */

class EditableComboBoxPresenter extends ComboBoxPresenter {
  override def getComponent(itemProperty: ItemProperty, value: Serializable) = {
    val comboBox = super.getComponent(itemProperty, value)

    comboBox.peer.setEditable(true)
    comboBox
  }
}
