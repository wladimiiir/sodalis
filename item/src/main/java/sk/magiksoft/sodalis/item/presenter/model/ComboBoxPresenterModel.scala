package sk.magiksoft.sodalis.item.presenter.model

import java.io.Serializable

/**
 * @author wladimiiir
 * @since 2010/6/19
 */

class ComboBoxPresenterModel extends Serializable {
  var items: List[String] = Nil
  var enumerationKey: String = null
}
