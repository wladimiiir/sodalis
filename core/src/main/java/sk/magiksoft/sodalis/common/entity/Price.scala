package sk.magiksoft.sodalis.common.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import math.BigDecimal
import java.math.{MathContext, RoundingMode, BigDecimal => jBD}
import java.text.DecimalFormat
import scala.beans.BeanProperty

/*
* Copyright (c) 2011
*/

/**
 * @author wladimiiir
 * @since 2011/3/11
 */

class Price extends AbstractDatabaseEntity {
  var price = BigDecimal(0, Price.DefaultMathContext)
  @BeanProperty var tax = 0
  @BeanProperty var taxIncluded = false

  def this(price: Price) = {
    this()
    updateFrom(price)
  }

  def getPrice: jBD = new jBD(price.toString)

  def setPrice(price: jBD) {
    this.price = BigDecimal(price.toPlainString, Price.DefaultMathContext)
  }

  def formattedPrice(tax: Boolean = true, rounder: Rounder = Price.DefaultRounder) = new DecimalFormat(rounder.formatString).format(price(tax, rounder))

  def price(tax: Boolean = true, rounder: Rounder = Price.DefaultRounder): BigDecimal = taxIncluded match {
    case true => tax match {
      case true => rounder.roundPrice(price)
      case false => rounder.roundPrice(rounder.roundPrice(price) - rounder.roundTax(price * this.tax / (100 + this.tax)))
    }
    case false => tax match {
      case true => rounder.roundPrice(rounder.roundPrice(price) + rounder.roundTax(rounder.roundPrice(price) * this.tax / 100))
      case false => rounder.roundPrice(price)
    }
  }

  def tax(rounder: Rounder = Price.DefaultRounder): BigDecimal = taxIncluded match {
    case true => rounder.roundTax(price * this.tax / (100 + this.tax))
    case false => rounder.roundTax(rounder.roundPrice(price) * this.tax / 100)
  }

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case price: Price => {
        this.price = price.price
        this.tax = price.tax
        this.taxIncluded = price.taxIncluded
      }
      case _ =>
    }
  }
}

object Price {
  val DefaultRounder = new Rounder
  val DefaultMathContext = new MathContext(34, RoundingMode.HALF_UP)
}
