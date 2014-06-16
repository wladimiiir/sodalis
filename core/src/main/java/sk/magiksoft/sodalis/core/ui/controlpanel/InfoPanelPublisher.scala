/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.core.ui.controlpanel

import swing.event.ValueChanged
import swing.Publisher
import sk.magiksoft.sodalis.common.ui.{TaxChanged, PriceWithTaxChanged, PriceChanged}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/15/11
 * Time: 8:52 PM
 * To change this template use File | Settings | File Templates.
 */

trait InfoPanelPublisher extends AbstractInfoPanel with Publisher {
  reactions += {
    case ValueChanged(_) => fireEditing
    case PriceChanged(_, _) => fireEditing
    case PriceWithTaxChanged(_, _) => fireEditing
    case TaxChanged(_, _) => fireEditing
  }
}