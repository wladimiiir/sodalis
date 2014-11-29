package sk.magiksoft.sodalis.core.pattern

/**
 * @author wladimiiir
 * @since 2010/5/16
 */

trait PatternResolver {
  def resolvePattern(pattern: String): String

  def getAvailablePatterns: Array[Pattern]
}
