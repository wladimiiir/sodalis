package sk.magiksoft.sodalis.updater

import javax.xml.ws.Endpoint
import scala.App

/**
 *
 * @author Y12370
 * @since 17. 6. 2014, 11:28
 * @version 1.0
 */
object Publisher extends App {
  Endpoint.publish("http://localhost:8999/sodalis/UpdateService", new UpdateService)
}