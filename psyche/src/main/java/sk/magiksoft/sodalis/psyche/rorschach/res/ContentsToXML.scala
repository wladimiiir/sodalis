package sk.magiksoft.sodalis.psyche.rorschach.res

/*
 * Copyright (c) 2011
 */

import java.io.File
import sk.magiksoft.sodalis.psyche.rorschach.entity.Content
import sk.magiksoft.sodalis.core.imex.ImExManager

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

object ContentsToXML {

  def main(args: Array[String]) {
    exportContents()
  }

  private def exportContents() {
    val list = new java.util.LinkedList[Content]()

    list.add(createContent("M", "celok", ""))
    list.add(createContent("(M)", "veľký detail", ""))
    list.add(createContent("Md", "celok poskladaný z jednotlivých častí", ""))
    list.add(createContent("MdObj", "postupné poskladanie detailov do celku", ""))
    list.add(createContent("T", "drobný detail", ""))
    list.add(createContent("Td", "medzipriestor (biela plocha vo škvrne alebo po jej obvode)", ""))
    list.add(createContent("Abstr", "malá časť bielej plochy", ""))
    list.add(createContent("Amorf", "oligofrenický detail", ""))
    list.add(createContent("Anat", "biely detail s prechodom do celku", ""))
    list.add(createContent("Arch", "zahrnutá celá škvrna + biele časti (biela plocha)", ""))
    list.add(createContent("Astr", "identifikácia bielých častí", ""))
    list.add(createContent("Blato", "detail rozšírený na celok, kde detail sedí, ale celok nie", ""))
    list.add(createContent("BN", "bodný nástroj", ""))
    list.add(createContent("DP", "dopravný prostriedok", ""))
    list.add(createContent("Dym", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Expl", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Farba", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Geog", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Geom", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Ilustr", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Jask", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Kameň", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Karik", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Krv", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Land", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Ľad", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Maska I", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Maska II", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Maska III", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Maľba", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Mat", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Mrak", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Myt", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Obj", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Oči", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Oheň", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Orn", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Pfl", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Potrava", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Priep", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Príš", "detaily spojené do bizarného celku", ""))
    list.add(createContent("RTG", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Sacrum", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Scéna", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Sex", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Skala", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Socha", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Symb", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Šaty", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Voda", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Vrch", "detaily spojené do bizarného celku", ""))
    list.add(createContent("Znak", "detaily spojené do bizarného celku", ""))

    ImExManager.exportData(new File("contents.xml"), list)
  }

  private def createContent(name: String, description: String, interpretation: String) = {
    val content = new Content
    content.name = name
    content.description = description
    content.interpretation = interpretation
    content
  }
}
