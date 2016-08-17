package io.udash.demos.files.jetty

import io.udash.rpc._
import java.io.{File, InputStream}
import java.nio.file.Files
import java.util.UUID
import javax.servlet.MultipartConfigElement
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
  contextHandler.addServlet(uploadsHolder, "/upload/*")
}

class DemoFileUploadServlet(uploadDir: String) extends FileUploadServlet(Set("file", "files")) {
  override protected def handleFile(name: String, content: InputStream): Unit = {
    val targetName: String = s"${UUID.randomUUID()}_$name"
    val targetFile = new File(uploadDir, targetName)
    Files.copy(content, targetFile.toPath)
  }
}

       