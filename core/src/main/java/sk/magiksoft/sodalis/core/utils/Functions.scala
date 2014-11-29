package sk.magiksoft.sodalis.core.utils


/**
 * @author wladimiiir
 * @since 2010/6/10
 */

object Functions {
  def transform[A, B](list: List[A], transformation: (A => B)): List[B] = {
    list match {
      case null => Nil
      case Nil => Nil
      case x :: xs => transformation(x) :: transform(xs, transformation)
    }
  }
}
