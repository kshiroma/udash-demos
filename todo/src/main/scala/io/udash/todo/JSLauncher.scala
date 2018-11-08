package io.udash.todo

import io.udash.logging.CrossLogging
import io.udash.wrappers.jquery._
import org.scalajs.dom.Element

import scala.scalajs.js.annotation.JSExport

object JSLauncher extends CrossLogging {

  @JSExport
  def main(args: Array[String]): Unit = {
    jQ((_: Element) => {
      jQ(".todoapp").get(0) match {
        case None =>
          logger.error("Application root element not found! Check your index.html file!")
        case Some(root) =>
          ApplicationContext.applicationInstance.run(root)
      }
      jQ(".dekitayo").get(0) match {
        case None => logger.error("do not find ")
        case Some(root) =>
          ApplicationContext.dekitayoApplicatonInstance.run(root)
      }
    })
  }
}
