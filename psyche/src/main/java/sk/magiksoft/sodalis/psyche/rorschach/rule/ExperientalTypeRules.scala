package sk.magiksoft.sodalis.psyche.rorschach.rule

import java.lang.Double
import sk.magiksoft.sodalis.dsl.rule.Rules
import Rules._

/**
 * @author wladimiiir
 * @since 2011/5/31
 */

object ExperientalTypeRules extends Rules[(Double, Double)] {
  add {
    rule complying {
      case (ls, rs) =>
        (ls == 0 and rs == 0) or (ls == 0 and rs == 0.5) or
          (ls == 0.5 and rs == 0) or (ls == 0.5 and rs == 0.5)
    } giving "koartovaný"
  }

  add {
    rule complying {
      case (ls, rs) =>
        (ls == 0 and rs == 1) or (ls == 0.5 and rs == 1) or (ls == 1 and rs == 1) or
          (ls == 1 and rs == 0) or (ls == 1 and rs == 0.5)
    } giving "koartatívny"
  }
  add {
    rule complying {
      case (ls, rs) =>
        ls >= 1 and rs >= 1 and ls <= 3 and rs <= 3 and
          (ls >= 1.5 or rs >= 1.5) and (ls - rs).abs <= 1
    } giving "ambiekválny"
  }
  add {
    rule complying {
      case (ls, rs) =>
        ((ls >= 4 or rs >= 4) and (ls - rs).abs <= 1) or
          ((ls >= 4.5 or rs >= 4.5) and (ls - rs).abs <= 1.5) or
          (ls >= 4.5 and rs >= 4.5)
    } giving "dilatovaný"
  }
  add {
    rule complying {
      case (ls, rs) => (ls == 0 and rs == 1.5) or (ls == 0.5 and rs >= 4)
    } giving "extrovertovaný bez introverzie"
  }
  add {
    rule complying {
      case (ls, rs) => (ls == 0.5 and rs >= 1.5 and rs <= 3.5) or
        (ls == 1 and rs >= 2.5 and rs <= 5.5) or
        (ls == 1.5 and rs >= 3 and rs <= 5.5)
    } giving "extrovertovaný"
  }
  add {
    rule complying {
      case (ls, rs) => (ls == 2 and rs >= 3.5 and rs <= 5.5) or
        (ls == 2.5 and rs == 4) or
        (ls == 3 and rs == 5) or
        (ls == 3.5 and rs == 5.5)
    } giving "vyvážený extrovertovaný"
  }
  add {
    rule complying {
      case (ls, rs) => (ls >= 1 and ls <= 2 and rs >= 6)
    } giving "dilatatívny extrovertovaný"
  }
  add {
    rule complying {
      case (ls, rs) => (ls >= 1.5 and rs == 0) or (ls == 4 or rs == 0.5)
    } giving "introvertovaný bez extraverzie"
  }
  add {
    rule complying {
      case (ls, rs) => (ls >= 1.5 and ls <= 3.5 and rs == 0.5) or
        (ls >= 2.5 and ls <= 5.5 and rs == 1) or
        (ls >= 3 and ls <= 5.5 and rs == 1.5)
    } giving "introvertovaný"
  }
  add {
    rule complying {
      case (ls, rs) => (ls >= 3.5 and ls <= 5.5 and rs == 2) or
        (ls >= 4 and rs == 2.5) or
        (ls >= 5 and rs == 3) or
        (ls >= 5.5 and rs == 3.5)
    } giving "vyvážený introvertovaný"
  }
  add {
    rule complying {
      case (ls, rs) => (ls >= 6 and rs >= 1 and rs <= 2)
    } giving "dilatatívny introvertovaný"
  }
}
