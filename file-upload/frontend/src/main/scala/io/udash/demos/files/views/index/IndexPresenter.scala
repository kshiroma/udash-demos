package io.udash.demos.files.views.index

import io.udash._
import io.udash.demos.files.{ApplicationServerContexts, IndexState}
import io.udash.logging.CrossLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class IndexPresenter(model: ModelProperty[UploadViewModel]) extends Presenter[IndexState.type] with CrossLogging {
  import io.udash.demos.files.ApplicationContext._

  private val uploader = new FileUploader(Url(ApplicationServerContexts.uploadContextPrefix))

  rpcService.listenStorageUpdate(() => reloadUploadedFiles())

  override def handleState(state: IndexState.type): Unit = {
    reloadUploadedFiles()
  }

  def uploadSelectedFiles(): Unit = {
    uploader
      .upload("files", model.subSeq(_.selectedFiles).get)
      .listen(model.subProp(_.state).set(_))
  }

  def reloadUploadedFiles(): Unit = {
    serverRpc.loadUploadedFiles() onComplete {
      case Success(files) =>
        model.subProp(_.uploadedFiles).set(files)
      case Failure(ex) =>
        logger.error(ex.getMessage)
    }
  }
}
