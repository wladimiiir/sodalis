package sk.magiksoft.sodalis.psyche.entity


/**
 * @author wladimiiir
 * @since 2011/6/24
 */

trait PsychoTestCreator {
  def createPsychoTest(generalPsychoTest: PsychoTest): Option[PsychoTest]

  def getPsychoTestName: String
}
