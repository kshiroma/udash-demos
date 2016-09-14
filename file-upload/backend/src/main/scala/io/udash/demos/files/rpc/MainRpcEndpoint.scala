package io.udash.demos.files.rpc
import io.udash.demos.files.UploadedFile
import io.udash.demos.files.services.FilesStorage

import scala.concurrent.Future

class MainRpcEndpoint extends MainServerRPC {
  override def loadUploadedFiles(): Future[Seq[UploadedFile]] =
    Future.successful(FilesStorage.allFiles)
}
