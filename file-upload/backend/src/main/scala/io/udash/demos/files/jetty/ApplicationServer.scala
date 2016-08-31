package io.udash.demos.files.jetty

import scala.concurrent.ExecutionContext.Implicits.global
import javax.servlet.MultipartConfigElement

import io.udash.demos.files.ApplicationServerContexts
import io.udash.demos.files.rpc.{MainRpcEndpoint, MainServerRPC}
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

  private val uploadsHolder = {
    val holder = new ServletHolder(new DemoFileUploadServlet(resourceBase + "/uploads"))
    holder.getRegistration.setMultipartConfig(new MultipartConfigElement(""))
    holder
  }
  contextHandler.addServlet(uploadsHolder, ApplicationServerContexts.uploadContextPrefix + "/*")

  private val downloadsHolder = {
    val holder = new ServletHolder(new DemoFileDownloadServlet(resourceBase + "/uploads", ApplicationServerContexts.downloadContextPrefix))
    holder.getRegistration.setMultipartConfig(new MultipartConfigElement(""))
    holder
  }
  contextHandler.addServlet(downloadsHolder, ApplicationServerContexts.downloadContextPrefix + "/*")

  private val atmosphereHolder = {
    import io.udash.rpc._

    val config = new DefaultAtmosphereServiceConfig[MainServerRPC]((_) =>
      new DefaultExposesServerRPC[MainServerRPC](new MainRpcEndpoint)
    )
    val framework = new DefaultAtmosphereFramework(config)

    framework.init()

    val atmosphereHolder = new ServletHolder(new RpcServlet(framework))
    atmosphereHolder.setAsyncSupported(true)
    atmosphereHolder
  }
  contextHandler.addServlet(atmosphereHolder, ApplicationServerContexts.atmosphereContextPrefix + "/*")
}
       