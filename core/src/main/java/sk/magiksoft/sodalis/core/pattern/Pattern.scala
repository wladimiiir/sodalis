package sk.magiksoft.sodalis.core.pattern

/**
 * @author wladimiiir
 * @since 2010/5/16
 */

class Pattern(val pattern: String, val name: String, val patternTypes: Array[PatternType.PatternType]) {
  override def toString = name
}
