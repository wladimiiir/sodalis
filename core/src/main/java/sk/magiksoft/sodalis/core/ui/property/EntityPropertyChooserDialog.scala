package sk.magiksoft.sodalis.core.ui.property

import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import swing.{ScrollPane, ListView}
import sk.magiksoft.sodalis.core.entity.property.{Translation, Translator}
import collection.mutable.ListBuffer
import swing.ListView.Renderer
import java.awt.Window
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2010/10/17
 */

class EntityPropertyChooserDialog(owner: Window, translator: Translator[_]) extends OkCancelDialog(owner, LocaleManager.getString("propertyChooser")) {
  private val propertyList = new ListView[Translation[_]](translator.getTranslations) {
    renderer = Renderer(translation => translation.name)
  }

  initComponents()

  private def initComponents() {
    setMainPanel(new ScrollPane(propertyList).peer)
    setModal(true)
    setSize(150, 300)
    setLocationRelativeTo(SodalisApplication.get().getMainFrame)
  }

  def getSelectedKeys: List[String] = propertyList.selection.items.map {
    translation => translation.key
  }.toList

  def getSelectedTranslations: List[Translation[_]] = propertyList.selection.items.toList

  def setSelectedKeys(keys: List[String]) {
    val indices = new ListBuffer[Int]

    for (key <- keys) {
      propertyList.listData.indexWhere(t => t.key == key) match {
        case -1 =>
        case index: Int => indices += index
      }
    }

    propertyList.selectIndices(indices: _*)
  }
}
