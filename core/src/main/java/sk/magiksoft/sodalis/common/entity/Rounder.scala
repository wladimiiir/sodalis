package sk.magiksoft.sodalis.common.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import math.BigDecimal.RoundingMode

/*
* Copyright (c) 2011
*/

/**
 * @author wladimiiir
 * @since 2011/3/11
 */

class Rounder(var priceRound: Int = 2, var taxRound: Int = 2, var formatString: String = "0.00") extends AbstractDatabaseEntity {

  def roundPrice(price: BigDecimal) = round(price, priceRound)

  def roundTax(tax: BigDecimal) = round(tax, taxRound)

  def round(value: BigDecimal, round: Int): BigDecimal = value.setScale(round, RoundingMode.HALF_UP)

  def formatFractionDigits = {
    val index = formatString.indexOf('.')
    index match {
      case -1 => 0
      case index: Int => formatString.substring(index).length - 1
    }
  }

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case rounder: Rounder => {
        priceRound = rounder.priceRound
        taxRound = rounder.taxRound
        formatString = rounder.formatString
      }
      case _ =>
    }
  }
}
