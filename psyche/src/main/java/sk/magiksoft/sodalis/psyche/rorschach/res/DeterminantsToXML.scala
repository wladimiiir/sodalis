package sk.magiksoft.sodalis.psyche.rorschach.res

/*
 * Copyright (c) 2011
 */

import java.io.File
import sk.magiksoft.sodalis.psyche.rorschach.entity.Determinant
import sk.magiksoft.sodalis.core.imex.ImExManager

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

object DeterminantsToXML {

  def main(args: Array[String]) {
    exportDeterminants()
  }

  private def exportDeterminants() {
    val list = new java.util.LinkedList[Determinant]()

    list.add(createDeterminant("F", "celok", "", true))
    list.add(createDeterminant("FFb", "veľký detail", "", true))
    list.add(createDeterminant("FHd", "drobný detail", "", true))
    list.add(createDeterminant("F(Fb)", "medzipriestor (biela plocha vo škvrne alebo po jej obvode)", "", true))
    list.add(createDeterminant("aFFb", "zahrnutá celá škvrna + biele časti (biela plocha)", "", true))
    list.add(createDeterminant("nFFb", "identifikácia bielých častí", "", true))
    list.add(createDeterminant("B", "biely detail s prechodom do celku", "", true))
    list.add(createDeterminant("Bsec", "malá časť bielej plochy", "", true))
    list.add(createDeterminant("Bkl", "oligofrenický detail", "", true))
    list.add(createDeterminant("BT", "postupné poskladanie detailov do celku", "", true))
    list.add(createDeterminant("BO", "celok poskladaný z jednotlivých častí", "", true))
    list.add(createDeterminant("FbF", "celok poskladaný z jednotlivých častí", "", false))
    list.add(createDeterminant("HdF", "celok poskladaný z jednotlivých častí", "", false))
    list.add(createDeterminant("aFbF", "celok poskladaný z jednotlivých častí", "", false))
    list.add(createDeterminant("Fb", "celok poskladaný z jednotlivých častí", "", false))
    list.add(createDeterminant("Hd", "celok poskladaný z jednotlivých častí", "", false))
    list.add(createDeterminant("aFb", "celok poskladaný z jednotlivých častí", "", false))
    list.add(createDeterminant("FbN", "celok poskladaný z jednotlivých častí", "", false))
    list.add(createDeterminant("nFbF", "celok poskladaný z jednotlivých častí", "", false))
    list.add(createDeterminant("nFb", "celok poskladaný z jednotlivých častí", "", false))
    list.add(createDeterminant("Abstr B", "celok poskladaný z jednotlivých častí", "", false))
    list.add(createDeterminant("Asoc B", "celok poskladaný z jednotlivých častí", "", false))

    ImExManager.exportData(new File("determinants.xml"), list)
  }

  private def createDeterminant(name: String, description: String, interpretation: String, qualitySign: Boolean) = {
    val determinant = new Determinant
    determinant.name = name


    determinant.description = description
    determinant.interpretation = interpretation
    determinant.qualitySign = qualitySign
    determinant
  }
}
