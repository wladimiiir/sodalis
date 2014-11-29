package sk.magiksoft.sodalis.core.filter.element

/**
 * @author wladimiiir
 * @since 2011/2/25
 */
class NoComparatorDateColumnComponent extends DateColumnComponent {
  comparatorComboBox.setVisible(false)

  override def getComparator = null

  override def isIncluded = dateChooser.getCalendar != null
}
