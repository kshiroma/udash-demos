package io.udash.demos.files.views.index

import io.udash._
import io.udash.demos.files.{ApplicationServerContexts, IndexState}

import scala.util.{Failure, Success}

class IndexPresenter(model: ModelProperty[UploadViewModel]) extends Presenter[IndexState.type] with StrictLogging {
  import io.udash.demos.files.Context._

  rpcService.listenStorageUpdate(reloadUplaodedFiles)

  override def handleState(state: IndexState.type): Unit = {
    reloadUplaodedFiles()
  }

  private val uploader = new FileUploader(Url(ApplicationServerContexts.uploadContextPrefix))

  def uploadSelectedFiles(): Unit =
    uploader
      .upload("files", model.subSeq(_.selectedFiles).get)
      .listen(model.subProp(_.state).set)

  def reloadUplaodedFiles(): Unit =
    serverRpc.loadUploadedFiles() onComplete {
      case Success(files) =>
        model.subProp(_.uploadedFiles).set(files)
      case Failure(ex) =>
        logger.error(ex.getMessage)
    }
}
