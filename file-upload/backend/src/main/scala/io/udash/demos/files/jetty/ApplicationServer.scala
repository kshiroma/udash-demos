package io.udash.demos.files.jetty

import scala.concurrent.ExecutionContext.Implicits.global

import java.io.{File, InputStream}
import java.net.URLDecoder
import java.nio.file.Files
import java.util.UUID
import javax.servlet.MultipartConfigElement
import javax.servlet.http.HttpServletRequest

import io.udash.demos.files.rpc.{ClientRPC, MainRpcEndpoint, MainServerRPC}
import io.udash.demos.files.services.FilesStorage
import io.udash.demos.files.{ApplicationServerContexts, UploadedFile}
import io.udash.rpc._
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

class DemoFileUploadServlet(uploadDir: String) extends FileUploadServlet(Set("file", "files")) {
  new File(uploadDir).mkdir()

  override protected def handleFile(name: String, content: InputStream): Unit = {
    val targetName: String = s"${UUID.randomUUID()}_$name"
    val targetFile = new File(uploadDir, targetName)
    Files.copy(content, targetFile.toPath)
    FilesStorage.add(
      UploadedFile(name, targetName, targetFile.length())
    )

    // Notify clients
    ClientRPC(AllClients).fileStorageUpdated()
  }
}

class DemoFileDownloadServlet(filesDir: String, contextPrefix: String) extends FileDownloadServlet {
  override protected def resolveFile(request: HttpServletRequest): File = {
    val name = URLDecoder.decode(request.getRequestURI.stripPrefix(contextPrefix + "/"), "UTF-8")
    new File(filesDir + File.separator + name)
  }

  override protected def presentedFileName(name: String): String =
    FilesStorage.allFiles
      .find(_.serverFileName == name)
      .map(_.name)
      .getOrElse(name)
}
       