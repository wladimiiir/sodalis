package sk.magiksoft.sodalis.ftpman.entity

import scala.collection.mutable.ListBuffer


/**
 * @author wladimiiir
 * @since 2011/5/6
 */

class FTPScanCriteria {
  var threads = 10
  var hostFrom = ""
  var hostTo = ""

  def hostList = {
    val pattern = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}"
    (hostFrom.matches(pattern) && hostTo.matches(pattern)) match {
      case false => List(hostFrom)
      case true => {
        val hostFrom = this.hostFrom.split("\\.").map {
          _.toInt
        }
        val hostTo = this.hostTo.split("\\.").map {
          _.toInt
        }
        var hosts = new ListBuffer[String]
        for (a <- Range(hostFrom(0), hostTo(0) + 1)) {
          for (b <- Range(hostFrom(1), hostTo(1) + 1)) {
            for (c <- Range(hostFrom(2), hostTo(2) + 1)) {
              for (d <- Range(hostFrom(3), hostTo(3) + 1)) {
                hosts += a.toString + '.' + b.toString + '.' + c.toString + '.' + d.toString
              }
            }
          }
        }
        hosts.toList
      }
    }
  }
}
