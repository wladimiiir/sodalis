package sk.magiksoft.test

import io.Source


/**
 * @author wladimiiir
 * @since 2010/11/17
 */

object CityZIPConverter {
  def main(args: Array[String]) {
    val lines = Source.fromFile("/home/wladimiiir/obce.csv").getLines.drop(1)
    val xmlString = <xml>
      {for (line <- lines if (line.split(";")(0).size > 0)) yield
        <value>
          <text>
            {line.split(";")(0).drop(1).dropRight(1)}
          </text>
          <zipCode>
            {if (line.split(";")(3).size > 2) line.split(";")(3).drop(1).dropRight(1).filter(c => c != ' ') else ""}
          </zipCode>
        </value>}
    </xml>

    print(xmlString)
  }
}
