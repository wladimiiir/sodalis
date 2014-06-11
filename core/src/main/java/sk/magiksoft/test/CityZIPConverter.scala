
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.test

import io.Source


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/17/10
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */

object CityZIPConverter {
  def main(args: Array[String]) {
    val lines = Source.fromFile("/home/wladimiiir/obce.csv").getLines.drop(1)
    val xmlString = <xml>
      {for (line <- lines if(line.split(";")(0).size>0)) yield
        <value>
          <text>{line.split(";")(0).drop(1).dropRight(1)}</text>
          <zipCode>{
              if (line.split(";")(3).size>2) line.split(";")(3).drop(1).dropRight(1).filter(c=>c!=' ') else ""
            }</zipCode>
        </value>}
    </xml>

    print(xmlString)
  }
}