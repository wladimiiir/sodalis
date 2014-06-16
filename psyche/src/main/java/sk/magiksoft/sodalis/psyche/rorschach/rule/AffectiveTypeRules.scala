/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.rule

/*
 * Copyright (c) 2011
 */

/*
 * Copyright (c) 2011
 */

import java.lang.Double

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/31/11
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */

object AffectiveTypeRules extends Rules[(Int, (Double, Double))] {
  add {
    rule complying {
      case (_, (ls, rs)) =>
        (ls == 0 and rs == 0) or (ls == 0 and rs == 1) or (ls == 1 and rs == 0) or (ls == 1 and rs == 1) or
          (ls == 0 and rs == 0.5) or (ls == 0.5 and rs == 0.5) or (ls == 0.5 and rs == 0)
    } giving "vyprahnutý"
  }

  add {
    rule complying {
      case (answerCount, (ls, rs)) =>
        if (answerCount < 25)
          (ls == 2 and rs == 2)
        else
          (ls - rs == 2 or rs - ls == 2)
    } giving "napätý"
  }
  add {
    rule complying {
      case (answerCount, (ls, rs)) =>
        if (answerCount < 25)
          ls - rs >= 2
        else
          ls - rs >= 3
    } giving "s dôrazom na zw"
  }
  add {
    rule complying {
      case (answerCount, (ls, rs)) =>
        if (answerCount < 25)
          rs - ls >= 3
        else
          rs - ls >= 4
    } giving "s dôrazom na Hd"
  }
}