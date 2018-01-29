package io.udash.todo.jetty

import io.udash.todo.services.InMemoryTodoStorage
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.gzip.GzipHandler
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler, ServletHolder}

class ApplicationServer(val port: Int, resourceBase: String) {
  private val server = new Server(port)
  private val contextHandler = new ServletContextHandler
  private val appHolder = createAppHolder()
  private val atmosphereHolder = createAtmosphereHolder()

  contextHandler.setSessionHandler(new SessionHandler)
  contextHandler.setGzipHandler(new GzipHandler)
  contextHandler.getSessionHandler.addEventListener(new org.atmosphere.cpr.SessionSupport())
  contextHandler.addServlet(atmosphereHolder, "/atm/*")
  contextHandler.addServlet(appHolder, "/*")
  server.setHandler(contextHandler)

  def start(): Unit = server.start()
  def stop(): Unit = server.stop()

  private def createAppHolder() = {
    val appHolder = new ServletHolder(new DefaultServlet)
    appHolder.setAsyncSupported(true)
    appHolder.setInitParameter("resourceBase", resourceBase)
    appHolder
  }

  private def createAtmosphereHolder() = {
    import io.udash.rpc._
    import io.udash.todo.rpc._
    import scala.concurrent.ExecutionContext.Implicits.global

    val config = new DefaultAtmosphereServiceConfig(_ =>
      new DefaultExposesServerRPC[MainServerRPC](new ExposedRpcInterfaces(InMemoryTodoStorage))
    )

    val framework = new DefaultAtmosphereFramework(config)

    val atmosphereHolder = new ServletHolder(new RpcServlet(framework))
    atmosphereHolder.setAsyncSupported(true)
    atmosphereHolder
  }

}

       