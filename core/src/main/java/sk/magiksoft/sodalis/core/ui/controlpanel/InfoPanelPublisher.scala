package sk.magiksoft.sodalis.core.ui.controlpanel

import swing.event.ValueChanged
import swing.Publisher
import sk.magiksoft.sodalis.common.ui.{TaxChanged, PriceWithTaxChanged, PriceChanged}

/**
 * @author wladimiiir
 * @since 2011/3/15
 */

trait InfoPanelPublisher extends AbstractInfoPanel with Publisher {
  reactions += {
    case ValueChanged(_) => fireEditing
    case PriceChanged(_, _) => fireEditing
    case PriceWithTaxChanged(_, _) => fireEditing
    case TaxChanged(_, _) => fireEditing
  }
}
