package sk.magiksoft.sodalis.psyche.rorschach.res

import sk.magiksoft.sodalis.psyche.rorschach.entity.Aperception
import java.io.File
import sk.magiksoft.sodalis.core.imex.ImExManager

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

object AperceptionsToXML {

  def main(args: Array[String]) {
    exportAperceptions()
  }

  private def exportAperceptions() {
    val list = new java.util.LinkedList[Aperception]()

    list.add(createAperception("G", "G", "celok", ""))
    list.add(createAperception("D", "D", "veľký detail", ""))
    list.add(createAperception("Gkomb", "G", "celok poskladaný z jednotlivých častí", ""))
    list.add(createAperception("DGkomb", "G;D;DGkomb", "postupné poskladanie detailov do celku", ""))
    list.add(createAperception("Dd", "Dd", "drobný detail", ""))
    list.add(createAperception("Dzw", "zw", "medzipriestor (biela plocha vo škvrne alebo po jej obvode)", ""))
    list.add(createAperception("Ddzw", "Dd;zw", "malá časť bielej plochy", ""))
    list.add(createAperception("Do", "Do", "oligofrenický detail", ""))
    list.add(createAperception("DzwG", "G;zw", "biely detail s prechodom do celku", ""))
    list.add(createAperception("Gzw", "Gzw", "zahrnutá celá škvrna + biele časti (biela plocha)", ""))
    list.add(createAperception("zw", "zw", "identifikácia bielých častí", ""))
    list.add(createAperception("DGkonf", "G;D;DGkonf", "detail rozšírený na celok, kde detail sedí, ale celok nie", ""))
    list.add(createAperception("DGkont", "G;D", "detaily spojené do bizarného celku", ""))

    ImExManager.exportData(new File("aperceptions.xml"), list)
  }

  private def createAperception(name: String, taEntryGroups: String, description: String, interpretation: String) = {
    val aperception = new Aperception
    aperception.name = name
    aperception.taEntryGroups = taEntryGroups
    aperception.description = description
    aperception.interpretation = interpretation
    aperception
  }
}
