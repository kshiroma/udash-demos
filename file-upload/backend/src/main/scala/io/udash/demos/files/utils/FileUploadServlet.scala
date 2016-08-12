package io.udash.demos.files.utils

import java.io.InputStream
import java.nio.file.Paths
import javax.servlet.annotation.MultipartConfig
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

abstract class FileUploadServlet(fileFields: Set[String]) extends HttpServlet {
  override protected def doPost(request: HttpServletRequest, response: HttpServletResponse): Unit = {
    import scala.collection.JavaConversions._

    request.getParts.toStream
      .filter(part => fileFields.contains(part.getName) || fileFields.contains(part.getName.stripSuffix("[]")))
      .foreach(filePart => {
        val fileName = Paths.get(filePart.getSubmittedFileName).getFileName.toString
        val fileContent = filePart.getInputStream
        handleFile(fileName, fileContent)
      })
  }

  protected def handleFile(name: String, content: InputStream): Unit
}
