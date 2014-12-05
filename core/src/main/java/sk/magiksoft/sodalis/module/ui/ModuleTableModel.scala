package sk.magiksoft.sodalis.module.ui

import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.module.entity.ModuleEntity

/**
 * @author wladimiiir
 * @since  2014/12/04
 */
class ModuleTableModel extends ObjectTableModel[ModuleEntity](Array(
  LocaleManager.getString("name")
)) {
  override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef = columnIndex match {
    case 0 => objects.get(rowIndex).getModule.getModuleDescriptor.getDescription
    case _ => ""
  }
}
