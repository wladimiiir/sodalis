package sk.magiksoft.sodalis.psyche.rorschach.res

/*
 * Copyright (c) 2011
 */

/*
 * Copyright (c) 2011
 */

import java.io.File
import sk.magiksoft.sodalis.psyche.rorschach.entity.{Vulgarity, VulgarAnswer}
import sk.magiksoft.sodalis.core.imex.ImExManager

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

object VulgarAnswersToXML {

  def main(args: Array[String]) {
    exportVulgarAnswers()
  }

  private def exportVulgarAnswers() {
    val list = new java.util.LinkedList[VulgarAnswer]()

    list.add(createVulgarAnswer("netopier", "", 1, "W", 35, Vulgarity.V1))
    list.add(createVulgarAnswer("motýľ", "", 1, "W", 21, Vulgarity.V2))
    list.add(createVulgarAnswer("panva, panvová kosť", "", 1, "W", 17, Vulgarity.V2))
    list.add(createVulgarAnswer("RTG snímok", "", 1, "W", 14, Vulgarity.V3))
    list.add(createVulgarAnswer("žena, človek", "", 1, "D4", 13, Vulgarity.V3))
    list.add(createVulgarAnswer("chrobák", "", 1, "DW", 11, Vulgarity.V3))
    list.add(createVulgarAnswer("2 postavy niečo držia", "", 1, "DW", 11, Vulgarity.V3))
    list.add(createVulgarAnswer("2 zvieratá", "", 1, "D2", 10, Vulgarity.V3))
    list.add(createVulgarAnswer("kosť, stavec,...", "", 1, "W,WS", 8, Vulgarity.V3))
    list.add(createVulgarAnswer("chrobák", "", 1, "D4", 8, Vulgarity.V3))

    list.add(createVulgarAnswer("zviera", "", 2, "D1", 46, Vulgarity.V1))
    list.add(createVulgarAnswer("2 ľudia", "", 2, "W", 26, Vulgarity.V1))
    list.add(createVulgarAnswer("motýľ", "", 2, "D3", 11, Vulgarity.V3))
    list.add(createVulgarAnswer("ženské genitálie", "", 11, "D3,Dd24", 8, Vulgarity.V3))
    list.add(createVulgarAnswer("krv", "", 2, "D2,D3", 10, Vulgarity.V3))
    list.add(createVulgarAnswer("panva, panvová kosť", "", 2, "D1,DS5", 9, Vulgarity.V3))

    list.add(createVulgarAnswer("2 ľudské postavy (figúry, karikatúry)", "", 3, "D9", 97, Vulgarity.V1))
    list.add(createVulgarAnswer("motýľ", "", 3, "D3", 12, Vulgarity.V3))
    list.add(createVulgarAnswer("vnútorné orgány", "", 3, "D3", 12, Vulgarity.V3))
    list.add(createVulgarAnswer("kôš, balík,...", "", 3, "D7", 12, Vulgarity.V3))
    list.add(createVulgarAnswer("panva, panvová kosť", "", 3, "Dd31,D8", 9, Vulgarity.V3))
    list.add(createVulgarAnswer("zviera (nie hmyz)", "", 3, "D2", 8, Vulgarity.V3))

    list.add(createVulgarAnswer("zvieracia koža", "", 4, "W", 32, Vulgarity.V1))
    list.add(createVulgarAnswer("ľudská postava, človeku podobná postava, príšera, opica", "", 4, "W", 26, Vulgarity.V1))
    list.add(createVulgarAnswer("čižma", "", 4, "D6", 22, Vulgarity.V2))

    list.add(createVulgarAnswer("netopier", "", 5, "W", 61, Vulgarity.V1))
    list.add(createVulgarAnswer("motýľ", "", 5, "W", 41, Vulgarity.V1))
    list.add(createVulgarAnswer("vták", "", 5, "W", 9, Vulgarity.V3))

    list.add(createVulgarAnswer("zvieracia koža, kožušina,...", "", 6, "W", 40, Vulgarity.V1))
    list.add(createVulgarAnswer("socha, totem", "", 6, "D3", 15, Vulgarity.V3))
    list.add(createVulgarAnswer("hlava", "", 6, "Dd23", 13, Vulgarity.V3))
    list.add(createVulgarAnswer("fúzy", "", 6, "Dd26", 13, Vulgarity.V3))
    list.add(createVulgarAnswer("vták, motýľ", "", 6, "D3", 9, Vulgarity.V3))

    list.add(createVulgarAnswer("ľudská hlava", "", 7, "D1", 40, Vulgarity.V1))
    list.add(createVulgarAnswer("štvornohé zviera", "", 7, "D2", 29, Vulgarity.V1))
    list.add(createVulgarAnswer("oblak, mrak", "", 7, "W,D", 18, Vulgarity.V2))
    list.add(createVulgarAnswer("človek, človeku podobná postava", "", 7, "D2", 22, Vulgarity.V2))
    list.add(createVulgarAnswer("hlava", "", 7, "D3", 14, Vulgarity.V3))
    list.add(createVulgarAnswer("motýľ", "", 7, "D4", 9, Vulgarity.V3))
    list.add(createVulgarAnswer("ženské genitálie", "", 7, "D6", 8, Vulgarity.V3))

    list.add(createVulgarAnswer("štvornohé zviera", "", 8, "D1", 93, Vulgarity.V1))
    list.add(createVulgarAnswer("chrbtica, stavec", "", 8, "W,WS", 23, Vulgarity.V2))
    list.add(createVulgarAnswer("lekársky obraz, vnútorné orgány", "", 8, "W", 14, Vulgarity.V3))
    list.add(createVulgarAnswer("vrch, skala", "", 8, "D4", 13, Vulgarity.V3))
    list.add(createVulgarAnswer("vrch, skala", "", 8, "D2,D7", 12, Vulgarity.V3))
    list.add(createVulgarAnswer("strom", "", 8, "D4", 9, Vulgarity.V3))
    list.add(createVulgarAnswer("voda, more", "", 8, "D5", 8, Vulgarity.V3))

    list.add(createVulgarAnswer("človek, človeku podobná postava", "", 9, "D3", 22, Vulgarity.V2))
    list.add(createVulgarAnswer("zvieracia hlava", "", 9, "Dd23", 15, Vulgarity.V3))
    list.add(createVulgarAnswer("ľudská hlava", "", 9, "D4", 13, Vulgarity.V3))
    list.add(createVulgarAnswer("maľba", "", 9, "W", 10, Vulgarity.V3))
    list.add(createVulgarAnswer("výbuch", "", 9, "W", 10, Vulgarity.V3))
    list.add(createVulgarAnswer("oheň, výbuch", "", 9, "D4", 8, Vulgarity.V3))
    list.add(createVulgarAnswer("hlava, lebka", "", 9, "Dd-DS22", 8, Vulgarity.V3))

    list.add(createVulgarAnswer("pavúk, krab, chobotnica, stonožka,...", "", 10, "D1", 43, Vulgarity.V1))
    list.add(createVulgarAnswer("plod stromu", "", 10, "D3", 27, Vulgarity.V1))
    list.add(createVulgarAnswer("zviera (aj hmyz)", "", 10, "D8", 23, Vulgarity.V2))
    list.add(createVulgarAnswer("štvornohé zviera", "", 10, "D7", 19, Vulgarity.V2))
    list.add(createVulgarAnswer("had, húsenica", "", 10, "D4", 19, Vulgarity.V2))
    list.add(createVulgarAnswer("zvieracia hlava", "", 10, "D5", 16, Vulgarity.V2))
    list.add(createVulgarAnswer("človek, človeku podobná postava", "", 10, "D9", 11, Vulgarity.V3))
    list.add(createVulgarAnswer("štvornohé zviera", "", 10, "D2", 10, Vulgarity.V3))
    list.add(createVulgarAnswer("pľúca a hrtan", "", 10, "D11", 9, Vulgarity.V3))
    list.add(createVulgarAnswer("panva, panvová kosť", "", 10, "D6", 9, Vulgarity.V3))
    list.add(createVulgarAnswer("2 zvieratá (skáčuce)", "", 10, "D12", 8, Vulgarity.V3))
    list.add(createVulgarAnswer("atramentová škvrna", "", 10, "D1", 8, Vulgarity.V3))

    ImExManager.exportData(new File("vulgarAnswers.xml"), list)
  }

  private def createVulgarAnswer(name: String, interpretation: String, blotIndex: Int,
                                 localization: String, percentage: Int, vulgarity: Vulgarity.Value) = {
    val answer = new VulgarAnswer
    answer.name = name
    answer.interpretation = interpretation
    answer.blotIndex = blotIndex
    answer.localization = localization
    answer.percentage = percentage
    answer.vulgarity = vulgarity
    answer
  }
}
