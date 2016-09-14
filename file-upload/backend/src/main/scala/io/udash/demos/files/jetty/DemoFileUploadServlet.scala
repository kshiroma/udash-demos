package io.udash.demos.files.jetty

import scala.concurrent.ExecutionContext.Implicits.global
import java.io.{File, InputStream}
import java.nio.file.Files
import java.util.UUID

import io.udash.demos.files.UploadedFile
import io.udash.demos.files.rpc.ClientRPC
import io.udash.demos.files.services.FilesStorage
import io.udash.rpc._

class DemoFileUploadServlet(uploadDir: String) extends FileUploadServlet(Set("file", "files")) {
  new File(uploadDir).mkdir()

  override protected def handleFile(name: String, content: InputStream): Unit = {
    val targetName: String = s"${UUID.randomUUID()}_${name.replaceAll("[^a-zA-Z0-9.-]", "_")}"
    val targetFile = new File(uploadDir, targetName)
    Files.copy(content, targetFile.toPath)
    FilesStorage.add(
      UploadedFile(name, targetName, targetFile.length())
    )

    // Notify clients
    ClientRPC(AllClients).fileStorageUpdated()
  }
}
