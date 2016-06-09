package io.udash.demos.rest.jetty

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.gzip.GzipHandler
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler, ServletHolder}

class ApplicationServer(val port: Int, resourceBase: String) {
  private val server = new Server(port)
  private val contextHandler = new ServletContextHandler

  contextHandler.setSessionHandler(new SessionHandler)
  contextHandler.setGzipHandler(new GzipHandler)
  server.setHandler(contextHandler)

  def start() = server.start()

  def stop() = server.stop()

  private val appHolder = {
    val appHolder = new ServletHolder(new DefaultServlet)
    appHolder.setAsyncSupported(true)
    appHolder.setInitParameter("resourceBase", resourceBase)
    appHolder
  }
  contextHandler.addServlet(appHolder, "/*")

  private val restApiHolder = {
    import spray.servlet.Servlet30ConnectorServlet
    import spray.servlet.Initializer

    contextHandler.addEventListener(new Initializer())
    val apiHolder = new ServletHolder(new Servlet30ConnectorServlet)
    apiHolder
  }
  contextHandler.addServlet(restApiHolder, s"/${ApplicationServer.restPrefix}/*")
}

object ApplicationServer {
  val restPrefix = "api"
}
       