package io.udash.todo

import io.udash._
import io.udash.i18n._
import io.udash.todo.storage.RemoteTodoStorage
import io.udash.wrappers.jquery._
import org.scalajs.dom.ext.LocalStorage
import org.scalajs.dom.{Element, document}

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object Context {
  implicit val executionContext = scalajs.concurrent.JSExecutionContext.Implicits.queue
  private val routingRegistry = new RoutingRegistryDef
  private val viewPresenterRegistry = new StatesToViewPresenterDef

  implicit val applicationInstance = new Application[RoutingState](routingRegistry, viewPresenterRegistry, RootState)

  import io.udash.rpc._
  import io.udash.todo.rpc._
  val serverRpc = DefaultServerRPC[MainClientRPC, MainServerRPC](new RPCService)

  implicit val provider = new RemoteTranslationProvider(serverRpc.i18n, Some(LocalStorage), 10 seconds)
  implicit val langProperty = LangProperty(Lang("en"))
}

object Init extends JSApp with StrictLogging {
  import Context._

  @JSExport
  override def main(): Unit = {
    jQ(document).ready((_: Element) => {
      val appRoot = jQ(".todoapp").get(0)
      if (appRoot.isEmpty) logger.error("Application root element not found! Check your index.html file!")
      else applicationInstance.run(appRoot.get)
    })
  }
}