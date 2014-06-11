/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.rule

/*
 * Copyright (c) 2011
 */

import sk.magiksoft.sodalis.dsl.rule.Rules._
import java.lang.Double
import sk.magiksoft.sodalis.dsl.rule.Rules

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/31/11
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */

object SecondaryFormulaRules extends Rules[(Double, Double)] {
  add {
    rule complying {
      case (ls, rs) =>
        (ls == 0 and rs == 0) or (ls == 0.5 and rs == 0.5) or
                (ls == 0.5 and rs == 1) or (ls == 1 and rs == 0) or
                (ls == 1 and rs == 0.5)
    } giving "vyprahnutá"
  }

  add {
    rule complying {
      case (ls, rs) => rs - ls == 2
    } giving "senzoriálne akcentovaná"
  }
  add {
    rule complying {
      case (ls, rs) => ls - rs == 2
    } giving "kinesteticky akcentovaná"
  }
  add {
    rule complying {
      case (ls, rs) => (ls == 2 and rs == 2) or
              (ls == 4 and rs == 3) or
              (ls == 3 and rs == 4) or
              (ls == 4 and rs == 6)
    } giving "napätá"
  }
}