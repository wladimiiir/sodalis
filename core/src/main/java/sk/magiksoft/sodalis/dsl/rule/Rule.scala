package sk.magiksoft.sodalis.dsl.rule

/**
 * @author wladimiiir
 * @since 2011/5/31
 */

class Rule[A](mainCondition: A => Condition) {
  var givingOption: Option[String] = None

  def giving(result: String) = {
    givingOption = Option(result)
    this
  }

  def accepts(input: A) = mainCondition(input).fulfilled
}

object Rule {
  def complying[A](rule: A => Condition): Rule[A] = new Rule[A](rule)
}
