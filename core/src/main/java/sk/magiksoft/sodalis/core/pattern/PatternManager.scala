
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.core.pattern

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 16, 2010
 * Time: 12:43:39 PM
 * To change this template use File | Settings | File Templates.
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