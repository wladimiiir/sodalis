package sk.magiksoft.sodalis.core.pattern

/**
 * @author wladimiiir
 * @since 2010/5/16
 */

object PatternManager {
  private val PATTERN = java.util.regex.Pattern.compile("${([A-Za-z0-9]+)}$")

  private var patternResolvers = List[PatternResolver]()

  def registerPatternResolver(resolver: PatternResolver) = {
    patternResolvers ::= resolver
  }

  def resolvePatterns(value: String): String = {
    val matcher = PATTERN.matcher(value)

    while (matcher.find) {
      val pattern = matcher.group(1)
      for (patternResolver <- patternResolvers) {
        val patternValue = patternResolver.resolvePattern(pattern)
        if (patternValue != null) {
          return patternValue
        }
      }
    }

    return null
  }
}
