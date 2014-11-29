package sk.magiksoft.sodalis.common.ui

/*
 * Copyright (c) 2011
 */

import swing.GridBagPanel.Fill
import swing._
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.factory.ColorList
import java.text.NumberFormat
import javax.swing._
import sk.magiksoft.sodalis.common.entity.{Rounder, Price}
import swing.event._
import java.awt.{Font, Color}

/**
 * @author wladimiiir
 * @since 2011/3/11
 */

class PriceComponent extends GridBagPanel {
  private val rounder = new Rounder
  private val price = new Price

  private val priceNumberFormat = NumberFormat.getInstance
  private val taxNumberFormat = NumberFormat.getInstance

  private val priceUI = new FormattedTextField(priceNumberFormat) {
    peer.setHorizontalAlignment(SwingConstants.RIGHT)
    peer.setColumns(6)
  }
  private val taxesUI = new ComboBox[Int](Array(0, 10, 20))
  private val taxPriceUI = new FormattedTextField(taxNumberFormat) {
    editable = false
    background = Color.WHITE
    peer.setHorizontalAlignment(SwingConstants.RIGHT)
    peer.setColumns(6)
  }
  private val priceWithTaxUI = new FormattedTextField(priceNumberFormat) {
    font = font.deriveFont(Font.BOLD)
    peer.setHorizontalAlignment(SwingConstants.RIGHT)
    peer.setColumns(6)
  }

  private def updatePrice() {
    price.taxIncluded = false
    price.price = priceUI.text match {
      case value: String if value.replace(',', '.').forall(c => c.isDigit || c == '.') => BigDecimal(value.replace(',', '.'), Price.DefaultMathContext)
      case _ => price.price
    }
    publish(new PriceChanged(this, new Price(price)))
    updatePrices()
  }

  private def updatePriceWithTax() {
    price.taxIncluded = true
    price.price = priceWithTaxUI.text match {
      case value: String if value.replace(',', '.').forall(c => c.isDigit || c == '.') => BigDecimal(value.replace(',', '.'), Price.DefaultMathContext)
      case _ => price.price
    }
    publish(new PriceWithTaxChanged(this, new Price(price)))
    updatePrices()
  }

  reactions += {
    case KeyPressed(component, Key.Enter, 0, _) if component eq priceUI => updatePrice()
    case FocusLost(component, _, _) if component eq priceUI => updatePrice()
    case KeyPressed(component, Key.Enter, 0, _) if component eq priceWithTaxUI => updatePriceWithTax()
    case FocusLost(component, _, _) if component eq priceWithTaxUI => updatePriceWithTax()
    case SelectionChanged(combo) => {
      price.tax = taxesUI.selection.item
      publish(new TaxChanged(this, new Price(price)))
      updatePrices()
    }
  }

  initComponent()

  private def initComponent() {
    add(createDescriptionLabel(LocaleManager.getString("noVAT")), new Constraints {
      grid = (0, 0)
      weightx = 1
      insets = new Insets(0, 0, 0, 1)
      fill = Fill.Both
    })
    add(createDescriptionLabel(LocaleManager.getString("VAT")), new Constraints {
      grid = (1, 0)
      weightx = 0
      insets = new Insets(0, 0, 0, 1)
      fill = Fill.Both
    })
    add(createDescriptionLabel(LocaleManager.getString("tax")), new Constraints {
      grid = (2, 0)
      weightx = 0
      insets = new Insets(0, 0, 0, 1)
      fill = Fill.Both
    })
    add(createDescriptionLabel(LocaleManager.getString("withVAT")), new Constraints {
      grid = (3, 0)
      weightx = 1
      fill = Fill.Both
    })
    add(priceUI, new Constraints {
      grid = (0, 1)
      weightx = 1
      fill = Fill.Both
    })
    add(taxesUI, new Constraints {
      grid = (1, 1)
      weightx = 0
      fill = Fill.Both
    })
    add(taxPriceUI, new Constraints {
      grid = (2, 1)
      weightx = 1
      insets = new Insets(0, 0, 1, 1)
      fill = Fill.Both
    })
    add(priceWithTaxUI, new Constraints {
      grid = (3, 1)
      weightx = 1
      fill = Fill.Both
    })

    def createDescriptionLabel(description: String) = new Label {
      text = description
      horizontalTextPosition = Alignment.Left
      opaque = true
      foreground = ColorList.DARK_GRAY_1
      border = BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(1, 1, 0, 1, Color.LIGHT_GRAY),
        BorderFactory.createEmptyBorder(0, 1, 0, 1)
      )
      background = ColorList.BLUE_1
    }
  }

  private def updatePrices() {
    deafTo(priceUI.keys, taxesUI.selection, priceWithTaxUI.keys)
    priceUI.text = priceNumberFormat.format(price.price(false, rounder))
    taxesUI.selection.item = price.tax
    taxPriceUI.text = taxNumberFormat.format(price.tax(rounder))
    priceWithTaxUI.text = priceNumberFormat.format(price.price(true, rounder))
    price.taxIncluded match {
      case true => {
        priceUI.background = Color.WHITE
        priceWithTaxUI.background = ColorList.LIGHT_YELLOW_1
      }
      case false => {
        priceUI.background = ColorList.LIGHT_YELLOW_1
        priceWithTaxUI.background = Color.WHITE
      }
    }
    listenTo(priceUI.keys, taxesUI.selection, priceWithTaxUI.keys)
  }

  def setRounder(rounder: Rounder) {
    this.rounder.updateFrom(rounder)
    priceNumberFormat.setMinimumFractionDigits(rounder.formatFractionDigits)
    taxNumberFormat.setMinimumFractionDigits(rounder.formatFractionDigits)
    updatePrices()
  }

  def updatePrice(price: Price) {
    price.updateFrom(this.price)
  }

  def setPrice(price: Price) {
    this.price.updateFrom(price)
    updatePrices()
  }

  priceNumberFormat.setMinimumFractionDigits(2)
  priceNumberFormat.setMaximumFractionDigits(2)
  taxNumberFormat.setMinimumFractionDigits(2)
  taxNumberFormat.setMaximumFractionDigits(2)
}

case class PriceChanged(component: PriceComponent, price: Price) extends Event

case class PriceWithTaxChanged(component: PriceComponent, price: Price) extends Event

case class TaxChanged(component: PriceComponent, price: Price) extends Event
